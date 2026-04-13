package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.session.GameDifficulty;
import com.github.atollysis.systems.SoundSystem;

public class _TitleScreen implements Screen, InputProcessor {

    /*
     * FIELDS
     */

//    private final OrthographicCamera camera;
//    private final Stage stage = new Stage(new ScreenViewport());
    private final Main main;
    private final Assets assets;
    private final SoundSystem soundSystem;
    private final SpriteBatch batch;

    /*
     * CONSTRUCTOR
     */

    public _TitleScreen(Assets assets, SoundSystem soundSystem, Main main) {
//        camera = new OrthographicCamera();
//        camera.setToOrtho(
//            false,
//            GameConfig.getWorldWidth(),
//            GameConfig.getWorldHeight()
//        );
        this.main = main;
        this.assets = assets;
        this.soundSystem = soundSystem;
        batch = new SpriteBatch();
    }

    /*
     * METHODS
     */

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GameConfig.white());
        batch.begin();
//        batch.draw(assets.titleScreen(), 0, 0);
        batch.end();
        soundSystem.update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    /*
     * INPUTS
     */

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1:
                main.newGame(GameDifficulty.EASY);
                return true;
            case Input.Keys.NUM_2:
                main.newGame(GameDifficulty.AVERAGE);
                return true;
            case Input.Keys.NUM_3:
                main.newGame(GameDifficulty.DIFFICULT);
                return true;
            case Input.Keys.TAB:
                GameConfig.toggleDebugMode();
                return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
