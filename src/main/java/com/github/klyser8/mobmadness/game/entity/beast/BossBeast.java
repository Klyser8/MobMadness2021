package com.github.klyser8.mobmadness.game.entity.beast;

import com.github.klyser8.mobmadness.MobMadness;
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

public abstract class BossBeast extends Beast {

    public static final String MM_BOSS_TAG = "mm.boss";

    protected BossBar bossBar = null;
    protected int playerCount;

    protected final Map<EntityDamageEvent.DamageCause, Double> dmgCauseModifiers;
    protected final Map<SoulStateType, Double> soulStateModifiers;
    protected final List<PotionEffectType> potionImmunityList;

    public BossBeast(MobMadness plugin, BeastType type, World world, Game game,
                     Map<EntityDamageEvent.DamageCause, Double> dmgCauseModifiers,
                     Map<SoulStateType, Double> soulStateModifiers, List<PotionEffectType> potionImmunityList) {
        super(plugin, type, world, game);
        this.dmgCauseModifiers = dmgCauseModifiers;
        this.soulStateModifiers = soulStateModifiers;
        this.potionImmunityList = potionImmunityList;
        playerCount = game.getPlayers().size();
        initializeBossBar();
    }

    protected abstract void initializeBossBar();

    @Override
    public void spawn(Location spawnLoc) {
        showBossBar();
    }

    @Override
    protected Mob spawnEntity(Location spawnLoc, double maxHP) {
        super.spawnEntity(spawnLoc, maxHP);
        mob.setMetadata(MM_BOSS_TAG, new FixedMetadataValue(plugin, game.getTier().getNumericTier()));
        return mob;
    }

    protected void showBossBar() {
        if (bossBar == null) return;
        for (Player player : game.getPlayers().keySet()) {
            player.showBossBar(bossBar);
        }
    }

    protected void hideBossBar() {
        if (bossBar == null) return;
        for (Player player : game.getPlayers().keySet()) {
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
