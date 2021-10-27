package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Barricade extends MadItem {

    public static final int COOLDOWN = 300; //In ticks
    public static final int BARRICADE_DURATION = 600; //In ticks

    private final Sound buildSound;
    private final Sound buildSoundReverb;
    private final Sound crumbleSound;
    private final Sound breakSound;
    public Barricade(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.BARRICADE, new ItemStack(ItemType.BARRICADE.getMaterial(), amount), plugin, gameData, true);
        buildSound = new Sound("block.netherrack.place", 1.5f, 1.0f);
        buildSoundReverb = new Sound("block.netherrack.place", 2.0f, 0.5f);
        crumbleSound = new Sound("block.nether_bricks.hit", 0.5f, 1.0f);
        breakSound = new Sound("block.shroomlight.break", 1.5f, 0.5f);
        setup();
    }

    protected void setup() {
        setDisplayName(color(type.getName()));
        setLore(Arrays.asList(
                "&7Builds a short lasting",
                "&f3x2 &7barricade where clicked."
        ));

        setTags(true, false, true, false, false, null);
    }

    public void build(Profile profile, ItemStack item, Location origin) {
        Player player = profile.getPlayer();
        World world = player.getWorld();
        if (player.getCooldown(type.getMaterial()) > 0) return;
        buildSound.play(origin, SoundCategory.BLOCKS);
        buildSoundReverb.play(origin, SoundCategory.BLOCKS);
        player.setCooldown(item.getType(), COOLDOWN);
        BlockFace facing = player.getFacing();
        //Map containing the old block's BlockData
        Map<Location,BlockData> oldBlocksData = new HashMap<>();
        oldBlocksData.put(origin, origin.getBlock().getBlockData());
        oldBlocksData.put(origin.clone().add(0, 1, 0),
                origin.clone().add(0, 1, 0).getBlock().getBlockData());
        //Rotate the barricade appropriately, based on the direction the player faces.
        if (facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
            oldBlocksData.put(origin.clone().add(1, 0, 0),
                    origin.clone().add(1, 0, 0).getBlock().getBlockData());
            oldBlocksData.put(origin.clone().add(-1, 0, 0),
                    origin.clone().add(-1, 0, 0).getBlock().getBlockData());
            oldBlocksData.put(origin.clone().add(1, 1, 0),
                    origin.clone().add(1, 1, 0).getBlock().getBlockData());
            oldBlocksData.put(origin.clone().add(-1, 1, 0),
                    origin.clone().add(-1, 1, 0).getBlock().getBlockData());
        } else if (facing == BlockFace.WEST || facing == BlockFace.EAST) {
            oldBlocksData.put(origin.clone().add(0, 0, 1),
                    origin.clone().add(1, 0, 0).getBlock().getBlockData());
            oldBlocksData.put(origin.clone().add(0, 0, -1),
                    origin.clone().add(-1, 0, 0).getBlock().getBlockData());
            oldBlocksData.put(origin.clone().add(0, 1, 1),
                    origin.clone().add(1, 1, 0).getBlock().getBlockData());
            oldBlocksData.put(origin.clone().add(0, 1, -1),
                    origin.clone().add(-1, 1, 0).getBlock().getBlockData());
        }
        List<Location> newBlocks = new ArrayList<>(oldBlocksData.keySet());
        for (Location loc : newBlocks) {
            Block block = loc.getBlock();
            if (block.getType().isSolid() && block.getType() != type.getMaterial()) continue;
            block.setBlockData(item.getType().createBlockData());
            block.getState().update(true);
            world.spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0.5, 0.5, 0.5), 25,
                    0.25, 0.25, 0.25, 1, Material.ANDESITE.createBlockData());
        }
        item.setAmount(item.getAmount() - 1);
        new BarricadeRunnable(newBlocks, oldBlocksData).runTaskTimer(plugin, 0, 1);
    }

    /**
     * Class extending {@link BukkitRunnable}.
     * Counts how long the barricade has been up for.
     * Additionally, plays sounds and particles at specific times.
     */
    private class BarricadeRunnable extends BukkitRunnable {

        int lifeTime = BARRICADE_DURATION;

        private final List<Location> newBlocks;
        private final Map<Location, BlockData> oldBlocksData;
        private final Random random;

        private BarricadeRunnable(List<Location> newBlocks, Map<Location, BlockData> oldBlocksData) {
            this.newBlocks = newBlocks;
            this.oldBlocksData = oldBlocksData;
            random = new Random();
            //Decrease the volume for crumbling reverb
            buildSoundReverb.setVolume(0.5f);
        }

        @Override
        public void run() {
            if (lifeTime == 0 || !plugin.getGameManager().getGames().containsKey(gameData.getGameMap().getMapType())) {
                destroy();
            }
            if (lifeTime == 150) {
                for (Location loc : newBlocks) {
                    if (loc.getBlock().getType() != type.getMaterial()) continue;
                    Block block = loc.getBlock();
                    block.getLocation().getBlock().setType(Material.COBBLESTONE_WALL);
                    block.getState().update(true);
                }
            }
            if (lifeTime % 2 == 0 && lifeTime <= 150) {
                crumbleSound.setPitch(random.nextFloat() + 0.25f);
                int i = random.nextInt(6);
                Location loc = newBlocks.get(i).clone();
                if (random.nextBoolean()) {
                    crumbleSound.play(loc, SoundCategory.BLOCKS);
                    buildSoundReverb.play(loc, SoundCategory.BLOCKS);
                }
                loc.getWorld().spawnParticle(Particle.BLOCK_CRACK,
                        loc.add(0.5, 0.5, 0.5), 5,
                        0.25, 0.25, 0.25, 1, Material.COBBLESTONE.createBlockData());
            }
            lifeTime--;
        }

        private void destroy() {
            for (Location loc : oldBlocksData.keySet()) {
                loc.getBlock().setBlockData(oldBlocksData.get(loc));
                loc.getBlock().setType(oldBlocksData.get(loc).getMaterial());
                loc.getBlock().getState().update(true);
                breakSound.play(loc, SoundCategory.BLOCKS);
            }
            cancel();
        }
    }
}
