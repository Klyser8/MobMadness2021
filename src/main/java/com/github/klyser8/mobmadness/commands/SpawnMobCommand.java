package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.beast.BeastType;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.MadUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@Command("mobmadness")
@Alias("mm")
public class SpawnMobCommand extends CommandBase {

    private final MobMadness plugin;
    public SpawnMobCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("spawn")
    @Permission("mm.command.spawn")
    @Completion({"#madmobs", "#range"})
    public void spawnCommand(CommandSender sender, BeastType type, @Optional Integer amount) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;
        Game game = MadUtils.getEntityGame(plugin, player);
        if (game == null) return;
        if (amount == null) amount = 1;
        for (int i = 0; i < amount; i++) {
            BeastType.createMob(type, player.getWorld(), game, ((Player) sender).getLocation());
        }
    }
}
