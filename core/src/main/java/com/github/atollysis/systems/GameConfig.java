package com.github.atollysis.systems;

public class GameConfig {
    /*
     * FIELDS
     */
    private static final int TILE_SIZE = 32;
    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 360;

    private static final int MAX_DIGIT_LENGTH = 3;
    private static final long MAX_PATIENT_LEVEL = Math.round(Math.pow(10, MAX_DIGIT_LENGTH)) - 1;

    /*
     * GETTERS
     */
    public static int getTileSize() {
        return TILE_SIZE;
    }

    public static int getWorldWidth() {
        return WORLD_WIDTH;
    }

    public static int getWorldHeight() {
        return WORLD_HEIGHT;
    }

    public static int getMaxDigitLength() {
        return MAX_DIGIT_LENGTH;
    }

    public static long getMaxPatientLevel() {
        return MAX_PATIENT_LEVEL;
    }

}
