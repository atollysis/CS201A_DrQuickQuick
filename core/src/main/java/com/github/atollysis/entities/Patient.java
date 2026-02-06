package com.github.atollysis.entities;

import com.badlogic.gdx.math.Vector2;

public class Patient {

    /*
     * FIELDS
     */
    // True sort place
    private final int sortId;
    // Number to display to the player
    private final int sortUrgency;
    // Physical map coords
    private final Vector2 coords;

    // If minigame is finished
    private boolean isSorted = false;
    // Which place the player sorted it
    private int sortedPlace = -1;

    /*
     * CONSTRUCTOR
     */
    public Patient(int sortId, int sortNumber, Vector2 coords) {
        this.sortId = sortId;
        this.sortUrgency = sortNumber;
        this.coords = coords;
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

    public Vector2 getCoords() {
        return coords;
    }

    public boolean isSorted() {
        return isSorted;
    }

    public int getSortedPlace() {
        return sortedPlace;
    }

    /*
     * SETTERS
     */
    public void setSorted(boolean isSorted, int sortedPlace) {
        this.isSorted = isSorted;
        this.sortedPlace = sortedPlace;
    }

}
