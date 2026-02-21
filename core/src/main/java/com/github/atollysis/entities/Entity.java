package com.github.atollysis.entities;

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

    // Implemented by child classes
    public abstract Rectangle getBounds();

    /*
     * SETTERS
     */
    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

}
