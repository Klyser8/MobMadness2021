package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.Tier;
import com.github.klyser8.mobmadness.game.world.MapType;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.Game;
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
        Game game = plugin.getGameManager().getGames().get(type);
        if (!isGameValid(game)) return;
        plugin.getGameManager().setupPlayer(player, game);
        ((Player) sender).teleport(type.getSpawn());
        for (Player loopPlayer : game.getPlayers().keySet()) {
            loopPlayer.sendMessage(English.PLAYER_JOIN.getComponent(player));
        }
    }

    @CompleteFor("join")
    public List<String> commandCompletion(List<String> list) {
        List<String> ongoingMaps = new ArrayList<>();
        for (MapType mapType : MapType.values()) {
            Game game = plugin.getGameManager().getGames().get(mapType);
            if (isGameValid(game)) {
                ongoingMaps.add(mapType.toString().toLowerCase());
            }
        }
        return ongoingMaps;
    }

    private boolean isGameValid(Game game)  {
        return game != null && game.getTier() == Tier.I;
    }
}
