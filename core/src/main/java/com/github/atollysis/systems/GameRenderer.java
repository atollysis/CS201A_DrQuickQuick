package com.github.atollysis.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private static final float TILE_SIZE = GameConfig.getTileSize();
    private static final float WORLD_WIDTH = GameConfig.getWorldWidth();
    private static final float WORLD_HEIGHT = GameConfig.getWorldHeight();
    // Camera
    private final OrthographicCamera camera = new OrthographicCamera();
    private final Viewport viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
    // Sprites
    private final SpriteBatch batch = new SpriteBatch();
    private final Sprite spritePlayer;
//    private final Array<Sprite> spritePatients;
    // Other renderers
    private final DebugRenderer debugRenderer = new DebugRenderer();
    private boolean isDebugRendererActive = true;

    /*
     * CONSTRUCTOR
     */
    public GameRenderer(Assets assets, PatientManager patientManager) {
        viewport.apply();

        spritePlayer = new Sprite(assets.playerTexture());

//        Array<Patient> patientArray = patientManager.getPatientArray();
//        spritePatients = new Array<>(patientArray.size);
//        Texture patientTexture = assets.patientTexture();

//        for (Patient patient : patientArray) {
//            Vector2 pos = patient.getPosition();
//            Sprite patientSprite = new Sprite(patientTexture);
//            patientSprite.setX(pos.x - patientTexture.getWidth() / 2f); // Middle
//            patientSprite.setY(pos.y); // Bottom
//            spritePatients.add(patientSprite);
////            System.out.format("New patient added at (%.2f, %.2f)!\n", coords.x, coords.y);
//        }
    }

    /*
     * GDX METHODS
     */
    public void handleResize(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        batch.dispose();
        debugRenderer.dispose();
    }

    /*
     * METHODS
     */
    public void centerCamera(Player player) {
        camera.position.set(
            player.getPosition().x,
            player.getPosition().y,
            0);
        camera.update();
    }

    public void render(
                GameSession gameSession,
                Assets assets) {
        Player p = gameSession.getPlayer();
        PatientManager pMgr = gameSession.getPatientManager();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawBackground(gameSession.getTileMap(), assets);
        drawPatients(pMgr);
        drawPlayer(p, assets);

        batch.end();

        if (isDebugRendererActive)
            debugRenderer.renderBounds(
                camera,
                p,
                pMgr.getPatientArray());
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
        spritePlayer.setX(player.getPosition().x - playerTexture.getWidth() / 2f); // Middle
        spritePlayer.setY(player.getPosition().y); // Bottom

        spritePlayer.draw(batch);
    }

    private void drawPatients(PatientManager patientManager) {
        for (Patient p : patientManager.getPatientArray()) {
            // TODO: Frustum culling
            if (patientManager.getHoveredPatient() == p)
                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

            p.getSprite().draw(batch);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /*
     * GETTERS
     */
    public static float getTileSize() {
        return TILE_SIZE;
    }

    public Vector3 getMouseToWorldCoords() {
        Vector3 mouse = new Vector3(
            Gdx.input.getX(),
            Gdx.input.getY(),
            0
        );

        camera.unproject(mouse);
        return mouse;
    }

    public Vector3 getWorldToMouseCoords(float coordX, float coordY) {
        Vector3 world = new Vector3(
            coordX,
            coordY,
            0
        );

        camera.project(world);
        return world;
    }

}
