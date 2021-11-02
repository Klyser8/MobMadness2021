package com.github.klyser8.mobmadness.game.mob;

/**
 * Interface used for bosses that have 'child' bosses.
 */
public interface ParentBoss {

    void updateMergedBossBar();

    void setupOtherBosses();

}
