package com.github.atollysis.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.github.atollysis.entities.EntityManager;
import com.github.atollysis.entities.Player;
import com.github.atollysis.maps.MapGenerator;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.screens.GameScreen;

public class GameSession {

    /*
     * FIELDS
     */
    private final TileMap tileMap;
    private final Player player;
    private final EntityManager entityManager;

    private final GameDifficulty difficulty = GameDifficulty.random();
    private float time = 0f;

    private final CollisionSystem collisionSystem;

    private final Inputs inputs;

    /*
     * CONSTRUCTOR
     */
    public GameSession(GameScreen screen, Assets assets, SoundSystem soundSystem, Main main) {
//        tileMap = MapLoader.loadMap();
        tileMap = MapGenerator.generateMap();
        player = new Player(tileMap);
        entityManager = new EntityManager(difficulty, tileMap, assets);

        collisionSystem = new CollisionSystem(tileMap);

        inputs = new Inputs(screen, entityManager, soundSystem, main);
        Gdx.input.setInputProcessor(inputs);
    }

    /*
     * METHODS
     */
    public void updateState(float delta, Vector3 mouseWorldCoords) {
        time += delta;
        player.handleInputUpdatePos(delta, collisionSystem);
        entityManager.updateHoveredPatient(mouseWorldCoords);
    }

    public GameResult createResults() {
        return new GameResult(this);
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

    public EntityManager getPatientManager() {
        return entityManager;
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
