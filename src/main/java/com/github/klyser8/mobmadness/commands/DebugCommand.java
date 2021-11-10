package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.GameManager;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.game.world.SpawnArea;
import com.github.klyser8.mobmadness.util.MadUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

@Command("mobmadness")
@Alias("mm")
public class DebugCommand extends CommandBase {

    private final MobMadness plugin;
    private boolean debugging = false;
    public DebugCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("debug")
    @Permission("mm.command.debug")
    public void debugCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) return;
        Random random = new Random();
        GameManager gameManager = plugin.getGameManager();

        debugging = !debugging;                                                                //Switch debugging on/off
        if (!debugging) return;                          //If debugging is off, no point in running the rest of the code
        for (Game game : gameManager.getGames().values()) {
            for (SpawnArea area : game.getGameMap().getSpawnAreas())
            {
                Color color = Color.fromRGB(random.nextInt(254), random.nextInt(254), random.nextInt(254));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Location loc : MadUtils.getHollowRectangle(area.getCorner1(), area.getCorner2())) {
                            World world = loc.getWorld();
                            world.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0,
                                    new Particle.DustOptions(color, 1));
                        }
                        if (!debugging) {
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 10);
            }
        }
    }
}
