package com.github.atollysis.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.github.atollysis.entities.PatientManager;
import com.github.atollysis.entities.Player;
import com.github.atollysis.maps.MapLoader;
import com.github.atollysis.maps.TileMap;

public class GameSession {

    /*
     * FIELDS
     */
    private final TileMap tileMap;
    private final Player player;
    private final PatientManager patientManager;

    private float time = 0f;

    private final CollisionSystem collisionSystem;

    private final Inputs inputs;

    /*
     * CONSTRUCTOR
     */
    public GameSession(Assets assets) {
        tileMap = MapLoader.loadMap();
        player = new Player(tileMap);
        patientManager = new PatientManager(tileMap, assets);

        collisionSystem = new CollisionSystem(tileMap);

        inputs = new Inputs(patientManager);
        Gdx.input.setInputProcessor(inputs);
    }

    /*
     * METHODS
     */
    public void updateState(float delta, Vector3 mouseWorldCoords) {
        time += delta;
        player.handleInputUpdatePos(delta, collisionSystem);
        patientManager.updateHoveredPatient(mouseWorldCoords);
    }

    /*
     * GETTERS
     */
    public TileMap getTileMap() {
        return tileMap;
    }

    public Player getPlayer() {
        return player;
    }

    public PatientManager getPatientManager() {
        return patientManager;
    }

    public float getTime() {
        return time;
    }

    public CollisionSystem getCollisionSystem() {
        return collisionSystem;
    }

    public Inputs getInputs() {
        return inputs;
    }

    /*
     * SETTERS
     */
    public void setTime(float time) {
        this.time = time;
    }

}
