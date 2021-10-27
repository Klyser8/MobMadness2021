package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.GameManager;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.game.world.MapType;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;

@Command("mobmadness")
@Alias("mm")
public class StopGameCommand extends CommandBase {

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

    @CompleteFor("stop")
    public List<String> commandCompletion(List<String> list) {
        List<String> freeMaps = new ArrayList<>();
        for (MapType mapType : MapType.values()) {
            if (!plugin.getGameManager().getGames().containsKey(mapType)) continue;
            freeMaps.add(mapType.toString().toLowerCase());
        }
        return freeMaps;
    }
}
