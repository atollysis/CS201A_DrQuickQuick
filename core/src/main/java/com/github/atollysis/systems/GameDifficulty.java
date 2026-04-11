package com.github.atollysis.systems;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;

public enum GameDifficulty {
    EASY(
        2,
        new GridPoint2(20, 10),
        8,
        5,
        5
    ),
    AVERAGE(
        3,
        new GridPoint2(30, 20),
        16,
        10,
        30
    ),
    DIFFICULT(
        5,
        new GridPoint2(60, 30),
        32,
        20,
        80
    );

    /*
     * METHOD
     */
    public static GameDifficulty random() {
        var arr = GameDifficulty.values();
        int i = MathUtils.random(0, arr.length - 1);
        return arr[i];
    }

    /*
     * FIELDS
     */

    private final int patientDigitLength;
    private final GridPoint2 roomSize;
    private final int roomCount;
    private final int patientCount;
    private final int obstacleCount;

    /*
     * CONSTRUCTOR
     */

    private GameDifficulty(
        int patientDigitLength,
        GridPoint2 roomSize,
        int roomCount,
        int patientCount,
        int obstacleCount
    ) {
        this.patientDigitLength = patientDigitLength;
        this.roomSize = roomSize;
        this.roomCount = roomCount;
        this.patientCount = patientCount;
        this.obstacleCount = obstacleCount;
    }

    /*
     * GETTERS
     */

    public int getPatientDigitLength() {
        return patientDigitLength;
    }

    public GridPoint2 getRoomSize() {
        return roomSize;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public int getObstacleCount() {
        return obstacleCount;
    }

}
