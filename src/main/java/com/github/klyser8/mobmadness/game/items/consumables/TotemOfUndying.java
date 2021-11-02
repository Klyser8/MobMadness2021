package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

public class TotemOfUndying extends MadItem {

    public TotemOfUndying(MobMadness plugin, GameData gameData) {
        super(ItemType.TOTEM_OF_UNDYING, new ItemStack(ItemType.TOTEM_OF_UNDYING.getMaterial(), 1), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Allows the holder to withstand").color(NamedTextColor.GRAY),
                Component.text("fatal damage once").color(NamedTextColor.GRAY),
                Component.text("--------------------------------").color(DARK_GRAY),
                Component.text("Must be held upon death in").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true),
                Component.text("order to work").color(DARK_GRAY).decoration(TextDecoration.ITALIC, true)
        ));

        setTags(true, false, true, false, false, null);
    }
}
