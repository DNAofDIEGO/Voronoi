package ch.kbw.voronoi.model.palettes;

public abstract class Palettes {

    public static final ColorPalette RANDOM = new RandomPalette();
    public static final ColorPalette CAMOUFLAGE;
    public static final ColorPalette NAVY;
    public static final ColorPalette PINK;
    public static final ColorPalette OCEAN;
    public static final ColorPalette MUNISH;
    public static final ColorPalette DEVIL;
    public static final ColorPalette PURPLE;
    public static final ColorPalette GREEN;

    static {
        double[][] camo = {{96, 68, 57},
                {158, 154, 117},
                {28, 34, 46},
                {65, 83, 59},
                {85, 72, 64}
        };
        double[][] navy = {{255, 255, 255},
                {226, 226, 211},
                {153, 153, 102},
                {97, 97, 64},
                {16, 34, 69}
        };
        double[][] bizliGayIschOkay = {
                {255, 132, 244},
                {225, 132, 244},
                {243, 163, 253},
                {237, 185, 255},
                {233, 216, 251}
        };
        double[][] ocean = {
                {23, 106, 144},
                {17, 84, 120},
                {11, 62, 95},
                {6, 40, 71},
                {0, 18, 46}
        };
        double[][] munish = {
                {250, 109, 41},
                {28, 78, 138},
                {255, 255, 255},
                {187, 187, 187},
                {170, 170, 170}
        };
        double[][] devil = {
                {69, 0, 0},
                {114, 0, 0},
                {167, 0, 0},
                {206, 0, 0},
                {255, 0, 0}
        };
        double[][] purple = {
                {217, 115, 247},
                {182, 117, 243},
                {139, 60, 249},
                {132, 0, 213},
                {77, 0, 125}
        };
        double[][] green = {
                {179, 198, 177},
                {143, 174, 144},
                {112, 142, 114},
                {74, 104, 80},
                {53, 74, 54}
        };

        CAMOUFLAGE = new PresetPalette(camo);
        NAVY = new PresetPalette(navy);
        PINK = new PresetPalette(bizliGayIschOkay);
        OCEAN = new PresetPalette(ocean);
        MUNISH = new PresetPalette(munish);
        DEVIL = new PresetPalette(devil);
        PURPLE = new PresetPalette(purple);
        GREEN = new PresetPalette(green);
    }

    /**
     * Gets all preset ColorPalettes
     *
     * @return String[] of all ColorPalettes
     */
    public static String[] getPresets() {
        return new String[]{"Random", "Camouflage", "Navy", "Pink", "Ocean", "Munish", "Devil", "Purple", "Green"};
    }
}
