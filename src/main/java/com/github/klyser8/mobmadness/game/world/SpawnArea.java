package com.github.klyser8.mobmadness.game.world;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Contains data related to spawn areas of monsters.
 * This is where mobs will spawn.
 */
public class SpawnArea {

    private final World world;
    private final Location corner1;
    private final Location corner2;

    private final int size;

    public SpawnArea(World world, Location corner1, Location corner2) {
        this.world = world;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.size = calculateSize();
    }

    public World getWorld() {
        return world;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public int getSize() {
        return size;
    }

    private int calculateSize() {
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        return Math.abs((maxX - minX) * (maxY - minY) * (maxZ - minZ));
    }

    /*
    private int calculateSize() {
        int absX1 = Math.abs(corner1.getBlockX());
        int absX2 = Math.abs(corner2.getBlockX());
        int absY1 = Math.abs(corner1.getBlockY());
        int absY2 = Math.abs(corner2.getBlockY());
        int absZ1 = Math.abs(corner1.getBlockZ());
        int absZ2 = Math.abs(corner2.getBlockZ());

        return Math.abs(Math.abs(absX2 - absX1) * Math.abs(absY2 - absY1) * Math.abs(absZ2 - absZ1));
    }
     */
}
