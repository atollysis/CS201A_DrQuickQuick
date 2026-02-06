package com.github.atollysis.runner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.atollysis.Assets;
import com.github.atollysis.GameInterface;
import com.github.atollysis.GameRenderer;
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

    /*
     * METHODS
     */
    @Override
    public void create() {
        assets = new Assets();

        currTileMap = MapLoader.loadMap();
        player = new Player();
        patientManager = new PatientManager(currTileMap, assets, GameRenderer.getTileSize());

        gameRenderer = new GameRenderer(assets, patientManager);
        gameRenderer.centerCamera(player);

        interfaceRenderer = new GameInterface();
    }

    @Override
    public void render() {
        // BACK
        float delta = Gdx.graphics.getDeltaTime();
        time += delta;
        player.handleInputUpdatePos(delta);
        gameRenderer.centerCamera(player);

        // FRONT
        ScreenUtils.clear(0, 0, 0, 1);
        gameRenderer.render(player, currTileMap, assets);
        interfaceRenderer.render(time);
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
