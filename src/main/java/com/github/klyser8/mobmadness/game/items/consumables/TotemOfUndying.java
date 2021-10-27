package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class TotemOfUndying extends MadItem {

    public TotemOfUndying(MobMadness plugin, GameData gameData) {
        super(ItemType.TOTEM_OF_UNDYING, new ItemStack(ItemType.TOTEM_OF_UNDYING.getMaterial(), 1), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        setDisplayName(color("&aTotem of Undying"));
        setLore(Arrays.asList(
                "&7Allows the holder to withstand",
                "&7fatal damage once.",
                "&8--------------------------------",
                "&8&oMust be held upon death in",
                "&8&oorder to work."
        ));

        setTags(true, false, true, false, false, null);
    }
}
