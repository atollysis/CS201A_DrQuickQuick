package com.github.atollysis.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.atollysis.systems.scores.Grade;

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

    private final Texture textureFloor = new Texture("tiles/debug_floor.png");
    private final Texture textureWall = new Texture("tiles/wall_2.png");

    private final Texture whitePixel;

//    private final Texture titleScreen = new Texture("title_screen.png");

    private final Texture logo = new Texture("drquickquick_logo.png");

    private final Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    private final ObjectMap<Grade, Texture> gradeTextureMap;

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

        gradeTextureMap = new ObjectMap<>();
        for (Grade g : Grade.values()) {
            String filepath = String.format("grades/grade_%s.png", g.toString().toLowerCase());
            Texture t = new Texture(Gdx.files.internal(filepath));
            gradeTextureMap.put(g, t);
        }

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
        for (Grade g : Grade.values())
            gradeTextureMap.get(g).dispose();
        logo.dispose();
        skin.dispose();
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

    public Texture getGradeTexture(Grade g) {
        return gradeTextureMap.get(g);
    }

//    public Texture titleScreen() {
//        return titleScreen;
//    }

    public Texture logo() {
        return logo;
    }

    public Skin skin() {
        return skin;
    }

}
