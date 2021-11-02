package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

public class IronNugget extends MadItem {

    public IronNugget(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.IRON_NUGGET, new ItemStack(ItemType.IRON_NUGGET.getMaterial(), amount), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();
        setCompLore(Collections.singletonList(
                Component.text("Main game currency").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true)
        ));

        tags.put(PICKUP_SOUND_TAG, "block.note_block.bit");
        setTags(false, true, true, false, false, tags);
    }
}
