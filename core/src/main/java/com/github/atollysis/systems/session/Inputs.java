package com.github.atollysis.systems.session;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.github.atollysis.entities.EntityManager;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.SoundSystem;
import com.github.atollysis.systems.screens.GameScreen;

public class Inputs implements InputProcessor {

    /*
     * FIELDS
     */
    private final GameScreen screen;
    private final EntityManager entityManager;
    private final SoundSystem soundSystem;
    private final Main main;

    /*
     * CONSTRUCTOR
     */
    public Inputs(
        GameScreen screen,
        EntityManager entityManager,
        SoundSystem soundSystem,
        Main main
    ) {
        this.screen = screen;
        this.entityManager = entityManager;
        this.soundSystem = soundSystem;
        this.main = main;
    }

    /*
     * METHODS
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.P:
                if (!soundSystem.isCrossfading())
                    screen.togglePaused();
                return true;
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
            case Input.Keys.BACKSPACE:
                if (!GameConfig.isInDebugMode())
                    return false;
                main.toTitleScreen();
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
        if (button == Input.Buttons.LEFT && entityManager.getHoveredPatient() != null) {
            entityManager.sortPatient();
            if (!entityManager.allPatientsSorted()) {
                soundSystem.playSFXHeal();
            } else {
                if (GameConfig.isInDebugMode())
                    entityManager.debug_showResults();
                soundSystem.playSFXCheer();
                main.endGame();
            }
            return true;
        }
        else if (button == Input.Buttons.RIGHT && entityManager.getHoveredPatient() != null) {
            entityManager.highlightPatient();
            soundSystem.playSFXHighlight(entityManager.getHighlightedPatient() == null);
        }
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
        if (GameConfig.isInDebugMode()) {
            screen.debug_handleScroll(amountX, amountY);
            return true;
        }
        return false;
    }
}
