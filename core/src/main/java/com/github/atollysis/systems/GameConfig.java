package com.github.atollysis.systems;

public class GameConfig {
    /*
     * FIELDS
     */
    private static final int TILE_SIZE = 32;
    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 360;

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
}
