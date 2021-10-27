package com.github.klyser8.mobmadness;

import com.github.klyser8.mobmadness.game.items.misc.Compass;
import com.github.klyser8.mobmadness.game.items.MadItem;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener for random events/tests.
 */
public class RandomListener implements Listener {

    private final MobMadness plugin;
    public RandomListener(MobMadness plugin) {
        this.plugin = plugin;
    }

/*    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.COMPASS) {
            Compass compass = new Compass(plugin);
            compass.openShop(event);
        } else if (item.getType() == Material.CLOCK) {
            Compass compass = new Compass(plugin);
            player.getInventory().addItem(compass.cloneItem());
        }
    }*/

    /*@EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.BLAZE_ROD) return;
        for (EntityType type : EntityType.values()) {
            if (!type.isSpawnable()) continue;
            if (!type.isAlive()) continue;
            Entity entity = world.spawnEntity(world.getSpawnLocation(), type);
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity mob = (LivingEntity) entity;
            if (mob.getAttribute(Attribute.GENERIC_ARMOR) == null) continue;
            System.out.println(mob.getAttribute(Attribute.GENERIC_ARMOR).getValue() + ": " + type.toString());
        }
    }*/

/*    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (!victim.hasMetadata("mad")) return;
        plugin.getGameManager().getMobMap().get(victim).createLootTable();
    }*/

}
