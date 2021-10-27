package com.github.klyser8.mobmadness.game.player;

import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.klyser8.mobmadness.util.MadUtils.getMadMobClosestToLocation;
import static me.mattstudios.mf.base.components.MfUtil.color;

public class ProfileRunnable extends BukkitRunnable {

    //How often the loop should run (In ticks)
    public static final int TICK_INTERVAL = 2;
    //If the player uses mana, how much mana per <TICK_INTERVAL> should be regenerated.
    public static final float MANA_REGEN = 0.0025f;

    private int clock = 0;

    private final Profile profile;
    private final GameData gameData;
    public ProfileRunnable(Profile profile, GameData gameData) {
        this.profile = profile;
        this.gameData = gameData;
    }

    @Override
    public void run() {
        Player player = profile.getPlayer();
        if (profile.isXPMana()) {
            if (player.getExp() + (MANA_REGEN * TICK_INTERVAL) >= 1.00f) {
                player.setExp(0.99f);
            } else {
                player.setExp(player.getExp() + (MANA_REGEN * TICK_INTERVAL));
            }
        }

        if (clock % 20 == 0 && gameData.getTime() > 0) {
            player.setLevel(player.getLevel() + 1);
            pointCompassAtMonster(player);
        }
        clock+= TICK_INTERVAL;
    }

    /**
     * Points the compass to the monster that is closest
     * to the player specified.
     *
     * @param player player to execute method on.
     */
    private void pointCompassAtMonster(Player player) {
        LivingEntity mob = getMadMobClosestToLocation(player.getLocation(), gameData);
        if (mob != null) {
            player.setCompassTarget(mob.getLocation());
            if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                player.sendActionBar(color("&eClosest monster:&r " + mob.getCustomName() + "&r - &eDistance:&r " +
                        Math.round(player.getLocation().distance(mob.getLocation())) + " - &eHeight:&r " +
                        Math.round(mob.getLocation().getBlockY() - player.getLocation().getBlockY())));
            }
        }
    }
}
