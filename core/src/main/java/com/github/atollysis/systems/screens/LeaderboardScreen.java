package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.scores.Grade;
import com.github.atollysis.systems.scores.Score;
import com.github.atollysis.systems.scores.ScoreManager;
import com.github.atollysis.systems.session.GameDifficulty;

public class LeaderboardScreen implements Screen {

    /*
     * INNER ENUM
     */
    public enum LeaderboardSort {
        RECENT, TIME, PCT_SORTED
    }

    /*
     * FIELDS
     */
    private static final String[] leaderboardHeaders = {
        "NAME",
        "GRADE",
        "DIFF",
        "TIME",
        "SORTED"
    };
    private static final float PAD_TOP = 10, PAD_LEFT = 30, PAD_BOTTOM = 10, PAD_RIGHT = 30;
    private static final float PADDING = 50;

    private final Assets assets;
    private final ScoreManager scoreManager;
    private final Main main;

    private final Stage stage = new Stage(new FitViewport(
        Gdx.graphics.getWidth(),
        Gdx.graphics.getHeight()
    ));

    private Array<Score> currentScores = null;
    private Table leaderboardTable;

    private final ObjectMap<String, TextButton> filterButtons = new ObjectMap<>();
    ButtonGroup<TextButton> sortGroup = new ButtonGroup<>();

    private GameDifficulty currentDiff = null;
    private LeaderboardSort currentSort = LeaderboardSort.RECENT;

    /*
     * CONSTRUCTOR
     */
    public LeaderboardScreen(
        Assets assets,
        ScoreManager scoreManager,
        Main main
    ) {
        this.assets = assets;
        this.scoreManager = scoreManager;
        this.main = main;

        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("ui/fonts/montserrat_128.fnt"));
        titleFont.getData().setScale(2.0f);
//        titleFont.getRegion().getTexture().setFilter(
//            Texture.TextureFilter.Linear,
//            Texture.TextureFilter.Linear
//        );
        Label title = new Label("LEADERBOARDS", assets.skin(), "title");
        title.setAlignment(Align.center);
        title.getStyle().font = titleFont;
        title.setStyle(new Label.LabelStyle(titleFont, assets.skin().getColor("quackblack")));

        Table filterTabs = setupFilterTabs();
        Table sortTabs = setupSortTabs();
        ScrollPane scroll = setupLeaderboard();

        Table root = new Table();
        root.add(title)
            .fillX()
            .padBottom(PADDING)
            .row();
        root.add(filterTabs)
            .fillX()
            .row();
        root.add(sortTabs)
            .fillX()
            .row();
        root.add(scroll)
            .expand()
            .fill()
            .padTop(PADDING);
        root.pad(PADDING);
        root.setFillParent(true);
        stage.addActor(root);

        renderLeaderboard();
    }

    private Table setupFilterTabs() {
        Table tabs = new Table();

        TextButton btn_back = newButton("Back");
        btn_back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.toTitleScreen();
            }
        });
        btn_back.pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);
        tabs.add(btn_back)
            .colspan(GameDifficulty.values().length + 2)
            .center()
            .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT)
            .row();

        tabs.add(newLabel("Filter by"))
            .center()
            .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);

        TextButton btn_all = newButton("ALL");
        btn_all.setDisabled(true);
        filterButtons.put("ALL", btn_all);
        btn_all.setProgrammaticChangeEvents(true);
        btn_all.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for (TextButton btn : filterButtons.values())
                    btn.setDisabled(false);
                btn_all.setDisabled(true);

                currentDiff = null;
                renderLeaderboard();
            }
        });
        tabs.add(btn_all)
            .center()
            .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);

        for (GameDifficulty diff : GameDifficulty.values()) {
            TextButton btn_diff = newButton(diff.name());
            filterButtons.put(diff.name(), btn_diff);
            btn_diff.setProgrammaticChangeEvents(true);
            btn_diff.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    for (TextButton btn : filterButtons.values())
                        btn.setDisabled(false);
                    btn_diff.setDisabled(true);

                    currentDiff = diff;
                    renderLeaderboard();
                }
            });
            tabs.add(btn_diff)
                .center()
                .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);
        }

        return tabs;
    }

    private Table setupSortTabs() {
        Table tabs = new Table();

        TextButton btn_recent = newSortButton("RECENT", LeaderboardSort.RECENT);
        btn_recent.setDisabled(true);
        TextButton btn_time = newSortButton("TIME", LeaderboardSort.TIME);
        TextButton btn_pct = newSortButton("PERCENT", LeaderboardSort.PCT_SORTED);

        sortGroup.add(btn_time, btn_pct, btn_recent);

        tabs.add(newLabel("Sort by"))
            .center()
            .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);
        tabs.add(btn_recent)
            .center()
            .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);
        tabs.add(btn_time)
            .center()
            .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);
        tabs.add(btn_pct)
            .center()
            .pad(PAD_TOP, PAD_LEFT, PAD_BOTTOM, PAD_RIGHT);

        return tabs;
    }

    private ScrollPane setupLeaderboard() {
        leaderboardTable = new Table();
        leaderboardTable.top();

        ScrollPane scroll = new ScrollPane(leaderboardTable, assets.skin());
        scroll.setFadeScrollBars(false);
        scroll.setScrollingDisabled(true, false);

        return scroll;
    }

    private TextButton newButton(String txt) {
        TextButton btn = new TextButton(txt, assets.skin(), "default");
        btn.pad(5, 30, 5, 30);
        return btn;
    }

    private TextButton newSortButton(String txt, LeaderboardSort sort) {
        TextButton btn = newButton(txt);
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentScores == null)
                    return;

                for (TextButton btn : sortGroup.getButtons())
                    btn.setDisabled(false);
                btn.setDisabled(true);

                currentSort = sort;
                renderLeaderboard();
            }
        });

        return btn;
    }

    private void renderLeaderboard() {
        leaderboardTable.clear();

        // name, grade, diff, time, sorted
        for (String headerTxt : leaderboardHeaders) {
            leaderboardTable.add(newLabel(headerTxt))
                .center()
                .expandX()
                .padBottom(PADDING);
        }
        leaderboardTable.row();

        currentScores = scoreManager.filterBy(currentDiff);
        scoreManager.sortBy(currentSort, currentScores);

        for (Score score : currentScores) {
            Table gradeWrapper = new Table();
            Grade grade = Grade.getGrade(score.getPercentSorted());
            gradeWrapper.setBackground(assets.skin().newDrawable(
                "white",
                grade.getColor(assets)
            ));
            gradeWrapper.add(newLabel(grade.name()));

            leaderboardTable.add(newLabel(score.getName())).center();
            assert Grade.getGrade(score.getPercentSorted()) != null;
            leaderboardTable.add(gradeWrapper).center().fillX();
            leaderboardTable.add(newLabel(score.getDifficulty().name())).center();
            String time = String.format("%.2f s", score.getTimeTaken());
            leaderboardTable.add(newLabel(time)).center();
            String pct = String.format("%.2f%%", score.getPercentSorted());
            leaderboardTable.add(newLabel(pct)).center();
            leaderboardTable.row();
        }
    }

    private Label newLabel(String txt) {
        return new Label(txt, assets.skin(), "menu");
    }

    /*
     * OVERRIDDEN METHODS
     */

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        renderLeaderboard();
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

}
