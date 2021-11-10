package com.github.klyser8.mobmadness.game;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.misc.Arrows;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.MadUtils;
import com.github.klyser8.mobmadness.util.Sound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    //Prevents blocks from being placed in survival in mobmadness
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL ||
                isWorldInvalid(event.getPlayer().getWorld())) return;
        event.setCancelled(true);
    }

    //Prevents blocks from being broken in survival in mobmadness
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL ||
                isWorldInvalid(event.getPlayer().getWorld())) return;
        event.setCancelled(true);
    }

    //Prevents block from being destroyed through odd means, like tridents
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockDestroy(BlockDestroyEvent event) {
        event.setCancelled(true);
    }

    //Prevents farmland to be destroyed when stepped on
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFarmStep(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null ||
                isWorldInvalid(event.getClickedBlock().getWorld())) return;
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock().getType() != Material.FARMLAND) return;
        event.setCancelled(true);
    }

    //Prevents fire from being extinguished
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFireExtinguish(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null
                || isWorldInvalid(event.getClickedBlock().getWorld())
                || player.getGameMode() != GameMode.SURVIVAL
                || event.getAction() != Action.LEFT_CLICK_BLOCK
                || event.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType() != Material.FIRE)
            return;
        event.setCancelled(true);
    }

    //Prevents interactive blocks to be interacted with
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        Block block = event.getClickedBlock();
        Material mat = block.getType();
        ItemStack handItem = event.getItem();
        if (isWorldInvalid(event.getClickedBlock().getWorld())) {
            return;
        }
        if (mat == Material.RESPAWN_ANCHOR
                && event.getItem() != null
                && event.getItem().getType() != Material.GLOW_BERRIES) {
            event.setCancelled(true);
        }
        boolean interactable = switch (mat) {
            case ANDESITE_STAIRS, ACACIA_STAIRS, STONE_BRICK_STAIRS, BIRCH_STAIRS, STONE_STAIRS, BLACKSTONE_STAIRS,
                    BRICK_STAIRS, COBBLED_DEEPSLATE_STAIRS, COBBLESTONE_STAIRS, CRIMSON_STAIRS, CUT_COPPER_STAIRS,
                    DARK_OAK_STAIRS, DARK_PRISMARINE_STAIRS, DEEPSLATE_BRICK_STAIRS, DEEPSLATE_TILE_STAIRS,
                    DIORITE_STAIRS, SANDSTONE_STAIRS, END_STONE_BRICK_STAIRS, EXPOSED_CUT_COPPER_STAIRS, GRANITE_STAIRS,
                    JUNGLE_STAIRS, MOSSY_COBBLESTONE_STAIRS, MOSSY_STONE_BRICK_STAIRS, NETHER_BRICK_STAIRS, OAK_STAIRS,
                    OXIDIZED_CUT_COPPER_STAIRS, POLISHED_ANDESITE_STAIRS, POLISHED_BLACKSTONE_BRICK_STAIRS,
                    POLISHED_BLACKSTONE_STAIRS, SMOOTH_QUARTZ_STAIRS, POLISHED_DEEPSLATE_STAIRS,
                    POLISHED_DIORITE_STAIRS, POLISHED_GRANITE_STAIRS, PRISMARINE_BRICK_STAIRS, PRISMARINE_STAIRS,
                    PURPUR_STAIRS, QUARTZ_STAIRS, RED_NETHER_BRICK_STAIRS, RED_SANDSTONE_STAIRS,
                    SMOOTH_RED_SANDSTONE_STAIRS, SMOOTH_SANDSTONE_STAIRS, SPRUCE_STAIRS, WARPED_STAIRS,
                    WAXED_CUT_COPPER_STAIRS, WAXED_EXPOSED_CUT_COPPER_STAIRS, WAXED_OXIDIZED_CUT_COPPER_STAIRS,
                    WAXED_WEATHERED_CUT_COPPER_STAIRS, WEATHERED_CUT_COPPER_STAIRS, CAULDRON, LAVA_CAULDRON,
                    POWDER_SNOW_CAULDRON, WATER_CAULDRON, IRON_TRAPDOOR, CANDLE, CYAN_CANDLE, BLACK_CANDLE,
                    BLUE_CANDLE, BROWN_CANDLE, GRAY_CANDLE, GREEN_CANDLE, LIGHT_BLUE_CANDLE, LIGHT_GRAY_CANDLE,
                    LIME_CANDLE, MAGENTA_CANDLE, ORANGE_CANDLE, PINK_CANDLE, PURPLE_CANDLE, RED_CANDLE,
                    WHITE_CANDLE, YELLOW_CANDLE, CAVE_VINES ->
//            case CAVE_VINES_PLANT:
                    false;
            default -> mat.isInteractable();
        };
        if (player.getGameMode() == GameMode.SURVIVAL
                && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && interactable) {
            event.setCancelled(true);
        }
    }

    //Prevents fire charges from being used
    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null
                || isWorldInvalid(event.getPlayer().getWorld())
                || player.getGameMode() != GameMode.SURVIVAL
                || event.getAction() != Action.RIGHT_CLICK_BLOCK
                || event.getItem() == null) return;
        if (event.getItem().getType() != Material.FIRE_CHARGE) return;
        event.setCancelled(true);
    }

    //Prevents ender pearl teleportation
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnderpearlTeleport(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL ||
                isWorldInvalid(event.getFrom().getWorld())) return;
        event.setCancelled(true);
    }

    //Prevents items with the DROP_TAG from being dropped
    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.DROP_TAG)) return;
        if (nbt.getBoolean(MadItem.DROP_TAG)) return;
        event.getPlayer().sendMessage(English.ERROR_CANNOT_DROP_ITEM.getComponent());
        event.setCancelled(true);
    }

    //Prevents items with the UNBREAKING tag from losing durability
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDurabilityDecrease(PlayerItemDamageEvent event) {
        ItemStack item = event.getItem();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.UNBREAKING_TAG)) return;
        if (!nbt.getBoolean(MadItem.UNBREAKING_TAG)) return;
        event.setCancelled(true);
    }

    //Prevents explosions from breaking blocks, and prevents Fireballs from exploding
    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplosion(EntityExplodeEvent event) {
        if (isWorldInvalid(event.getLocation().getWorld())) return;
        event.blockList().clear();
        if (event.getEntity().getType() == EntityType.FIREBALL) {
            event.setCancelled(true);
        }
    }

    //Prevents blocks from exploding
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockExplosion(BlockExplodeEvent event) {
        if (isWorldInvalid(event.getBlock().getLocation().getWorld())) return;
        event.setCancelled(true);
    }

    //Prevents TNTs with MM tag from damaging players
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (isWorldInvalid(event.getDamager().getWorld())) return;
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION
                && event.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getDamager();
            if (tnt.hasMetadata(MadItem.MM_ENUM_TAG)) event.setDamage(0);
        }
    }

    //Prevents fireballs from dealing any form of 'default' damage
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFireballDamage(EntityDamageByEntityEvent event) {
        if (isWorldInvalid(event.getEntity().getWorld())) return;
        if (event.getDamager().getType() != EntityType.FIREBALL) return;
        if (event.getDamager() instanceof Blaze) return;
        event.setDamage(0);
    }

    //Prevents projectiles shot from players from hitting other players
    @EventHandler(priority = EventPriority.LOWEST)
    public void onFireballHitPlayer(ProjectileCollideEvent event) {
        if (isWorldInvalid(event.getEntity().getWorld())) return;
        Projectile projectile = event.getEntity();
        if (projectile instanceof ThrownPotion) return;
        if (!(projectile.getShooter() instanceof Player)) return;
        if (!(event.getCollidedWith() instanceof Player)) return;
        event.setCancelled(true);
    }

    //Prevents concrete powder from solidifying when in water
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockForm(BlockFormEvent event) {
        if (isWorldInvalid(event.getBlock().getWorld())) return;
        if (event.getBlock().getType().toString().contains("CONCRETE_POWDER")) {
            event.setCancelled(true);
        }
    }

    //Stops corals from going gray
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    //Adds sounds to picked up items
    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack item = event.getItem().getItemStack();
        Player player = (Player) event.getEntity();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.PICKUP_SOUND_TAG)) return;
        Sound sound = new Sound(nbt.getString(MadItem.PICKUP_SOUND_TAG), 0.5f, 2.0f);
        sound.play(event.getItem().getLocation(), SoundCategory.MASTER);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onArrowPickup(PlayerPickupArrowEvent event) {
        Player player = event.getPlayer();
        Game game = MadUtils.getEntityGame(plugin, player);
        System.out.println(event.getItem().getType());
        if (game == null) return;
        if (event.getItem().getItemStack().getType() == Material.ARROW) {
            Arrows arrows = new Arrows(plugin, event.getItem().getItemStack().getAmount(), game);
            event.getItem().setItemStack(arrows.getItemStack());
        } else if (event.getItem().getItemStack().getType() == Material.SPECTRAL_ARROW) {
            Arrows arrows = new Arrows(plugin, event.getItem().getItemStack().getAmount(), game);
            event.getItem().setItemStack(arrows.getItemStack());
        }
    }

    /**
     * Checks whether the provided world is able to host Mob Madness games.
     *
     * @param world the world to test.
     * @return true if the world is called "mobmadness", false otherwise.
     */
    private boolean isWorldInvalid(World world) {
        return !world.getName().contains("mobmadness");
    }

}
