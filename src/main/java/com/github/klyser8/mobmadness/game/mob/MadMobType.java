package com.github.klyser8.mobmadness.game.mob;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.classic.MadCreeper;
import com.github.klyser8.mobmadness.game.mob.classic.MadSkeleton;
import com.github.klyser8.mobmadness.game.mob.classic.MadSpider;
import com.github.klyser8.mobmadness.game.mob.classic.MadZombie;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public enum MadMobType {
    ZOMBIE(EntityType.ZOMBIE, 12, 2, 0.25, 1),
    SPIDER(EntityType.SPIDER, 10, 2, 0.27, 1),
    CREEPER(EntityType.CREEPER, 8, 10, 0.22, 2),
    SKELETON(EntityType.SKELETON, 6, 1, 0.2, 2);


    private final EntityType type;
    private final double hp;
    private final double dmg;
    private final double speed;
    private final int weight;
    MadMobType(EntityType type, double hp, double dmg, double speed, int weight) {
        this.type = type;
        this.hp = hp;
        this.dmg = dmg;
        this.speed = speed;
        this.weight = weight;
    }

    public static MadEntity createMob(MadMobType type, World world, GameData gameData) {
        switch (type) {
            case ZOMBIE:
            default: return new MadZombie(JavaPlugin.getPlugin(MobMadness.class), world, gameData);
            case SPIDER: return new MadSpider(JavaPlugin.getPlugin(MobMadness.class), world, gameData);
            case CREEPER: return new MadCreeper(JavaPlugin.getPlugin(MobMadness.class), world, gameData);
            case SKELETON: return new MadSkeleton(JavaPlugin.getPlugin(MobMadness.class), world, gameData);
        }
    }

    public EntityType getEntityType() {
        return type;
    }

    public double getHP() {
        return hp;
    }

    public double getDmg() {
        return dmg;
    }

    public double getSpeed() {
        return speed;
    }

    public int getWeight() {
        return weight;
    }
}
