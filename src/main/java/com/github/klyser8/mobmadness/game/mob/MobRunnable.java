package com.github.klyser8.mobmadness.game.mob;

public abstract class MobRunnable<T extends MadEntity> implements Runnable {

    protected final T entity;

    protected MobRunnable(T entity) {
        this.entity = entity;
    }

    @Override
    public void run() {
        if (entity.generalCooldown > 0) {
            entity.generalCooldown--;
        }
    }
}
