package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


@Command(value = "mobmadness", alias = "mm")
public class PauseGameCommand extends BaseCommand {

    private final MobMadness plugin;
    public PauseGameCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("pause")
    @Permission("mm.command.pause")
    public void pauseCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;
    }
}
