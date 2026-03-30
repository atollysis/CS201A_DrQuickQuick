package com.github.atollysis.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.github.atollysis.systems.Assets;

import java.util.Objects;

public class Patient extends Entity {

    /*
     * FIELDS
     */
    private static final Rectangle BOUNDS = new Rectangle(0f, 0f, 32f, 16f);

    // True sort place
    private final int sortId;
    // Number to display to the player
    private final int sortUrgency;

    // If minigame is finished
    private boolean isSorted = false;
    // Which place the player sorted it
    private int sortedPlace = -1;

    // Front end
    private final Sprite sprite;

    /*
     * CONSTRUCTOR
     */
    public Patient(Assets assets, int sortId, int sortNumber) {
        sprite = new Sprite(assets.patientTexture());
        this.sortId = sortId;
        this.sortUrgency = sortNumber;
    }

    /*
     * METHODS
     */
    public boolean isProperlySorted() {
        return sortId == sortedPlace;
    }

    public boolean boundsContains(float x, float y) {
        this.sprite.setPosition(
            this.position.x - BOUNDS.width / 2f,
            this.position.y
        );
        return sprite.getBoundingRectangle().contains(x, y);

    }

    /*
     * GETTERS
     */
    public int getSortId() {
        return sortId;
    }

    public int getSortUrgency() {
        return sortUrgency;
    }

    public boolean isSorted() {
        return isSorted;
    }

    public int getSortedPlace() {
        return sortedPlace;
    }

    public Sprite getSprite() {
        return sprite;
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
    public void setSorted(boolean isSorted, int sortedPlace) {
        this.isSorted = isSorted;
        this.sortedPlace = sortedPlace;
    }

    public void setCoords(Vector2 coords) {
        this.position.set(coords);
        this.sprite.setPosition(
            coords.x - BOUNDS.width / 2f,
            coords.y
        );
    }

    /*
     * OVERRIDDEN METHODS
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Patient patient = (Patient) o;
        return sortId == patient.sortId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sortId);
    }

}
