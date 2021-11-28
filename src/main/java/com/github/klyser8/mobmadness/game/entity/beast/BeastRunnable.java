package com.github.klyser8.mobmadness.game.entity.beast;

import org.bukkit.Bukkit;

public abstract class BeastRunnable<T extends Beast<?>> implements Runnable {

    protected final T beast;

    protected BeastRunnable(T beast) {
        this.beast = beast;
    }

    @Override
    public void run() {
        if (beast.getMob().isDead()) {
            Thread.currentThread().interrupt();
        }
        if (beast.generalCooldown > 0) {
            beast.generalCooldown--;
        }
        //if the boss is too far (on Y axis) from the spawn point, it gets teleported back to it.
        if (beast.game == null) return;
        if (beast instanceof BossBeast && beast.getMob() != null) {
            if (Math.abs(beast.game.getMapSpawnPoint().getY() - beast.getMob().getLocation().getY()) > 30) {
                beast.getMob().teleport(beast.game.getMapSpawnPoint());
            }
        }
    }
}
