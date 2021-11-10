package com.github.klyser8.mobmadness.game;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Kreeper;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Scheleton;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Spyder;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Zomby;
import com.github.klyser8.mobmadness.game.entity.beast.classic.jockey.Jockey;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.game.world.SpawnArea;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.ComponentHelper;
import com.github.klyser8.mobmadness.util.MadUtils;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import java.util.*;

import static com.github.klyser8.mobmadness.util.MadUtils.pickSpawnArea;
import static java.lang.Math.abs;

/**
 * Main game loop. This is where any loop or repeated action related to the game takes place.
 *
 */
public class GameRunnable implements Runnable {

    //How often the loop should run (In ticks)
    public static final int TICK_INTERVAL = 20;
    //What age mobs may be removed from the game (In ticks)
    public static final int MOB_AGE_CAP = 7200;

    //Sound for chat notifications.
    private final Sound notifySound = new Sound("block.note_block.hat", 0.5f, 2.0f);
    //Sound played at game start.
    private final Sound startSound = new Sound("block.respawn_anchor.set_spawn", 10.0f, 1.0f);
    //Sound played when a mob spawns.
    private final Sound mobSpawnSound = new Sound("entity.zombie.infect", 5.0f, 1.0f);
    //Sound played when the game's tier increases
    private final Sound tierUpSound = new Sound("entity.wither.spawn", 10.0f, 1.0f);
    //Sound played when the game's tier decreases
    private final Sound tierDownSound = new Sound("entity.ghast.scream", 10.0f, 1.0f);
    //Sound player when a boss is summoned
    private final Sound bossSummonedSound = new Sound("entity.evoker.prepare_summon", 10.0f, 1.0f);

    //Current in-game time (in seconds)
    private int clock = -60 * TICK_INTERVAL;

