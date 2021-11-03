package com.github.klyser8.mobmadness.game.mob;

public abstract class DualMobRunnable implements Runnable {

    protected final Beast firstMob;
    protected final Beast secondMob;

    public DualMobRunnable(Beast firstMob, Beast secondMob) {
        this.firstMob = firstMob;
        this.secondMob = secondMob;
    }

    @Override
    public void run() {

    }
}
