package com.github.klyser8.mobmadness.game.mob.classic;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.misc.Crystal;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.game.mob.MadMobType;
import com.github.klyser8.mobmadness.game.mob.loot.LootTable;
import com.github.klyser8.mobmadness.storage.GameData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

public class MadSkeleton extends MadEntity {

    public MadSkeleton(MobMadness plugin, World world, GameData gameData) {
        super(plugin, MadMobType.SKELETON, world, gameData);
        IronNugget ironNugget = new IronNugget(plugin, random.nextInt(3) + 1, gameData);
        Crystal crystal = new Crystal(plugin, 1, gameData);
        LootTable.LootTableBuilder builder = new LootTable.LootTableBuilder();
        builder.add(ironNugget.getItemStack(), 999)
                .add(crystal.getItemStack(), 1);
        lootTable = builder.build();
    }

    public void spawn(Location spawnLoc) {
        Skeleton skeleton = (Skeleton) spawnEntity(spawnLoc, type.getHP(), "Skeleton", ChatColor.of("#cccccc"));
        skeleton.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(type.getDmg());
        skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getHP());
        skeleton.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(type.getSpeed());
        skeleton.setShouldBurnInDay(false);
        if (skeleton.getEquipment().getItemInMainHand().getEnchantments().size() > 0) {
            ItemStack bow = skeleton.getEquipment().getItemInMainHand();
            for (Enchantment ench : skeleton.getEquipment().getItemInMainHand().getEnchantments().keySet()) {
                bow.removeEnchantment(ench);
            }
            skeleton.getEquipment().setItemInMainHand(bow);
        }
        if (skeleton.getEquipment().getArmorContents() == null) return;
        ItemStack[] items = {};
        skeleton.getEquipment().setArmorContents(items);
    }

}
