package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import com.github.klyser8.mobmadness.util.MadUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class DetonationOrb extends MadItem {

    public static final int RADIUS = 2;
    public static final int DAMAGE = 25;
    public static final int FIRE_TICKS = 100;

    private final Sound placeSound;
    private final Sound ambientFireSound;
    public DetonationOrb(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.DETONATION_ORB, new ItemStack(ItemType.DETONATION_ORB.getMaterial(), amount), plugin, gameData, true);
        placeSound = new Sound("block.coral_block.place", 1.0f, 0.5f);
        ambientFireSound = new Sound("block.fire.ambient", 1.0f, 1.0f);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Places a trap on the ground which").color(NamedTextColor.GRAY),
                Component.text("activates after a short delay.").color(NamedTextColor.GRAY),
                Component.text("Explodes once a mob gets close").color(NamedTextColor.GRAY)
        ));

        setTags(true, false, true, false, false, null);
    }

    /**
     * Places down a "Dead Fire Coral Fan" above the chosen block. After which, an instance of
     * {@link DetonationOrbRunnable} is created, running every tick (0.05s).
     * Instead of being placed above the clicked block, it may replace the block itself if it is considered passable.
     *
     * @param player the player who placed the orb
     * @param item the ItemStack of the orb
     * @param loc the location where the orb should appear
     * @param oldBlockData the {@link BlockData} of the block replaced by the orb (if there's one)
     */
    public void place(Player player, ItemStack item, Location loc, BlockData oldBlockData) {
        World world = loc.getWorld();
        placeSound.play(loc, SoundCategory.PLAYERS);
        ambientFireSound.play(loc, SoundCategory.AMBIENT);
        loc.getBlock().setType(Material.DEAD_FIRE_CORAL_FAN);
        Waterlogged blockData = ((Waterlogged) loc.getBlock().getBlockData());
        blockData.setWaterlogged(false);
        loc.getBlock().setBlockData(blockData);
        DetonationOrbRunnable runnable = new DetonationOrbRunnable(player, loc, oldBlockData);
        runnable.runTaskTimer(plugin, 0, 1);
        item.setAmount(item.getAmount() - 1);
    }

    /**
     * Class extending {@link BukkitRunnable}.
     * Periodically checks whether a mob approaches the placed orb, to then damage it and knock it away upon
     * being close enough.
     * Additionally, plays sounds and particles at specific times.
     */
    private class DetonationOrbRunnable extends BukkitRunnable {

        private final Player player;
        private final Location clickLoc;
        private final BlockData oldBlockData;
        private final World world;

        private final Sound readySound1;
        private final Sound readySound2;
        private final Sound explodeSound;

        int clock = -1;

        DetonationOrbRunnable(Player player, Location clickLoc, BlockData oldBlockData) {
            this.player = player;
            this.oldBlockData = oldBlockData;
            this.clickLoc = clickLoc;
            world = clickLoc.getWorld();
            readySound1 = new Sound("item.bucket_fill.lava", 1.0f, 1.0f);
            readySound2 = new Sound("entity.magma_cube.death", 1.0f, 0.5f);
            explodeSound = new Sound("entity.generic.explode", 3.0f, 0.5f);
        }

        @Override
        public void run() {
            clock++;
            playLavaParticles();
            readyTrap();
            if (clock > 40) {

                playFlameParticles();
                playAmbientFire();
                List<LivingEntity> nearbyEntities = getEntitiesNearTrap();
                if (MadUtils.getMobsInArea(clickLoc,
                        RADIUS / 2.0, RADIUS / 2.0, RADIUS / 2.0).size() > 0) {
                    triggerTrap(nearbyEntities);
                    remove();
                }
            }
            if (!plugin.getGameManager().getGames().containsKey(gameData.getGameMap().getMapType())) {
                remove();
            }
        }

        private void playLavaParticles() {            //Play lava particles every 0.25 seconds, before the trap is ready
            if (clock % 5 == 0 && clock < 40) {
                world.spawnParticle(Particle.LAVA, clickLoc, 1,
                        0, 0, 0, 0, null, true);
            }
        }

        private void readyTrap() {                                //Ready the trap. Make sounds effects and change block
            if (clock == 40) {
                clickLoc.getBlock().setType(Material.FIRE_CORAL_FAN);
                Waterlogged blockData = ((Waterlogged) clickLoc.getBlock().getBlockData());
                blockData.setWaterlogged(false);
                clickLoc.getBlock().setBlockData(blockData);
                readySound1.play(clickLoc, SoundCategory.PLAYERS);
                readySound2.play(clickLoc, SoundCategory.PLAYERS);
            }
        }

        private void playFlameParticles() {                                  //Every 0.1 seconds, spawn a flame particle
            if (clock % 2 == 0) {
                clickLoc.getWorld().spawnParticle(Particle.FLAME, clickLoc, 1,
                        0.25, 0.1, 0.25, 0, null, true);
            }
        }

        private void playAmbientFire() {                                      //Every 4 seconds, play ambient fire sound
            if (clock % 80 == 0) {
                ambientFireSound.play(clickLoc, SoundCategory.AMBIENT);
            }
        }

        private List<LivingEntity> getEntitiesNearTrap() {                         //Loop through entities near the trap
            List<LivingEntity> nearbyEntities = new ArrayList<>();
            for (Entity entity : clickLoc.getNearbyEntities(2, 2, 2)) {
                if (!(entity instanceof LivingEntity) || entity instanceof Player) continue;
                nearbyEntities.add((LivingEntity) entity);
            }
            return nearbyEntities;
        }

        private void triggerTrap(List<LivingEntity> nearbyEntities) {          //If an entity is found, trigger the trap
            explodeSound.play(clickLoc, SoundCategory.PLAYERS);
            world.spawnParticle(Particle.LAVA, clickLoc, 10,
                    1, 1, 1, 0, null, true);
            world.spawnParticle(Particle.EXPLOSION_LARGE, clickLoc,
                    5, 1, 1, 1, 0, null, true);
            world.spawnParticle(Particle.FLAME, clickLoc, 25,
                    1, 1, 1, 0.25, null, true);
            //Damage and knock entities away.
            for (LivingEntity entity : nearbyEntities) {
                entity.damage(DAMAGE, player);
                entity.setFireTicks(FIRE_TICKS);
                Vector unitVector = entity.getLocation().toVector().subtract(clickLoc.clone().add(0, -1, 0).toVector()).normalize();
                entity.setVelocity(unitVector.multiply(1.1 - (clickLoc.distance(clickLoc) / 4)));
            }
        }

        private void remove() {                                                //Remove the trap and cancel the runnable
            clickLoc.getBlock().setType(oldBlockData.getMaterial());
            clickLoc.getBlock().setBlockData(oldBlockData);
            cancel();
        }
    }
}
