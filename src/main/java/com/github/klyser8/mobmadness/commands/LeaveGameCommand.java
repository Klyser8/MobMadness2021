package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@Command("mobmadness")
@Alias("mm")
public class LeaveGameCommand extends CommandBase {

    private final MobMadness plugin;
    public LeaveGameCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("leave")
    @Permission("mm.command.leave")
    public void leaveCommand(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;
    }
}
