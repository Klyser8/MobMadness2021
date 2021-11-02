package com.github.klyser8.mobmadness.game.player;

import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;

import static com.github.klyser8.mobmadness.util.MadUtils.getMadMobClosestToLocation;

public class ProfileRunnable implements Runnable {

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
        if (gameData.getTime() > 0) {
            if (clock % 20 == 0) {
                pointCompassAtMonster(player);
            }
            if (clock % 30 == 0) {
                player.setLevel(player.getLevel() + 1);
            }
        }
        clock += TICK_INTERVAL;
    }

    /**
     * Points the compass to the monster that is closest
     * to the player specified.
     *
     * @param player player to execute method on.
     */
    private void pointCompassAtMonster(Player player) {
        if (player.getNearbyEntities(100, 20, 100).size() == 0) return;
        LivingEntity mob = getMadMobClosestToLocation(player.getLocation());
        if (mob != null) {
            player.setCompassTarget(mob.getLocation());
            if (player.getInventory().getItemInMainHand().getType() == Material.COMPASS) {
                TextComponent mobName = (TextComponent) (mob.customName() == null ? mob.name() : mob.customName());
                TextComponent component = Component.text("Closest Monster: ").color(NamedTextColor.YELLOW)
                        .append(mobName)
                        .append(Component.text(" - "))
                        .append(Component.text("Distance: ").color(NamedTextColor.GOLD))
                        .append(Component.text(Math.round(player.getLocation().distance(mob.getLocation()))))
                        .append(Component.text(" - "))
                        .append(Component.text("Height: ").color(NamedTextColor.GOLD))
                        .append(Component.text(
                                Math.round(mob.getLocation().getBlockY() - player.getLocation().getBlockY())));
                player.sendActionBar(component);
            }
        }
    }
}
