package com.github.klyser8.mobmadness.game.entity.beast;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.Tier;
import com.github.klyser8.mobmadness.game.entity.Soul;
import com.github.klyser8.mobmadness.game.entity.beast.loot.LootTable;
import com.github.klyser8.mobmadness.game.entity.soulstates.SoulState;
import com.github.klyser8.mobmadness.game.entity.soulstates.SoulStateType;
import com.github.klyser8.mobmadness.storage.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

/**
 * Represents a beast in a Mob Madness game.
 * Works as an 'extension' to the {@link Mob} class, with extra data.
 */
public abstract class Beast implements Soul {

    /**
     * Tag used to store the beast's type {@link BeastType} as a string.
     */
    public static final String BEAST_TYPE_TAG = "mm.type";
    /**
     * Tag used to store the beast's default tier. Stores a reference to {@link Tier#getNumericTier()}
     */
    public static final String TIER_TAG = "mm.tier";

    //Arrow Tags
    /**
     * Tag used to store whether the beast is currently 'marked'. Deprecated, as marked should be a soul state
     */
    public static final String MARKED_TAG = "mm.arrow.marked";
    /**
     * Tag used to store whether the beast has been pierced by a given arrow or not.
     * Stores a reference to {@link Arrow#getUniqueId()#toString()}
     */
    public static final String PIERCED_TAG = "mm.arrow.pierced";

    //Shotgun Tags
    /**
     * Tag used to store how many pellets an entity has been hit by at once.
     */
    public static final String PELLETS_DMG_TAG = "mm.smudger.damage";
    /**
     * Tag used to store the velocity that should be applied to the beast once a smudger's pellet's logic
     * has been applied.
     */
    public static final String PELLETS_KNOCKBACK_TAG = "mm.smudger.knockback";

    //AI tags
    /**
     * Tag used to store whether the beast should ignore players or not.
     * This prevents a Beast from targeting players completely
     */
    public static final String IGNORE_PLAYERS_TAG = "mm.ai.ignore_players";

    //General cooldown used for beasts abilities.
    protected int generalCooldown = 0;

    protected final MobMadness plugin;
    protected final Game game;

    protected final BeastType type;
    protected final World world;
    protected Location spawnLoc;

    protected Runnable mobRunnable = null;

    protected final Map<SoulStateType, SoulState> currentSoulStates;

    protected LootTable lootTable;
    protected Random random;

    protected Mob mob;

    public Beast(MobMadness plugin, BeastType type, World world, Game game) {
        this.plugin = plugin;
        this.type = type;
        this.world = world;
        this.game = game;
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
        mob.setMetadata(BEAST_TYPE_TAG, new FixedMetadataValue(plugin, type));
        mob.setMetadata(TIER_TAG, new FixedMetadataValue(plugin, game.getTier().getNumericTier()));
        mob.setRemoveWhenFarAway(false);

        game.getBeastMap().put(mob, this);
        game.setCurrentWeight(game.getCurrentWeight() + type.getWeight());
        return mob;
    }

    public abstract void spawn(Location spawnLoc);

    public void handleDeath() {
        game.getBeastMap().remove(mob);
        game.setCurrentWeight(game.getCurrentWeight() - type.getWeight());
        world.dropItem(mob.getLocation(), lootTable.calculate());
        if (mob.getKiller() != null) {
            Player killer = mob.getKiller();
            killer.setLevel(killer.getLevel() + type.getWeight());
        }
    }

    public void remove() {
        game.setCurrentWeight(game.getCurrentWeight() - type.getWeight());
        mob.remove();
    }

    public Mob getMob() {
        return mob;
    }

    @Override
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

    @Override
    public LivingEntity getLivingEntity() {
        return mob;
    }
}
