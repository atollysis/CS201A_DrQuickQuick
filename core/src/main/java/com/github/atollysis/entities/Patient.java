package com.github.atollysis.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

    /*
     * CONSTRUCTOR
     */
    public Patient(int sortId, int sortNumber) {
        this.sortId = sortId;
        this.sortUrgency = sortNumber;
    }

    /*
     * METHODS
     */
    public boolean isProperlySorted() {
        return sortId == sortedPlace;
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
    }

}
