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
import org.bukkit.attribute.Attribute;

public class Kreeper extends Beast {

    public Kreeper(MobMadness plugin, World world, Game game) {
        super(plugin, BeastType.CREEPER, world, game);
        createLootTable();
    }

    @Override
    protected void createLootTable() {
        IronNugget ironNugget = new IronNugget(plugin, random.nextInt(3) + 1, game);
        Crystal crystal = new Crystal(plugin, 1, game);
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
