package ch.kbw.voronoi.model;

import ch.kbw.voronoi.model.math.Parable;
import ch.kbw.voronoi.model.palettes.ColorPalette;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class VoronoiModel {
    private ArrayList<Point> points;
    private double line = -1;
    private Parable[] parables;
    private Parable lp;
    private int[][] canvas;
    private boolean[][] skeleton;
    private Thread algorithmThread;
    private boolean killThread;
    private boolean animation, p;
    private ColorPalette cp;

    /**
     * The constructor that initializes all important things
     */
    public VoronoiModel() {
        points = new ArrayList<>();
        canvas = new int[600][600];
        skeleton = new boolean[600][600];
        parables = new Parable[points.size()];
        fill(canvas, -1);
        killThread = false;
        animation = true;
        p = true;
    }

    private void fill(int[][] can, int i) {
        for (int[] ca : can) {
            for (int c : ca) {
                c = i;
            }
        }
    }

    /**
     * Generates a (*.vor) file to store the generated points
     */
    public void saveFile() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName("voronoi");
        fc.setTitle("Save points");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Voronoi files (*.vor)", "*.vor");
        fc.getExtensionFilters().add(extFilter);
        try {
            File file = fc.showSaveDialog(new Stage());
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Point p : points) {
                bw.write(p.getX() + "," + p.getY());
                bw.newLine();
            }
            bw.flush();
        } catch (FileNotFoundException e) {
            System.err.println("The file could not be found.");
        } catch (IOException e) {
            System.err.println("The file could not be written.");
        }
    }

    /**
     * Loads a (*.vor) file
     */
    public void loadFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Voronoi files (*.vor)", "*.vor");
        fc.getExtensionFilters().add(extFilter);
        try {
            File file = fc.showOpenDialog(new Stage());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp;
            points.clear();
            while ((temp = br.readLine()) != null) {
                String[] parts = temp.split(",");
                points.add(new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), cp));
            }
        } catch (FileNotFoundException e) {
            System.err.println("The file could not be found.");
        } catch (IOException e) {
            System.err.println("The file could not be read.");
        }
    }

    private double root(double a, int amount) {
        double root = a;
        for (int i = 0; i < amount; i++) {
            root = Math.sqrt(root);
        }
        return root;
    }

    /**
     * Tells the algorithmThread to commit suicide (stop definitly) for 100 milliseconds
     */
    public void killThread() {
        killThread = true;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        killThread = false;
    }

    /**
     * Generates points on the Canvas
     *
     * @param amount is the amount of points that should be generated
     */
    public void generatePoints(int amount) {
        killThread();
        points.clear();
        resetCanvas();
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            points.add(new Point(r.nextInt(600), r.nextInt(600), cp));
        }
        Collections.sort(points, (o1, o2) -> ((Integer) o1.getY()).compareTo(o2.getY()));
    }

    /**
     * The algorithm for the voronoiExpansion or manhattan
     *
     * @param manhattan is a boolean if the manhattan or voronoi diagram is generated
     */
    public void expansionVoronoi(boolean manhattan) {

        // create a thread to not interfere with the fx-thread
        algorithmThread = new Thread(() -> {

            ArrayList<Point> ps = (ArrayList<Point>) points.clone();

            // creates a survivability array and fills it with 0's
            int[] psn = new int[ps.size()];
            for (int u = 0; u < psn.length; u++) {
                psn[u] = 0;
            }

            // loops with .1 so the algorithm is more accurate
            for (double i = 1.0; i < 850; i += 0.1) {

                // Pause without Thread.sleep(millis);
                long nt = System.nanoTime();
                while (System.nanoTime() - nt <= 10000000L && animation) {
                    //DONOTHING
                    continue;
                }

                // loops through points
                for (int po = 0; po < ps.size(); po++) {
                    Point p = ps.get(po);

                    // diagnoses the point as dead
                    boolean dead = true;

                    // puts the points coordinates into more suitable variables.
                    int x = p.getX(), y = p.getY();

                    // loops through all vertical lines that are effected by the change
                    for (int j = 0; j < i; j++) {

                        // sets the horizontal span for the effected area
                        int span;
                        if (manhattan) {
                            span = (int) Math.round(i);
                            //span = (int) Math.round(root(Math.pow(i, 4) - Math.pow(j, 4), 2));
                        } else {
                            span = (int) Math.round(root(Math.pow(i, 2) - Math.pow(j, 2), 1));
                            //span = (int) Math.round(root(Math.pow(i, 8) - Math.pow(j, 8), 3));
                        }

                        // fills the color into the canvas if it is null
                        for (int k = -span; k <= span; k++) {

                            // detects threadsuicide
                            if (killThread) {
                                break;
                            }

                            if (x + k >= 0 && y + j >= 0 && x + k < 600 && y + j < 600) {
                                if (canvas[x + k][y + j] == 0) {
                                    canvas[x + k][y + j] = p.getCol();

                                    // revives the point
                                    dead = false;
                                }
                            }

                            // detects threadsuicide
                            if (killThread) {
                                break;
                            }

                            if (x - k >= 0 && y - j >= 0 && x - k < 600 && y - j < 600) {
                                if (canvas[x - k][y - j] == 0) {
                                    canvas[x - k][y - j] = p.getCol();

                                    // revives th point
                                    dead = false;
                                }
                            }

                            // detects threadsuicide
                            if (killThread) {
                                break;
                            }
                        }
                    }

                    // detects threadsuicide
                    if (killThread) {
                        break;
                    }

                    // kills the point if it was dead 10 times in a row
                    if (dead) {
                        if (psn[po] >= 10) {
                            ps.remove(po);
                            System.arraycopy(psn, po + 1, psn, po, psn.length - 1 - po);
                        } else {
                            psn[po] += 1;
                        }
                    } else {
                        psn[po] = 0;
                    }
                }

                // detects threadsuicide
                if (killThread) {
                    killThread = false;
                    break;
                }
            }
        });
        algorithmThread.setDaemon(true);
        algorithmThread.start();
    }

    /**
     * A simpler algorithm that checks all pixels and dyes them according to the nearest point
     */
    public void simpleVoronoi() {

        // create a thread to not interfere with the fx-thread
        algorithmThread = new Thread(() -> {

            // loops through the canvas
            for (int x = 0; x < 600; x++) {
                for (int y = 0; y < 600; y++) {

                    // pause without thr
                    long nt = System.nanoTime();
                    while (System.nanoTime() - nt <= 100000L && animation) {
                        //DONOTHING
                        continue;
                    }
                    int bestColor = -1;
                    double bestDistance = -1;
                    for (Point p : points) {
                        if (bestDistance == -1 || bestDistance > p.getDifference(x, y)) {
                            bestDistance = p.getDifference(x, y);
                            bestColor = p.getCol();
                        }
                    }
                    canvas[x][y] = bestColor;
                    if (killThread) {
                        break;
                    }
                }
                if (killThread) {
                    killThread = false;
                    break;
                }
            }
        });
        algorithmThread.setDaemon(true);
        algorithmThread.start();

    }

    /**
     * Resets the Canvas with null entries
     */
    public void resetCanvas() {
        canvas = new int[600][600];
        fill(canvas, -1);
    }

    public boolean[][] getSkeleton() {
        return skeleton;
    }

    /**
     * A basic getter
     *
     * @return the points ArrayList
     */
    public ArrayList<Point> getPoints() {
        return points;
    }

    public Parable getLp() {
        return lp;
    }

    /**
     * A basic getter
     *
     * @return the Canvas ArrayList
     */
    public int[][] getCanvas() {
        return canvas;
    }

    /**
     * A basic setter
     *
     * @param animation if the animations should be executed
     */
    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    /**
     * A basic getter
     *
     * @return if the points should be drawn
     */
    public boolean isP() {
        return p;
    }

    /**
     * A basic setter
     *
     * @param p if the points should be drawn (used in draw(); in the controller)
     */
    public void setP(boolean p) {
        this.p = p;
    }

    /**
     * A basic setter
     *
     * @param cp the most recent ColorPalette
     */
    public void setCp(ColorPalette cp) {
        this.cp = cp;
    }

    /**
     * A basic getter
     *
     * @return the skeleton
     */
    public Parable[] getParables() {
        return parables;
    }

    public void fortuneVoronoi() {
        skeleton = new boolean[600][600];
        algorithmThread = new Thread(() -> {
            for (line = -1; line <= canvas.length * 2; line += 0.1) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parables = new Parable[points.size()];
                for (int i = 0; i < parables.length; i++) {
                    parables[i] = new Parable();
                }

                for (int j = canvas.length; j > 0; j--) {
                    int i = 0;
                    while (i < points.size() && points.get(i).getY() <= line) {
                        Point point = points.get(i);
                        if (line != point.getY()) {
                            int y = (int) (((point.getX() - j) * (point.getX() - j) + (point.getY() * point.getY()) - (line * line)) / (2 * point.getY() - 2 * line));
                            parables[i].addLeft(new Integer[]{j, y});
                        }
                        i++;
                    }

                }

                int[] lowest = new int[canvas.length];
                int[] lowpoi = new int[canvas.length];
                for (int a = 0; a < lowest.length; a++) {
                    lowest[a] = -1;
                    lowpoi[a] = -1;
                }

                int pCount = 0;
                for (Parable p : parables) {
                    for (Integer[] point : p.getParable()) {
                        int x = point[0];
                        if (x < 600 && x >= 0) {
                            if (point[1] >= lowest[x]) {
                                lowest[x] = point[1];
                                lowpoi[x] = pCount;
                            }
                        }
                    }
                    pCount++;
                }

                int last = -1;
                for (int i = 0; i < lowpoi.length; i++) {
                    if (lowpoi[i] != last && last != -1 && lowest[i] >= 0 && lowest[i] < 600) {
                        skeleton[i][lowest[i]] = true;
                    }
                    last = lowpoi[i];
                }

                Parable p = new Parable();
                for (int y = 0; y < lowest.length; y++) {
                    if (lowest[y] < 600 && lowest[y] >= 0) {
                        p.addLeft(new Integer[]{y, lowest[y]});
                    }
                }
                lp = p;
            }
            line = -1;
            lp = null;
        });
        algorithmThread.setDaemon(true);
        algorithmThread.start();
    }

    public int[] simplify(int[][] can) {
        int[] result = new int[can.length * can[0].length];
        for (int i = 0; i < can.length; i++) {
            for (int j = 0; j < can[0].length; j++) {
                result[j + i * can[0].length] = (can[j][i] != 0) ? can[j][i] + 0xFF000000 : 0;
            }
        }
        return result;
    }

    public double getLine() {
        return line;
    }
}
