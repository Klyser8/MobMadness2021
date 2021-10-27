package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
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
        setLore(Arrays.asList(
                "&7Teleports the user to the map's",
                "&7spawn point upon consumption."
        ));
        setTags(true, false, true, false, false, null);
    }

    /**
     * Teleports the player to the spawn point of the current map.
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
