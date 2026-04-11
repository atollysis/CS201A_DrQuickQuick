package com.github.atollysis.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Assets {

    /*
     * FIELDS
     */
    private final Texture texturePlayer = new Texture("entities/ducktor.png");
//    private final Texture texturePatient = new Texture("entities/patient1.png");

    private final Array<Texture> texturePatients = new Array<>(new Texture[] {
        new Texture("entities/patient1.png"),
        new Texture("entities/patient2.png"),
        new Texture("entities/patient3.png")
    });

    private final ObjectMap<Texture, Float> textureObstacles;

    private final Texture textureFloor = new Texture("debug_floor.png");
    private final Texture textureWall = new Texture("wall_2.png");

    private final Texture whitePixel;

    private final Texture titleScreen = new Texture("title_screen.png");

    /*
     * CONSTRUCTOR
     */
    public Assets() {
        textureObstacles = new ObjectMap<>();
        textureObstacles.put(
            new Texture("entities/obstacles/chair.png"),
            32f
        );
        textureObstacles.put(
            new Texture("entities/obstacles/dispenser.png"),
            24f
        );
        textureObstacles.put(
            new Texture("entities/obstacles/extinguisher.png"),
            20f
        );
        textureObstacles.put(
            new Texture("entities/obstacles/plant.png"),
            24f
        );

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
        whitePixel.dispose();
        for (Texture t : textureObstacles.keys())
            t.dispose();
        for (Texture t : texturePatients)
            t.dispose();
    }

    /*
     * GETTERS
     */
    public Texture playerTexture() {
        return texturePlayer;
    }

    public Texture patientTexture() {
        return texturePatients.get(0);
    }

    public Texture randomPatientTexture() {
        int index = MathUtils.random(0, texturePatients.size - 1);
        return texturePatients.get(index);
    }

    public Texture randomObstacleTexture() {
        int index = MathUtils.random(0, textureObstacles.size - 1);
        return textureObstacles.keys().toArray().get(index);
    }

    public float getObstacleWidth(Texture key) {
        return textureObstacles.get(key);
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

    public Texture titleScreen() {
        return titleScreen;
    }

}
