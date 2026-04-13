package com.github.atollysis.systems.scores;

import com.badlogic.gdx.utils.TimeUtils;
import com.github.atollysis.systems.session.GameDifficulty;
import com.github.atollysis.systems.session.GameResult;

public class Score implements Comparable<Score> {

    /*
     * FIELDS
     */
    private String name;
    private long timestamp;
    private GameDifficulty difficulty;
    private float timeTaken;
    private float percentSorted;

    /*
     * CONSTRUCTORS
     */

    public Score(String name, GameResult result) {
        this.name = name;
        this.timestamp = TimeUtils.millis();
        this.difficulty = result.getDifficulty();
        this.timeTaken = result.getTime();
        this.percentSorted = result.getPercentSorted();
    }

    public Score() {
        // No-arg constructor for the json method
    }

    /*
     * METHODS
     */
    public boolean isComplete() {
        return name != null
            && timestamp > 0
            && difficulty != null
            && timeTaken > 0;
    }

    /*
     * GETTERS
     */

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public float getTimeTaken() {
        return timeTaken;
    }

    public float getPercentSorted() {
        return percentSorted;
    }

    /*
     * SETTERS
     */

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDifficulty(GameDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setTimeTaken(float timeTaken) {
        this.timeTaken = timeTaken;
    }

    public void setPercentSorted(float percentSorted) {
        this.percentSorted = percentSorted;
    }

    /*
     * OVERRIDDEN METHODS
     */

    @Override
    public int compareTo(Score o) {
        // Returns are all negative to reverse them
        int result = Float.compare(percentSorted, o.percentSorted);
        if (result != 0)
            return -result;
        // If same, sort by time (reversed)
        result = Float.compare(o.timeTaken, timeTaken);
        if (result != 0)
            return -result;
        // Else (very rare float equality), sort by recency
        return -Float.compare(timestamp, o.timestamp);
    }

}
