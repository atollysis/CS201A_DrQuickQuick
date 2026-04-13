package com.github.atollysis.maps;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {

    TOP, RIGHT, BOTTOM, LEFT;

    public Direction opposite() {
        return switch (this) {
            case TOP    -> BOTTOM;
            case BOTTOM -> TOP;
            case LEFT   -> RIGHT;
            case RIGHT  -> LEFT;
        };
    }

    public GridPoint2 getNeighbor(int x, int y) {
        return switch (this) {
            case TOP    -> new GridPoint2(x, y - 1);
            case BOTTOM -> new GridPoint2(x, y + 1);
            case LEFT   -> new GridPoint2(x - 1, y);
            case RIGHT  -> new GridPoint2(x + 1, y);
        };
    }

    public GridPoint2 getNeighbor(GridPoint2 point) {
        return getNeighbor(point.x, point.y);
    }

}
