package com.github.klyser8.mobmadness.game.world;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameMap {

    private final MapType mapType;
    private final List<SpawnArea> spawnAreas;

    private final int mapSize;

    public GameMap(MapType mapType, List<SpawnArea> spawnAreas) {
        this.mapType = mapType;
        spawnAreas.sort((o1, o2) -> o2.getSize() - o1.getSize());
        this.spawnAreas = spawnAreas;
        Collections.reverse(spawnAreas);
        int size = 0;
        for (SpawnArea area : spawnAreas) {
            size+=area.getSize();
        }
        mapSize = size;
    }

    public MapType getMapType() {
        return mapType;
    }

    public List<SpawnArea> getSpawnAreas() {
        return spawnAreas;
    }

    public int getMapSize() {
        return mapSize;
    }
}
