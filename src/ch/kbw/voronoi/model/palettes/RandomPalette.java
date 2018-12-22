package ch.kbw.voronoi.model.palettes;

import java.util.Random;

public class RandomPalette extends ColorPalette {

    /**
     * Overwritten getter
     *
     * @return a random Color
     */
    @Override
    public int getColor() {
        Random rand = new Random();
        return rand.nextInt(256) + rand.nextInt(256) * 255 + rand.nextInt(256) * 255 * 255;
    }
}
