package com.github.klyser8.mobmadness.game.items;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.consumables.*;
import com.github.klyser8.mobmadness.game.items.misc.LifeElixir;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import static com.github.klyser8.mobmadness.util.MadUtils.isMMItem;

/**
 * Listener class for any events relating miscellaneous and consumable items.
 * Examples being the compass, BHG, dematerializer.
 */
public class ItemListener implements Listener {

    private final MobMadness plugin;
    public ItemListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemRightClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (!isMMItem(item)) return;
        Player player = event.getPlayer();
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        if (item.getType() == Material.BEETROOT_SOUP) {
            LifeElixir elixir = new LifeElixir(plugin, item.getAmount(), gameData);
            elixir.healthUp(player, item);
            event.setCancelled(true);
        }
        else if (item.getType() == Material.COOKIE) {
            Cookie cookie = new Cookie(plugin, item.getAmount(),gameData);
            cookie.eat(player, item);
            event.setCancelled(true);
        } else if (item.getType() == Material.TNT) {
            ThrowableTNT tnt = new ThrowableTNT(plugin, item.getAmount(), gameData);
            tnt.toss(player, item);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        if (item == null) return;
        NBTItem nbt = new NBTItem(item);
        Player player = event.getPlayer();
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        Profile profile = gameData.getPlayers().get(player);
        if (!isMMItem(item)) return;
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
        ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (type == ItemType.DETONATION_ORB) {
            Location trapLoc;
            if (clickedBlock.getType() == Material.FIRE_CORAL_FAN) return;
            //If the block is solid, place trap on top. Otherwise, replace block.
            if (clickedBlock.getType().isSolid()) {
                trapLoc = clickedBlock.getLocation().add(0.5, 1, 0.5);
                if (trapLoc.getBlock().getType() != Material.AIR) return;
            } else {
                trapLoc = clickedBlock.getLocation().add(0.5, 0, 0.5);
            }
            BlockData oldBlockData = trapLoc.getBlock().getBlockData();
            if (trapLoc.getBlock().isLiquid()) return;
            new DetonationOrb(plugin, 1, gameData).place(player, item, trapLoc, oldBlockData);
        } else if (type == ItemType.BARRICADE) {
            Location barricadeLoc;
            if (clickedBlock.getType().isSolid()) {
                barricadeLoc = event.getClickedBlock().getLocation().add(0, 1, 0);
                if (barricadeLoc.getBlock().getType() != Material.AIR &&
                        barricadeLoc.getBlock().getType() != type.getMaterial()) return;
            } else {
                barricadeLoc = clickedBlock.getLocation();
            }
            Barricade barricade = new Barricade(plugin, item.getAmount(), gameData);
            barricade.build(profile, item, barricadeLoc);
        } else if (type == ItemType.AETHERS_BERRIES) {
            AethersBerries berries = new AethersBerries(plugin, item.getAmount(), gameData);
            if (event.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
                berries.chargeAnchor(player, item, clickedBlock);
                event.setCancelled(true);
            }
        }
    }

    /*@EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        Block clickedBlock = event.getBlockAgainst();
        Location barricadeLoc;
        NBTItem nbt = new NBTItem(item);
        Player player = event.getPlayer();
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        MMProfile profile = gameData.getPlayers().get(player);
        if (!isMMItem(item)) return;
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
        ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
        if (type == ItemType.BARRICADE) {
            if (clickedBlock.getType().isSolid()) {
                barricadeLoc = event.getBlockPlaced().getLocation();
                if (barricadeLoc.getBlock().getType() != Material.AIR &&
                        barricadeLoc.getBlock().getType() != type.getMaterial()) return;
            } else {
                barricadeLoc = clickedBlock.getLocation();
            }
            Barricade barricade = new Barricade(plugin, item.getAmount(), gameData);
            barricade.build(profile, item, barricadeLoc);
        }
    }*/

    @EventHandler
    public void onFoodEat(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        Profile profile = gameData.getPlayers().get(player);
        if (!isMMItem(item)) return;
        NBTItem nbt = new NBTItem(item);
        ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
        if (type == ItemType.VEILING_POTION) {
            VeilingPotion potion = new VeilingPotion(plugin, item.getAmount(), gameData);
            potion.drink(profile, item);
            if (event.getReplacement() == null) return;
            event.setReplacement(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (!event.getFrom().getWorld().getName().equalsIgnoreCase("mobmadness")) return;
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) return;
        Player player = event.getPlayer();
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        new ChorusFruit(plugin, 1, gameData).handleTeleport(event);
    }

    @EventHandler
    public void onProjectileThrow(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        EnderPearl pearl = (EnderPearl) event.getEntity();
        if (!pearl.getWorld().getName().equalsIgnoreCase("mobmadness")) return;
        pearl.setMetadata(MadItem.MM_ENUM_TAG, new FixedMetadataValue(plugin, ItemType.VOID_ORB.toString()));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        EnderPearl pearl = (EnderPearl) event.getEntity();
        if (!pearl.hasMetadata(MadItem.MM_ENUM_TAG)) return;
        Player player = (Player) pearl.getShooter();
        if (player == null) return;
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        new VoidOrb(plugin, 1, gameData).dematerialize(pearl);

    }

}
