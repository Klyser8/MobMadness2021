package com.github.klyser8.mobmadness.game.entity.beast;

/**
 * Interface used for bosses that have 'child' bosses.
 */
public interface ParentBoss {

    void updateMergedBossBar();

    void setupOtherBosses();

}
