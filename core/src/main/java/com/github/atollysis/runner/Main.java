package com.github.atollysis.runner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.github.atollysis.systems.*;
import com.github.atollysis.systems.scores.Score;
import com.github.atollysis.systems.scores.ScoreManager;
import com.github.atollysis.systems.screens.GameScreen;
import com.github.atollysis.systems.screens.LeaderboardScreen;
import com.github.atollysis.systems.screens.ResultScreen;
import com.github.atollysis.systems.screens.TitleScreen;
import com.github.atollysis.systems.session.GameDifficulty;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    /*
     * FIELDS
     */
    // Systems
    private Assets assets;
    private SoundSystem soundSystem;
    private ScoreManager scoreManager;
    // Screens
    private GameScreen gameScreen;
    private TitleScreen titleScreen;
    private LeaderboardScreen leaderboardScreen;

    /*
     * METHODS
     */
    @Override
    public void create() {
        assets = new Assets();
        GameConfig.setDifficulty(GameDifficulty.AVERAGE);
        scoreManager = new ScoreManager();
        soundSystem = new SoundSystem();

        leaderboardScreen = new LeaderboardScreen(assets, scoreManager, this);

        titleScreen = new TitleScreen(assets, soundSystem, this);
//        Gdx.input.setInputProcessor(titleScreen);
        this.setScreen(titleScreen);
        soundSystem.play(SoundSystem.Tracks.TITLE);
    }

    public void endGame() {
        Screen resultScreen = new ResultScreen(
            assets,
            soundSystem,
            gameScreen.getResults(),
            this
        );
        this.setScreen(resultScreen);
        soundSystem.play(SoundSystem.Tracks.RESULTS);
    }

    public void newGame(GameDifficulty difficulty) {
        GameConfig.setDifficulty(difficulty);
        gameScreen = new GameScreen(assets, soundSystem, this);
        this.setScreen(gameScreen);
//        soundSystem.play(SoundSystem.Tracks.GAME_START);
    }

    public void toTitleScreen() {
//        Gdx.input.setInputProcessor(titleScreen);
//        soundSystem.play(SoundSystem.Tracks.TITLE);
        this.setScreen(titleScreen);
    }

    public void teLeaderboards() {
        this.setScreen(leaderboardScreen);
    }

    public void newScore(Score score) {
        scoreManager.add(score);
    }

    /*
     * OVERRIDDEN METHODS
     */

    @Override
    public void render() {
        soundSystem.update(Gdx.graphics.getDeltaTime());
        super.render();
    }

    @Override
    public void dispose() {
        soundSystem.dispose();
    }

}
