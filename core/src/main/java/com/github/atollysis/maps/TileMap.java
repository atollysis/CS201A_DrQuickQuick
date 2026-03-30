package com.github.atollysis.maps;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.atollysis.entities.Entity;
import com.github.atollysis.systems.GameConfig;

public class TileMap {

    /*
     * FIELDS
     */
    private static final int TILE_SIZE = GameConfig.getTileSize();

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

    public Vector2 getRandomCoords(Entity entity) {
        int randTileX, randTileY;

        do {
            randTileX = MathUtils.random(0, this.width - 1);
            randTileY = MathUtils.random(0, this.height - 1);
        } while (this.getTileAt(randTileX, randTileY) != TileType.FLOOR);

        float randOffsetX = MathUtils.random(
            entity.getBounds().width / 2f,
            TILE_SIZE - entity.getBounds().width / 2f
        );

        float randOffsetY = MathUtils.random(
            0,
            TILE_SIZE - entity.getBounds().height
        );

        return new Vector2(
            (randTileX * TILE_SIZE) + randOffsetX,
            (randTileY * TILE_SIZE) + randOffsetY
        );
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
