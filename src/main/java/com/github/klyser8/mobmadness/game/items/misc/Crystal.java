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
import java.util.List;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

public class Crystal extends MadItem {

    public Crystal(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.CRYSTAL, new ItemStack(ItemType.CRYSTAL.getMaterial(), amount), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();
        setCompLore(List.of(
                Component.text("Emits a mystical aura.").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true)
        ));

        tags.put(PICKUP_SOUND_TAG, "entity.player.levelup");
        setTags(false, true, true, true, false, tags);
    }
}
