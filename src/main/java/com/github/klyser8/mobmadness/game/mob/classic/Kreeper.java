package com.github.klyser8.mobmadness.game.mob.classic;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.misc.Crystal;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.game.mob.Beast;
import com.github.klyser8.mobmadness.game.mob.BeastType;
import com.github.klyser8.mobmadness.game.mob.loot.LootTable;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;

public class Kreeper extends Beast {

    public Kreeper(MobMadness plugin, World world, GameData gameData) {
        super(plugin, BeastType.CREEPER, world, gameData);
        createLootTable();
    }

    @Override
    protected void createLootTable() {
        IronNugget ironNugget = new IronNugget(plugin, random.nextInt(3) + 1, gameData);
        Crystal crystal = new Crystal(plugin, 1, gameData);
        LootTable.LootTableBuilder builder = new LootTable.LootTableBuilder();
        builder.add(ironNugget.getItemStack(), 999)
                .add(crystal.getItemStack(), 1);
        lootTable = builder.build();
    }

    public void spawn(Location spawnLoc) {
        org.bukkit.entity.Creeper creeper = (org.bukkit.entity.Creeper) spawnEntity(spawnLoc, type.getHP());
        creeper.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(type.getDmg());
        creeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getHP());
        creeper.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(type.getSpeed());
        creeper.setMaxFuseTicks(30);
    }
}
