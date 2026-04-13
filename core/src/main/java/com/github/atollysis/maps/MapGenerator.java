package com.github.atollysis.maps;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.github.atollysis.systems.GameConfig;
import com.github.atollysis.systems.session.GameDifficulty;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapGenerator {

    /*
     * FIELDS
     */

    // General
    private static int ROOM_COUNT = 8;
    private static final List<BiFunction<int[][], GridPoint2, GridPoint2>> EXPAND_METHODS = List.of(
        MapGenerator::expandTopLeft,
        MapGenerator::expandTopRight,
        MapGenerator::expandBottomLeft,
        MapGenerator::expandBottomRight
    );
    private static List<GridPoint2> ROOM_ORIGINS = new ArrayList<>();
    private static final float MIN_FLOOR_PCT = 0.40f;
    private static final float MAX_FLOOR_PCT = 0.70f;

    // Room-related
    private static int DOORWAY_ID = ROOM_COUNT + 1;
    private static final int WALL_ID = 100;

    private static final int MIN_WIDTH = 2;
    private static final int MIN_HEIGHT = 3;
    private static final int MIN_AREA = 9;
    private static final int MAX_EXPAND_RETRIES = 3;
    private static boolean FIRST_EXPANSION;

    /*
     * MAIN STATIC METHOD
     */

    public static TileMap generateMap() {
        GridPoint2 dims = GameConfig.getDifficulty().getRoomSize();
        return generateMap(dims.x, dims.y);
    }

    public static TileMap generateMap(int width, int height) {
        TileType[][] map;
        long count, area = (long) width * height;
        boolean valid;
        do {
            valid = false;
            map = generate(width, height);
            if (map == null)
                continue;
            // Tiles must be between these ratios to also be valid
            count = Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(tile -> tile == TileType.FLOOR)
                .count();
            valid = count < (long) (area * MAX_FLOOR_PCT)
                && count > (long) (area * MIN_FLOOR_PCT);
        } while (!valid);
        return new TileMap(map, width, height);
    }

    private static TileType[][] generate(int width, int height) {
        int[][] tiles = new int[height][width];

        ROOM_ORIGINS = Stream.generate(() -> MapGenerator.randomPoint(width, height))
            .limit(ROOM_COUNT)
            .collect(Collectors.toList());

        // List<BiFunction<int[][], GridPoint2, GridPoint2>>
        var expandMethods = new ArrayList<>(EXPAND_METHODS);
        Collections.shuffle(expandMethods);

        // BiFunction<int[][], GridPoint2, GridPoint2>
        // The first expansion relocates the point (with tries)
        FIRST_EXPANSION = true;
        var firstMethod = expandMethods.remove(0);
        applyFirstExpansion(firstMethod, tiles);
        FIRST_EXPANSION = false;

        expandMethods.remove(0);
        for (var method : expandMethods) {
            applyExpansion(method, tiles);
        }

        boolean validMap = addGaps(tiles);
        if (!validMap)
            return null;

        tiles = addWallTiles(tiles);

        if (!allConnected(tiles))
            return null;

        // Convert int[][] to TileType[][]
        TileType[][] tilesConverted = new TileType[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                tilesConverted[row][col] = switch (tiles[row][col]) {
                    case 0 -> TileType.EMPTY;
                    case WALL_ID -> TileType.WALL;
                    default -> TileType.FLOOR;
                };
            }
        }

        return tilesConverted;
    }

    /*
     * HELPERS
     */

    private static GridPoint2 randomPoint(int width, int height) {
        return new GridPoint2(
            MathUtils.random(0, width - 1),
            MathUtils.random(0, height - 1)
        );
    }

    private static void applyFirstExpansion(
        BiFunction<int[][], GridPoint2, GridPoint2> method,
        int[][] map
    ) {
        for (int i = 0; i < ROOM_ORIGINS.size(); i++) {
            int attempts = 0;
            GridPoint2 currPoint = ROOM_ORIGINS.get(i);
            GridPoint2 dims;

            // Attempt to expand given the current point
            do {
                dims = method.apply(map, currPoint);
                // If invalid, reassign a new point
                if (dims == null) {
                    attempts++;
                    currPoint = relocatePoint(map, currPoint);
                    ROOM_ORIGINS.set(i, currPoint);
                }
            } while (dims == null && attempts < MAX_EXPAND_RETRIES);

            if (dims != null)
                expandRoom(map, currPoint, dims);
            else
                ROOM_ORIGINS.remove(currPoint);
        }
    }

    private static void applyExpansion(
        BiFunction<int[][], GridPoint2, GridPoint2> method,
        int[][] map
    ) {
        /*
         * For subsequent expansions, there is already an
         * expanded room; thus, just skip if it can't
         */
        for (GridPoint2 point : ROOM_ORIGINS) {
            GridPoint2 dims = method.apply(map, point);
            if (dims != null)
                expandRoom(map, point, dims);
        }
    }

    private static GridPoint2 relocatePoint(int[][] map, GridPoint2 point) {
        ROOM_ORIGINS.remove(point);
        GridPoint2 newPoint;
        // Find a non-floor point
        do {
            newPoint = randomPoint(map[0].length, map.length);
        } while (map[newPoint.y][newPoint.x] != 0);
        // Then make it a new point
        ROOM_ORIGINS.add(newPoint);
        return newPoint;
    }

    // See implementations below to see why this is hideous
    private static void expandRoom(int[][] map, GridPoint2 point, GridPoint2 dims) {
        boolean xAtTop = dims.x > 0;
        boolean yAtLeft = dims.y > 0;
        int endY = MathUtils.clamp(
            point.y + dims.y,
            0,
            map.length - 1
        );
        int endX = MathUtils.clamp(
            point.x + dims.x,
            0,
            map[0].length - 1
        );
        int roomID = ROOM_ORIGINS.indexOf(point);

        for (
            int row = point.y;
            (yAtLeft) ? row <= endY : row >= endY;
            row = (yAtLeft) ? row + 1 : row - 1
        ) {
            for (
                int col = point.x;
                (xAtTop) ? col <= endX : col >= endX;
                col = (xAtTop) ? col + 1 : col - 1
            ) {
                map[row][col] = roomID;
            }
        }
    }

    /*
     * EXPAND METHODS
     *
     * Given a point (that is TileType.FLOOR), these methods search and find the
     * maximum size the room can grow. The naming convention assumes the passed
     * point is in that position, e.g., expandTopLeft() considers the point the
     * upper left corner of the room.
     *
     * The static fields determine if the room is valid. As of now:
     *     - Minimum width = 3
     *     - Minimum height = 3 (will become 2 due to the wall conversion)
     *     - Minimum area = 9 sq. units
     *
     * If invalid, the methods return null.
     */

    private static GridPoint2 expandTopLeft(int[][] map, GridPoint2 point) {
        GridPoint2 p = new GridPoint2(
            getDisplacementX(map, point, true),
            getDisplacementY(map, point, true)
        );
        int area = Math.abs(p.x * p.y);
        return (p.x == 0 || p.y == 0 || area < MIN_AREA) ? null : p;
    }

    private static GridPoint2 expandTopRight(int[][] map, GridPoint2 point) {
        GridPoint2 p = new GridPoint2(
            getDisplacementX(map, point, true),
            -getDisplacementY(map, point, false)
        );
        int area = Math.abs(p.x * p.y);
        return (p.x == 0 || p.y == 0 || area < MIN_AREA) ? null : p;
    }

    private static GridPoint2 expandBottomLeft(int[][] map, GridPoint2 point) {
        GridPoint2 p = new GridPoint2(
            -getDisplacementX(map, point, false),
            getDisplacementY(map, point, true)
        );
        int area = Math.abs(p.x * p.y);
        return (p.x == 0 || p.y == 0 || area < MIN_AREA) ? null : p;
    }

    private static GridPoint2 expandBottomRight(int[][] map, GridPoint2 point) {
        GridPoint2 p = new GridPoint2(
            -getDisplacementX(map, point, false),
            -getDisplacementY(map, point, false)
        );
        int area = Math.abs(p.x * p.y);
        return (p.x == 0 || p.y == 0 || area < MIN_AREA) ? null : p;
    }

    /*
     * ACTUAL IMPLEMENTATIONS
     *
     * This is incredibly ugly but I don't wanna copy-paste this another time LOL
     */

    private static int getDisplacementX(int[][] map, GridPoint2 point, boolean xAtTop) {
        int displacementX = 0;
        boolean validExpansion;
        GridPoint2 currPoint = new GridPoint2();
        int roomID = ROOM_ORIGINS.indexOf(point);

        do {
            validExpansion = false;
            displacementX++;
            // Check the tile at the same row as well as the rows above/below
            int startY = point.y + (xAtTop ? -1 : 1);
            startY = MathUtils.clamp(startY, 0, map.length - 1);
            int endY = point.y + (xAtTop ? MIN_HEIGHT + 2 : -(MIN_HEIGHT + 2));
            endY = MathUtils.clamp(endY, 0, map.length - 1);

            for (
                int y = startY;
                (xAtTop) ? y < endY : y >= endY;
                y = (xAtTop) ? y + 1 : y - 1
            ) {
                int xPos = point.x + (xAtTop ? displacementX : -displacementX);
                currPoint.set(xPos, y);
//                System.out.format("Trying coords (%d, %d)...\n", xPos, y);
                int tileID;
                try {
                    tileID = map[currPoint.y][currPoint.x];
                    validExpansion = !ROOM_ORIGINS.contains(currPoint)
                        && (tileID == 0 || tileID == roomID);
                    if (!validExpansion)
                        break;
                } catch (Exception e) {
                    validExpansion = false;
                }
            }
//            System.out.format("Displacement X: %d\n", displacementX);
        } while (validExpansion);

        // When not valid anymore, the real room width will be 2 less (1 gap + the offending floor)
        displacementX -= 2;

        if (FIRST_EXPANSION && displacementX + 1 < MIN_WIDTH) // displacement plus the existing tile
            return 0;

        if (displacementX < 0)
            displacementX = 0;

        return displacementX;
    }

    private static int getDisplacementY(int[][] map, GridPoint2 point, boolean yAtLeft) {
        int displacementY = 0;
        boolean validExpansion;
        GridPoint2 currPoint = new GridPoint2();
        int roomID = ROOM_ORIGINS.indexOf(point);

        do {
            validExpansion = false;
            displacementY++;
            // Check the tile at the same column as well as the rows to the right/left
            int startX = point.x + (yAtLeft ? -1 : 1);
            startX = MathUtils.clamp(startX, 0, map[0].length - 1);
            int endX = point.x + (yAtLeft ? MIN_WIDTH + 2 : -(MIN_WIDTH + 2));
            endX = MathUtils.clamp(endX, 0, map[0].length - 1);

            for (
                int x = startX;
                (yAtLeft) ? x < endX : x >= endX;
                x = (yAtLeft) ? x + 1 : x - 1
            ) {
                int yPos = point.y + (yAtLeft ? displacementY : -displacementY);
                currPoint.set(x, yPos);
//                    System.out.format("Trying coords (%d, %d)...\n", x, yPos);
                int tileID;
                try {
                    tileID = map[currPoint.y][currPoint.x];
                    validExpansion = !ROOM_ORIGINS.contains(currPoint)
                        && (tileID == 0 || tileID == roomID);
                    if (!validExpansion)
                        break;
                } catch (Exception e) {
                    validExpansion = false;
                }
            }
//            System.out.format("Displacement Y: %d\n", displacementY);
        } while (validExpansion);

        // When not valid anymore, the real room height will be 2 less (1 gap + the offending floor)
        displacementY -= 2;

        if (FIRST_EXPANSION && displacementY + 1 < MIN_HEIGHT) // displacement plus the existing tile
            return 0;

        if (displacementY < 0)
            displacementY = 0;

        return displacementY;
    }

    /*
     * GAP METHOD
     */

    private static boolean addGaps(int[][] map) {
        List<GridPoint2> doorCandidates = new ArrayList<>();

        int width = map[0].length, height = map.length;
        long floorCount = Arrays.stream(map)
            .flatMapToInt(Arrays::stream)
            .filter(tile -> tile != 0)
            .count();

        GridPoint2 firstCoord;
        do {
            firstCoord = randomPoint(width, height);
        } while (getTile(map, firstCoord) == 0);

        while (true) {
            boolean[][] visited = new boolean[height][width];
            Queue<GridPoint2> queue = new ArrayDeque<>();

            queue.add(firstCoord);
            visited[firstCoord.y][firstCoord.x] = true;
            floorCount--;

            // BFS
            while (!queue.isEmpty()) {
                GridPoint2 p = queue.poll();

                for (Direction dir : Direction.values()) {
                    GridPoint2 neighbor = dir.getNeighbor(p);

                    boolean isFloor, isVisited;
                    try {
                        isFloor = getTile(map, neighbor) != 0;
                        isVisited = visited[neighbor.y][neighbor.x];
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }

                    if (isFloor && !isVisited) {
                        visited[neighbor.y][neighbor.x] = true;
                        queue.add(neighbor);
                        floorCount--;
                    }
                }
            }

            if (floorCount == 0)
                break;

            doorCandidates.clear();

            // Find doorways
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (map[row][col] != 0)
                        continue;

                    for (Direction dir : Direction.values()) {
                        GridPoint2 first = dir.getNeighbor(col, row);
                        GridPoint2 second = dir.opposite().getNeighbor(col, row);

                        boolean bothFloors, onlyOneVisited;
                        try {
                            bothFloors = getTile(map, first) != 0
                                && getTile(map, second) != 0;
                            onlyOneVisited = visited[first.y][first.x]
                                != visited[second.y][second.x];
                        } catch (IndexOutOfBoundsException e) {
                            continue;
                        }

                        if (bothFloors && onlyOneVisited) {
                            doorCandidates.add(new GridPoint2(col, row));
                            break;
                        }
                    }
                }
            }

            if (doorCandidates.isEmpty())
                break; // failsafe

            // carve random candidate
            int i = MathUtils.random(0, doorCandidates.size() - 1);
            GridPoint2 carvePoint = doorCandidates.get(i);
            map[carvePoint.y][carvePoint.x] = DOORWAY_ID;
            queue.add(carvePoint);
