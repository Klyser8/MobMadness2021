package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class Arrows extends MadItem {

    public Arrows(MobMadness plugin, int amount, Game game) {
        super(ItemType.ARROWS, new ItemStack(ItemType.ARROWS.getMaterial(), amount), plugin, game, true);
        setup();
    }

    protected void setup() {
        /*setCompLore(List.of(
                Component.text("Used in Bows and Crossbows").color(GRAY)
        ));*/
        setLore(List.of(color("&7Used in Bows and Crossbows")));
        setTags(true, false, false, false, false, null);
    }
}
