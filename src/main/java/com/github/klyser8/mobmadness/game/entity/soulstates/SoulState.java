package com.github.klyser8.mobmadness.game.entity.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.Soul;
import com.github.klyser8.mobmadness.storage.Game;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A soul state is a status effect applied to an entity.
 * These are applied to monsters usually, but also to players if the subclasses are
 * written correctly.
 */
public abstract class SoulState {

    protected final MobMadness plugin;
    protected final Game game;
    protected final SoulStateType type;
    protected final Soul soul;
    protected int level;
    protected int duration; //in ticks
    protected int currentDuration; //in ticks

    protected final boolean applied;

    protected SoulState(MobMadness plugin, Game game, SoulStateType type, Soul soul,
                        int level, int duration) {
        this.plugin = plugin;
        this.game = game;
        this.type = type;
        this.soul = soul;
        this.level = level;
        this.duration = duration;
        this.currentDuration = duration;
        //Checks whether the entity already has a soul state of this type. If the entity does, the soul state is stopped
        //And replaced with this new instance (only if this new one is judged to be stronger).
        if (!soul.getCurrentSoulStates().containsKey(type)
                || soul.getCurrentSoulStates().get(type).level < level
                || soul.getCurrentSoulStates().get(type).getCurrentDuration() < currentDuration) {
            if (soul.getCurrentSoulStates().containsKey(type)) {
                soul.getCurrentSoulStates().get(type).endSoulState();
            }
            soul.getCurrentSoulStates().put(type, this);
            applied = true;
        } else {
            applied = false;
        }
    }

    /**
     * This is what runs the SoulState's code every tick.
     * However, this will start only if the soul state was applied correctly.
     */
    protected void runSoulState(int particleInterval, int soundInterval) {
        startSoulState();
        new BukkitRunnable() {
            @Override
            public void run() {
                applySoulStateLoop();
                if (particleInterval > 0 && currentDuration % particleInterval == 0) {
                    playSoulStateParticles();
                }
                if (soundInterval > 0 && currentDuration % soundInterval == 0) {
                    playSoulStateSounds();
                }
                currentDuration--;
                if (currentDuration == 0 || soul.getLivingEntity().isDead()) {
                    cancel();
                    endSoulState();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    /**
     * Logic to start the soul state. Only runs once, at the beginning.
     */
    protected abstract void startSoulState();

    /**
     * Logic to run the soul state itself. This is where the code dictating the soul state's behavior is written.
     */
    protected abstract void applySoulStateLoop();

    /**
     * Logic which plays particle effects for the soul state
     */
    protected abstract void playSoulStateParticles();

    /**
     * Logic which plays sound effects for the soul state
     */
    protected abstract void playSoulStateSounds();

    public void endSoulState() {
        soul.getCurrentSoulStates().remove(getType());
    }

    public SoulStateType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        if (currentDuration > this.duration) {
            currentDuration = this.duration;
        }
    }

    public int getCurrentDuration() {
        return currentDuration;
    }
}
