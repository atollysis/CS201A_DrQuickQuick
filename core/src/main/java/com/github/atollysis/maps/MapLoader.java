package com.github.atollysis.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    /*
     * FIELDS
     */
    private static final String PATH_NAME = "data/map.txt";

    public static TileMap loadMap() {
        FileHandle file = Gdx.files.internal(PATH_NAME);

        String[] mapRows = file.readString().split("\n");

        List<List<TileType>> tiles = new ArrayList<>();

        int mapWidth = -1, mapHeight;

        for (String row : mapRows) {
            List<TileType> converted = row.chars()
                .mapToObj(ch -> TileType.getType((char) ch))
                .toList();
            mapWidth = Math.max(mapWidth, converted.size());
            tiles.add(converted);
        }
        mapHeight = tiles.size();

        TileType[][] output = new TileType[mapHeight][mapWidth];

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                output[y][x] = tiles.get(y).get(x);
            }
        }

        return new TileMap(output, mapWidth, mapHeight);
    }

}
