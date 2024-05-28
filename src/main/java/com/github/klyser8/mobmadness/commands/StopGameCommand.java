package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.GameManager;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.game.world.MapType;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

@Command(value = "mobmadness", alias = "mm")
public class StopGameCommand extends BaseCommand {

    private final MobMadness plugin;
    public StopGameCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("stop")
    @Permission("mm.command.stop")
    public void stopCommand(CommandSender sender, @Optional MapType type) {
        if (sender instanceof ConsoleCommandSender) return;
        GameManager gameManager = plugin.getGameManager();
        if (type == null) {                                                      //If no map is selected, stop ALL games
            List<MapType> toBeDeleted = new ArrayList<>(gameManager.getGames().keySet());
            for (MapType mapType : toBeDeleted) {
                gameManager.stopGame(mapType);
            }
            sender.sendMessage(English.STOPPED_ALL_GAMES.getComponent());
            return;
        }
        if (!gameManager.getGames().containsKey(type)) {           //If the game selected is not ongoing, throw an error
            sender.sendMessage(English.ERROR_NO_GAME.getComponent());
            return;
        }
        gameManager.stopGame(type);
        sender.sendMessage(English.STOPPED_GAME.getComponent(type));
    }

    public List<String> commandCompletion(List<String> list) { //TODO implement command completion
        List<String> freeMaps = new ArrayList<>();
        for (MapType mapType : MapType.values()) {
            if (!plugin.getGameManager().getGames().containsKey(mapType)) continue;
            freeMaps.add(mapType.toString().toLowerCase());
        }
        return freeMaps;
    }
}
