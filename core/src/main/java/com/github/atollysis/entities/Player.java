package com.github.atollysis.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Player {

    /*
     * FIELDS
     */
    private static final float ACCEL = 1400;
    private static final float MAX_SPEED = 400f;
    private static final float FRICTION = 1000f;

    private final Vector2 playerPosition = new Vector2(0, 0);
    private final Vector2 playerVelocity = new Vector2(0, 0);
    private FacingDirection facingDirection = FacingDirection.RIGHT;

    /*
     * DEFAULT CONSTRUCTOR USED
     */

    /*
     * METHODS
     */
    public void handleInputUpdatePos(float delta) {
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

        playerPosition.mulAdd(playerVelocity, delta);
    }

    /*
     * GETTERS
     */
    public float getPosX() {
        return playerPosition.x;
    }

    public float getPosY() {
        return playerPosition.y;
    }

    public FacingDirection getFacingDirection() {
        return facingDirection;
    }

}
