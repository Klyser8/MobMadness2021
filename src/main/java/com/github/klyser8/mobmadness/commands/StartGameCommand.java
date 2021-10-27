package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.game.world.MapType;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static me.mattstudios.mf.base.components.MfUtil.color;

@Command("mobmadness")
@Alias("mm")
public class StartGameCommand extends CommandBase {

    private Random random = new Random();
    private final MobMadness plugin;
    public StartGameCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("start")
    @Permission("mm.command.start")
    public void startCommand(CommandSender sender, @Optional MapType type) {
        if (type == null) {
            type = pickRandomMap();
        }
        if (type == null || plugin.getGameManager().getGames().containsKey(type)) {
            sender.sendMessage(color(English.ERROR_MAP_TAKEN.getMsg(null)));
            return;
        }
        GameData gameData = plugin.getGameManager().createGame(type);
        Player player = (Player) sender;
        plugin.getGameManager().setupPlayer(player, gameData);
        ((Player) sender).teleport(type.getSpawn());
    }

    @CompleteFor("start")
    public List<String> commandCompletion(List<String> list) {
        List<String> freeMaps = new ArrayList<>();
        for (MapType mapType : MapType.values()) {
            if (plugin.getGameManager().getGames().containsKey(mapType)) continue;
            freeMaps.add(mapType.toString().toLowerCase());
        }
        return freeMaps;
    }

    private MapType pickRandomMap() {
        List<Integer> mapIDs = new ArrayList<>();
        for (MapType map : MapType.values()) {
            if (plugin.getGameManager().getGames().containsKey(map)) continue;
            mapIDs.add(map.getId());
        }
        if (mapIDs.size() > 0) {
            return MapType.getMapById(mapIDs.get(random.nextInt(mapIDs.size())));
        }
        return null;
    }
}
