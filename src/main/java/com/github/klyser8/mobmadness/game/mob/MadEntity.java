package com.github.klyser8.mobmadness.game.mob;

import com.destroystokyo.paper.entity.Pathfinder;
import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.loot.LootTable;
import com.github.klyser8.mobmadness.game.mob.soulstates.SoulState;
import com.github.klyser8.mobmadness.game.mob.soulstates.SoulStateType;
import com.github.klyser8.mobmadness.storage.GameData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public abstract class MadEntity {

    public static final String MM_TAG = "mm.mm";
    public static final String DAMAGE_TAG = "mm.damage";
    public static final String MULTIPLIER_TAG = "mm.multiplier";
    public static final String TIER_TAG = "mm.tier";
    //Arrow Tags
    public static final String MARKED_TAG = "mm.marked";
    public static final String PIERCED_TAG = "mm.pierced";
    public static final String ENCHANTED_TAG = "mm.enchanted";
    //Shotgun Tags
    public static final String PELLETS_DMG_TAG = "mm.smudger.damage";
    public static final String PELLETS_KNOCKBACK_TAG = "mm.smudger.knockback";

    protected final MobMadness plugin;
    protected final GameData gameData;

    protected final MadMobType type;
    protected final World world;
    protected Location spawnLoc;

    protected Map<SoulStateType, SoulState> currentSoulStates;

    protected LootTable lootTable;
    protected Random random;

    protected Mob mob;

    public MadEntity(MobMadness plugin, MadMobType type, World world, GameData gameData) {
        this.plugin = plugin;
        this.type = type;
        this.world = world;
        this.gameData = gameData;
        random = new Random();

        currentSoulStates = new HashMap<>();
    }

    protected Mob spawnEntity(Location spawnLoc, double maxHP, String name, ChatColor color) {
        this.spawnLoc = spawnLoc;
        mob = (Mob) world.spawnEntity(spawnLoc, type.getEntityType(), CreatureSpawnEvent.SpawnReason.REINFORCEMENTS);
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHP);
        mob.setHealth(maxHP);
        mob.setCustomName(color + name);
        mob.setCustomNameVisible(true);
        mob.setRemoveWhenFarAway(false);
        mob.setMetadata(MM_TAG, new FixedMetadataValue(plugin, type));
        mob.setMetadata(DAMAGE_TAG, new FixedMetadataValue(plugin, type.getDmg()));
        mob.setMetadata(TIER_TAG, new FixedMetadataValue(plugin, gameData.getTier().getNumericTier()));
        mob.setRemoveWhenFarAway(false);

        gameData.getMobMap().put(mob, this);
        gameData.setCurrentWeight(gameData.getCurrentWeight() + type.getWeight());
        return mob;
    }

    public abstract void spawn(Location spawnLoc);

    public void handleDeath() {
        gameData.getMobMap().remove(mob);
        gameData.setCurrentWeight(gameData.getCurrentWeight() + type.getWeight());
        world.dropItem(mob.getLocation(), lootTable.calculate());
    }

    public Mob getMob() {
        return mob;
    }

    public Map<SoulStateType, SoulState> getCurrentSoulStates() {
        return currentSoulStates;
    }

    /**
     * Returns the location the mob spawned in
     * @return
     */
    public Location getSpawnLoc() {
        return spawnLoc;
    }
}
