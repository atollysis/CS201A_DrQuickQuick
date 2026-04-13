package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.session.GameDifficulty;
import com.github.atollysis.systems.SoundSystem;

public class TitleScreen implements Screen {

    /*
     * FIELDS
     */
    private static final int BTN_WIDTH = 400;
    private static final int LAYOUT_PADDING = 10;
    private static final int PADDING = 20;

    private final Main main;
    private final Assets assets;
    private final SoundSystem soundSystem;

    private final Stage stage;

    /*
     * CONSTRUCTOR
     */
    public TitleScreen(
        Assets assets,
        SoundSystem soundSystem,
        Main main
    ) {
        this.main = main;
        this.assets = assets;
        this.soundSystem = soundSystem;
        stage = new Stage(new ScreenViewport());

        setupLayout();
        Gdx.input.setInputProcessor(stage);
    }

    private void setupLayout() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label description = newLabel(
            "A selection sort simulator!\n"
            + "Sort the patients from greatest\n"
            + "to lowest as fast as you can."
        );

        Label controls = newLabel(
            "WASD - Move\n"
            + "LEFT CLICK - Sort\n"
            + "RIGHT CLICK - Highlight\n"
            + "P - Pause\n"
            + "1/2/3 - Quick Start\n"
        );

        Table buttonTable = new Table();
        for (GameDifficulty diff : GameDifficulty.values()) {
            TextButton btn = new TextButton(diff.name(), assets.skin(), "menu");
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    main.newGame(diff);
                }
            });
            btn.pad(PADDING);
            buttonTable.add(btn)
                .width(BTN_WIDTH)
                .pad(PADDING)
                .left()
                .row();
        }
        TextButton btn_leaderboard = new TextButton("Leaderboards", assets.skin(), "special");
        btn_leaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.teLeaderboards();
            }
        });
        btn_leaderboard.pad(PADDING);
        buttonTable.add(btn_leaderboard)
            .width(BTN_WIDTH)
            .pad(PADDING)
            .left()
            .row();

        Table rightTable = new Table();
        rightTable.add(description)
            .expand()
            .fill()
            .pad(LAYOUT_PADDING)
            .row();
        rightTable.add(buttonTable)
            .left()
            .row();
        rightTable.add(controls)
            .expand()
            .fill()
            .pad(LAYOUT_PADDING);

        Image logo = new Image(assets.logo());

        // Final layout
        root.add(logo)
            .pad(PADDING)
            .expand()
            .center();
        root.add(rightTable)
            .expand()
            .fill()
            .pad(PADDING);
    }

    private Label newLabel(String txt) {
        Label lbl = new Label(txt, assets.skin(), "menu");
        lbl.setWrap(true);
        return lbl;
    }

    /*
     * METHODS
     */

    @Override
    public void show() {
        soundSystem.play(SoundSystem.Tracks.TITLE);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
//        soundSystem.update(delta);

        ScreenUtils.clear(GameConfig.white());
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        //
    }

    @Override
    public void resume() {
        //
    }

    @Override
    public void hide() {
        //
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
