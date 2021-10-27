package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Compass extends MadItem {

    public static final String OPEN_SHOP_TAG = "mm.open_shop";

    public Compass(MobMadness plugin, GameData gameData) {
        super(ItemType.COMPASS, new ItemStack(ItemType.COMPASS.getMaterial(), 1), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();
        setLore(Arrays.asList(
                "&3Mob Tracker:&7 Shows the location",
                "&7of the closest enemy when held.",
                "&8---------------------------------",
                "&3Shop:&7 Opens the item shop",
                "&7when right clicked."
        ));
        tags.put(OPEN_SHOP_TAG, "");
        setTags(false, false, true, false, false, tags);
    }
}
