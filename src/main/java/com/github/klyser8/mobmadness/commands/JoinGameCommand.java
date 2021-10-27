package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.world.MapType;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Command("mobmadness")
@Alias("mm")
public class JoinGameCommand extends CommandBase {

    private final MobMadness plugin;
    public JoinGameCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("join")
    @Permission("mm.command.join")
    @Completion("#maptypes")
    public void joinCommand(CommandSender sender, @Optional MapType type) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;

    }

    @CompleteFor("join")
    public List<String> commandCompletion(List<String> list) {
        List<String> freeMaps = new ArrayList<>();
        for (MapType mapType : MapType.values()) {
            if (plugin.getGameManager().getGames().containsKey(mapType)) continue;
            freeMaps.add(mapType.toString().toLowerCase());
        }
        return freeMaps;
    }
}
