package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * One gold nugget is worth 64 Iron Nuggets
 */
public class GoldNugget extends MadItem {

    public GoldNugget(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.GOLD_NUGGET, new ItemStack(ItemType.GOLD_NUGGET.getMaterial(), amount), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();
        setLore(Arrays.asList(
                "&8&oMain game currency.",
                "&8&oWorth 64 Iron Nuggets."
        ));

        tags.put(PICKUP_SOUND_TAG, "entity.experience_orb.pickup");
        setTags(false, true, true, false, false, tags);
    }
}
