package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class ChorusFruit extends MadItem {

    public static final int COOLDOWN = 120;

    public ChorusFruit(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.CHORUS_FRUIT, new ItemStack(ItemType.CHORUS_FRUIT.getMaterial(), amount), plugin, gameData, true);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Teleports the user to the map's").color(NamedTextColor.GRAY),
                Component.text("Respawn Anchor").color(ColorHelper.INDIGO)
        ));
        setTags(true, false, true, false, false, null);
    }

    /**
     * Teleports the player to the map's Respawn Anchor.
     *
     * @param event an instance of {@link PlayerTeleportEvent}
     */
    public void handleTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.getCooldown(type.getMaterial()) > 0) return;
        event.setTo(gameData.getGameMap().getMapType().getSpawn());
        player.setCooldown(type.getMaterial(), COOLDOWN);
    }
}
