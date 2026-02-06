package com.github.atollysis;

import com.badlogic.gdx.graphics.Texture;

public class Assets {

    /*
     * FIELDS
     */
    private final Texture texturePlayer = new Texture("doctor.png");
    private final Texture texturePatient = new Texture("patient.png");

    private final Texture textureFloor = new Texture("debug_floor.png");
    private final Texture textureWall = new Texture("debug_wall.png");

    /*
     * DEFAULT CONSTRUCTOR USED
     */

    /*
     * METHODS
     */
    public void disposeTextures() {
        texturePlayer.dispose();
        textureFloor.dispose();
        textureWall.dispose();
    }

    /*
     * GETTERS
     */
    public Texture playerTexture() {
        return texturePlayer;
    }

    public Texture patientTexture() {
        return texturePatient;
    }

    public Texture floorTexture() {
        return textureFloor;
    }

    public Texture wallTexture() {
        return textureWall;
    }

}
