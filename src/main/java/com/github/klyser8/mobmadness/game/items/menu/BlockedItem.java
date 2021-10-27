package com.github.klyser8.mobmadness.game.items.menu;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Item placed in the shop inventory, if a certain resource is out of stock or not unlocked yet.
 */
public class BlockedItem extends MadItem {

    public BlockedItem(MobMadness plugin, GameData gameData) {
        super(ItemType.BLOCKED_ITEM, new ItemStack(Material.BARRIER, 1), plugin, gameData, true);
        nbt.setBoolean(MadItem.CLICKABLE_TAG, false);
        setup();
    }

    @Override
    public void setup() {
        setDisplayName(color("&4&uUnavailable"));
        setTags(false, false, true,
                false, false, null);
    }
}
