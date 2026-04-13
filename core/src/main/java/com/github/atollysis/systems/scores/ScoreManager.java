package com.github.atollysis.systems.scores;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.github.atollysis.systems.screens.LeaderboardScreen;
import com.github.atollysis.systems.session.GameDifficulty;

import java.util.Comparator;

public class ScoreManager {

    /*
     * FIELDS
     */
    private static final String FILENAME = "leaderboard.json";
    private final Json json = new Json();
    private Array<Score> scores;

    /*
     * CONSTRUCTOR
     */
    public ScoreManager() {
        load();
    }

    /*
     * METHODS
     */
    public void add(Score score) {
        scores.add(score);
        save();
    }

    /*
     * If null, shows all scores.
     */
    public Array<Score> filterBy(GameDifficulty difficulty) {
        if (difficulty == null)
            return scores;

        Array<Score> filtered = new Array<>();

        for (Score s : scores) {
            if (s.getDifficulty() == difficulty)
                filtered.add(s);
        }

        return filtered;
    }

    public void sortBy(LeaderboardScreen.LeaderboardSort sort, Array<Score> list) {
        switch (sort) {
            case TIME       -> sortByTime(list);
            case PCT_SORTED -> sortByPercent(list);
            case RECENT     -> sortByRecent(list);
        }
    }

    private void sortByRecent(Array<Score> list) {
        list.sort(Comparator.comparing(Score::getTimestamp).reversed());
    }

    private void sortByTime(Array<Score> list) {
        list.sort(Comparator.comparing(Score::getTimeTaken));
    }

    private void sortByPercent(Array<Score> list) {
        // Percent > time > timestamp
        list.sort(Comparator.naturalOrder());
    }

    // PRIVATE

    private void load() {
        FileHandle file = Gdx.files.local(FILENAME);

        if (!file.exists()) {
            scores = new Array<>();
            return;
        }

        scores = json.fromJson(Array.class, Score.class, file);
    }

    private void save() {
        FileHandle file = Gdx.files.local(FILENAME);
        file.writeString(json.prettyPrint(scores), false);
    }

}
