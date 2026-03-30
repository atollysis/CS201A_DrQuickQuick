package com.github.atollysis.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.maps.TileType;
import com.github.atollysis.systems.GameConfig;

public class PatientManager {

    /*
     * FIELDS
     */
    private static final int PATIENT_COUNT = 10;
    private static final int MAX_SORT_NUM_DISPLACEMENT = (int) (GameConfig.getMaxPatientLevel() / PATIENT_COUNT);

    private final Array<Patient> patientArray = new Array<>(PATIENT_COUNT);
    private Patient hoveredPatient = null;

    /*
     * CONSTRUCTOR
     */
    public PatientManager(TileMap tileMap, Assets assets, float tileSize) {
        int sortDisplay = 0;

        // Randomly generate and populate patient array
        for (int i = 0; i < PATIENT_COUNT; i++) {
            sortDisplay += MathUtils.random(1, MAX_SORT_NUM_DISPLACEMENT);
            Patient patient = new Patient(assets, i, sortDisplay);
            patient.setCoords(tileMap.getRandomCoords(patient));
            patientArray.add(patient);
        }
    }

    /*
     * METHODS
     */
    public void updateHoveredPatient(Vector3 mouseCoords) {
        for (Patient p : this.patientArray) {
            if (p.boundsContains(mouseCoords.x, mouseCoords.y)) {
                // Prev
                this.deselectHoveredPatient();
                // Next
                p.getSprite().setColor(
                    1f, 1f, 0.5f, 1f
                );

                this.hoveredPatient = p;
                return;
            }
        }
        this.deselectHoveredPatient();
    }

    private void deselectHoveredPatient() {
        if (this.hoveredPatient != null) {
            this.hoveredPatient.getSprite().setColor(
                Color.WHITE
            );
        }
        this.hoveredPatient = null;
    }

    /*
     * GETTERS
     */
    public Array<Patient> getPatientArray() {
        return patientArray;
    }

    public Patient getHoveredPatient() {
        return hoveredPatient;
    }

}
