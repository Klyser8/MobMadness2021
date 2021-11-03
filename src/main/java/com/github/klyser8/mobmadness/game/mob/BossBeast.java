package com.github.klyser8.mobmadness.game.mob;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.soulstates.SoulState;
import com.github.klyser8.mobmadness.game.mob.soulstates.SoulStateType;
import com.github.klyser8.mobmadness.storage.GameData;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BossBeast extends Beast {

    public static final String MM_BOSS_TAG = "mm.boss";

    protected BossBar bossBar = null;
    protected int playerCount;

    protected final Map<EntityDamageEvent.DamageCause, Double> dmgCauseModifiers;
    protected final Map<SoulStateType, Double> soulStateModifiers;
    protected final List<PotionEffectType> potionImmunityList;

    public BossBeast(MobMadness plugin, BeastType type, World world, GameData gameData,
                     Map<EntityDamageEvent.DamageCause, Double> dmgCauseModifiers,
                     Map<SoulStateType, Double> soulStateModifiers, List<PotionEffectType> potionImmunityList) {
        super(plugin, type, world, gameData);
        this.dmgCauseModifiers = dmgCauseModifiers;
        this.soulStateModifiers = soulStateModifiers;
        this.potionImmunityList = potionImmunityList;
        playerCount = gameData.getPlayers().size();
        initializeBossBar();
    }

    protected abstract void initializeBossBar();

    @Override
    public void spawn(Location spawnLoc) {
        showBossBar();
    }

    protected void showBossBar() {
        if (bossBar == null) return;
        for (Player player : gameData.getPlayers().keySet()) {
            player.showBossBar(bossBar);
        }
    }

    protected void hideBossBar() {
        if (bossBar == null) return;
        for (Player player : gameData.getPlayers().keySet()) {
            player.hideBossBar(bossBar);
        }
    }

    public abstract void updateBossBar();

    @Override
    public void handleDeath() {
        super.handleDeath();
        hideBossBar();
    }

    @Override
    public void remove() {
        super.remove();
        hideBossBar();
    }

    public void handleDamage(EntityDamageEvent event) {
        if (dmgCauseModifiers.containsKey(event.getCause())) {
            if (dmgCauseModifiers.get(event.getCause()) == 0) {
                event.setCancelled(true);
            }
            event.setDamage(event.getDamage() * dmgCauseModifiers.get(event.getCause()));
        }
    }

    public SoulState handleSoulState(SoulState soulState) {
        if (soulStateModifiers.containsKey(soulState.getType())) {
            soulState.setDuration((int) (soulState.getDuration() * soulStateModifiers.get(soulState.getType())));
            if (soulStateModifiers.get(soulState.getType()) < 0.33 && soulState.getLevel() > 1) {
                soulState.setLevel(soulState.getLevel() - 1);
            }
        }
        return soulState;
    }

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
