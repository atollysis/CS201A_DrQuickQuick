package com.github.atollysis.runner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.github.atollysis.systems.*;
import com.github.atollysis.systems.screens.GameScreen;
import com.github.atollysis.systems.screens.ResultScreen;
import com.github.atollysis.systems.screens.TitleScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    /*
     * FIELDS
     */
    private Assets assets;
    // Music
    private SoundSystem soundSystem;
    // Screens
    private GameScreen gameScreen;
    private TitleScreen titleScreen;

    /*
     * METHODS
     */
    @Override
    public void create() {
        assets = new Assets();
        GameConfig.setDifficulty(GameDifficulty.AVERAGE);

        soundSystem = new SoundSystem();

        titleScreen = new TitleScreen(assets, soundSystem, this);
        Gdx.input.setInputProcessor(titleScreen);
        this.setScreen(titleScreen);
        soundSystem.play(SoundSystem.Tracks.TITLE);
    }

    public void endGame() {
        GameResult results = gameScreen.getResults();
        Screen resultScreen = new ResultScreen(assets, soundSystem, results);
        this.setScreen(resultScreen);
        soundSystem.play(SoundSystem.Tracks.RESULTS);
    }

    public void newGame(GameDifficulty difficulty) {
        GameConfig.setDifficulty(difficulty);
        gameScreen = new GameScreen(assets, soundSystem, this);
        this.setScreen(gameScreen);
        soundSystem.play(SoundSystem.Tracks.GAME_START);
    }

    public void toTitleScreen() {
        Gdx.input.setInputProcessor(titleScreen);
        this.setScreen(titleScreen);
        soundSystem.play(SoundSystem.Tracks.TITLE);
    }

    @Override
    public void dispose() {
        soundSystem.dispose();
    }

}
