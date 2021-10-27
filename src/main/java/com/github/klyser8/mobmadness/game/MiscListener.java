package com.github.klyser8.mobmadness.game;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.misc.Arrows;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import com.github.klyser8.mobmadness.util.Sound;
import de.tr7zw.nbtapi.NBTItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * EventListener for miscellaneous events.
 * Mostly cancels events that would ruin the experience of MobMadness.
 *
 */
public class MiscListener implements Listener {

    private final MobMadness plugin;
    public MiscListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler                                          //Prevents blocks from being placed in survival in mobmadness
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL ||
                !isMobMadness(event.getPlayer().getWorld())) return;
        event.setCancelled(true);
    }

    @EventHandler                                          //Prevents blocks from being broken in survival in mobmadness
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL ||
                !isMobMadness(event.getPlayer().getWorld())) return;
        event.setCancelled(true);
    }

    @EventHandler                                 //Prevents block from being destroyed through odd means, like tridents
    public void onBlockDestroy(BlockDestroyEvent event) {
        event.setCancelled(true);
    }

    @EventHandler                                                    //Prevents farmland to be destroyed when stepped on
    public void onFarmStep(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null ||
                !isMobMadness(event.getClickedBlock().getWorld())) return;
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock().getType() != Material.FARMLAND) return;
        event.setCancelled(true);
    }

    @EventHandler                                                                //Prevents fire from being extinguished
    public void onFireExtinguish(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null
                || !isMobMadness(event.getClickedBlock().getWorld())
                || player.getGameMode() != GameMode.SURVIVAL
                || event.getAction() != Action.LEFT_CLICK_BLOCK
                || event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType() != Material.FIRE)
            return;
        event.setCancelled(true);
    }

    @EventHandler                                                    //Prevents interactive blocks to be interacted with
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        Material mat = block.getType();
        ItemStack handItem = event.getItem();
        if (!isMobMadness(event.getClickedBlock().getWorld())) {
            return;
        }
        if (mat == Material.RESPAWN_ANCHOR
                && event.getItem() != null
                && event.getItem().getType() != Material.GLOW_BERRIES) {
            event.setCancelled(true);
        }
        boolean interactable = mat.isInteractable();
        switch (mat) {
            case ANDESITE_STAIRS:
            case ACACIA_STAIRS:
            case STONE_BRICK_STAIRS:
            case BIRCH_STAIRS:
            case STONE_STAIRS:
            case BLACKSTONE_STAIRS:
            case BRICK_STAIRS:
            case COBBLED_DEEPSLATE_STAIRS:
            case COBBLESTONE_STAIRS:
            case CRIMSON_STAIRS:
            case CUT_COPPER_STAIRS:
            case DARK_OAK_STAIRS:
            case DARK_PRISMARINE_STAIRS:
            case DEEPSLATE_BRICK_STAIRS:
            case DEEPSLATE_TILE_STAIRS:
            case DIORITE_STAIRS:
            case SANDSTONE_STAIRS:
            case END_STONE_BRICK_STAIRS:
            case EXPOSED_CUT_COPPER_STAIRS:
            case GRANITE_STAIRS:
            case JUNGLE_STAIRS:
            case MOSSY_COBBLESTONE_STAIRS:
            case MOSSY_STONE_BRICK_STAIRS:
            case NETHER_BRICK_STAIRS:
            case OAK_STAIRS:
            case OXIDIZED_CUT_COPPER_STAIRS:
            case POLISHED_ANDESITE_STAIRS:
            case POLISHED_BLACKSTONE_BRICK_STAIRS:
            case POLISHED_BLACKSTONE_STAIRS:
            case SMOOTH_QUARTZ_STAIRS:
            case POLISHED_DEEPSLATE_STAIRS:
            case POLISHED_DIORITE_STAIRS:
            case POLISHED_GRANITE_STAIRS:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_STAIRS:
            case PURPUR_STAIRS:
            case QUARTZ_STAIRS:
            case RED_NETHER_BRICK_STAIRS:
            case RED_SANDSTONE_STAIRS:
            case SMOOTH_RED_SANDSTONE_STAIRS:
            case SMOOTH_SANDSTONE_STAIRS:
            case SPRUCE_STAIRS:
            case WARPED_STAIRS:
            case WAXED_CUT_COPPER_STAIRS:
            case WAXED_EXPOSED_CUT_COPPER_STAIRS:
            case WAXED_OXIDIZED_CUT_COPPER_STAIRS:
            case WAXED_WEATHERED_CUT_COPPER_STAIRS:
            case WEATHERED_CUT_COPPER_STAIRS:
            case CAULDRON:
            case LAVA_CAULDRON:
            case POWDER_SNOW_CAULDRON:
            case WATER_CAULDRON:
            case IRON_TRAPDOOR:
            case CANDLE:
            case CYAN_CANDLE:
            case BLACK_CANDLE:
            case BLUE_CANDLE:
            case BROWN_CANDLE:
            case GRAY_CANDLE:
            case GREEN_CANDLE:
            case LIGHT_BLUE_CANDLE:
            case LIGHT_GRAY_CANDLE:
            case LIME_CANDLE:
            case MAGENTA_CANDLE:
            case ORANGE_CANDLE:
            case PINK_CANDLE:
            case PURPLE_CANDLE:
            case RED_CANDLE:
            case WHITE_CANDLE:
            case YELLOW_CANDLE:
            case CAVE_VINES: //TODO fix glow berries harvesting!
//            case CAVE_VINES_PLANT:
                interactable = false;
        }
        if (player.getGameMode() == GameMode.SURVIVAL
                && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && interactable) {
            event.setCancelled(true);
        }
    }

    @EventHandler                                                                //Prevents fire charges from being used
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null
                || !isMobMadness(event.getPlayer().getWorld())
                || player.getGameMode() != GameMode.SURVIVAL
                || event.getAction() != Action.RIGHT_CLICK_BLOCK
                || event.getItem() == null) return;
        if (event.getItem().getType() != Material.FIRE_CHARGE) return;
        event.setCancelled(true);
    }

    @EventHandler                                                                   //Prevents ender pearl teleportation
    public void onEnderpearlTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL ||
                !isMobMadness(event.getFrom().getWorld())) return;
        event.setCancelled(true);
    }

    @EventHandler                                                  //Prevents items with the DROP_TAG from being dropped
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.DROP_TAG)) return;
        if (nbt.getBoolean(MadItem.DROP_TAG)) return;
        event.getPlayer().sendMessage(color(English.ERROR_CANNOT_DROP_ITEM.getMsg(null)));
        event.setCancelled(true);
    }

    @EventHandler                                        //Prevents items with the UNBREAKING tag from losing durability
    public void onDurabilityDecrease(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.UNBREAKING_TAG)) return;
        if (!nbt.getBoolean(MadItem.UNBREAKING_TAG)) return;
        event.setCancelled(true);
    }

    @EventHandler                      //Prevents explosions from breaking blocks, and prevents Fireballs from exploding
    public void onExplosion(EntityExplodeEvent event) {
        if (!isMobMadness(event.getLocation().getWorld())) return;
        event.blockList().clear();
        if (event.getEntity().getType() == EntityType.FIREBALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent event) {                             //Prevents blocks from exploding
        if (!isMobMadness(event.getBlock().getLocation().getWorld())) return;
        event.setCancelled(true);
    }

    @EventHandler                                                      //Prevents TNTs with MM tag from damaging players
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!isMobMadness(event.getDamager().getWorld())) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                && event.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getDamager();
            if (tnt.hasMetadata(MadItem.MM_ENUM_TAG)) event.setDamage(0);
        }
    }

    @EventHandler                                 //Disables bow damage, taking into consideration only the arrow damage
    public void onArrowDamage(EntityDamageByEntityEvent event) {
        if (!isMobMadness(event.getDamager().getWorld())) return;
        if (!(event.getDamager() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getDamager();
        event.setDamage(arrow.getDamage());
    }

    @EventHandler                                         //Prevents fireballs from dealing any form of 'default' damage
    public void onFireballDamage(EntityDamageByEntityEvent event) {
        if (!isMobMadness(event.getEntity().getWorld())) return;
        if (event.getDamager().getType() != EntityType.FIREBALL) return;
        if (event.getDamager() instanceof Blaze) return;
        event.setDamage(0);
    }

    @EventHandler                                    //Prevents projectiles shot from players from hitting other players
    public void onFireballHitPlayer(ProjectileCollideEvent event) {
        if (!isMobMadness(event.getEntity().getWorld())) return;
        Projectile projectile = event.getEntity();
        if (projectile instanceof ThrownPotion) return;
        if (!(projectile.getShooter() instanceof Player)) return;
        if (!(event.getCollidedWith() instanceof Player)) return;
        event.setCancelled(true);
    }

    @EventHandler                                             //Prevents concrete powder from solidifying when in water
    public void onBlockForm(BlockFormEvent event) {
        if (!isMobMadness(event.getBlock().getWorld())) return;
        if (event.getBlock().getType().toString().contains("CONCRETE_POWDER")) {
            event.setCancelled(true);
        }
    }

    @EventHandler                                                                         //Stops corals from going gray
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler                                                                       //Adds sounds to picked up items
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack item = event.getItem().getItemStack();
        Player player = (Player) event.getEntity();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.PICKUP_SOUND_TAG)) return;
        Sound sound = new Sound(nbt.getString(MadItem.PICKUP_SOUND_TAG), 0.5f, 2.0f);
        sound.play(event.getItem().getLocation(), SoundCategory.MASTER);
    }

    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent event) {
        Player player = event.getPlayer();
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        if (event.getItem().getType() == EntityType.ARROW) {
            Arrows arrows = new Arrows(plugin, event.getItem().getItemStack().getAmount(), gameData);
            event.getItem().setItemStack(arrows.getItemStack());
        } else if (event.getItem().getType() == EntityType.SPECTRAL_ARROW) {
            Arrows arrows = new Arrows(plugin, event.getItem().getItemStack().getAmount(), gameData);
            event.getItem().setItemStack(arrows.getItemStack());
        }
    }

    /**
     * Checks whether the provided world is able to host Mob Madness games.
     *
     * @param world the world to test.
     * @return true if the world is called "mobmadness", false otherwise.
     */
    private boolean isMobMadness(World world) {
        return world.getName().equalsIgnoreCase("mobmadness");
    }

}
