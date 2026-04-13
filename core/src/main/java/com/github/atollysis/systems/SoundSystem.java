package com.github.atollysis.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class SoundSystem {

    /*
     * FIELDS
     */

    // Music
    private static final float FADE_DURATION = 0.8f;

    private Tracks currTrack;
    private Tracks nextTrack;

    // Track fading
    private boolean fading = false;
    private boolean fadeIntoNext = false;
    private float fadeDuration;
    private float fadeTimer;
    private float fadeStartVolume;

    // Crossfading (paused)
    private boolean crossfading = false;
    private Tracks crossFrom;
    private Tracks crossTo;
    private float crossTimer;
    private float crossDuration = 0.4f;
    private float crossFromStart = 1f;
    private float crossToStart = 0f;

    // SFX
    private static final int HEAL_SOUNDS_COUNT = 10; //
    private static final float HEAL_VOLUME = 0.2f;
    private static final float SFX_VOLUME = 0.2f;

    private final Array<Sound> healSfxList = new Array<>(HEAL_SOUNDS_COUNT);
    private final Sound patientHighlight;
    private final Sound patientUnhighlight;
    private final Sound levelCompleteCheer;

    /*
     * DEFAULT CONSTRUCTOR USED
     */
    public SoundSystem() {
        // Music
        Tracks.GAME_START.music().setOnCompletionListener(m -> {
            playTrack(Tracks.GAME_LOOP);
        });
        // SFX
        for (int i = 1; i <= HEAL_SOUNDS_COUNT; i++) {
            String path = String.format("audio/sfx/patient_heal_%02d.wav", i);
            Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
            healSfxList.add(s);
        }
        patientHighlight = Gdx.audio.newSound(Gdx.files.internal(
            "audio/sfx/patient_highlight.wav"
        ));
        patientUnhighlight = Gdx.audio.newSound(Gdx.files.internal(
            "audio/sfx/patient_unhighlight.wav"
        ));
        levelCompleteCheer = Gdx.audio.newSound(Gdx.files.internal(
            "audio/sfx/level_complete.mp3"
        ));
    }

    /*
     * SFX METHODS
     */

    public void playSFXHeal() {
        int i = MathUtils.random(0, HEAL_SOUNDS_COUNT - 1);
        healSfxList.get(i).play(HEAL_VOLUME);
    }

    public void playSFXHighlight(boolean highlightPatientIsNull) {
        if (highlightPatientIsNull)
            patientUnhighlight.play(SFX_VOLUME);
        else
            patientHighlight.play(SFX_VOLUME);
    }

    public void playSFXCheer() {
        levelCompleteCheer.play(SFX_VOLUME);
    }

    /*
     * MUSIC METHODS
     */

    public void play(Tracks track) {
        if (currTrack == null) {
            playTrack(track);
            return;
        }

        nextTrack = track;
        fadeIntoNext = true;
        fadeOut(FADE_DURATION);
    }

    private void playTrack(Tracks track) {
        if (track == Tracks.GAME_LOOP) {
            Tracks.GAME_LOOP_PAUSED.setVolume(0f);
            Tracks.GAME_LOOP_PAUSED.music.setPosition(0f);
            Tracks.GAME_LOOP_PAUSED.music.play();
        } else if (track == Tracks.GAME_LOOP_PAUSED) {
            Tracks.GAME_LOOP.setVolume(0f);
            Tracks.GAME_LOOP.music.setPosition(0f);
            Tracks.GAME_LOOP.music.play();
        }
        currTrack = track;
        currTrack.music().play();
    }

    private void stopTrack() {
        if (currTrack == null)
            return;

        if (currTrack == Tracks.GAME_LOOP) {
            Tracks.GAME_LOOP_PAUSED.music().stop();
            Tracks.GAME_LOOP_PAUSED.resetVolume();
        } else if (currTrack == Tracks.GAME_LOOP_PAUSED) {
            Tracks.GAME_LOOP.music().stop();
            Tracks.GAME_LOOP.resetVolume();
        }
        currTrack.music().stop();
        currTrack.resetVolume();
    }

    public void update(float delta) {
        // Pause Crossfade
        if (crossfading) {
            crossTimer += delta;

            float t = Math.min(1f, crossTimer / crossDuration);

            float fromVol = crossFromStart * (1f - t);
            float toVol   = crossToStart + (1f - crossToStart) * t;

            crossFrom.setVolume(fromVol * crossFrom.baseVolume);
            crossTo.setVolume(toVol * crossTo.baseVolume);

            if (t >= 1f) {
                crossfading = false;
                crossFrom.setVolume(0f);
                crossTo.resetVolume();
                currTrack = crossTo;
            }

            return;
        }

        // Track Fade
        if (!fading || currTrack == null)
            return;

        fadeTimer += delta;

        float t = fadeTimer / fadeDuration;
        float newVolume = fadeStartVolume * (1f - t);
        currTrack.setVolume(Math.max(0f, newVolume));

        if (t >= 1f) {
            fading = false;
            stopTrack();

            if (fadeIntoNext && nextTrack != null) {
                playTrack(nextTrack);
            }
        }
    }

    public void fadeOut(float duration) {
        if (currTrack == null)
            return;

        fading = true;
        fadeDuration = duration;
        fadeTimer = 0f;
        fadeStartVolume = currTrack.volume();
    }

    public void crossfadeTo(Tracks to) {
        if (currTrack == null
            || currTrack == to
            || currTrack == Tracks.GAME_START)
            return;

        crossfading = true;

        crossFrom = currTrack;
        crossTo = to;

        crossTimer = 0f;

        crossFromStart = currTrack.baseVolume;
        crossToStart = to.baseVolume;

        if (!crossTo.music.isPlaying()) {
            crossTo.setVolume(0f);
            crossTo.music.play();
        }
    }

    public void setStartLoopListener(boolean paused) {
        Tracks.GAME_START.music.setOnCompletionListener(m -> {
            playTrack(paused ?
                Tracks.GAME_LOOP_PAUSED :
                Tracks.GAME_LOOP
            );
        });
    }

    public boolean isCrossfading() {
        return crossfading;
    }

    public void dispose() {
        Tracks.disposeAll();
        patientHighlight.dispose();
        patientUnhighlight.dispose();
        levelCompleteCheer.dispose();
        for (Sound s : healSfxList)
            s.dispose();
    }

    /*
     * MUSIC ENUM
     */
    public enum Tracks {
        GAME_START(
            "audio/music/quickquick_game-intro.ogg",
            0.2f,
            false
        ),
        GAME_LOOP(
            "audio/music/quickquick_game-loop.ogg",
            0.2f,
            true
        ),
        GAME_LOOP_PAUSED(
            "audio/music/quickquick_game-loop-paused.ogg",
            0.2f,
            true
        ),
        TITLE(
            "audio/music/quickquick_title.ogg",
            0.1f,
            true
        ),
        RESULTS(
            "audio/music/quickquick_results.ogg",
            0.4f,
            true
        );

        // Fields
        private final Music music;
        private final float baseVolume;

        // Constructor
        private Tracks(String path, float volume, boolean isLooping) {
            this.music = Gdx.audio.newMusic(Gdx.files.internal(path));
            this.baseVolume = volume;
            music.setVolume(volume);
            music.setLooping(isLooping);
        }

        // Getters
        public Music music() {
            return music;
        }

        public float volume() {
            return baseVolume;
        }

        // Setters
        public void setVolume(float v) {
            music.setVolume(v);
        }

        public void resetVolume() {
            music.setVolume(baseVolume);
        }

        // GDX Method
        public static void disposeAll() {
            for (Tracks t : Tracks.values())
                t.music.dispose();
        }
    }

}
