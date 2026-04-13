package com.github.atollysis.systems.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.atollysis.entities.Patient;
import com.github.atollysis.entities.EntityManager;
import com.github.atollysis.runner.Main;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.screens.GameScreen;
import com.github.atollysis.systems.session.GameSession;

public class GameInterface {

    /*
     * FIELDS
     */
    private static final float BOX_PADDING = 60f;
    private static String FORMAT_STRING = "%0" + GameConfig.getMaxDigitLength() + "d";

    private static final BitmapFont FONT = new BitmapFont(Gdx.files.internal("ui/fonts/montserrat_128.fnt"));
    private static final BitmapFont FONT_SMALL = new BitmapFont(Gdx.files.internal("ui/fonts/montserrat_128.fnt"));

    private static final Label.LabelStyle STYLE_TIMER = new Label.LabelStyle(
        FONT,
        GameConfig.black()
    );
    private static final Label.LabelStyle STYLE_LBL = new Label.LabelStyle(
        FONT,
        GameConfig.black()
    );
    private static final Label.LabelStyle STYLE_PROMPT = new Label.LabelStyle(
        FONT_SMALL,
        GameConfig.black()
    );

    private final GlyphLayout txtLayout = new GlyphLayout();

    private final Stage stage = new Stage(new ScreenViewport());
    // Actors
    private final Label lbl_timer;
    private final Label lbl_hoveredUrgency;
    private final Label lbl_highlightUrgency;
    private final Label lbl_patientsLeft;
    private final Label lbl_hoverPrompt;

    // Textures
    private final Assets assets;

    private final float MARGIN;
    private final Table pauseScreen;

    /*
     * CONSTRUCTOR
     */
    public GameInterface(Assets assets, GameScreen screen, Main main) {
        FONT.getData().setScale(2.0f);

        this.assets = assets;

        lbl_timer = new Label(
            "0.00",
            STYLE_TIMER);
        MARGIN = lbl_timer.getHeight() / 1.5f;
        lbl_timer.setAlignment(Align.center);
        lbl_timer.setPosition(
            Gdx.graphics.getWidth() / 2f - lbl_timer.getWidth() / 2,
            Gdx.graphics.getHeight() - lbl_timer.getHeight() * 2f);
        stage.addActor(lbl_timer);

        lbl_hoveredUrgency = new Label(
            "?",
            STYLE_LBL);
        lbl_hoveredUrgency.setVisible(false);
        stage.addActor(lbl_hoveredUrgency);

        lbl_highlightUrgency = new Label(
            "?",
            STYLE_LBL);
        lbl_highlightUrgency.setVisible(false);
        stage.addActor(lbl_highlightUrgency);

        lbl_patientsLeft = new Label(
            "?",
            STYLE_LBL);
        lbl_patientsLeft.setVisible(true);
        lbl_patientsLeft.setAlignment(Align.right);
        lbl_patientsLeft.setPosition(
            Gdx.graphics.getWidth() - lbl_patientsLeft.getWidth() - BOX_PADDING * 1.2f,
            Gdx.graphics.getHeight() - lbl_patientsLeft.getHeight() * 2f);
        stage.addActor(lbl_patientsLeft);

        lbl_hoverPrompt = new Label(
            "LEFT CLICK: Sort Patient\nRIGHT CLICK: Highlight Patient",
            STYLE_PROMPT);
        lbl_hoverPrompt.setVisible(false);
        lbl_hoverPrompt.setAlignment(Align.left);
        lbl_hoverPrompt.setPosition(
            BOX_PADDING * 1.2f,
            Gdx.graphics.getHeight() - lbl_hoverPrompt.getHeight() * 1.9f);
        stage.addActor(lbl_hoverPrompt);

        pauseScreen = getPauseScreen(screen, main);
        pauseScreen.setVisible(false);
        stage.addActor(pauseScreen);
    }

