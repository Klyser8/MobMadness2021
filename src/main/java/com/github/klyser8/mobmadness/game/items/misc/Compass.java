package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Compass extends MadItem {

    public static final String OPEN_SHOP_TAG = "mm.open_shop";

    public Compass(MobMadness plugin, GameData gameData) {
        super(ItemType.COMPASS, new ItemStack(ItemType.COMPASS.getMaterial(), 1), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        Map<String, String> tags = new HashMap<>();
        setCompLore(Arrays.asList(
                Component.text("Mob Tracker").color(ColorHelper.ORANGE)
                        .append(Component.text(": Shows the location").color(GRAY)),
                Component.text("of the closest enemy when held").color(GRAY),
                Component.text("--------------------------------").color(DARK_GRAY),
                Component.text("Shop").color(ColorHelper.REAL_AQUA)
                        .append(Component.text(": Opens the item shop").color(GRAY)),
                Component.text("when ").color(GRAY)
                        .append(Component.text("Right-Clicked").color(WHITE))

                ));
        tags.put(OPEN_SHOP_TAG, "");
        setTags(false, false, true, false, false, tags);
    }
}
