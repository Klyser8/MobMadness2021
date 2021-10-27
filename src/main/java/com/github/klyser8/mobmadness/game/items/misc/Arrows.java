package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Arrows extends MadItem {

    public Arrows(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.ARROWS, new ItemStack(ItemType.ARROWS.getMaterial(), amount), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        setLore(Collections.singletonList(
                "&7Used for Crossbows and Bows"
        ));

        setTags(true, false, true, false, false, null);
    }
}
