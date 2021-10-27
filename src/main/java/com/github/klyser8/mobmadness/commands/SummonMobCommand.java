package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.game.mob.MadMobType;
import com.github.klyser8.mobmadness.game.world.SpawnArea;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static me.mattstudios.mf.base.components.MfUtil.color;

@Command("mobmadness")
@Alias("mm")
public class SummonMobCommand extends CommandBase {

    private final MobMadness plugin;
    public SummonMobCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("summon")
    @Permission("mm.command.summon")
    @Completion({"#madmobs", "#range"})
    public void summonCommand(CommandSender sender, MadMobType type, @Optional Integer amount) {
        if (sender instanceof ConsoleCommandSender) return;
        Player player = (Player) sender;
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        if (amount == null) amount = 1;
        for (int i = 0; i < amount; i++) {
            SpawnArea spawnArea = MadUtils.pickSpawnArea(gameData);
            if (spawnArea == null) continue;
            Location spawnLoc = MadUtils.pickRandomSpawnLocation(spawnArea.getCorner1(), spawnArea.getCorner2());
            if (spawnLoc == null) continue;
            MadEntity mob = MadMobType.createMob(type, player.getWorld(), gameData);
            mob.spawn(spawnLoc);
            final TextComponent component = Component.text("Spawned ", NamedTextColor.YELLOW)
                    .append(Component.text(mob.getMob().getCustomName()))
                    .append(Component.text(" at location: ", NamedTextColor.YELLOW))
                    .append(Component.text(spawnLoc.getBlockX() + ", ", NamedTextColor.WHITE))
                    .append(Component.text(spawnLoc.getBlockY() + ", ", NamedTextColor.WHITE))
                    .append(Component.text(spawnLoc.getBlockZ() + ", ", NamedTextColor.WHITE));
            /*component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    color("/tp " + spawnLoc.getBlockX() + " " + spawnLoc.getBlockY() + " " + spawnLoc.getBlockZ())));
            component.setText(color("&eSpawned " + mob.getMob().getCustomName() + "&e at location:&f " +
                    spawnLoc.getBlockX() + ", " + spawnLoc.getBlockY() + ", " + spawnLoc.getBlockZ()));*/
            player.sendMessage(component);
        }
    }
}
