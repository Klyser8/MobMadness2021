package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@Command("mobmadness")
@Alias("mm")
public class PauseGameCommand extends CommandBase {

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
