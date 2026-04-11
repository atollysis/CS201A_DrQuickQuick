package com.github.atollysis.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.GameDifficulty;

public class EntityManager {

    /*
     * FIELDS
     */
    private static final Color CLR_SORTED = new Color(0.25f, 0.25f, 0.25f, 1f);
    private static final Color CLR_HIGHLIGHTED = new Color(0.5f, 1f, 0.2f, 1f);

    private static int PATIENT_COUNT = GameConfig.getDifficulty().getPatientCount();
    private static int OBSTACLE_COUNT = GameConfig.getDifficulty().getObstacleCount();
    private static int MAX_SORT_NUM_DISPLACEMENT = (int) (GameConfig.getMaxPatientLevel() / PATIENT_COUNT);

    private final Array<Entity> entityArray = new Array<>(PATIENT_COUNT + OBSTACLE_COUNT);

    private final Array<Obstacle> obstacleArray = new Array<>(OBSTACLE_COUNT);
    private final Array<Patient> patientArray = new Array<>(PATIENT_COUNT);
    private final Array<Patient> playerSortedPatientArray = new Array<>(PATIENT_COUNT);

    private Patient hoveredPatient = null;
    private Patient highlightedPatient = null;

    /*
     * CONSTRUCTOR
     */
    public EntityManager(GameDifficulty difficulty, TileMap tileMap, Assets assets) {
        int sortDisplay = 0;

        // Randomly generate and populate patient array
        for (int i = 0; i < PATIENT_COUNT; i++) {
            sortDisplay += MathUtils.random(1, MAX_SORT_NUM_DISPLACEMENT);
            int sortIndex = PATIENT_COUNT - i - 1;
            Patient patient = new Patient(assets, sortIndex, sortDisplay);
            patient.setCoords(tileMap.getRandomCoords(patient));
            patientArray.add(patient);
            entityArray.add(patient);
        }

        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            Obstacle obstacle = new Obstacle(assets);
            obstacle.setCoords(tileMap.getRandomCoords(obstacle));
            obstacleArray.add(obstacle);
            entityArray.add(obstacle);
        }

        entityArray.sort((e1, e2) -> Float.compare(
            e1.getVisualY(),
            e2.getVisualY()
        ));
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

        if (highlightedPatient != null && highlightedPatient == hoveredPatient)
            highlightedPatient = null;
        hoveredPatient = null;
    }

    public void highlightPatient() {
        if (hoveredPatient == null)
            return;

        // Idempotent
        if (highlightedPatient != null && highlightedPatient == hoveredPatient) {
            highlightedPatient = null;
            return;
        }

        highlightedPatient = hoveredPatient;
    }

    public void debug_showResults() {
        System.out.println("INDEX | URGENCY | PLACE | SORTED?");
        for (int i = 0; i < PATIENT_COUNT; i++) {
            Patient p = patientArray.get(i);
            System.out.format(
                "%5d | %7d | %5d | %7b\n",
                p.getSortId(),
                p.getSortUrgency(),
                p.getSortedPlace(),
                p.isProperlySorted()
            );
        }
    }

    public boolean allPatientsSorted() {
        return playerSortedPatientArray.size == PATIENT_COUNT;
    }

    public String getPatientsLeftString() {
        return String.format(
            "%d/%d left",
            PATIENT_COUNT - playerSortedPatientArray.size,
            PATIENT_COUNT
        );
    }

    /*
     * GETTERS
     */
    public int getPopulation() {
        return PATIENT_COUNT;
    }

    public Array<Patient> getPatientArray() {
        return patientArray;
    }

    public Array<Patient> getPlayerSortedPatientArray() {
        return playerSortedPatientArray;
    }

    public Array<Obstacle> getObstacleArray() {
        return obstacleArray;
    }

    public Array<Entity> getEntityArray() {
        return entityArray;
    }

    public Patient getHoveredPatient() {
        return hoveredPatient;
    }

    public Patient getHighlightedPatient() {
        return highlightedPatient;
    }

    /*
     * SETTERS
     */
    public static void setValues(GameDifficulty difficulty) {
        PATIENT_COUNT = difficulty.getPatientCount();
        OBSTACLE_COUNT = difficulty.getObstacleCount();
        MAX_SORT_NUM_DISPLACEMENT = (int) (GameConfig.getMaxPatientLevel() / PATIENT_COUNT);
    }

}
