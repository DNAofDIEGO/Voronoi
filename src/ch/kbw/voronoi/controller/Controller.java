package ch.kbw.voronoi.controller;


import ch.kbw.voronoi.model.Point;
import ch.kbw.voronoi.model.VoronoiModel;
import ch.kbw.voronoi.model.math.Parable;
import ch.kbw.voronoi.model.palettes.Palettes;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField pointAmount;

    @FXML
    private Canvas canvas;

    @FXML
    private ChoiceBox<String> algorithm, color;

    @FXML
    private CheckBox animation, points;

    private final int DOTSIZE = 3;
    private final int WIDTH = 600, HEIGHT = 600;

    private VoronoiModel model;

    private GraphicsContext gc;

    private boolean tog = false;

    @FXML
    private void toggle(ActionEvent event) {
        if (tog) {
            tog = false;
        } else {
            tog = true;
        }
    }

    @FXML
    private void createPoints(ActionEvent event) {
        Object mode;
        if ((mode = color.getValue()) != null) {
            switch (mode.toString()) {
                case "Random":
                    model.setCp(Palettes.RANDOM);
                    break;

                case "Camouflage":
                    model.setCp(Palettes.CAMOUFLAGE);
                    break;

                case "Navy":
                    model.setCp(Palettes.NAVY);
                    break;

                case "Pink":
                    model.setCp(Palettes.PINK);
                    break;

                case "Ocean":
                    model.setCp(Palettes.OCEAN);
                    break;

                case "Munish":
                    model.setCp(Palettes.MUNISH);
                    break;

                case "Devil":
                    model.setCp(Palettes.DEVIL);
                    break;

                case "Purple":
                    model.setCp(Palettes.PURPLE);
                    break;

                case "Green":
                    model.setCp(Palettes.GREEN);
                    break;
            }
        }
        model.generatePoints(Integer.parseInt(pointAmount.getText()));
    }

    @FXML
    private void voronoi(ActionEvent event) {
        model.killThread();
        Object mode;
        if ((mode = algorithm.getValue()) != null) {
            switch (mode.toString()) {
                case "Simple":
                    model.simpleVoronoi();
                    model.resetCanvas();
                    break;

                case "Expansion":
                    model.expansionVoronoi(false);
                    model.resetCanvas();
                    break;

                case "Manhattan":
                    model.expansionVoronoi(true);
                    model.resetCanvas();
                    break;

                case "Fortune":
                    model.fortuneVoronoi();
                    break;
            }
        }
    }

    @FXML
    private void anim(ActionEvent event) {
        model.setAnimation(animation.isSelected());
    }

    @FXML
    private void point(ActionEvent event) {
        model.setP(points.isSelected());
    }

    @FXML
    private void readFile(ActionEvent event) {
        model.loadFile();
        model.resetCanvas();
    }

    @FXML
    private void save(ActionEvent event) {
        model.saveFile();
    }

    /**
     * Draws the VoronoiModel Object on the Canvas to visualize it
     * This is called 60 times every second with an AnimationTimer
     */
    private void draw() {

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int [][] can = model.getCanvas();
        int width = can.length;
        int height = can[0].length;
        ArrayList<Point> points = model.getPoints();

        WritableImage img = new WritableImage(width, height);
        PixelWriter pw = img.getPixelWriter();
        pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), model.simplify(can), 0, width);
        gc.drawImage(img, 0, 0);

        if (points.size() > 0) {
            if (model.isP()) {
                for (Point p : points) {
                    int rgb = p.getCol();
                    int b = rgb % 0x100;
                    int g = (rgb >> 8) % 0x100;
                    int r = (rgb >> 16) % 0x100;

                    gc.setFill(Color.rgb(r, g, b).invert());
                    gc.fillOval(p.getX() - (DOTSIZE - 1), p.getY() - (DOTSIZE - 1) / 2, DOTSIZE, DOTSIZE);
                }
            }

            gc.setFill(Color.BLACK);

            /*boolean[][] skeleton = model.getSkeleton();
            for (int x = 0; x < skeleton.length; x++) {
                for (int y = 0; y < skeleton[x].length; y++) {
                    if (skeleton[x][y]) {
                        gc.fillRect(x - 1, y - 1, 2, 2);
                    }
                }
            }*/
        }

        gc.setStroke(Color.GREEN);

        if (model.getLp() != null) {
            Parable p = model.getLp();
            for (int i = 0; i < p.getParable().size(); i++) {
                if (i > 0) {
                    gc.strokeLine(p.get(i)[0], p.get(i)[1], p.get(i - 1)[0], p.get(i - 1)[1]);
                }
            }
        }

        gc.setStroke(Color.BLACK);

        gc.strokeLine(0, model.getLine(), canvas.getWidth(), model.getLine());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animation.setSelected(true);
        points.setSelected(true);
        model = new VoronoiModel();
        canvas.setHeight(HEIGHT + DOTSIZE);
        canvas.setWidth(WIDTH + DOTSIZE);
        gc = canvas.getGraphicsContext2D();
        algorithm.setItems(FXCollections.observableArrayList(
                "Simple", "Expansion", "Manhattan", "Fortune"));
        algorithm.setValue("Simple");
        color.setItems(FXCollections.observableArrayList(
                Palettes.getPresets()));
        color.setValue("Random");
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long now) {
                draw();
            }
        };
        at.start();
    }
}