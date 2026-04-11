package com.github.atollysis.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.atollysis.systems.Assets;

public class Obstacle extends Entity {

    /*
     * FIELDS
     */

    private final Rectangle bounds;
    private final Sprite sprite;

    /*
     * CONSTRUCTOR
     */

    public Obstacle(Assets assets) {
        sprite = new Sprite(assets.randomObstacleTexture());
        bounds = new Rectangle(0f, 0f, assets.getObstacleWidth(sprite.getTexture()), 16f);
    }

    /*
     * GETTERS
     */

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public float getVisualX() {
        return this.position.x - bounds.width / 2f;
    }

    @Override
    public float getVisualY() {
        return this.position.y;
    }

    /*
     * SETTERS
     */
    public void setCoords(Vector2 coords) {
        this.position.set(coords);
        this.sprite.setPosition(
            coords.x - bounds.width / 2f,
            coords.y
        );
    }
}
