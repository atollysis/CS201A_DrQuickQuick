package com.github.atollysis.systems;

import com.badlogic.gdx.math.Vector2;
import com.github.atollysis.entities.Player;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.maps.TileType;

public class CollisionSystem {

    /*
     * FIELDS
     */
    private static final int TILE_SIZE = GameConfig.getTileSize();
    private final TileMap map;

    /*
     * CONSTRUCTOR
     */
    public CollisionSystem(TileMap map) {
        this.map = map;
    }

    /*
     * METHODS
     */
//    public void checkMapCollision(Player player) {
//        Vector2 pos = player.getPosition();
//        int x = (int) (pos.x / TILE_SIZE);
//        int y = (int) (pos.y / TILE_SIZE);
//
//        checkCenter(player, x, y);
//        checkX(player, x, y);
//        checkY(player, x, y);
//    }

//    private void checkCenter(Player player, int x, int y) {
//        if (this.map.getTileAt(x, y) == TileType.FLOOR)
//            return;
//        // TODO
//    }

    public void resolveX(Player player) {
        Vector2 pos = player.getPosition();
        int x = (int) (pos.x / TILE_SIZE);
        int y = (int) (pos.y / TILE_SIZE);

        float tileX = x * TILE_SIZE;
        float tileXEnd = tileX + TILE_SIZE;

        boolean overlapLeft = (pos.x - player.getBounds().width / 2f) < tileX;
        boolean leftWalkable = this.isWalkable(x - 1, y);
        if (!leftWalkable && overlapLeft) {
            player.setPosition(
                tileX + player.getBounds().width / 2f,
                pos.y);
            player.resetVelocityX();
            return;
        }

        boolean overlapRight = (pos.x + player.getBounds().width / 2f) > tileXEnd;
        boolean rightWalkable = this.isWalkable(x + 1, y);
        if (!rightWalkable && overlapRight) {
            player.setPosition(
                tileX + player.getBounds().width / 2f,
                pos.y);
            player.resetVelocityX();
        }
    }

    public void resolveY(Player player) {
        Vector2 pos = player.getPosition();
        int x = (int) (pos.x / TILE_SIZE);
        int y = (int) (pos.y / TILE_SIZE);

        float tileY = y * TILE_SIZE;
        float tileYEnd = tileY + TILE_SIZE;

        boolean overlapBottom = pos.y < tileY + TILE_SIZE;
        boolean bottomWalkable = isWalkable(x, y);
        if (!bottomWalkable && overlapBottom) {
            player.setPosition(
                pos.x,
                tileY + TILE_SIZE);
            player.resetVelocityY();
            return;
        }

        boolean overlapTop = (pos.y + player.getBounds().height) > tileYEnd;
        boolean topWalkable = this.isWalkable(x, y + 1);
        if (!topWalkable && overlapTop) {
            player.setPosition(
                pos.x,
                tileYEnd - player.getBounds().height);
            player.resetVelocityY();
        }
    }

    private boolean isWalkable(int x, int y) {
        return this.map.getTileAt(x, y) == TileType.FLOOR;
    }

}
