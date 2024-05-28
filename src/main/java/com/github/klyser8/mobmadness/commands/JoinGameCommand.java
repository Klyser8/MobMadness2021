package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.world.MapType;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Command(value = "mobmadness", alias = "mm")
public class JoinGameCommand extends BaseCommand {

    private final MobMadness plugin;
    public JoinGameCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("join")
    @Permission("mm.command.join")
    public void joinCommand(CommandSender sender, @Suggestion("maptype") @Optional MapType type) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;
    }

    @CompleteFor("join") //todo implement command completion
    public List<String> commandCompletion(List<String> list) {
        List<String> freeMaps = new ArrayList<>();
        for (MapType mapType : MapType.values()) {
            if (plugin.getGameManager().getGames().containsKey(mapType)) continue;
            freeMaps.add(mapType.toString().toLowerCase());
        }
        return freeMaps;
    }
}
