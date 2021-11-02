package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class Arrows extends MadItem {

    public Arrows(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.ARROWS, new ItemStack(ItemType.ARROWS.getMaterial(), amount), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        setCompLore(List.of(
                Component.text("Used in Bows and Crossbows").color(GRAY)
        ));

        setTags(true, false, true, false, false, null);
    }
}
