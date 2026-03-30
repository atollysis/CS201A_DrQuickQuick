package com.github.atollysis.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.maps.TileType;

public class PatientManager {

    /*
     * FIELDS
     */
    private static final int PATIENT_COUNT = 10;
    private static final int MAX_SORT_NUM_DISPLACEMENT = 100 / PATIENT_COUNT;

    private final Array<Patient> patientArray = new Array<>(PATIENT_COUNT);

    /*
     * CONSTRUCTOR
     */
    public PatientManager(TileMap tileMap, Assets assets, float tileSize) {
        int sortDisplay = 0;

        // Randomly generate and populate patient array
        for (int i = 0; i < PATIENT_COUNT; i++) {
            sortDisplay += MathUtils.random(1, MAX_SORT_NUM_DISPLACEMENT);
            Patient patient = new Patient(i, sortDisplay);
            patient.setCoords(tileMap.getRandomCoords(patient));
            patientArray.add(patient);
        }
    }

    /*
     * GETTERS
     */
    public Array<Patient> getPatientArray() {
        return patientArray;
    }
}
