package com.github.klyser8.mobmadness.game.mob;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.loot.LootTable;
import com.github.klyser8.mobmadness.game.mob.soulstates.SoulState;
import com.github.klyser8.mobmadness.game.mob.soulstates.SoulStateType;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public abstract class Beast {

    public static final String MM_TYPE_TAG = "mm.type";
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
    //AI tags
    public static final String IGNORE_PLAYERS_TAG = "mm.ai.ignore_players";

    protected int generalCooldown = 0;

    protected final MobMadness plugin;
    protected final GameData gameData;

    protected final BeastType type;
    protected final World world;
    protected Location spawnLoc;

    protected Runnable mobRunnable = null;

    protected final Map<SoulStateType, SoulState> currentSoulStates;

    protected LootTable lootTable;
    protected Random random;

    protected Mob mob;

    public Beast(MobMadness plugin, BeastType type, World world, GameData gameData) {
        this.plugin = plugin;
        this.type = type;
        this.world = world;
        this.gameData = gameData;
        random = new Random();

        currentSoulStates = new HashMap<>();
    }

    protected void createLootTable() {

    }

    protected Mob spawnEntity(Location spawnLoc, double maxHP) {
        this.spawnLoc = spawnLoc;
        mob = (Mob) world.spawnEntity(spawnLoc, type.getEntityType(), CreatureSpawnEvent.SpawnReason.REINFORCEMENTS);
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHP);
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(type.getDmg());
        mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(type.getSpeed());
        mob.setHealth(maxHP);
        mob.customName(type.getNameComp().color(type.getNameColor()));
        mob.setCustomNameVisible(true);
        mob.setRemoveWhenFarAway(false);
        mob.setMetadata(MM_TYPE_TAG, new FixedMetadataValue(plugin, type));
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
        gameData.setCurrentWeight(gameData.getCurrentWeight() - type.getWeight());
        world.dropItem(mob.getLocation(), lootTable.calculate());
        if (mob.getKiller() != null) {
            Player killer = mob.getKiller();
            killer.setLevel(killer.getLevel() + type.getWeight());
        }
    }

    public void remove() {
        gameData.setCurrentWeight(gameData.getCurrentWeight() - type.getWeight());
        mob.remove();
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

    /**
     * Util method to check if the entity currently has a specific soul state.
     *
     * @param type the soul state to check for
     * @return
     */
    public boolean hasSoulState(SoulStateType type) {
        return currentSoulStates.containsKey(type);
    }

    /**
     * Returns the general cooldown of this entity. Used for abilities.
     * @return
     */
    public int getGeneralCooldown() {
        return generalCooldown;
    }

    /**
     * Sets the general cooldown of this entity. Used for abilities
     * @param generalCooldown
     */
    public void setGeneralCooldown(int generalCooldown) {
        this.generalCooldown = generalCooldown;
    }
}
