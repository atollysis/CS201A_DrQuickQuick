package com.github.atollysis.systems.renderers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.github.atollysis.entities.Entity;
import com.github.atollysis.entities.Player;

public class DebugRenderer {
    /*
     * FIELDS
     */
    private ShapeRenderer renderer = new ShapeRenderer();

    /*
     * DEFAULT CONSTRUCTOR USED
     */

    /*
     * LIBGDX-SPECIFIC
     */
    public void dispose() {
        this.renderer.dispose();
    }

    /*
     * METHODS
     */
    public void renderBounds(OrthographicCamera camera, Player player, Array<Entity> entities) {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(Color.RED);
        for (Entity patient : entities)
            renderEntity(patient);
        renderEntity(player);

        renderer.end();
    }

    private void renderEntity(Entity e) {
        renderer.rect(
            e.getVisualX(),
            e.getVisualY(),
            e.getBounds().width,
            e.getBounds().height);
    }
}
