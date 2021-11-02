package com.github.klyser8.mobmadness.util;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.game.world.SpawnArea;
import com.github.klyser8.mobmadness.storage.GameData;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Predicate;

import static com.github.klyser8.mobmadness.util.MadMath.isInteger;
import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Class which contains dozens of methods which access the BukkitAPI.
 */
public class MadUtils {

    /** Chooses a random location in a box, and returns it.
     *  The selected location must be empty, and have space
     *  for a mob to spawn in. Additionally, there should
     *  be a solid block below it.
     *
     * @param min a corner of the chosen box
     * @param max a corner which is opposite to the min corner
     * @return a random location.
     */
    //TODO fix mobs spawning near players and in walls
    public static Location pickRandomSpawnLocation(Location min, Location max) {
        World world = min.getWorld();
        int attempts = 0;
        while (attempts < 25) {
            //Gets a new absolute location, not allowing negative numbers. Used to pick a random number for x, y and z.
            Location range = new Location(world, Math.abs(max.getX() - min.getX()),
                    Math.abs(max.getY() - min.getY()), Math.abs(max.getZ() - min.getZ()));
            //Multiplies the approx. Location by a double between 0 and 1, to get a random loc.
            Location spawnLoc = new Location(world,
                    (Math.random() * range.getX()) + (Math.min(min.getX(), max.getX())),
                    (Math.random() * range.getY()) + (Math.min(min.getY(), max.getY())),
                    (Math.random() * range.getZ()) + (Math.min(min.getZ(), max.getZ())));
            int y = spawnLoc.getBlockY();
            //Checks if there is an empty block 5 blocks above and 5 blocks under the picked location for a mob to spawn in.
            //Avoids selecting an area above a "dangerous" block.
            int yOffset = -5;
            while (yOffset < 5) {
                Block beneath = world.getBlockAt(spawnLoc.clone().add(0, yOffset - 1, 0));
                Block above = world.getBlockAt(spawnLoc.clone().add(0, yOffset + 1, 0));
                if (world.getBlockAt(spawnLoc.clone().add(0, yOffset, 0)).isPassable() &&
                        (above.isPassable() && beneath.isSolid()) &&
                        (beneath.getType() != Material.MAGMA_BLOCK && beneath.getType() != Material.CACTUS
                        && beneath.getType() != Material.POINTED_DRIPSTONE)) {
                    spawnLoc.setY(y + yOffset);
                    return spawnLoc.add(0.5, 0.5, 0.5);
                }
                yOffset++;
            }
            attempts++;
        }
        return null;
    }

    /**
     * Gets the MadEntity closest to a location, excluding players.
     *
     * @param loc
     * @return the mob closest to said location
     */
    /*public static LivingEntity getMadMobClosestToLocation(Location loc, GameData gameData) {
        LivingEntity entity = null;
        //Loops all entities in the current game
        Player player;
        for (MadEntity madEntity : gameData.getMobMap().values()) {
            //A value needs to be assigned to entity to avoid NPE.
            if (entity == null ||
                    (loc.distance(madEntity.getMob().getLocation()) < loc.distance(entity.getLocation())
                    && !entity.isDead())) {
                entity = madEntity.getMob();
            }
        }
        return entity;
    }*/
    public static LivingEntity getMadMobClosestToLocation(Location loc) {
        List<LivingEntity> nearbyEntities = (List<LivingEntity>) loc.getNearbyLivingEntities(100, 20, 100);
        nearbyEntities.removeIf(livingEntity -> livingEntity instanceof Player);
        nearbyEntities.sort(Comparator.comparingDouble(o -> o.getLocation().distanceSquared(loc)));
        return nearbyEntities.size() == 0 ? null : nearbyEntities.get(0);
    }

    /** Returns the closest mob to the player.
     *
     * @param loc entity to check
     * @return the closest mob found
     */
    public static LivingEntity getClosestMob(Location loc, double rangeX, double rangeY, double rangeZ) {
        List<LivingEntity> entityList = new ArrayList<>();
        for (Entity nearbyEntity : loc.getNearbyEntities(rangeX, rangeY, rangeZ)) {
            if (nearbyEntity instanceof LivingEntity && !(nearbyEntity instanceof Player) && !(nearbyEntity instanceof ArmorStand)) {
                entityList.add((LivingEntity) nearbyEntity);
            }
        }
        LivingEntity closestMob = null;
        double distance = 2000;
        for (LivingEntity mob : entityList) {
            if (distance > mob.getLocation().distance(loc) && !mob.isDead()) {
                distance = mob.getLocation().distance(loc);
                closestMob = mob;
            }
        }
        return closestMob;
    }

    /**
     * Gets the direction between two locations.
     *
     * @param Start Starting location
     * @param End Target location
     * @return Direction vector
     */
    public static Vector getDirectionBetweenLocations(Location Start, Location End) {
        Vector from = Start.toVector();
        Vector to = End.toVector();
        return to.subtract(from);
    }