    private final MobMadness plugin;
    private final GameManager gameManager;
    private final Game game;
    private final Random random;
    public GameRunnable(MobMadness plugin, Game game) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        this.game = game;
        this.random = new Random();
    }

    @Override
    public void run() {
        switch (clock) {
            case -60 * TICK_INTERVAL -> sendMessage(game.getPlayers().keySet(),
                    English.SIXTY_SECONDS_MESSAGE.getComponent(), notifySound);
            case -30 * TICK_INTERVAL -> sendMessage(game.getPlayers().keySet(),
                    English.THIRTY_SECONDS_MESSAGE.getComponent(), notifySound);
            case -10 * TICK_INTERVAL -> sendMessage(game.getPlayers().keySet(),
                    English.TEN_SECONDS_MESSAGE.getComponent(), notifySound);
            case -5 * TICK_INTERVAL, -4 * TICK_INTERVAL, -3 * TICK_INTERVAL, -2 * TICK_INTERVAL,
                    -1 * TICK_INTERVAL -> sendMessage(game.getPlayers().keySet(), English.COUNTDOWN_MESSAGE
                    .getComponent(abs(clock / 20)), notifySound);
            case 0 -> {
                sendMessage(game.getPlayers().keySet(), English.GAME_STARTED.getComponent(), startSound);
                game.setMaxWeight(gameManager.calculateMaxWeight(
                        game.getGameMap().getMapSize(), Tier.I, game.getPlayers().size()));
            }
            default -> {
                //Set the clock to -10 if all online players are in the game
                if (clock < -11 * TICK_INTERVAL) {
                    if (Bukkit.getServer().getOnlinePlayers().size() == game.getPlayers().size()) {
                        clock = -11 * TICK_INTERVAL;
                    }
                }
                //Prevent mobs from spawning and the timer from increasing if the game hasn't started
                if (clock > 0) {
                    if (checkTier()) {
                        for (Profile profile : game.getPlayers().values()) {
                            profile.rerollShop();
                        }
                    }
                    game.setTime(game.getTime() + 1);
                    attemptMobSpawn();
                }
                if (game.getAnchorCharges() > 0) {
                    playAnchorEffect();
                }
            }
        }
        clock += TICK_INTERVAL;
    }

    private boolean checkTier() {
        Tier calcTier = Tier.getTierUsingScore(game.calculateAvgScore());
        if (game.getTier() == calcTier || game.getTier() == null) return false;
        if (calcTier.getNumericTier() > game.getTier().getNumericTier()) {
            if (game.getCurrentBoss() == null) {
                spawnBoss(calcTier);
            } else if (game.getCurrentBoss().getMob().isDead()) {
                tierUp(calcTier);
                game.setCurrentBoss(null);
                return true;
            }
        } else {
            tierDownSound.play(game.getRespawnAnchor().getLocation(), SoundCategory.NEUTRAL);
            for (Player player : game.getPlayers().keySet()) {
                player.sendMessage(English.TIER_DECREASE_MSG.getComponent(calcTier.getNumericTier()));
            }
            game.setTier(calcTier);
        }
        return false;
    }

    private void spawnBoss(Tier calcTier) {
        switch (calcTier) {
            case I, II, III, IV, V, VI -> game.setCurrentBoss(
                    new Jockey(plugin, game.getMapSpawnPoint().getWorld(), game));
        }
        bossSummonedSound.play(game.getMapSpawnPoint(), SoundCategory.HOSTILE);
        game.getCurrentBoss().spawn(game.getMapSpawnPoint());
    }

    private void tierUp(Tier calcTier) {
        tierUpSound.play(game.getRespawnAnchor().getLocation(), SoundCategory.NEUTRAL);
        for (Player player : game.getPlayers().keySet()) {
            ComponentHelper.sendPlayerTitle(player,
                    English.TIER_INCREASE_TITLE.getComponent(calcTier),
                    English.TIER_INCREASE_SUBTITLE.getComponent(calcTier),
                    20, 60, 20);
        }
        game.setTier(calcTier);
    }

    private void attemptMobSpawn() {
        if (game.getCurrentWeight() >= game.getMaxWeight()) {
            for (Mob mob : game.getBeastMap().keySet()) {
                if (mob.getTicksLived() > MOB_AGE_CAP && mob.getKiller() == null) {
                    game.getBeastMap().get(mob).remove();
                }
            }
        }
        calculateBound();
        World world = game.getGameMap().getMapType().getSpawn().getWorld();
        int bound = random.nextInt(game.getBound());
        Beast beast = null;
        if (bound > 2000) return;
        switch (game.getTier()) {
            case I:
                if (bound > 1500) {
                    beast = new Zomby(plugin, world, game);
                } else if (bound > 1000) {
                    beast = new Spyder(plugin, world, game);
                } else if (bound > 500) {
                    beast = new Kreeper(plugin, world, game);
                } else if (bound > 0) {
                    beast = new Scheleton(plugin, world, game);
                }
                //When the boss is alive, mobs may be replaced with spiders.
                if (game.getCurrentBoss() != null
                        && !(beast instanceof Spyder)
                        && random.nextBoolean()) {
                    beast = new Spyder(plugin, world, game);
                }
                break;
            case IV:
                break;
            case V:
                break;
            case VI:
                break;
        }
        SpawnArea area = pickSpawnArea(game);
        if (area == null || beast == null) return;
        Location spawnLoc = MadUtils.pickRandomSpawnLocation(area.getCorner1(), area.getCorner2());
        if (spawnLoc == null) return;
        beast.spawn(spawnLoc);
        mobSpawnSound.play(spawnLoc, SoundCategory.HOSTILE);
    }

    private void calculateBound() {
        int bound = 0;
        switch (game.getGameMap().getMapType()) {
            case CRYPTIC_CAVE:
                bound = 12000;
        }
        bound -= game.calculateAvgScore();
        bound *= (1.1 - (game.getPlayers().size() / 10));
        game.setBound(bound);
    }

    /**
     * Sends a message to the provided set of players, with a sound effect.
     * Colors the message automatically.
     *
     * @param set the set of players to send the msg to.
     * @param comp the message to send.
     * @param sound the sound effect to play to the players.
     */
    private void sendMessage(Set<Player> set, TextComponent comp, Sound sound) {
        for (Player player : set) {
            sound.play(player.getLocation(), SoundCategory.MASTER, player);
            player.sendMessage(comp);
        }
    }

    private void playAnchorEffect() {
        World world = game.getGameMap().getMapType().getSpawn().getWorld();
        if (game.getRespawnAnchor().getType() == Material.RESPAWN_ANCHOR) {
            RespawnAnchor anchor = (RespawnAnchor) game.getRespawnAnchor().getBlockData();
            world.spawnParticle(Particle.WAX_OFF, game.getRespawnAnchor().getLocation().add(0.5, 1.0, 0.5),
                    anchor.getCharges(), 0.25, 0.25, 0.25, 1);
        }
    }
}
