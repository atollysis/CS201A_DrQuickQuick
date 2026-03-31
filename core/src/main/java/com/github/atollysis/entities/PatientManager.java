package com.github.atollysis.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.systems.GameConfig;

public class PatientManager {

    /*
     * FIELDS
     */
    private static final Color CLR_SORTED = new Color(0.25f, 0.25f, 0.25f, 1f);
    private static final Color CLR_HIGHLIGHTED = new Color(0.5f, 1f, 0.2f, 1f);

    private static final int PATIENT_COUNT = 5;
    private static final int MAX_SORT_NUM_DISPLACEMENT = (int) (GameConfig.getMaxPatientLevel() / PATIENT_COUNT);

    private final Array<Patient> patientArray = new Array<>(PATIENT_COUNT);
    private final Array<Patient> playerSortedPatientArray = new Array<>(PATIENT_COUNT);

    private Patient hoveredPatient = null;

    /*
     * CONSTRUCTOR
     */
    public PatientManager(TileMap tileMap, Assets assets) {
        int sortDisplay = 0;

        // Randomly generate and populate patient array
        for (int i = 0; i < PATIENT_COUNT; i++) {
            sortDisplay += MathUtils.random(1, MAX_SORT_NUM_DISPLACEMENT);
            int sortIndex = PATIENT_COUNT - i - 1;
            Patient patient = new Patient(assets, sortIndex, sortDisplay);
            patient.setCoords(tileMap.getRandomCoords(patient));
            patientArray.add(patient);
        }
    }

    /*
     * METHODS
     */
    public void updateHoveredPatient(Vector3 mouseCoords) {
        for (Patient p : this.patientArray) {
            if (p.isSorted())
                continue;

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

    public void sortPatient() {
        if (hoveredPatient == null)
            return;

        hoveredPatient.setSorted(
            true,
            playerSortedPatientArray.size
        );
        hoveredPatient.getSprite().setColor(CLR_SORTED);

        playerSortedPatientArray.add(hoveredPatient);

        hoveredPatient = null;

        if (playerSortedPatientArray.size == PATIENT_COUNT)
            debug_showResults();
    }

    private void debug_showResults() {
        System.out.println("INDEX | URGENCY | PLACE | SORTED?");
        for (int i = 0; i < PATIENT_COUNT; i++) {
            Patient p = patientArray.get(i);
            System.out.format(
                "%5d | %7d | %5d | %7b\n",
                p.getSortId(),
                p.getSortUrgency(),
                p.getSortedPlace(),
                p.isProperlySorted());
        }
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
