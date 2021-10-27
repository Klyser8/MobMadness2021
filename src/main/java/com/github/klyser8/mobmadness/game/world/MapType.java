package com.github.klyser8.mobmadness.game.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum MapType {

    CRYPTIC_CAVE(0, "Cryptic Cave",
            new Location(Bukkit.getWorld("mobmadness"), 9983, 100, -13), 6);

    private final int id;
    private final String name;
    private final Location spawn;
    private final int maxPlayers;

    MapType(int id, String name, Location spawn, int maxPlayers) {
        this.id = id;
        this.name = name;
        this.spawn = spawn;
        this.maxPlayers = maxPlayers;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * returns a copy of the map's spawn point.
     *
     * @return
     */
    public Location getSpawn() {
        return spawn.clone();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public static MapType getMapById(int id) {
        switch (id) {
            case 0: return CRYPTIC_CAVE;
        }
        return null;
    }

    public static int getMapAmount() {
        return values().length;
    }
}