    public Table getPauseScreen(GameScreen screen, Main main) {
        Table pauseScreen = new Table();
        pauseScreen.setFillParent(true);

        Color overlay = Color.BLACK;
        overlay.a = 0.5f;
        pauseScreen.setBackground(assets.skin().newDrawable(
            "white",
            overlay
        ));

        BitmapFont titleFont = new BitmapFont(Gdx.files.internal("ui/fonts/montserrat_128.fnt"));
        titleFont.getData().setScale(2.0f);
        Label title = new Label("PAUSED", assets.skin(), "title");
        title.setAlignment(Align.center);
        title.getStyle().font = titleFont;
        title.setStyle(new Label.LabelStyle(titleFont, assets.skin().getColor("quackblack")));

        TextButton btn_unpause = newButton("Back to Game");
        btn_unpause.pad(10, 30, 10, 30);
        btn_unpause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.setPaused(false);
                pauseScreen.setVisible(false);
            }
        });

        TextButton btn_quit = newButton("Quit");
        btn_quit.pad(10, 30, 10, 30);
        btn_quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.toTitleScreen();
            }
        });

        Table content = new Table();
        content.setBackground(assets.skin().newDrawable(
            "white",
            assets.skin().getColor("quackwhite")
        ));

        content.add(title)
            .center()
            .expandX()
            .padBottom(50)
            .row();
        content.add(btn_unpause)
            .center()
            .expandX()
            .padBottom(30)
            .row();
        content.add(btn_quit)
            .center()
            .expandX();

        content.pad(50);

        pauseScreen.add(content)
            .center()
            .width(content.getPrefWidth() * 1.5f);

        return pauseScreen;
    }

    private TextButton newButton(String txt) {
        TextButton btn = new TextButton(txt, assets.skin(), "default");
        btn.pad(5, 30, 5, 30);
        return btn;
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
    public void render(GameSession gameSession, GameRenderer gameRenderer, boolean paused) {
        lbl_timer.setText(String.format("%.2f", gameSession.getTime()));
        lbl_patientsLeft.setText(gameSession.getPatientManager().getPatientsLeftString());
        lbl_hoverPrompt.setVisible(
            gameSession.getPatientManager().getHoveredPatient() != null
        );

        drawUI();

        this.drawHoverPatientLevel(gameSession.getPatientManager(), gameRenderer);
        this.drawHighlightPatientLevel(gameSession.getPatientManager(), gameRenderer);

        pauseScreen.setVisible(paused);

        stage.act();
        stage.draw();
    }

    private void drawUI() {
        Batch batch = stage.getBatch();

        batch.begin();
        // Timer
        final GridPoint2 timerDims = new GridPoint2(330, 110);
        batch.setColor(Color.WHITE);
        batch.draw(
            assets.whitePixel(),
            (Gdx.graphics.getWidth() - timerDims.x) / 2f,
            Gdx.graphics.getHeight() - timerDims.y - MARGIN,
            timerDims.x,
            timerDims.y
        );
        // Patient counter
        txtLayout.setText(FONT, lbl_patientsLeft.getText());
        float width = txtLayout.width + BOX_PADDING;
        batch.draw(
            assets.whitePixel(),
            Gdx.graphics.getWidth() - width - MARGIN,
            Gdx.graphics.getHeight() - timerDims.y - MARGIN,
            width,
            timerDims.y
        );
        // Prompt
        if (lbl_hoverPrompt.isVisible()) {
            txtLayout.setText(FONT_SMALL, lbl_hoverPrompt.getText());
            batch.draw(
                assets.whitePixel(),
                MARGIN,
                Gdx.graphics.getHeight() - timerDims.y - MARGIN,
                txtLayout.width + MARGIN,
                timerDims.y
            );
        }
        batch.end();
    }

    private void drawHoverPatientLevel(EntityManager entityManager, GameRenderer gameRenderer) {
        Patient p = entityManager.getHoveredPatient();
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

    private void drawHighlightPatientLevel(EntityManager entityManager, GameRenderer gameRenderer) {
        Patient p = entityManager.getHighlightedPatient();
        if (p == null) {
            lbl_highlightUrgency.setVisible(false);
            return;
        }
        lbl_highlightUrgency.setVisible(true);

        String txt = String.format(FORMAT_STRING, p.getSortUrgency());
        lbl_highlightUrgency.setText(txt);
        txtLayout.setText(FONT, txt);

        Vector2 worldCoords = new Vector2(p.getPosition()).add(
            0,
            assets.patientTexture().getHeight() * 1.2f
        );

        Vector3 boxCoords = gameRenderer.getWorldToMouseCoords(worldCoords.x, worldCoords.y);
        float width = txtLayout.width + BOX_PADDING;
        float height = txtLayout.height + BOX_PADDING;

        float centeredX = boxCoords.x - width / 2f;

        float clampedX = MathUtils.clamp(
            centeredX,
            MARGIN,
            Gdx.graphics.getWidth() - MARGIN - width
        );
        float clampedY = MathUtils.clamp(
            boxCoords.y,
            MARGIN,
            Gdx.graphics.getHeight() - (height + MARGIN) * 2f
        );

        Batch batch = stage.getBatch();
        batch.begin();
        batch.setColor(Color.OLIVE);
        batch.draw(
            this.assets.whitePixel(),
            clampedX,
            clampedY,
            width,
            height
        );
        batch.setColor(Color.WHITE);
        batch.end();

        lbl_highlightUrgency.setPosition(
            clampedX + BOX_PADDING / 2f,
            clampedY + BOX_PADDING / 3f
        );
    }

    /*
     * GETTERS
     */
    public Stage getStage() {
        return stage;
    }

    /*
     * STATIC SETTERS
     */
    public static void setValues() {
        FORMAT_STRING = "%0" + GameConfig.getMaxDigitLength() + "d";
    }

}
