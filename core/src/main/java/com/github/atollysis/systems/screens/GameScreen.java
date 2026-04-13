package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.*;
import com.github.atollysis.systems.renderers.GameInterface;
import com.github.atollysis.systems.renderers.GameRenderer;
import com.github.atollysis.systems.session.GameResult;
import com.github.atollysis.systems.session.GameSession;

public class GameScreen implements Screen {

    /*
     * FIELDS
     */
    private final Assets assets;
    private final SoundSystem soundSystem;
    private final GameSession gameSession;
    private final GameRenderer gameRenderer;
    private final GameInterface interfaceRenderer;
    private boolean paused = false;

    private final InputMultiplexer inputs = new InputMultiplexer();

    /*
     * CONSTRUCTOR
     */
    public GameScreen(Assets assets, SoundSystem soundSystem, Main main) {
        this.assets = assets;
        this.soundSystem = soundSystem;
        gameSession = new GameSession(this, assets, soundSystem, main);
        gameRenderer = new GameRenderer(assets);
        gameRenderer.centerCamera(gameSession.getPlayer());
        interfaceRenderer = new GameInterface(assets, this, main);

        inputs.addProcessor(interfaceRenderer.getStage());
        inputs.addProcessor(gameSession.getInputs());
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

    /*
     * PAUSE METHODS
     */

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        updateSoundSystem();
    }

    public void togglePaused() {
        this.paused = !this.paused;
        updateSoundSystem();
    }

    private void updateSoundSystem() {
        soundSystem.setStartLoopListener(paused);
        if (paused) {
            soundSystem.crossfadeTo(
                SoundSystem.Tracks.GAME_LOOP_PAUSED
            );
        }
        else {
            soundSystem.crossfadeTo(
                SoundSystem.Tracks.GAME_LOOP
            );
        }
    }

    /*
     * OVERRIDDEN METHODS
     */

    @Override
    public void show() {
        paused = false;
        Gdx.input.setInputProcessor(inputs);
        soundSystem.play(SoundSystem.Tracks.GAME_START);
    }

    @Override
    public void render(float delta) {
        // BACK
        if (!paused) {
            gameSession.updateState(delta, gameRenderer.getMouseToWorldCoords());
            gameRenderer.centerCamera(gameSession.getPlayer());
        }

        // FRONT
        ScreenUtils.clear(Color.BLACK);
        gameRenderer.render(gameSession, assets);
        interfaceRenderer.render(gameSession, gameRenderer, paused);
    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.handleResize(width, height);
        interfaceRenderer.handleResize(width, height);
    }

    @Override
    public void pause() {
        //
    }

    @Override
    public void resume() {
        //
    }

    @Override
    public void hide() {
        //
    }

    @Override
    public void dispose() {
        interfaceRenderer.dispose();
        gameRenderer.dispose();
        assets.disposeTextures();
    }

}
