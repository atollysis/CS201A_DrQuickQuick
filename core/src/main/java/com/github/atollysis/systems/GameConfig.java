package com.github.atollysis.systems;

import com.badlogic.gdx.graphics.Color;
import com.github.atollysis.entities.EntityManager;
import com.github.atollysis.maps.MapGenerator;
import com.github.atollysis.systems.renderers.GameInterface;
import com.github.atollysis.systems.session.GameDifficulty;

public class GameConfig {
    /*
     * FIELDS
     */
    private static final Color WHITE_COLOR = Color.valueOf("E9FDFE");
    private static final Color BLACK_COLOR = Color.valueOf("011015");
    private static final Color GREEN_COLOR = Color.valueOf("27A22B");
    private static final Color RED_COLOR = Color.valueOf("FF6767");
    private static final Color YELLOW_COLOR = Color.valueOf("EFE363");

    private static final int TILE_SIZE = 32;
    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 360;

    private static GameDifficulty difficulty;
    private static int MAX_DIGIT_LENGTH;
    private static long MAX_PATIENT_LEVEL;

    private static final int MAX_NAME_LENGTH = 16;
    private static boolean inDebugMode = false;

    /*
     * GETTERS
     */
    public static Color white() {
        return WHITE_COLOR;
    }

    public static Color black() {
        return BLACK_COLOR;
    }

    public static Color green() {
        return GREEN_COLOR;
    }

    public static Color red() {
        return RED_COLOR;
    }

    public static Color yellow() {
        return YELLOW_COLOR;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public static int getWorldWidth() {
        return WORLD_WIDTH;
    }

    public static int getWorldHeight() {
        return WORLD_HEIGHT;
    }

    public static GameDifficulty getDifficulty() {
        return difficulty;
    }

    public static int getMaxDigitLength() {
        return MAX_DIGIT_LENGTH;
    }

    public static long getMaxPatientLevel() {
        return MAX_PATIENT_LEVEL;
    }

    public static int getMaxNameLength() {
        return MAX_NAME_LENGTH;
    }

    public static boolean isInDebugMode() {
        return inDebugMode;
    }

    /*
     * SETTERS
     */
    public static void setDifficulty(GameDifficulty difficulty) {
        GameConfig.difficulty = difficulty;
        MAX_DIGIT_LENGTH = difficulty.getPatientDigitLength();
        MAX_PATIENT_LEVEL = Math.round(Math.pow(10, MAX_DIGIT_LENGTH)) - 1;
        EntityManager.setValues(difficulty);
        MapGenerator.setValues(difficulty);
        GameInterface.setValues();
    }

    public static void toggleDebugMode() {
        inDebugMode = !inDebugMode;
    }

}
