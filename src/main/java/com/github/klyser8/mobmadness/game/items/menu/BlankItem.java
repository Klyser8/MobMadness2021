package com.github.klyser8.mobmadness.game.items.menu;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Item which gets placed in the shop inventory, in any slot where no ItemStack is present.
 * The {@link Material} used for the ItemStack changes based on the current tier.
 */
public class BlankItem extends MadItem {

    public BlankItem(MobMadness plugin, Game game) {
        super(ItemType.BLANK_ITEM, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1), plugin, game, true);
        nbt.setBoolean(MadItem.CLICKABLE_TAG, false);
        setup();
    }

    @Override
    public void setup() {
        Material type = switch (game.getTier()) {
            default -> Material.GRAY_STAINED_GLASS_PANE;
            case II -> Material.LIME_STAINED_GLASS;
            case III -> Material.BLUE_STAINED_GLASS_PANE;
            case IV -> Material.MAGENTA_STAINED_GLASS_PANE;
            case V -> Material.ORANGE_STAINED_GLASS_PANE;
            case VI -> Material.RED_STAINED_GLASS_PANE;
        };
        nbt.getItem().setType(type);
        setTags(false, false, true,
                false, false, null);
    }
}