    /**
     * Displays the 'cooldown' of an item through its durability meter.
     * Only works on items that have durability.
     * @param plugin instance of {@link MobMadness}
     * @param item the ItemStack to apply durability changes to
     * @param cooldown the 'cooldown'
     */
    public static void displayCooldown(MobMadness plugin, ItemStack item, Integer cooldown) {
        int halfCooldown = cooldown / 2;
        new BukkitRunnable() {
            short currentDurability = (short) (item.getType().getMaxDurability() - 1);
            @Override
            public void run() {
                double maxDurability = item.getType().getMaxDurability();
                //Starts cooldown
                item.setDurability((short) (maxDurability - 1));
                double durabilityPerTick = (short) Math.round((maxDurability - 1) / halfCooldown);
                if (currentDurability - durabilityPerTick <= 0) {
                    item.setDurability((short) 1);
                    cancel();
                }
                item.setDurability(currentDurability);
                currentDurability-=durabilityPerTick;
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    /**
     * Returns damage post armor and potion effect mitigations.
     *
     * @param damage amount of base damage to inflict
     * @param entity damaged entity
     * @return total damage after various mitigation calculations.
     */
    public static double getDamagePostMitigation(Double damage, LivingEntity entity) {
        double armor = entity.getAttribute(Attribute.GENERIC_ARMOR).getValue();
        double tough = entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
        double resistanceLevel = 0;
        if (entity.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            resistanceLevel = entity.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE).getAmplifier() + 1;
        }
        if (resistanceLevel > 6)
            resistanceLevel = 6;
        damage *= (1 - Math.min(20.0, Math.max(armor / 5.0, armor - damage / (2 + tough / 4.0))) / 25.0);
        damage *= (1 - (0.2 * resistanceLevel));
        return damage;
    }

    /**
     * Adds an item to a specific slot.
     * This method differs from {@link PlayerInventory#setItem(int, ItemStack)} as if an item is already present
     * in the given slot, instead of it being replaced by the new item, it gets moved to a different slot.
     * After the old item is moved to a different slot, the new item will be placed in the provided slot.
     *
     * @param player target player
     * @param slot slot to add the item to
     * @param item item to add to the inventory
     */
    public static void addItemToSlot(Player player, Integer slot, ItemStack item) {
        if (player.getInventory().firstEmpty() == -1) {
            return;
        }
        if (player.getInventory().getItem(slot) != null) {
            player.getInventory().setItem(player.getInventory().firstEmpty(), player.getInventory().getItem(slot));
        }
        player.getInventory().setItem(slot, item);
    }

    /**
     * Gets the game a player is currently in.
     *
     * @param plugin instance of the main class.
     * @param player player who's game should be returned.
     * @return instance of {@link GameData}, or null if player is not in a game.
     */
    public static GameData getPlayerGame(MobMadness plugin, Player player) {
        for (GameData gameData : plugin.getGameManager().getGames().values()) {
            if (!gameData.getPlayers().containsKey(player)) continue;
            return gameData.getPlayers().get(player).getCurrentGame();
        }
        return null;
    }

    /**
     * Gets all LivingEntities in an area.
     *
     * @param loc1 middle location
     * @param xRadius radius in blocks
     * @param yRadius radius in blocks
     * @param zRadius radius in blocks
     * @return a list containing all found entities
     */
    public static List<LivingEntity> getMobsInArea(Location loc1, Double xRadius, Double yRadius, Double zRadius) {
        List<LivingEntity> entityList = new ArrayList<>();
        for (LivingEntity entity : loc1.getNearbyLivingEntities(xRadius, yRadius, zRadius)) {
            if (entity instanceof Player) continue;
            entityList.add(entity);
        }
        return entityList;
    }

    /**
     * Checks if the provided item has the tag 'mm-mm'.
     * Also checks if the item is null or not, in which case if it is it returns false.
     *
     * @param item The item to check
     * @return true if the tag is present and true, otherwise false.
     */
    public static boolean isMMItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        NBTItem nbt = new NBTItem(item);
        return nbt.hasKey(MadItem.MM_ENUM_TAG);
    }

    /**
     * Checks if the provided material is 'pass through' or not.
     * A block that is passthrough must fulfill the following criteria:
     * - Does not cause suffocation
     * - Does does not block one's path
     *
     * @param type material to check for
     * @return true if the material fulfills the above criteria, false otherwise.
     */
    public static boolean isBlockPassThrough(Material type) {
        boolean passThrough;
        switch (type) {
            default: passThrough = false;
            case VINE:
            case GRASS:
            case TALL_GRASS:
            case SEAGRASS:
            case TALL_SEAGRASS:
            case SUGAR_CANE:
            case FERN:
            case LARGE_FERN:
            case WHEAT:
            case CARROTS:
            case POTATOES:
            case BEETROOTS:
            case SWEET_BERRY_BUSH:
            case MELON_STEM:
            case ATTACHED_MELON_STEM:
            case PUMPKIN_STEM:
            case ATTACHED_PUMPKIN_STEM:
            case RAIL:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
            case POWERED_RAIL:
            case COBWEB:
            case SCAFFOLDING:
            case ACACIA_SAPLING:
            case BIRCH_SAPLING:
            case DARK_OAK_SAPLING:
            case JUNGLE_SAPLING:
            case OAK_SAPLING:
            case SPRUCE_SAPLING:
            case BAMBOO_SAPLING:
                passThrough = true;
        }
        if (type.isSolid()) passThrough = false;
        return passThrough;
    }

    /**
     * Creates a list of locations which forms the shape of a hollow rectangle.
     * Each location is 1 block apart.
     *
     * @param corner1 the first corner of the rectangle
     * @param corner2 the opposing corner of the rectangle
     * @return a list containing all the locations
     */
    public static List<Location> getHollowRectangle(Location corner1, Location corner2) {
        List<Location> result = new ArrayList<>();
        World world = corner1.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        // 2 areas
        for (double x = minX; x <= maxX; x+=1) {
            for (double z = minZ; z <= maxZ; z+=1) {
                result.add(new Location(world, x, minY, z));
                result.add(new Location(world, x, maxY, z));
            }
        }

        // 2 sides (front & back)
        for (double x = minX; x <= maxX; x+=1) {
            for (double y = minY; y <= maxY; y+=1) {
                result.add(new Location(world, x, y, minZ));
                result.add(new Location(world, x, y, maxZ));
            }
        }

        // 2 sides (left & right)
        for (double z = minZ; z <= maxZ; z+=1) {
            for (double y = minY; y <= maxY; y+=1) {
                result.add(new Location(world, minX, y, z));
                result.add(new Location(world, maxX, y, z));
            }
        }

        return result;
    }

    /**
     * Creates a list of locations forming the shape of an ellipse at the provided location.
     *
     * @param loc where to create the ellipse
     * @return a list containing all the locations
     */
    public static List<Location> createEllipse(Location loc) {
        List<Location> locations = new ArrayList<>();
        for (double y = 0; y <= Math.PI; y += Math.PI / 5) { // 10 being the amount of circles.
            double radius = Math.sin(y); // we get the current radius
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 5) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                locations.add(loc.clone().add(x / 3, y / 3, z / 3));
            }
        }
        return locations;
    }

    /**
     * Picks a random player from a list of players.
     *
     * @param players the list of players to pick from
     * @return a random player from the given list.
     */
    public static Player pickRandomPlayer(Set<Player> players) {
        List<Player> playerList = new ArrayList<>(players);
        Random random = new Random();
        int index = random.nextInt(players.size());
        return playerList.get(index);
    }

    /**
     * Clears the inventory of the provided player. Will not clear an item with the 'mm.persist' tag
     *
     * @param player Player who's inventory shall be cleared.
     */
    public static void clearInventory(Player player) {
        PlayerInventory inv = player.getInventory();
        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);
            if (item == null) continue;
            NBTItem nbt = new NBTItem(item);
            if (nbt.hasKey(MadItem.PERSIST_TAG) && nbt.getBoolean(MadItem.PERSIST_TAG)) continue;
            inv.setItem(slot, null);
        }
    }

    /**
     * Picks a spawn area for a mob to spawn in, based on the current game.
     * The bigger a spawn area is, the more likely for it to be picked will be.
     *
     * @param gameData instance of the game p
     * @return
     */
    public static SpawnArea pickSpawnArea(GameData gameData) {
        Random random = new Random();
        //A sum of all the area sizes.
        int areaSizeSum = 0;
        //A list of all the area sizes.
        List<Integer> areaSizes = new ArrayList<>();
        //Loops through the map's spawn areas, and adds their size to the sum and the list of areas.
        for (SpawnArea area : gameData.getGameMap().getSpawnAreas()) {
            areaSizeSum += area.getSize();
            areaSizes.add(area.getSize());
        }
        int chance = random.nextInt(areaSizeSum);
        /*System.out.println("Area sizes: " + areaSizes);
        //Sorts the area sizes from smallest to biggest.
        Collections.sort(areaSizes);
        //TODO fix mob spawns
        for (int i = 0; i < areaSizes.size(); i++) {
            //If the loop would go out of bounds, return null
            if (i + 1 >= areaSizes.size()) return null;
            if (areaSizes.get(i) < chance && chance < areaSizes.get(i + 1)) {
                return gameData.getGameMap().getSpawnAreas().get(i);
            }
        }*/
        int currentAreaSize = 0;
        for (SpawnArea spawnArea : gameData.getGameMap().getSpawnAreas()) {
            currentAreaSize += spawnArea.getSize();
            if (chance < currentAreaSize) {
                return spawnArea;
            }
        }
        return null;
    }

    /**
     * Returns a random mob present in a Mob Madness game.
     *
     * @param gameData
     * @return
     */
    public static MadEntity getRandomMadMob(GameData gameData) {
        Random random = new Random();
        MadEntity[] values = gameData.getMobMap().values().toArray(new MadEntity[0]);
        if (values.length == 0) {
            return null;
        }
        return values[random.nextInt(values.length)];
    }

}
