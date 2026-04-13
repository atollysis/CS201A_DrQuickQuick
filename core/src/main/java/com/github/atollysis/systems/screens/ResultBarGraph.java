package com.github.atollysis.systems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.atollysis.systems.Assets;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.session.GameResult;

public class ResultBarGraph extends Actor {

    /*
     * FIELD
     */
    private static final float BAR_SPACE_PCT = 0.5f; // e.g., 0.5 = 50% bar width
    private static final int PADDING = 100;

    private static final float MAX_WIDTH_PCT = 0.75f;
    private static float MAX_WIDTH = Gdx.graphics.getWidth() * MAX_WIDTH_PCT - ResultScreen.getLeftSidePadding() * 2;
    private static float baseX = Gdx.graphics.getWidth() - MAX_WIDTH;
    private static float baseY = PADDING + 100;
    private static float MAX_HEIGHT = Gdx.graphics.getHeight() - PADDING - baseY;

    private final Assets assets;
    private final GameResult results;

    /*
     * CONSTRUCTOR
     */
    public ResultBarGraph(Assets assets, GameResult results) {
        this.assets = assets;
        this.results = results;
    }

    /*
     * DRAW METHOD
     */

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float barWidth = getBarWidth();

//        batch.setColor(Color.CHARTREUSE);
//        batch.draw(
//            assets.whitePixel(),
//            baseX,
//            PADDING,
//            MAX_WIDTH,
//            MAX_HEIGHT
//        );

        Array<GameResult.SortRecord> recs = results.getPatients();
        for (int i = 0; i < recs.size; i++) {
            GameResult.SortRecord rec = recs.get(i);
            float x = baseX + PADDING + (barWidth + barWidth * BAR_SPACE_PCT) * i;
            float heightRatio = rec.actual().getSortUrgency() / (float) results.getMaxUrgency();
            float height = MAX_HEIGHT * heightRatio;

            batch.setColor(rec.actual().isProperlySorted() ?
                GameConfig.green() :
                GameConfig.red()
            );

            batch.draw(
                assets.whitePixel(),
                x,
                baseY,
                barWidth,
                height
            );
        }
    }

    private float getBarWidth() {
        int pop = results.getPopulation();
        float numerator = MAX_WIDTH - 2 * PADDING;
        float denominator = pop + BAR_SPACE_PCT * pop - BAR_SPACE_PCT;
        return numerator / denominator;
    }

    /*
     * SETTER
     */
    public void updateWidth() {
        MAX_WIDTH = Gdx.graphics.getWidth() * MAX_WIDTH_PCT;
    }

    public static float getYCoord() {
        return baseY;
    }

    public float getXCoord(int index) {
        float barWidth = getBarWidth();
        return baseX + PADDING + (barWidth + barWidth * BAR_SPACE_PCT) * index + (barWidth / 2);
    }

}
