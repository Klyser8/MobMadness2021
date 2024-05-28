package com.github.klyser8.mobmadness.game.entity.beast.classic;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.misc.Crystal;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.beast.BeastType;
import com.github.klyser8.mobmadness.game.entity.beast.loot.LootTable;
import com.github.klyser8.mobmadness.storage.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Zombie;

public class Zomby extends Beast<Zombie> {

    public Zomby(MobMadness plugin, World world, Game game, Location spawnLoc) {
        super(plugin, BeastType.ZOMBIE, world, game, spawnLoc);
        createLootTable();
    }

    @Override
    protected void createLootTable() {
        IronNugget ironNugget = new IronNugget(plugin, random.nextInt(2) + 1, game);
        Crystal crystal = new Crystal(plugin, 1, game);
        LootTable.LootTableBuilder builder = new LootTable.LootTableBuilder();
        builder.add(ironNugget.getItemStack(), 999)
                .add(crystal.getItemStack(), 1);
        lootTable = builder.build();
    }

    @Override
    public void setup() {
        super.setup();
        mob.setShouldBurnInDay(false);
        mob.setAdult();
        mob.stopDrowning();
        mob.getEquipment().clear();
    }

}
