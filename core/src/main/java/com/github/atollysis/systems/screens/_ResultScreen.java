package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.atollysis.entities.Patient;
import com.github.atollysis.systems.*;
import com.github.atollysis.systems.session.GameDifficulty;
import com.github.atollysis.systems.session.GameResult;

public class _ResultScreen implements Screen {

    /*
     * FIELDS
     */
    private static final BitmapFont FONT = new BitmapFont(Gdx.files.internal("ui/fonts/montserrat_128.fnt"));
    private final GlyphLayout txtLayout = new GlyphLayout();
//    private static final Label.LabelStyle STYLE_LBL = new Label.LabelStyle(
//        FONT,
//        GameConfig.black()
//    );

    private final GameResult results;

    private final Assets assets;
    private final SoundSystem soundSystem;

    private final OrthographicCamera camera;
    private final Stage stage = new Stage(new ScreenViewport());

    /*
     * CONSTRUCTOR
     */
    public _ResultScreen(Assets assets, SoundSystem soundSystem, GameResult results) {
        this.results = results;
        this.assets = assets;
        this.soundSystem = soundSystem;

        camera = new OrthographicCamera();
        camera.setToOrtho(
            false,
            GameConfig.getWorldWidth(),
            GameConfig.getWorldHeight()
        );

        String[] txts = {
            "RESULTS",
            String.format(
                "%.2f secs   |   %.2f%% sorted",
                results.getTime(),
                results.getPercentSorted())
        };

        final int margin = 50;
        final int lineSpacing = 50;

        for (int i = 0; i < txts.length; i++) {
            Label lbl = newLabel(txts[i]);

            float x = Gdx.graphics.getWidth() / 2f - lbl.getWidth() / 2;
            float y = Gdx.graphics.getHeight() - lbl.getHeight() - margin - lineSpacing * i;
            lbl.setPosition(x, y);
            stage.addActor(lbl);
        }

        Label lbl_prompt = newLabel("Press 1/2/3 to play again, or BACKSPACE to go back to the Title Screen.");
        lbl_prompt.setAlignment(Align.center);
        lbl_prompt.setPosition(
            (Gdx.graphics.getWidth() - lbl_prompt.getWidth()) / 2f,
            margin
        );
        stage.addActor(lbl_prompt);
    }

    private Label newLabel(String txt) {
        Label lbl = new Label(txt, assets.skin(), "menu");
        lbl.setAlignment(Align.center);
        return lbl;
    }

    /*
     * METHODS
     */
    @Override
    public void show() {
        // TODO
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GameConfig.white());

        this.drawBarGraph();

        stage.act();
        stage.draw();
//        soundSystem.update(delta);
    }

    private void drawBarGraph() {
        Batch batch = stage.getBatch();

        int pop = results.getPopulation();

        float width = GameConfig.getDifficulty() != GameDifficulty.DIFFICULT ?
            120 :
            60;
        float margins = width / 3;
        float y = 250;
        float maxHeight = 500;
        float maxWidth = (width * pop) + (margins * (pop + 1));
        float xOffset = (Gdx.graphics.getWidth() - maxWidth) / 2f;

        batch.begin();
        for (int i = 0; i < results.getPatients().size; i++) {
            Patient p = results.getPatients().get(i).actual();
            float heightRatio = p.getSortUrgency() / (float) results.getMaxUrgency();
            float x = xOffset + margins * (i + 1) + width * i;

            if (p.isProperlySorted())
                batch.setColor(GameConfig.green());
            else
                batch.setColor(GameConfig.red());

            batch.draw(
                assets.whitePixel(),
                x,
                y,
                width,
                heightRatio * maxHeight
            );

            String convertedLvl = Integer.toString(p.getSortUrgency());
            txtLayout.setText(FONT, convertedLvl);
            Label patientLevel = newLabel(convertedLvl);
            float yCoord = y - 50;
            if (i % 2 == 0) {
                yCoord -= txtLayout.height * 1.2f;
            }
            patientLevel.setPosition(x + (width - txtLayout.width) / 2, yCoord);
            stage.addActor(patientLevel);
        }
        batch.setColor(Color.WHITE);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // TODO
    }

    @Override
    public void pause() {
        // TODO
    }

    @Override
    public void resume() {
        // TODO
    }

    @Override
    public void hide() {
        // TODO
    }

    @Override
    public void dispose() {
        // TODO
    }

}
