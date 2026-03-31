package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.systems.GameInterface;
import com.github.atollysis.systems.GameRenderer;
import com.github.atollysis.systems.GameSession;

public class GameScreen implements Screen {

    /*
     * FIELDS
     */
    private final Assets assets;
    private final GameSession gameSession;
    private final GameRenderer gameRenderer;
    private final GameInterface interfaceRenderer;

    /*
     * CONSTRUCTOR
     */
    public GameScreen(Assets assets) {
        this.assets = assets;
        gameSession = new GameSession(assets);
        gameRenderer = new GameRenderer(assets, gameSession.getPatientManager());
        gameRenderer.centerCamera(gameSession.getPlayer());
        interfaceRenderer = new GameInterface(assets);
    }

    /*
     * METHODS
     */
    @Override
    public void show() {
        // TODO
    }

    @Override
    public void render(float delta) {
        // BACK
        gameSession.updateState(delta, gameRenderer.getMouseToWorldCoords());
        gameRenderer.centerCamera(gameSession.getPlayer());

        // FRONT
        ScreenUtils.clear(0, 0, 0, 1);
        gameRenderer.render(gameSession, assets);
        interfaceRenderer.render(gameSession, gameRenderer);
    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.handleResize(width, height);
        interfaceRenderer.handleResize(width, height);
    }

    @Override
    public void pause() {
        // TODO
    }

    @Override
    public void resume() {
        // TODO
    }

    @Override
    public void hide() {
        // TODO
    }

    @Override
    public void dispose() {
        interfaceRenderer.dispose();
        gameRenderer.dispose();
        assets.disposeTextures();
    }

}
