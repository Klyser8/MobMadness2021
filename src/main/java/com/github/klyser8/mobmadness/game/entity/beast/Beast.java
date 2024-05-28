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
 *
 * @param <T> what mob the beast will be.
 */
public abstract class Beast<T extends Mob> implements Soul {

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

    protected T mob;

    /**
     * Creates a new instance of a Beast.
     * Once it is created, it is spawned right away.
     *
     * @param plugin instance of {@link MobMadness}
     * @param type the beast type to create
     * @param world the world which the beast will be in
     * @param game the game the beast is bound to
     * @param spawnLoc the spawning location of the beast
     */
    public Beast(MobMadness plugin, BeastType type, World world, Game game, Location spawnLoc) {
        this.plugin = plugin;
        this.type = type;
        this.world = world;
        this.game = game;
        this.spawnLoc = spawnLoc;
        random = new Random();
        currentSoulStates = new HashMap<>();
        //noinspection unchecked                                                              //Generic casting is okay!
        mob = (T) world.spawnEntity(spawnLoc, type.getEntityType(), CreatureSpawnEvent.SpawnReason.REINFORCEMENTS);
        setup();
    }

    protected abstract void createLootTable();

    /**
     * The standard set up for a beast is written in this method.
     * Mob Madness beasts are mobs which have specific behaviors, those being listed below
     * - Have a custom name, visible to players
     * - Will not despawn when far away
     * - Possess metadata indicating which tier they belong to and their {@link BeastType}
     */
    @SuppressWarnings("ConstantConditions")
    protected void setup() {
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getHP());
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(type.getDmg());
        mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(type.getSpeed());
        mob.setHealth(type.getHP());
        mob.customName(type.getNameComp().color(type.getNameColor()));
        mob.setCustomNameVisible(true);
        mob.setRemoveWhenFarAway(false);
        mob.setMetadata(BEAST_TYPE_TAG, new FixedMetadataValue(plugin, type));
        mob.setMetadata(TIER_TAG, new FixedMetadataValue(plugin, game.getTier().getNumericTier()));

        game.getBeastMap().put(mob, this);
        game.setCurrentWeight(game.getCurrentWeight() + type.getWeight());
    }

    /**
     * Handles death logic for the mob.
     */
    public void handleDeath() {
        game.getBeastMap().remove(mob);
        game.setCurrentWeight(game.getCurrentWeight() - type.getWeight());
        world.dropItem(mob.getLocation(), lootTable.calculate());
        if (mob.getKiller() != null) {
            Player killer = mob.getKiller();
            killer.setLevel(killer.getLevel() + type.getWeight());
        }
    }

    /**
     * Removes the mob without handling its death as regular.
     *
     */
    public void remove() {
        game.setCurrentWeight(game.getCurrentWeight() - type.getWeight());
        mob.remove();
    }

    /**
     * The instance of the beast's mob class.
     *
     * @return the instance of the beast's mob class.
     */
    public T getMob() {
        return mob;
    }

    /**
     * See {@link Soul}
     */
    @Override
    public Map<SoulStateType, SoulState> getCurrentSoulStates() {
        return currentSoulStates;
    }

    /**
     * Returns the location the mob spawned in
     * @return the mob's spawn location, or null if the mob hasn't spawned yet.
     */
    public Location getSpawnLoc() {
        return spawnLoc;
    }

    /**
     * Util method to check if the entity currently has a specific soul state.
     *
     * @param type the soul state to check for
     * @return whether the entity has the specified soul state.
     */
    public boolean hasSoulState(SoulStateType type) {
        return currentSoulStates.containsKey(type);
    }

    /**
     * Returns the general cooldown of this entity. Used for abilities.
     * @return the entity's cooldown
     */
    public int getGeneralCooldown() {
        return generalCooldown;
    }

    /**
     * Sets the general cooldown of this entity. Used for abilities
     * @param generalCooldown the cooldown time in ticks
     */
    public void setGeneralCooldown(int generalCooldown) {
        this.generalCooldown = generalCooldown;
    }

    /**
     * See {@link Soul}
     */
    @Override
    public LivingEntity getLivingEntity() {
        return mob;
    }
}
