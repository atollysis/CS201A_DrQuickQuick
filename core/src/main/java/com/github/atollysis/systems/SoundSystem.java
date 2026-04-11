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

    private boolean fading = false;
    private boolean fadeIntoNext = false;
    private float fadeDuration;
    private float fadeTimer;
    private float fadeStartVolume;

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

    private void playTrack(Tracks track) {
        currTrack = track;
        currTrack.music().play();
    }

    private void stopTrack() {
        if (currTrack != null) {
            currTrack.music().stop();
            currTrack.resetVolume();
        }
    }

    public void play(Tracks track) {
        if (currTrack == null) {
            playTrack(track);
            return;
        }

        nextTrack = track;
        fadeIntoNext = true;
        fadeOut(FADE_DURATION);
    }

    public void update(float delta) {
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
