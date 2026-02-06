package com.github.atollysis.maps;

public enum TileType {
    FLOOR,
    WALL,
    EMPTY;

    public static TileType getType(char dataCharacter) {
        return switch (dataCharacter) {
            case '=' -> FLOOR;
            case '*' -> WALL;
            case ' ' -> EMPTY;
            default ->
                throw new IllegalArgumentException("Unexpected character while reading map data: " + dataCharacter);
        };
    }
}
