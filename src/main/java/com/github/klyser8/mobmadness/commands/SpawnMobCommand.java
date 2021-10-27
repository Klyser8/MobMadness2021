package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.game.mob.MadMobType;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@Command("mobmadness")
@Alias("mm")
public class SpawnMobCommand extends CommandBase {

    private final MobMadness plugin;
    public SpawnMobCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("spawn")
    @Permission("mm.command.spawn")
    @Completion({"#madmobs", "#range"})
    public void spawnCommand(CommandSender sender, MadMobType type, @Optional Integer amount) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        if (amount == null) amount = 1;
        for (int i = 0; i < amount; i++) {
            MadEntity mob = MadMobType.createMob(type, player.getWorld(), gameData);
            mob.spawn(player.getLocation());
        }
    }
}
