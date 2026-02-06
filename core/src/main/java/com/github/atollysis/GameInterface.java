package com.github.atollysis;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.*;

public class GameInterface {

    /*
     * FIELDS
     */
    private final BitmapFont FONT = new BitmapFont(Gdx.files.internal("fonts/montserrat_128.fnt"));
    private final Label.LabelStyle STYLE_LBL = new Label.LabelStyle(FONT, Color.WHITE);

    private final Stage stage = new Stage(new ScreenViewport());
    // Actors
    private final Label lbl_timer;

    /*
     * CONSTRUCTOR
     */
    public GameInterface() {
//        FONT.getData().setScale(3.0f);

        lbl_timer = new Label(
            "0.00",
            STYLE_LBL);
//        lbl_timer.setAlignment(Align.center);
        float margin = lbl_timer.getHeight() / 1.5f;
        lbl_timer.setPosition(
            Gdx.graphics.getWidth() / 2f - lbl_timer.getWidth() / 2,
            Gdx.graphics.getHeight() - lbl_timer.getHeight() - margin);

        stage.addActor(lbl_timer);
    }

    /*
     * GDX METHODS
     */
    public void handleResize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        FONT.dispose();
    }

    /*
     * METHODS
     */
    public void render(float time) {
        lbl_timer.setText(String.format("%.2f", time));

        stage.act();
        stage.draw();
    }

}
