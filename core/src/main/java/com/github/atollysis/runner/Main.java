package com.github.atollysis.runner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.atollysis.systems.*;
import com.github.atollysis.entities.PatientManager;
import com.github.atollysis.entities.Player;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.maps.MapLoader;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    /*
     * FIELDS
     */
    private Assets assets;
    private GameRenderer gameRenderer;

    private TileMap currTileMap;
    private Player player;
    private PatientManager patientManager;

    private float time = 0f;
    private GameInterface interfaceRenderer;

    private CollisionSystem collisionSystem;

    private Inputs inputs;

    /*
     * METHODS
     */
    @Override
    public void create() {
        assets = new Assets();

        currTileMap = MapLoader.loadMap();
        player = new Player(currTileMap);
        patientManager = new PatientManager(currTileMap, assets);

        collisionSystem = new CollisionSystem(currTileMap);

        gameRenderer = new GameRenderer(assets, patientManager);
        gameRenderer.centerCamera(player);

        interfaceRenderer = new GameInterface(assets);

        inputs = new Inputs(patientManager);
        Gdx.input.setInputProcessor(inputs);


    }

    @Override
    public void render() {
        // BACK
        float delta = Gdx.graphics.getDeltaTime();
        time += delta;
        player.handleInputUpdatePos(delta, collisionSystem);
        patientManager.updateHoveredPatient(gameRenderer.getMouseToWorldCoords());
        gameRenderer.centerCamera(player);

        // FRONT
        ScreenUtils.clear(0, 0, 0, 1);
        gameRenderer.render(player, patientManager, currTileMap, assets);
        interfaceRenderer.render(time, patientManager, gameRenderer);
    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.handleResize(width, height);
        interfaceRenderer.handleResize(width, height);
    }

    @Override
    public void dispose() {
        interfaceRenderer.dispose();
        gameRenderer.dispose();
        assets.disposeTextures();
    }

}
