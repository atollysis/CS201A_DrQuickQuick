package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.*;
import com.github.atollysis.systems.renderers.GameInterface;
import com.github.atollysis.systems.renderers.GameRenderer;

public class GameScreen implements Screen {

    /*
     * FIELDS
     */
    private final Assets assets;
    private final SoundSystem soundSystem;
    private final GameSession gameSession;
    private final GameRenderer gameRenderer;
    private final GameInterface interfaceRenderer;

    /*
     * CONSTRUCTOR
     */
    public GameScreen(Assets assets, SoundSystem soundSystem, Main main) {
        this.assets = assets;
        this.soundSystem = soundSystem;
        gameSession = new GameSession(this, assets, soundSystem, main);
        gameRenderer = new GameRenderer(assets);
        gameRenderer.centerCamera(gameSession.getPlayer());
        interfaceRenderer = new GameInterface(assets);
    }

    /*
     * METHODS
     */
    public GameResult getResults() {
        return gameSession.createResults();
    }

    public void debug_handleScroll(float x, float y) {
        gameRenderer.debug_handleScroll(x, y);
    }

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
        ScreenUtils.clear(Color.BLACK);
        gameRenderer.render(gameSession, assets);
        interfaceRenderer.render(gameSession, gameRenderer);
        soundSystem.update(delta);
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
