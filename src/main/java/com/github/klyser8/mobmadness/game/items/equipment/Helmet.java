package com.github.klyser8.mobmadness.game.items.equipment;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Helmet extends MadItem {

    public Helmet(MobMadness plugin, Game game, ItemType type) {
        super(type, new ItemStack(type.getMaterial()), plugin, game, false);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();

        setTags(true, true, true, false, false, tags);
    }
}
