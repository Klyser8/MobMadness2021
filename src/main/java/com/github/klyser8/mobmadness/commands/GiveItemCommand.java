package com.github.klyser8.mobmadness.commands;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.MadUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.mattstudios.mf.base.components.MfUtil.color;

@Command("mobmadness")
@Alias("mm")
public class GiveItemCommand extends CommandBase {

    private final MobMadness plugin;
    public GiveItemCommand(MobMadness plugin) {
        this.plugin = plugin;
    }

    @SubCommand("give")
    @Permission("mm.command.give")
    @Completion({"#itemtypes", "#players"})
    public void giveItemCommand(CommandSender sender, ItemType type, Integer amount) {
        Player player = (Player) sender;
        Game game = MadUtils.getEntityGame(plugin, player);
        if (amount == 0) amount = 1;
        MadItem item = ItemType.createDummyItem(plugin, game, type, amount);
        if (item == null) return;
        player.getInventory().addItem(item.cloneItem());
    }
}
