package ch.kbw.voronoi.model;

import ch.kbw.voronoi.model.palettes.ColorPalette;

public class Point {
    private int x;
    private int y;
    private int col;

    /**
     * Point constructor
     *
     * @param x  x-coordinate of point
     * @param y  y-coordinate of point
     * @param cp colorpalette
     */
    Point(int x, int y, ColorPalette cp) {
        this.x = x;
        this.y = y;
        this.col = cp.getColor();
    }

    /**
     * Distance between this and another point
     *
     * @param pX point x-coordinate
     * @param pY point y-coordinate
     * @return distance
     */
    double getDifference(int pX, int pY) {
        int differenceX = Math.abs(x - pX);
        int differenceY = Math.abs(y - pY);
        return Math.sqrt(differenceX * differenceX + differenceY * differenceY);
    }

    /**
     * Simple getter
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Simple getter
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Simple getter
     *
     * @return color
     */
    public int getCol() {
        return col;
    }
}
