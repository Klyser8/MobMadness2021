package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.world.MapType;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "mobmadness", alias = "mm")
public class TeleportMapCommand extends BaseCommand {

    private final MobMadness plugin;
    public TeleportMapCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("teleport")
    @Permission("mm.command.teleport")
    public void teleportCommand(CommandSender sender, @Suggestion("maptypes") MapType type) {
//        sender.sendMessage(color("&aYou have been teleported to&f " +
//                type.toString().toLowerCase().replace("_", " "))); //TODO: fix command
        ((Player) sender).teleport(type.getSpawn());
    }
}
