package com.github.atollysis.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.systems.CollisionSystem;

public class Player extends Entity {

    /*
     * FIELDS
     */
    private static final Rectangle BOUNDS = new Rectangle(0f, 0f, 32f, 16f);
    private static final float ACCEL = 1400;
    private static final float MAX_SPEED = 400f;
    private static final float FRICTION = 1000f;

    private final Vector2 playerVelocity = new Vector2(0, 0);
    private FacingDirection facingDirection = FacingDirection.RIGHT;

    /*
     * CONSTRUCTOR
     */
    public Player(TileMap tileMap) {
        this.position.set(tileMap.getRandomCoords(this));
    }

    /*
     * METHODS
     */
    public void handleInputUpdatePos(float delta, CollisionSystem collisionSystem) {
        Vector2 acceleration = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            acceleration.y += ACCEL;
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            acceleration.y -= ACCEL;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            acceleration.x -= ACCEL;
            facingDirection = FacingDirection.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            acceleration.x += ACCEL;
            facingDirection = FacingDirection.RIGHT;
        }

        if (acceleration.len2() > 0) {
            acceleration.nor().scl(ACCEL * delta);
            playerVelocity.add(acceleration);
        } else {
            // Apply friction
            float speed = playerVelocity.len();
            if (speed > 0) {
                float drop = FRICTION * delta;
                float newSpeed = Math.max(speed - drop, 0);
                playerVelocity.scl(newSpeed / speed);
            }
        }

        if (playerVelocity.len() > MAX_SPEED)
            playerVelocity.nor().scl(MAX_SPEED);

        Vector2 newPos = new Vector2(this.position).mulAdd(playerVelocity, delta);

        this.position.set(newPos.x, this.position.y);
        collisionSystem.resolveX(this);
        this.position.set(this.position.x, newPos.y);
        collisionSystem.resolveY(this);
    }

    /*
     * GETTERS
     */
    public FacingDirection getFacingDirection() {
        return facingDirection;
    }

    @Override
    public Rectangle getBounds() {
        return BOUNDS;
    }

    @Override
    public float getVisualX() {
        return this.position.x - BOUNDS.width / 2f;
    }

    @Override
    public float getVisualY() {
        return this.position.y;
    }

    /*
     * SETTERS
     */
    public void resetVelocityX() {
        this.playerVelocity.x = 0;
    }

    public void resetVelocityY() {
        this.playerVelocity.y = 0;
    }

}
