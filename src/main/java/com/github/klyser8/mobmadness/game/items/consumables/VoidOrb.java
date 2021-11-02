package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

public class VoidOrb extends MadItem {

    public static final int RADIUS = 2;

    private final Sound popSound;
    public VoidOrb(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.VOID_ORB, new ItemStack(ItemType.VOID_ORB.getMaterial(), amount), plugin, gameData, true);
        popSound = new Sound("entity.enderman.teleport", 2.0f, 0.5f);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Dematerializes mobs near the").color(NamedTextColor.GRAY),
                Component.text("landing location of this item").color(NamedTextColor.GRAY),
                Component.text("--------------------------------").color(DARK_GRAY),
                Component.text("Demateralized mobs do not die,").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true),
                Component.text("Instead simply despawn.").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true),
                Component.text("Ineffective against bosses.").color(DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true)
        ));

        setTags(true, false, true, false, false, null);
    }

    /**
     * Despawns any mob near the landing location of the ender pearl.
     * Works only on hostile mobs, not on players.
     *
     * @param pearl the EnderPearl entity related to the event
     */
    public void dematerialize(EnderPearl pearl) {
        World world = pearl.getWorld();
        List<LivingEntity> entities = new ArrayList<>();
        //Particle effects & sound
        popSound.play(pearl.getLocation(), SoundCategory.PLAYERS);
        world.spawnParticle(Particle.FLASH, pearl.getLocation(), 5,
                0, 0, 0, 0, null, true);
        //Get entities near ender pearl
        for (Entity entity : pearl.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
            if (!(entity instanceof LivingEntity) || entity instanceof Player) continue;
            entities.add((LivingEntity) entity);
        }
        //Despawn entities
        for (LivingEntity entity : entities) {
            entity.remove();
            world.spawnParticle(Particle.REVERSE_PORTAL, entity.getLocation().add(0, 1, 0), 25,
                    0.25, 0.50, 0.25, 0.25, null);
        }
    }
}
