package com.github.klyser8.mobmadness.game.mob.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.storage.GameData;

/**
 * A soul state is what Mob Madness's potion effects/status effects are called.
 * These can be applied to monsters usually, but also to players if the subclasses are
 * written correctly.
 */
public abstract class SoulState {

    protected final MobMadness plugin;
    protected final GameData gameData;
    protected final SoulStateType type;
    protected final MadEntity entity;
    protected final int level;
    protected final int duration; //in ticks
    protected int currentDuration; //in ticks

    protected SoulState(MobMadness plugin, GameData gameData, SoulStateType type, MadEntity entity,
                        int level, int duration) {
        this.plugin = plugin;
        this.gameData = gameData;
        this.type = type;
        this.entity = entity;
        this.level = level;
        this.duration = duration;
        this.currentDuration = duration;
    }

    /**
     * Logic to run the soul state itself. This is where the code dictating the soul state's behavior is written.
     */
    public abstract void runSoulState();

    /**
     * Logic which plays particle effects for the soul state
     */
    public abstract void playSoulStateParticles();

    /**
     * Logic which plays sound effects for the soul state
     */
    public abstract void playSoulStateSounds();

    public SoulStateType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getDuration() {
        return duration;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }
}
