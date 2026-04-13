package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.SoundSystem;
import com.github.atollysis.systems.scores.Grade;
import com.github.atollysis.systems.scores.Score;
import com.github.atollysis.systems.session.GameDifficulty;
import com.github.atollysis.systems.session.GameResult;

public class ResultScreen implements Screen {

    /*
     * FIELDS
     */
    private static final float LEFT_SIDE_PADDING = 50;

    private static final BitmapFont FONT = new BitmapFont(Gdx.files.internal("ui/fonts/montserrat_128.fnt"));
    private final GlyphLayout txtLayout = new GlyphLayout();

    private final GameResult results;
    private boolean scoreSubmitted = false;

    private final Assets assets;
    private final SoundSystem soundSystem;
    private final Main main;

    private final Stage stage = new Stage(new FitViewport(
        Gdx.graphics.getWidth(),
        Gdx.graphics.getHeight()
    ));

    /*
     * FIELD
     */
    private final ResultBarGraph barGraph;

    /*
     * CONSTRUCTOR
     */
    public ResultScreen(
        Assets assets,
        SoundSystem soundSystem,
        GameResult results,
        Main main
    ) {
        this.assets = assets;
        this.soundSystem = soundSystem;
        this.results = results;
        this.main = main;

        barGraph = new ResultBarGraph(assets, results);

        setupLayout();
        setupLabels();
    }

    private void setupLayout() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Table leftSide = new Table();

        Table leftWrapper = new Table();
        leftWrapper.pad(LEFT_SIDE_PADDING);
        leftWrapper.setBackground(assets.skin().newDrawable(
            "white",
            assets.skin().getColor("quackyellow")
        ));
        leftWrapper.add(leftSide)
            .center()
            .fill()
            .expandY()
            .width(Value.percentWidth(0.25f, root))
            .pad(LEFT_SIDE_PADDING);

        FONT.getData().setScale(2.0f);
        FONT.getRegion().getTexture().setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );
        Label title = newLabel("QUACKERY\nCOMPLETE", "title");
        title.setAlignment(Align.center);
        title.getStyle().font = FONT;
        title.setStyle(new Label.LabelStyle(FONT, GameConfig.black()));

        Grade g = Grade.getGrade(results.getPercentSorted());
        Image gradeImg = new Image(assets.getGradeTexture(g));

        Table stats = new Table();

        stats.add(newLabel("TIME TAKEN", "subtitle"))
            .left()
            .expandX();
        String timeTaken = String.format("%.2fs", results.getTime());
        stats.add(newLabel(timeTaken, "subtitle"))
            .right()
            .expandX()
            .row();

        stats.add(newLabel("SORTED", "subtitle"))
            .left()
            .expandX();
        String pctSorted = String.format("%.2f%%", results.getPercentSorted());
        stats.add(newLabel(pctSorted, "subtitle"))
            .right()
            .expandX()
            .row();

        Table inputRow = new Table();
        TextField fld = getTextField();
        inputRow.add(fld)
            .expandX()
            .fill()
            .padRight(15);
        TextButton btn_submit = new TextButton("Save", assets.skin(), "default");
        btn_submit.pad(5, 30, 5, 30);
        btn_submit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (scoreSubmitted)
                    return;
                scoreSubmitted = true;

                Score score = new Score(fld.getText(), results);
                main.newScore(score);
                btn_submit.setDisabled(true);

            }
        });
        inputRow.add(btn_submit);

        fld.setTextFieldListener((tf, c) -> {
            btn_submit.setDisabled(tf.getText().isEmpty());
        });

        TextButton btn_back = new TextButton("Back To Title", assets.skin(), "menu");
        btn_back.pad(5, 15, 5, 15);
        btn_back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.toTitleScreen();
            }
        });

        leftSide.add(title)
            .padBottom(15)
            .row();
        leftSide.add(gradeImg)
            .padTop(50)
            .padBottom(50)
            .row();
        leftSide.add(stats)
            .expand()
            .fillX()
            .padBottom(50)
            .row();
        leftSide.add(inputRow)
            .expandX()
            .fillX()
            .padBottom(15)
            .row();
        leftSide.add(btn_back)
            .expandX()
            .fillX();

        root.add(leftWrapper)
            .fill()
//            .expand()
            .width(Value.percentWidth(0.30f, root));
//            .pad(LEFT_SIDE_PADDING);
        root.add(barGraph)
            .expand()
            .fill();
//        root.setDebug(true);
    }

    private void setupLabels() {
        Array<GameResult.SortRecord> recs = results.getPatients();
        for (int i = 0; i < recs.size; i++) {
            GameResult.SortRecord rec = recs.get(i);
            String convertedLvl = Integer.toString(rec.actual().getSortUrgency());
            Label patientLevel = newLabel(convertedLvl, "menu");
            patientLevel.setAlignment(Align.center);
            float yCoord = ResultBarGraph.getYCoord() - patientLevel.getHeight() * 1.2f;
            if (GameConfig.getDifficulty() == GameDifficulty.DIFFICULT
                && i % 2 == 0
            ) {
                yCoord -= patientLevel.getHeight() * 1.2f;
            }
            patientLevel.setPosition(
                barGraph.getXCoord(i) - (patientLevel.getWidth() / 2f),
                yCoord
            );
            stage.addActor(patientLevel);
        }
    }

    private Label newLabel(String txt, String style) {
        return new Label(txt, assets.skin(), style);
    }

    private TextField getTextField() {
        TextField fld = new TextField("Name", assets.skin());
        fld.setMaxLength(GameConfig.getMaxNameLength());

        fld.setTextFieldFilter((tf, c) -> {
            return Character.isLetter(c);
        });

        return fld;
    }

    /*
     * METHODS
     */

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(GameConfig.white());
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    /*
     * GETTERS
     */
    public static float getLeftSidePadding() {
        return LEFT_SIDE_PADDING;
    }

}
