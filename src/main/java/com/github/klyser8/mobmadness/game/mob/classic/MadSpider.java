package com.github.klyser8.mobmadness.game.mob.classic;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.misc.Crystal;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.game.mob.MadMobType;
import com.github.klyser8.mobmadness.game.mob.loot.LootTable;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class MadSpider extends MadEntity {

    public MadSpider(MobMadness plugin, World world, GameData gameData) {
        super(plugin, MadMobType.SPIDER, world, gameData);
        createLootTable();
    }

    @Override
    protected void createLootTable() {
        IronNugget ironNugget = new IronNugget(plugin, random.nextInt(2) + 1, gameData);
        Crystal crystal = new Crystal(plugin, 1, gameData);
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
