package com.github.atollysis.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.atollysis.entities.Patient;
import com.github.atollysis.entities.PatientManager;

public class GameInterface {

    /*
     * FIELDS
     */
    private final float BOX_PADDING = 30f;
    private final String FORMAT_STRING = "%0" + GameConfig.getMaxDigitLength() + "d";

    private final BitmapFont FONT = new BitmapFont(Gdx.files.internal("fonts/montserrat_128.fnt"));
    private final Label.LabelStyle STYLE_TIMER = new Label.LabelStyle(FONT, Color.WHITE);
    private final Label.LabelStyle STYLE_LBL = new Label.LabelStyle(FONT, Color.BLACK);
    private final GlyphLayout txtLayout = new GlyphLayout();

    private final Stage stage = new Stage(new ScreenViewport());
    // Actors
    private final Label lbl_timer;
    private final Label lbl_hoveredUrgency;

    // Textures
    private final Assets assets;

    /*
     * CONSTRUCTOR
     */
    public GameInterface(Assets assets) {
//        FONT.getData().setScale(3.0f);

        this.assets = assets;

        lbl_timer = new Label(
            "0.00",
            STYLE_TIMER);
//        lbl_timer.setAlignment(Align.center);
        float margin = lbl_timer.getHeight() / 1.5f;
        lbl_timer.setPosition(
            Gdx.graphics.getWidth() / 2f - lbl_timer.getWidth() / 2,
            Gdx.graphics.getHeight() - lbl_timer.getHeight() - margin);

        stage.addActor(lbl_timer);

        lbl_hoveredUrgency = new Label(
            "?",
            STYLE_LBL);
        lbl_hoveredUrgency.setVisible(false);
        stage.addActor(lbl_hoveredUrgency);
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
    public void render(float time, PatientManager patientManager, GameRenderer gameRenderer) {
        lbl_timer.setText(String.format("%.2f", time));

        this.drawHoverPatientLevel(patientManager, gameRenderer);

        stage.act();
        stage.draw();
    }

    private void drawHoverPatientLevel(PatientManager patientManager, GameRenderer gameRenderer) {
        Patient p = patientManager.getHoveredPatient();
        if (p == null) {
            lbl_hoveredUrgency.setVisible(false);
            return;
        }
        lbl_hoveredUrgency.setVisible(true);

        String txt = String.format(FORMAT_STRING, p.getSortUrgency());
        lbl_hoveredUrgency.setText(txt);
        txtLayout.setText(FONT, txt);

        Vector2 worldCoords = new Vector2(p.getPosition()).add(
            0,
            assets.patientTexture().getHeight() * 1.2f
        );

        Vector3 boxCoords = gameRenderer.getWorldToMouseCoords(worldCoords.x, worldCoords.y);
        float width = txtLayout.width + BOX_PADDING;
        float height = txtLayout.height + BOX_PADDING;

        float centeredX = boxCoords.x - width / 2f;

        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(
            this.assets.whitePixel(),
            centeredX,
            boxCoords.y,
            width,
            height
        );
        batch.end();

        lbl_hoveredUrgency.setPosition(
            centeredX + BOX_PADDING / 2f,
            boxCoords.y + BOX_PADDING / 3f
        );
    }

}
