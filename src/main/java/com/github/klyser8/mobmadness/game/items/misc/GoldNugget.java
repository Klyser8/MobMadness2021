package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

/**
 * One gold nugget is worth 64 Iron Nuggets
 */
public class GoldNugget extends MadItem {

    public GoldNugget(MobMadness plugin, int amount, Game game) {
        super(ItemType.GOLD_NUGGET, new ItemStack(ItemType.GOLD_NUGGET.getMaterial(), amount), plugin, game, true);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();
        setCompLore(Arrays.asList(
                Component.text("Main game currency.").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true),
                Component.text("Worth 64 ").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true)
                        .append(ItemType.IRON_NUGGET.getComp().color(ItemType.IRON_NUGGET.getColor()))
        ));

        tags.put(PICKUP_SOUND_TAG, "entity.experience_orb.pickup");
        setTags(false, true, true, false, false, tags);
    }
}
