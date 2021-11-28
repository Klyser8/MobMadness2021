package com.github.klyser8.mobmadness.storage;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.GameRunnable;
import com.github.klyser8.mobmadness.game.Tier;
import com.github.klyser8.mobmadness.game.entity.beast.BossBeast;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.game.world.GameMap;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains the data of a MobMadness game.
 */
public class Game {

    public static final String LOW_HP_PLAYERS_TEAM_NAME = "lowHP_players";

    //How many seconds have passed since the beginning of the game.
    private int time;
    //The current bound of the game.
    private int bound;
    //The maximum mob weight the game can reach.
    private int maxWeight;
    //The current mob weight of the game.
    private int currentWeight;
    //The map's respawn anchor.
    private final Block respawnAnchor;

    //The current tier of the game.
    private Tier tier = Tier.I;

    //The Map containing the players in this game.
    private final Map<Player, Profile> playerMap;

    //Map containing all present mobs in the game.
    private final Map<Mob, Beast> mobMap;

    private BossBeast currentBoss = null;

    //Team containing all players with low health. Used for colored glowing
    private Team lowHPTeam;

    //The game's scoreboard manager.
    private ScoreboardManager scoreboardManager;
    //The game's scoreboard.
    private Scoreboard board;
    //Main game loop.
    private final BukkitTask gameTask;
    //Instance of the game map.
    private final GameMap map;
    public Game(MobMadness plugin, GameMap map) {
        playerMap = new HashMap<>();
        mobMap = new LinkedHashMap<>();
        this.map = map;
        this.gameTask = Bukkit.getScheduler().runTaskTimer(plugin,
                new GameRunnable(plugin, this), 0, GameRunnable.TICK_INTERVAL);
        respawnAnchor = getMapSpawnPoint().add(0, -1, 0).getBlock();

        setupScoreboard();
    }

    private void setupScoreboard() {
        scoreboardManager = Bukkit.getScoreboardManager();
        board = scoreboardManager.getNewScoreboard();

        lowHPTeam = board.registerNewTeam(LOW_HP_PLAYERS_TEAM_NAME);

        lowHPTeam.color(NamedTextColor.RED);
    }

    /**
     * Calculates what the average score of the game is.
     *
     * @return the average score calculated
     */
    public int calculateAvgScore() {
        int score = 0;
        for (Player player : playerMap.keySet()) {
            score += player.getLevel();
        }
        return score / playerMap.size();
    }

    /**
     * Gets the current time (in seconds) of the game.
     *
     * @return time
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the time of the game.
     *
     * @param time the new time (in seconds).
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Gets the current bound of the game.
     * The bound is what dictates the frequency at which mobs spawn in a map.
     * Based off the size of the map, current players & current tier.
     *
     * @return bound
     */
    public int getBound() {
        return bound;
    }

    /**
     * Sets the current bound of the game.
     *
     * @param bound the new bound
     */
    public void setBound(int bound) {
        this.bound = bound;
    }

    /**
     * Gets the current weight of the game.
     * Each mob has a different weight. Harder mobs have a higher weight.
     *
     * @return {@link #currentWeight}
     */
    public int getCurrentWeight() {
        return currentWeight;
    }

    /**
     * Sets the current weight of the game.
     *
     * @param currentWeight the new weight
     */
    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    /**
     * Gets the current max weight of the game.
     * Dictated by current map, players present & tier.
     * Once the current weight reaches the {@link #maxWeight}, no more mobs can spawn.
     *
     * @return {@link #maxWeight}
     */
    public int getMaxWeight() {
        return maxWeight;
    }

    /**
     * Sets the maximum weight of the game.
     * @param maxWeight the new maximum weight
     */
    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    /**
     * Gets the current tier of the game.
     * The current tier is based off the average score of all the players.
     *
     * @return {@link #tier}
     */
    public Tier getTier() {
        return tier;
    }

    /**
     * Sets the current tier of the game.
     *
     * @param tier the new tier
     */
    public void setTier(Tier tier) {
        this.tier = tier;
    }

    /**
     * Gets a map containing {@link Player} instances and their respective {@link Profile} instances.
     *
     * @return {@link #playerMap}
     */
    public Map<Player, Profile> getPlayers() {
        return playerMap;
    }

    /**
     * Gets the current {@link GameMap} of the game.
     *
     * @return {@link #map}
     */
    public GameMap getGameMap() {
        return map;
    }

    /**
     * Gets the {@link BukkitTask} object of the game.
     * Runs all scheduled tasks of the game.
     *
     * @return {@link #gameTask}
     */
    public BukkitTask getGameTask() {
        return gameTask;
    }

    /**
     * Util method to return a copy of the map's spawn point more easily.
     *
     * @return the map's spawn point
     */
    public Location getMapSpawnPoint() {
        return map.getMapType().getSpawn();
    }

    public Map<Mob, Beast> getBeastMap() {
        return mobMap;
    }

    /**
     * Returns the amount of charges the game's respawn anchor has.
     *
     * @return how many charges the respawn anchor currently has
     */
    public int getAnchorCharges() {
        return ((RespawnAnchor) respawnAnchor.getBlockData()).getCharges();
    }

    /**
     * Sets the amount of charges of the game's respawn anchor.
     *
     * @param charges how many charges the respawn anchor should have. MAX 4
     */
    public void setAnchorCharges(int charges) {
        RespawnAnchor anchor = (RespawnAnchor) respawnAnchor.getBlockData();
        anchor.setCharges(charges);
        respawnAnchor.setBlockData(anchor);
    }

    public Block getRespawnAnchor() {
        return respawnAnchor;
    }

    public Scoreboard getBoard() {
        return board;
    }

    public BossBeast getCurrentBoss() {
        return currentBoss;
    }

    public void setCurrentBoss(BossBeast currentBoss) {
        this.currentBoss = currentBoss;
    }
}
