package com.github.klyser8.mobmadness.game.items.equipment;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Boots extends MadItem {

    public Boots(MobMadness plugin, GameData gameData, ItemType type, ItemStack stack) {
        super(type, stack, plugin, gameData, false);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();
        setDisplayName(color(type.getName()));

        setTags(true, true, true, false, false, tags);
    }
}
