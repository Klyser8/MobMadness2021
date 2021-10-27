package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ColorHelper;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class AethersBerries extends MadItem {

    public static final int COOLDOWN = 10;

    private final Sound useSound;
    public AethersBerries(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.AETHERS_BERRIES, new ItemStack(ItemType.AETHERS_BERRIES.getMaterial(), amount), plugin, gameData, true);
        useSound = new Sound("block.respawn_anchor.charge", 1.0f, 1.75f);
        setup();
    }

    protected void setup() {
        setLore(Arrays.asList(
                "&7Can be used to partially",
                "&7charge the &5Respawn Anchor.",
                "",
                "&5&oRespawn Anchor&7: &othe location",
                "&7&oplayers spawn once the game starts.",
                "&7&oIf supplied with Aether's Berries,",
                "&7&oeach berry allows players to respawn,",
                "&7&oonce."
        ));

        setTags(true, false, true, false, false, null);
    }

    public void chargeAnchor(Player player, ItemStack berry, Block clickedBlock) {
        if (player.getCooldown(type.getMaterial()) > 0) {
            return;
        }
        World world = clickedBlock.getWorld();
        if (gameData.getAnchorCharges() < 4) {
            gameData.setAnchorCharges(gameData.getAnchorCharges() + 1);
            useSound.play(clickedBlock.getLocation(), SoundCategory.BLOCKS);
            world.spawnParticle(Particle.WAX_OFF, clickedBlock.getLocation().add(0.5, 1.0, 0.5), 25,
                    0.25, 0.25, 0.25, 0);
            if (player.getGameMode() != GameMode.CREATIVE) {
                berry.setAmount(berry.getAmount() - 1);
                player.setCooldown(berry.getType(), 2400);
            }
        }
    }
}
