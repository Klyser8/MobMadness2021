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
import org.bukkit.entity.Spider;
import org.bukkit.potion.PotionEffect;

public class Spyder extends Beast {

    public Spyder(MobMadness plugin, World world, Game game) {
        super(plugin, BeastType.SPIDER, world, game);
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

    public void spawn(Location spawnLoc) {
        Spider spider = (Spider) spawnEntity(spawnLoc, type.getHP());
        spider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(type.getDmg());
        spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.getHP());
        spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(type.getSpeed());
        for (PotionEffect effect : spider.getActivePotionEffects()) {
            spider.removePotionEffect(effect.getType());
        }
//        spider.damage(0, MadUtils.pickRandomPlayer(gameData.getPlayers().keySet()));
    }

}
