package com.github.atollysis.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

    /*
     * FIELDS
     */
    private final Texture texturePlayer = new Texture("doctor.png");
    private final Texture texturePatient = new Texture("patient.png");

    private final Texture textureFloor = new Texture("debug_floor.png");
    private final Texture textureWall = new Texture("debug_wall.png");

    private final Texture whitePixel;

    /*
     * CONSTRUCTOR
     */
    public Assets() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        this.whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }

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

    public Texture whitePixel() {
        return whitePixel;
    }

}
