package com.github.klyser8.mobmadness.game.entity.beast;

public abstract class BeastRunnable<T extends Beast> implements Runnable {

    protected final T entity;

    protected BeastRunnable(T entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        if (entity.generalCooldown > 0) {
            entity.generalCooldown--;
        }
    }
}
