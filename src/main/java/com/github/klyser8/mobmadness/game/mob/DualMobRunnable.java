package com.github.klyser8.mobmadness.game.mob;

public abstract class DualMobRunnable implements Runnable {

    protected final MadEntity firstMob;
    protected final MadEntity secondMob;

    public DualMobRunnable(MadEntity firstMob, MadEntity secondMob) {
        this.firstMob = firstMob;
        this.secondMob = secondMob;
    }

    @Override
    public void run() {

    }
}
