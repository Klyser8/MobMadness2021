package com.github.klyser8.mobmadness.game.entity.beast.classic.jockey;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.misc.Crystal;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.game.entity.beast.BossBeast;
import com.github.klyser8.mobmadness.game.entity.beast.BeastType;
import com.github.klyser8.mobmadness.game.entity.beast.loot.LootTable;
import com.github.klyser8.mobmadness.storage.Game;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Jockey extends BossBeast {

    public Jockey(MobMadness plugin, World world, Game game) {
        super(plugin, BeastType.JOCKEY_SKELETON, world, game, Map.of(
                        EntityDamageEvent.DamageCause.FIRE, 0.1, EntityDamageEvent.DamageCause.FIRE_TICK, 0.1,
                        EntityDamageEvent.DamageCause.LAVA, 0.1, EntityDamageEvent.DamageCause.FALL, 0.0,
                        EntityDamageEvent.DamageCause.SUFFOCATION, 0.0),
                Map.of(), List.of());
        playerCount = game.getPlayers().size();
        createLootTable();
        mobRunnable = new JockeyRunnable(this, plugin, game);
        Bukkit.getScheduler().runTaskTimer(plugin, mobRunnable, 0, 1);
    }

    @Override
    protected void createLootTable() {
        int nuggetAmount = 5 + (5 * playerCount);
        IronNugget ironNugget = new IronNugget(plugin, random.nextInt(5) + nuggetAmount, game);
        Crystal crystal = new Crystal(plugin, 1, game);
        LootTable.LootTableBuilder builder = new LootTable.LootTableBuilder();
        builder.add(ironNugget.getItemStack(), 998)
                .add(crystal.getItemStack(), 2);
        lootTable = builder.build();
    }

    @Override
    protected void initializeBossBar() {
        bossBar = BossBar.bossBar(Component.text("Jockey").color(type.getNameColor()),
                0.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS,
                Set.of(BossBar.Flag.CREATE_WORLD_FOG, BossBar.Flag.DARKEN_SCREEN));
    }

    public void spawn(Location spawnLoc) {
        super.spawn(spawnLoc);
        Skeleton skeleton = (Skeleton) spawnEntity(spawnLoc, type.getHP() + ((playerCount - 1) * 25));
        skeleton.setShouldBurnInDay(false);
        if (skeleton.getEquipment().getItemInMainHand().getEnchantments().size() > 0) {
            ItemStack bow = skeleton.getEquipment().getItemInMainHand();
            for (Enchantment ench : skeleton.getEquipment().getItemInMainHand().getEnchantments().keySet()) {
                bow.removeEnchantment(ench);
            }
            bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
            skeleton.getEquipment().setItemInMainHand(bow);
        }
        if (skeleton.getEquipment().getArmorContents() == null) return;
        ItemStack[] items = {};
        skeleton.getEquipment().setArmorContents(items);
        skeleton.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
    }

    @Override
    public void updateBossBar() {
        bossBar.progress((float) (mob.getHealth() / mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
    }


}
