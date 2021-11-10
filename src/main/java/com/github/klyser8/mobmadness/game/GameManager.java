package com.github.klyser8.mobmadness.game;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.consumables.AethersBerries;
import com.github.klyser8.mobmadness.game.items.misc.Compass;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.game.world.GameMap;
import com.github.klyser8.mobmadness.game.world.MapType;
import com.github.klyser8.mobmadness.game.world.SpawnArea;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.*;

import static com.github.klyser8.mobmadness.util.MadUtils.addItemToSlot;
import static com.github.klyser8.mobmadness.util.MadUtils.clearInventory;

public class GameManager {

    //Default starting number of iron nuggets
    public static final int STARTING_NUGGETS = 40;

    //Map containing ongoing games. Since only one map per game is allowed,
    private final Map<MapType, Game> gamesMap;

    //Instance of MobMadness. Dependency Injection.
    private final MobMadness plugin;

    public GameManager(MobMadness plugin) {
        this.plugin = plugin;
        gamesMap = new HashMap<>();
    }

    /**
     * Creates a new MobMadness game, given the Map it should be hosted on.
     *
     * @param mapType The map to host the game on.
     * @return the instance of {@link Game} created.
     */
    public Game createGame(MapType mapType) {
        if (gamesMap.containsKey(mapType)) {
            plugin.getServer().getLogger().warning("Cannot create a game in map " + mapType.toString() + "!");
        }
        GameMap map = new GameMap(mapType, Objects.requireNonNull(createSpawnAreas(mapType)));
        Game game = new Game(plugin, map);

        gamesMap.put(mapType, game);
        return game;
    }

    /**
     * Stops an ongoing MobMadness game, given the map.
     *
     * @param mapType the game which map's hosted on to stop.
     */
    public void stopGame(MapType mapType) {
        Game game = gamesMap.get(mapType);
        for (Player player : game.getPlayers().keySet()) {
            player.setLevel(0);
            player.setExp(0.99f);
            player.teleport(player.getWorld().getSpawnLocation());
            Profile profile = game.getPlayers().get(player);
            profile.stopProfileRunnable();
            profile.stopCooldown();
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Profile.STARTING_HP);
            player.setHealth(Profile.STARTING_HP);
        }
        for (Beast beast : game.getBeastMap().values()) {
            beast.remove();
        }
        game.getPlayers().clear();
        game.getBeastMap().clear();
        game.getGameTask().cancel();
        //Resets the respawn anchor to 0 charges.
        game.setAnchorCharges(0);
        gamesMap.remove(mapType);
    }

    /**
     * Getter method for {@link #gamesMap}
     *
     * @return {@link #gamesMap}
     */
    public Map<MapType, Game> getGames() {
        return gamesMap;
    }

    /**
     * Creates an instance of {@link Profile} for the provided player. This requires an instance of {@link Game}.
     * The instance of {@link Game} should contain an instance of {@link Player} matching the one provided.
     *
     * @param player the player who's profile should be created.
     * @param game instance of {@link Game} containing the player.
     * @return the created instance of {@link Profile}
     */
    public Profile setupPlayer(Player player, Game game) {
        clearInventory(player);
        Profile profile = new Profile(plugin, player, game);
        game.getPlayers().put(player, profile);
        addItemToSlot(player, 0, new AethersBerries(plugin, 1, game).getItemStack());
        addItemToSlot(player, 8, new Compass(plugin, game).getItemStack());
        addItemToSlot(player, 17,
                new IronNugget(plugin, STARTING_NUGGETS, game).getItemStack());
        return profile;
    }

    /**
     * Creates spawn areas for the map provided.
     *
     * @param mapType the map to create spawn areas for.
     * @return A list containing {@link SpawnArea} instances.
     */
    private List<SpawnArea> createSpawnAreas(MapType mapType) {
        World world = Bukkit.getWorld("mobmadness");
        if (mapType == MapType.CRYPTIC_CAVE) {
            return Arrays.asList(
                    //Flaming Cave
                    new SpawnArea(world, new Location(world, 9949, 115, -30),
                            new Location(world, 9960, 100, 7)), //High part
                    new SpawnArea(world, new Location(world, 9961, 112, -9),
                            new Location(world, 9980, 100, -26)), //Low part

                    //Lush Cave
                    new SpawnArea(world, new Location(world, 9950, 104, -4),
                            new Location(world, 9987, 98, 40)), //Right of tree, tunnel excluded
                    new SpawnArea(world, new Location(world, 9988, 102, 10),
                            new Location(world, 10005, 98, 39)), //Left of tree, tunnel excluded
                    new SpawnArea(world, new Location(world, 9969, 113, 45),
                            new Location(world, 9940, 107, 22)), //Tunnel

                    //Ancient Settlement
                    new SpawnArea(world, new Location(world, 10031, 107, 19),
                            new Location(world, 10000, 92, -29)), //Quarry, refinery and streets.
                    new SpawnArea(world, new Location(world, 9999, 102, 5),
                            new Location(world, 9983, 92, -34)), //Emporium and portal
                    new SpawnArea(world, new Location(world, 10014, 108, -29),
                            new Location(world, 10063, 95, -57)), //Bridge of dungeon
                    new SpawnArea(world, new Location(world, 10065, 105, -33),
                            new Location(world, 10027, 116, -4))
            );
            /*case SNOW_VALLEY: return Arrays.asList(
                    new SpawnArea(world, new Location(world, 30040, 73, 46),
                            new Location(world, 30037, 70, 44)),
                    new SpawnArea(world, new Location(world, 30003, 77, 52),
                            new Location(world, 30037, 69, 7)),
                    new SpawnArea(world, new Location(world, 30071, 77, 53),
                            new Location(world, 30037, 69, 7))
            );*/
        }
        return null;
    }

    /**
     * Calculates what the max weight for the current map is.
     * It takes into account the size of the map in blocks, the current tier of the game and amount of current players.
     *
     * The maximum weight of a game dictates how many mobs can be present at the same time.
     * A higher weight results in a higher difficulty of the game.
     *
     * @param mapSize the size of the map
     * @param tier the game's current tier
     * @param playerNum the amount of players in the game
     * @return the current maximum weight of the map
     */
    public int calculateMaxWeight(int mapSize, Tier tier, int playerNum) {
        double num = 20000;
        double mapMult = 2 - (num/mapSize);
        double playerMult = (playerNum - 1d) / 10 + 1;
        return (int) Math.round(tier.getDefaultMaxWeight() * mapMult * playerMult);
    }
}
