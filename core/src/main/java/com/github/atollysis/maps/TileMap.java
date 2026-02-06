package com.github.atollysis.maps;

public class TileMap {

    /*
     * FIELDS
     */
    private final TileType[][] tiles;
    private final int width;
    private final int height;

    /*
     * CONSTRUCTOR
     */
    public TileMap(TileType[][] tiles, int width, int height) {
        this.tiles = tiles;
        this.width = width;
        this.height = height;
    }

    /*
     * GETTERS
     */
    public TileType getTileAt(int x, int y) {
        try {
            return this.tiles[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            return TileType.EMPTY;
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return height;
    }

}
