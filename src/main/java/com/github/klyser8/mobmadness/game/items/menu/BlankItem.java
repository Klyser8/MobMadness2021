package com.github.klyser8.mobmadness.game.items.menu;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Item which gets placed in the shop inventory, in any slot where no ItemStack is present.
 * The {@link Material} used for the ItemStack changes based on the current tier.
 */
public class BlankItem extends MadItem {

    public BlankItem(MobMadness plugin, GameData gameData) {
        super(ItemType.BLANK_ITEM, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), plugin, gameData, true);
        nbt.setBoolean(MadItem.CLICKABLE_TAG, false);
        setup();
    }

    @Override
    public void setup() {
        Material type;
        switch (gameData.getTier()) {
            default: type = Material.GRAY_STAINED_GLASS_PANE;
                break;
            case II: type = Material.LIME_STAINED_GLASS;
                break;
            case III: type = Material.BLUE_STAINED_GLASS_PANE;
                break;
            case IV: type = Material.MAGENTA_STAINED_GLASS_PANE;
                break;
            case V: type = Material.ORANGE_STAINED_GLASS_PANE;
                break;
            case VI: type = Material.RED_STAINED_GLASS_PANE;
                break;
        }
        nbt.getItem().setType(type);
        setDisplayName(color("&k"));
        setTags(false, false, true,
                false, false, null);
    }
}
