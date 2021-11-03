package com.github.klyser8.mobmadness.game.mob.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.Beast;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A soul state is what Mob Madness's potion effects/status effects are called.
 * These can be applied to monsters usually, but also to players if the subclasses are
 * written correctly.
 */
public abstract class SoulState {

    protected final MobMadness plugin;
    protected final GameData gameData;
    protected final SoulStateType type;
    protected final Beast beast;
    protected int level;
    protected int duration; //in ticks
    protected int currentDuration; //in ticks

    protected final boolean applied;

    protected SoulState(MobMadness plugin, GameData gameData, SoulStateType type, Beast beast,
                        int level, int duration) {
        this.plugin = plugin;
        this.gameData = gameData;
        this.type = type;
        this.beast = beast;
        this.level = level;
        this.duration = duration;
        this.currentDuration = duration;
        //Checks whether the entity already has a soul state of this type. If the entity does, the soul state is stopped
        //And replaced with this new instance (only if this new one is judged to be stronger).
        if (!beast.getCurrentSoulStates().containsKey(type)
                || beast.getCurrentSoulStates().get(type).level < level
                || beast.getCurrentSoulStates().get(type).getCurrentDuration() < currentDuration) {
            if (beast.getCurrentSoulStates().containsKey(type)) {
                beast.getCurrentSoulStates().get(type).endSoulState();
            }
            beast.getCurrentSoulStates().put(type, this);
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
                if (currentDuration % particleInterval == 0) {
                    playSoulStateParticles();
                }
                if (currentDuration % soundInterval == 0) {
                    playSoulStateSounds();
                }
                System.out.println(currentDuration);
                currentDuration--;
                if (currentDuration == 0 || beast.getMob().isDead()) {
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
        beast.getCurrentSoulStates().remove(getType());
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