//            System.out.format("Doorway carved at (%d, %d)!\n", carvePoint.x, carvePoint.y);
        }

        // See if the map is valid (all floor tiles visited)
        return floorCount <= 0;
    }

    // Helper classes for the gap finder

    private static int getTile(int[][] map, GridPoint2 point) {
        return getTile(map, point.x, point.y);
    }

    private static int getTile(int[][] map, int x, int y) {
        try {
            return map[y][x];
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    /*
     * ADD WALL TILES METHOD
     */
    private static int[][] addWallTiles(int[][] map) {
        int width = map[0].length, height = map.length;
        for (int row = height - 1; row > 0 ; row--) {
            for (int col = 0; col < width; col++) {
                boolean isDoorway;
                try {
                    isDoorway = map[row - 2][col] == 0
                        && (
                            map[row - 2][col - 1] != 0
                            || map[row - 2][col + 1] != 0
                        );
                } catch (IndexOutOfBoundsException e) {
                    isDoorway = false;
                }
                boolean isThinWall;
                try {
                    isThinWall = map[row - 2][col] != 0;
                } catch (IndexOutOfBoundsException e) {
                    isThinWall = false;
                }
                boolean isThinBackWall;
                try {
                    isThinBackWall = map[row + 1][col] != 0;
                } catch (IndexOutOfBoundsException e) {
                    isThinBackWall = false;
                }

                if (matchHoriDoorPattern(map, col, row)) {
                    map[row - 1][col] = DOORWAY_ID;
                }

                // If different vertically
                if (map[row][col] == 0
                    && map[row - 1][col] != 0) {
                    if (isDoorway) {
                        map[row - 2][col] = DOORWAY_ID;
                    }
                    if (isThinWall)
                        map[row - 1][col] = WALL_ID;
                    else if (isThinBackWall)
                        map[row][col] = DOORWAY_ID; // technically should be a room id
                    else
                        map[row][col] = WALL_ID;
                }
            }
        }
        // Special case for last row
        int lastRow = height - 1;
        for (int col = 0; col < width; col++) {
            if (map[lastRow][col] != 0) {
                map[lastRow][col] = WALL_ID;
                if (map[lastRow - 1][col] == 0)
                    map[lastRow - 1][col] = DOORWAY_ID;
            }
        }

        return map;
    }

    private static boolean matchHoriDoorPattern(int[][] map, int x, int y) {
        try {
            return getTile(map, x, y) != 0
                && getTile(map, x + 1, y) != 0
                && getTile(map, x + 2, y) != 0
                && getTile(map, x, y - 1) == 0 // Empty
                && getTile(map, x + 1, y - 1) != 0
                && getTile(map, x + 2, y - 1) == 0 // Empty
                && getTile(map, x, y - 2) != 0
                && getTile(map, x + 1, y - 2) != 0
                && getTile(map, x + 2, y - 2) != 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /*
     * FINAL VALIDITY CHECK
     */
    private static boolean allConnected(int[][] map) {
        int width = map[0].length, height = map.length;
        long floorCount = Arrays.stream(map)
            .flatMapToInt(Arrays::stream)
            .filter(tile -> tile != 0 && tile != WALL_ID)
            .count();

        GridPoint2 firstCoord;
        int tile;
        do {
            firstCoord = randomPoint(width, height);
            tile = getTile(map, firstCoord);
        } while (tile == 0 || tile == WALL_ID);

        boolean[][] visited = new boolean[height][width];
        Queue<GridPoint2> queue = new ArrayDeque<>();

        queue.add(firstCoord);
        visited[firstCoord.y][firstCoord.x] = true;
        floorCount--;

        // BFS
        while (!queue.isEmpty()) {
            GridPoint2 p = queue.poll();

            for (Direction dir : Direction.values()) {
                GridPoint2 neighbor = dir.getNeighbor(p);

                boolean isFloor, isVisited;
                try {
                    int tileValue = getTile(map, neighbor);
                    isFloor = tileValue != 0 && tileValue != WALL_ID;
                    isVisited = visited[neighbor.y][neighbor.x];
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }

                if (isFloor && !isVisited) {
                    visited[neighbor.y][neighbor.x] = true;
                    queue.add(neighbor);
                    floorCount--;
                }
            }
        }

        return floorCount == 0;
    }

    /*
     * SETTERS
     */
    public static void setValues(GameDifficulty difficulty) {
        ROOM_COUNT = difficulty.getRoomCount();
        DOORWAY_ID = ROOM_COUNT + 1;
    }

}
