package com.github.klyser8.mobmadness.game;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.game.mob.classic.MadCreeper;
import com.github.klyser8.mobmadness.game.mob.classic.MadSkeleton;
import com.github.klyser8.mobmadness.game.mob.classic.MadSpider;
import com.github.klyser8.mobmadness.game.mob.classic.MadZombie;
import com.github.klyser8.mobmadness.game.world.SpawnArea;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static com.github.klyser8.mobmadness.util.MadUtils.pickSpawnArea;
import static java.lang.Math.abs;
import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Main game loop. This is where any loop or repeated action related to the game takes place.
 *
 */
public class GameRunnable extends BukkitRunnable {

    //How often the loop should run (In ticks)
    public static final int TICK_INTERVAL = 20;
    //What age mobs may be removed from the game (In ticks)
    public static final int MOB_AGE_CAP = 7200;

    //Sound for chat notifications.
    private final Sound notifySound = new Sound("block.note_block.hat", 0.5f, 2.0f);
    //Sound played at game start.
    private final Sound startSound = new Sound("block.respawn_anchor.set_spawn", 10.0f, 1.0f);
    //Sound played once the respawn window has come to an end.
    private final Sound respawnEndedSound = new Sound("entity.wither.shoot", 1.0f, 0.5f);
    //Sound played when a mob spawns.
    private final Sound mobSpawnSound = new Sound("entity.zombie.infect", 1.5f, 1.0f);

    //Current in-game time (in seconds)
    private int clock = -60 * TICK_INTERVAL;

    private final MobMadness plugin;
    private final GameManager gameManager;
    private final GameData gameData;
    private final Random random;
    public GameRunnable(MobMadness plugin, GameData gameData) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.gameData = gameData;
        this.random = new Random();
    }

    @Override
    public void run() {
        switch (clock) {
            case -60 * TICK_INTERVAL:
                sendMessage(gameData.getPlayers().keySet(), English.SIXTY_SECONDS_MESSAGE.getMsg(null), notifySound);
                break;
            case -30 * TICK_INTERVAL:
                sendMessage(gameData.getPlayers().keySet(), English.THIRTY_SECONDS_MESSAGE.getMsg(null), notifySound);
                break;
            case -10 * TICK_INTERVAL:
                sendMessage(gameData.getPlayers().keySet(), English.TEN_SECONDS_MESSAGE.getMsg(null), notifySound);
                break;
            case -5 * TICK_INTERVAL:
            case -4 * TICK_INTERVAL:
            case -3 * TICK_INTERVAL:
            case -2 * TICK_INTERVAL:
            case -1 * TICK_INTERVAL:
                sendMessage(gameData.getPlayers().keySet(), English.COUNTDOWN_MESSAGE.getMsg(null)
                        .replace("<time>", String.valueOf(abs(clock / 20))), notifySound);
                break;
            case 0:
                sendMessage(gameData.getPlayers().keySet(), English.GAME_STARTED.getMsg(null), startSound);
                gameData.setMaxWeight(gameManager.calculateMaxWeight(
                        gameData.getGameMap().getMapSize(), Tier.I, gameData.getPlayers().size()));
                break;
            default:
                //Set the clock to -10 if all online players are in the game
                if (clock < -11 * TICK_INTERVAL) {
                    if (Bukkit.getServer().getOnlinePlayers().size() == gameData.getPlayers().size()) {
                        clock = -11 * TICK_INTERVAL;
                    }
                }
                //Prevent mobs from spawning and the timer from increasing if the game hasn't started
                if (clock > 0) {
                    gameData.setTime(gameData.getTime() + 1);
                    attemptMobSpawn();
                }
                if (gameData.getAnchorCharges() > 0) {
                    playAnchorEffect();
                }
                break;
        }
        clock += TICK_INTERVAL;
    }

    private void attemptMobSpawn() {
        if (gameData.getCurrentWeight() >= gameData.getMaxWeight()) {
            for (LivingEntity entity : gameData.getMobMap().keySet()) {
                if (entity.getTicksLived() > MOB_AGE_CAP) {
                    entity.damage(100000);
                }
            }
        }
        calculateBound();
        World world = gameData.getGameMap().getMapType().getSpawn().getWorld();
        int bound = random.nextInt(gameData.getBound());
        MadEntity entity = null;
//        System.out.println(bound);
        if (bound > 2000) return;
        switch (gameData.getTier()) {
            case I:
                if (bound > 1500) {
                    entity = new MadZombie(plugin, world, gameData);
                } else if (bound > 1000) {
                    entity = new MadSpider(plugin, world, gameData);
                } else if (bound > 500) {
                    entity = new MadCreeper(plugin, world, gameData);
                } else if (bound > 0) {
                    entity = new MadSkeleton(plugin, world, gameData);
                }
                break;
            case II:
                break;
            case III:
                break;
            case IV:
                break;
            case V:
                break;
            case VI:
                break;
        }
        SpawnArea area = pickSpawnArea(gameData);
//        System.out.println("Entity: " + entity);
//        System.out.println("Area: " + area);
        if (area == null || entity == null) return;
        Location spawnLoc = MadUtils.pickRandomSpawnLocation(area.getCorner1(), area.getCorner2());
        if (spawnLoc == null) return;
        entity.spawn(spawnLoc);
        mobSpawnSound.play(spawnLoc, SoundCategory.HOSTILE);
    }

    private void calculateBound() {
        int bound = 0;
        switch (gameData.getGameMap().getMapType()) {
            case CRYPTIC_CAVE:
                bound = 12000;
        }
        bound -= gameData.calculateScore();
        bound *= (1.1 - (gameData.getPlayers().size() / 10));
        gameData.setBound(bound);
    }

    /**
     * Sends a message to the provided set of players, with a sound effect.
     * Colors the message automatically.
     *
     * @param set the set of players to send the msg to.
     * @param msg the message to send.
     * @param sound the sound effect to play to the players.
     */
    private void sendMessage(Set<Player> set, String msg, Sound sound) {
        for (Player player : set) {
            sound.play(player.getLocation(), SoundCategory.MASTER, player);
            player.sendMessage(color(msg));
        }
    }

    private void playAnchorEffect() {
        World world = gameData.getGameMap().getMapType().getSpawn().getWorld();
        if (gameData.getRespawnAnchor().getType() == Material.RESPAWN_ANCHOR) {
            RespawnAnchor anchor = (RespawnAnchor) gameData.getRespawnAnchor().getBlockData();
            world.spawnParticle(Particle.WAX_OFF, gameData.getRespawnAnchor().getLocation().add(0.5, 1.0, 0.5),
                    anchor.getCharges(), 0.25, 0.25, 0.25, 1);
        }
    }
}
