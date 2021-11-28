package com.github.klyser8.mobmadness.game.entity.beast;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.Soul;
import com.github.klyser8.mobmadness.game.entity.soulstates.SoulState;
import com.github.klyser8.mobmadness.game.entity.soulstates.SoulStateType;
import com.github.klyser8.mobmadness.storage.Game;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a stronger beast which displays a {@link BossBar} once spawned.
 *
 * @param <T> what mob the boss will be.
 */
public abstract class BossBeast<T extends Mob> extends Beast<T> {

    public static final String MM_BOSS_TAG = "mm.boss";

    protected BossBar bossBar;
    protected int playerCount;

    protected final Map<EntityDamageEvent.DamageCause, Double> dmgCauseModifiers;
    protected final Map<SoulStateType, Double> soulStateModifiers;
    protected final List<PotionEffectType> potionImmunityList;

    public BossBeast(MobMadness plugin, BeastType type, World world, Game game, Location spawnLoc,
                     Map<EntityDamageEvent.DamageCause, Double> dmgCauseModifiers,
                     Map<SoulStateType, Double> soulStateModifiers, List<PotionEffectType> potionImmunityList) {
        super(plugin, type, world, game, spawnLoc);
        this.dmgCauseModifiers = dmgCauseModifiers;
        this.soulStateModifiers = soulStateModifiers;
        this.potionImmunityList = potionImmunityList;
        playerCount = game.getPlayers().size();
    }

    /**
     * See {@link Beast}
     */
    @Override
    protected void setup() {
        super.setup();
        mob.setMetadata(MM_BOSS_TAG, new FixedMetadataValue(plugin, game.getTier().getNumericTier()));
        initializeBossBar();
        showBossBar();
    }

    /**
     * Initializes the beast's {@link #bossBar}.
     */
    protected abstract void initializeBossBar();

    /**
     * Makes the bossBar visible to the game's players.
     */
    protected void showBossBar() {
        for (Player player : game.getPlayers().keySet()) {
            player.showBossBar(bossBar);
        }
        updateBossBar();
    }

    /**
     * Makes the bossBar invisible to the game's players.
     */
    protected void hideBossBar() {
        for (Player player : game.getPlayers().keySet()) {
            player.hideBossBar(bossBar);
        }
    }

    /**
     * Updates the progress of the boss bar.
     */
    public abstract void updateBossBar();

    /**
     * See {@link Beast}
     * Additionally, hides the beast's bossBar.
     */
    @Override
    public void handleDeath() {
        super.handleDeath();
        hideBossBar();
    }

    /**
     * See {@link Beast}
     */
    @Override
    public void remove() {
        super.remove();
        hideBossBar();
    }

    /**
     * Method used to apply modifiers to the damage taken by the boss beast.
     * Uses the values in {@link #dmgCauseModifiers} to alter the final damage applied to the beast.
     *
     * @param event a reference of a {@link EntityDamageEvent} event.
     */
    public void handleDamage(EntityDamageEvent event) {
        if (dmgCauseModifiers.containsKey(event.getCause())) {
            if (dmgCauseModifiers.get(event.getCause()) == 0) {
                event.setCancelled(true);
            }
            event.setDamage(event.getDamage() * dmgCauseModifiers.get(event.getCause()));
        }
    }

    /**
     * Method used to apply modifiers on a specific soulstate to the boss beast.
     * When
     *
     * @param soulState the soulstate to apply the modifier on
     * @return the updated soulstate
     */
    public SoulState handleSoulState(SoulState soulState) {
        if (soulStateModifiers.containsKey(soulState.getType())) {
            soulState.setDuration((int) (soulState.getDuration() * soulStateModifiers.get(soulState.getType())));
            if (soulStateModifiers.get(soulState.getType()) < 0.33 && soulState.getLevel() > 1) {
                soulState.setLevel(soulState.getLevel() - 1);
            }
        }
        return soulState;
    }

    /**
     * Method used to remove any potion effect which the beast is immune to.
     */
    public void handlePotionEffects() {
        Collection<PotionEffect> potionEffects = mob.getActivePotionEffects().stream().dropWhile(
                potionEffect -> potionImmunityList.contains(potionEffect.getType())).collect(Collectors.toList());
        mob.addPotionEffects(potionEffects);
        for (PotionEffectType potionType : potionImmunityList) {
            if (mob.hasPotionEffect(potionType)) {
                mob.removePotionEffect(potionType);
            }
        }
    }
}
