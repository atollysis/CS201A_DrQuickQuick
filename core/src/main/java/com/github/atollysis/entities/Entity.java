package com.github.atollysis.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    /*
     * FIELDS
     */
    protected final Vector2 position;

    /*
     * CONSTRUCTOR
     */
    public Entity() {
        this.position = new Vector2();
    }

    // GETTERS
    public Vector2 getPosition() {
        return this.position;
    }

    /*
     * ABSTRACT METHODS
     */
    public abstract Rectangle getBounds();
    // For rendering sprites
    public abstract Sprite getSprite();
    public abstract float getVisualX();
    public abstract float getVisualY();

    /*
     * SETTERS
     */
    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

}
