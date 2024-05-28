package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.beast.BeastType;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.MadUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@Command(value = "mobmadness", alias = "mm")
public class SpawnMobCommand extends BaseCommand {

    private final MobMadness plugin;
    public SpawnMobCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("spawn")
    @Permission("mm.command.spawn")
    public void spawnCommand(CommandSender sender, @Suggestion("madmobs") BeastType type, @Suggestion("range")  @Optional Integer amount) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;
        Game game = MadUtils.getEntityGame(plugin, player);
        if (game == null) return;
        if (amount == null) amount = 1;
        for (int i = 0; i < amount; i++) {
            Beast mob = BeastType.createMob(type, player.getWorld(), game);
            mob.spawn(player.getLocation());
        }
    }
}
