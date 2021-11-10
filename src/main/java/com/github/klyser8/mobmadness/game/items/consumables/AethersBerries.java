package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.ColorHelper;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class AethersBerries extends MadItem {

    public static final int COOLDOWN = 2400;

    private final Sound useSound;
    public AethersBerries(MobMadness plugin, int amount, Game game) {
        super(ItemType.AETHERS_BERRIES, new ItemStack(ItemType.AETHERS_BERRIES.getMaterial(), amount), plugin, game, true);
        useSound = new Sound("block.respawn_anchor.charge", 1.0f, 1.75f);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Can be used to partially").color(NamedTextColor.GRAY),
                Component.text("charge the ").color(NamedTextColor.GRAY)
                        .append(Component.text("Respawn Anchor").color(ColorHelper.INDIGO))
        ));

        setTags(true, false, true, false, false, null);
    }

    /**
     * Method which charges the game's respawn anchor.
     *
     * @param player the player who's attempting to charge the anchor
     * @param berry the itemstack used on the respawn anchor
     */
    public void chargeAnchor(Player player, ItemStack berry) {
        if (player.getCooldown(type.getMaterial()) > 0) {
            return;
        }
        World world = player.getWorld();
        if (game.getAnchorCharges() < 4) {
            game.setAnchorCharges(game.getAnchorCharges() + 1);
            useSound.play(game.getRespawnAnchor().getLocation(), SoundCategory.BLOCKS);
            world.spawnParticle(Particle.WAX_OFF, game.getRespawnAnchor().getLocation().add(0.5, 1.0, 0.5),
                    25, 0.25, 0.25, 0.25, 0);
            if (player.getGameMode() != GameMode.CREATIVE) {
                berry.setAmount(berry.getAmount() - 1);
                player.setCooldown(berry.getType(), COOLDOWN);
            }
        }
    }
}
