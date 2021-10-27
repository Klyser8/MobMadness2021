package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.world.MapType;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.mattstudios.mf.base.components.MfUtil.color;

@Command("mobmadness")
@Alias("mm")
public class TeleportMapCommand extends CommandBase {

    private final MobMadness plugin;
    public TeleportMapCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("teleport")
    @Alias("tp")
    @Permission("mm.command.teleport")
    @Completion("#maptypes")
    public void teleportCommand(CommandSender sender, MapType type) {
        sender.sendMessage(color("&aYou have been teleported to&f " +
                type.toString().toLowerCase().replace("_", " ")));
        ((Player) sender).teleport(type.getSpawn());
    }
}
