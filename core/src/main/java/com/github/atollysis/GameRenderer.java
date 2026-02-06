package com.github.atollysis;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.atollysis.entities.FacingDirection;
import com.github.atollysis.entities.Patient;
import com.github.atollysis.entities.PatientManager;
import com.github.atollysis.entities.Player;
import com.github.atollysis.maps.TileMap;
import com.github.atollysis.maps.TileType;

public class GameRenderer {

    /*
     * FIELDS
     */
    private static final float TILE_SIZE = 32;
    private static final float WORLD_WIDTH = 640f;
    private static final float WORLD_HEIGHT = 360f;
    // Camera
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    // Sprites
    private final SpriteBatch batch = new SpriteBatch();
    private final Sprite spritePlayer;
    private final Array<Sprite> spritePatients;

    /*
     * CONSTRUCTOR
     */
    public GameRenderer(Assets assets, PatientManager patientManager) {
        viewport.apply();

        spritePlayer = new Sprite(assets.playerTexture());

        Array<Patient> patientArray = patientManager.getPatientArray();
        spritePatients = new Array<>(patientArray.size);
        Texture patientTexture = assets.patientTexture();

        for (Patient patient : patientArray) {
            Vector2 coords = patient.getCoords();
            Sprite patientSprite = new Sprite(patientTexture);
            patientSprite.setX(coords.x - patientTexture.getWidth() / 2f); // Middle
            patientSprite.setY(coords.y); // Bottom
            spritePatients.add(patientSprite);
//            System.out.format("New patient added at (%.2f, %.2f)!\n", coords.x, coords.y);
        }
    }

    /*
     * GDX METHODS
     */
    public void handleResize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        batch.dispose();
    }

    /*
     * METHODS
     */
    public void centerCamera(Player player) {
        camera.position.set(
            player.getPosX(),
            player.getPosY(),
            0);
        camera.update();
    }

    public void render(Player player, TileMap tileMap, Assets assets) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawBackground(tileMap, assets);
        drawPatients();
        drawPlayer(player, assets);

        batch.end();
    }

    private Vector2 getViewableBottomLeft() {
        return new Vector2(
            camera.position.x - WORLD_WIDTH / 2,
            camera.position.y - WORLD_HEIGHT / 2
        );
    }

    private Vector2 getViewableTopRight() {
        return new Vector2(
            camera.position.x + WORLD_WIDTH / 2,
            camera.position.y + WORLD_HEIGHT / 2
        );
    }

    private void drawBackground(TileMap tileMap, Assets assets) {
        Vector2 viewableStart = getViewableBottomLeft();
        Vector2 viewableEnd = getViewableTopRight();

        int startX = (int) (viewableStart.x / TILE_SIZE) - 1;
        int endX   = (int) (viewableEnd.x / TILE_SIZE) + 1;
        int startY = (int) (viewableStart.y / TILE_SIZE) - 1;
        int endY   = (int) (viewableEnd.y / TILE_SIZE) + 1;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                TileType tile = tileMap.getTileAt(x, y);
                if (tile == null || tile == TileType.EMPTY)
                    continue;

                switch (tile) {
                    case FLOOR -> batch.draw(assets.floorTexture(), x * TILE_SIZE, y * TILE_SIZE);
                    case WALL -> batch.draw(assets.wallTexture(), x * TILE_SIZE, y * TILE_SIZE);
                }
            }
        }
    }

    private void drawPlayer(Player player, Assets assets) {
        boolean faceRight = player.getFacingDirection() == FacingDirection.RIGHT
            && spritePlayer.isFlipX();
        boolean faceLeft = player.getFacingDirection() == FacingDirection.LEFT
            && !spritePlayer.isFlipX();
        if (faceRight || faceLeft)
            spritePlayer.flip(true, false);

        Texture playerTexture = assets.playerTexture();
        spritePlayer.setX(player.getPosX() - playerTexture.getWidth() / 2f); // Middle
        spritePlayer.setY(player.getPosY()); // Bottom

        spritePlayer.draw(batch);
    }

    private void drawPatients() {
        for (Sprite patient : spritePatients) {
            // TODO: Frustum culling
//            BoundingBox spriteBounds = patient.getBoundingRectangle();
//            if (camera.frustum.boundsInFrustum(patient.getBoundingRectangle()))
                patient.draw(batch);
        }
    }

    /*
     * GETTERS
     */
    public static float getTileSize() {
        return TILE_SIZE;
    }

}
