package ch.kbw.voronoi.model.palettes;

import java.util.Random;

public class PresetPalette extends ColorPalette {

    private double[][] scheme;
    private Random rand;

    /**
     * Constructor for a preset palette
     *
     * @param scheme the colors (rgb 0-255)
     */
    PresetPalette(double[][] scheme) {
        this.scheme = scheme;
        this.rand = new Random();
    }

    /**
     * Overwritten getter
     *
     * @return a random color from the theme
     */
    @Override
    public int getColor() {
        int i = rand.nextInt(scheme.length);
        return ((int)scheme[i][0] << 16)  + ((int)scheme[i][1] << 8) + ((int)scheme[i][2]);
    }
}
