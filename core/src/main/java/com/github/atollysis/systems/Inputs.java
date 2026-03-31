package com.github.atollysis.systems;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.github.atollysis.entities.PatientManager;

public class Inputs implements InputProcessor {

    /*
     * FIELDS
     */
    private final PatientManager patientManager;

    /*
     * CONSTRUCTOR
     */
    public Inputs(PatientManager patientManager) {
        this.patientManager = patientManager;
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
        if (button == Input.Buttons.LEFT && patientManager.getHoveredPatient() != null) {
            patientManager.sortPatient();
            return true;
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
        return false;
    }
}
