package com.github.atollysis.systems.session;

import com.badlogic.gdx.utils.Array;
import com.github.atollysis.entities.Patient;
import com.github.atollysis.entities.EntityManager;
import com.github.atollysis.systems.GameConfig;

public class GameResult {

    /*
     * FIELDS
     */
    private final GameDifficulty difficulty;
    private final int population;
    private final Array<SortRecord> patients;
    private final float time;
    private final float percentSorted;
    private int maxUrgency = -1;

    /*
     * CONSTRUCTOR
     */
    public GameResult(GameSession gameSession) {
        EntityManager pMgr = gameSession.getPatientManager();

        difficulty = GameConfig.getDifficulty();
        population = pMgr.getPopulation();

        patients = new Array<>(population);
        for (int i = 0; i < population; i++) {
            patients.add(new SortRecord(
                pMgr.getPatientArray().get(i),
                pMgr.getPlayerSortedPatientArray().get(i)
            ));
        }

        time = gameSession.getTime();

        int correctlySorted = 0;
        for (Patient p : pMgr.getPlayerSortedPatientArray()) {
            if (p.getSortUrgency() > maxUrgency)
                maxUrgency = p.getSortUrgency();

            if (p.isProperlySorted())
                correctlySorted++;
        }
        percentSorted = (correctlySorted / (float) population) * 100;
    }

    /*
     * INNER RECORD CLASS
     */
    public record SortRecord(Patient sorted, Patient actual) { }

    /*
     * GETTERS
     */
    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public int getPopulation() {
        return population;
    }

    public Array<SortRecord> getPatients() {
        return patients;
    }

    public float getTime() {
        return time;
    }

    public float getPercentSorted() {
        return percentSorted;
    }

    public int getMaxUrgency() {
        return maxUrgency;
    }

}
