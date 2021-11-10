package com.github.klyser8.mobmadness.game.entity.beast;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.Tier;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Kreeper;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Scheleton;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Spyder;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Zomby;
import com.github.klyser8.mobmadness.game.entity.beast.classic.jockey.Jockey;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;


public enum BeastType {
    ZOMBIE(EntityType.ZOMBIE, Component.text("Zombie"), Tier.I.getTierColor(),
            12, 2, 0.25, 1),
    SPIDER(EntityType.SPIDER, Component.text("Spider"), Tier.I.getTierColor(),
            10, 2, 0.27, 2),
    CREEPER(EntityType.CREEPER, Component.text("Creeper"), Tier.I.getTierColor(),
            8, 10, 0.22, 2),
    SKELETON(EntityType.SKELETON, Component.text("Skeleton"), Tier.I.getTierColor(),
            6, 1, 0.2, 3),
    JOCKEY_SKELETON(EntityType.SKELETON, Component.text("Jockey Skeleton"), ColorHelper.LIGHT_GRAY,
            50, 3, 0.26, 15);


    private final EntityType type;
    private final TextComponent nameComp;
    private final TextColor nameColor;
    private final double hp;
    private final double dmg;
    private final double speed;
    private final int weight;
    BeastType(EntityType type, TextComponent nameComp, TextColor nameColor,
              double hp, double dmg, double speed, int weight) {
        this.type = type;
        this.nameComp = nameComp;
        this.nameColor = nameColor;
        this.hp = hp;
        this.dmg = dmg;
        this.speed = speed;
        this.weight = weight;
    }

    public static Beast createMob(BeastType type, World world, Game game) {
        return switch (type) {
            case ZOMBIE -> new Zomby(JavaPlugin.getPlugin(MobMadness.class), world, game);
            case SPIDER -> new Spyder(JavaPlugin.getPlugin(MobMadness.class), world, game);
            case CREEPER -> new Kreeper(JavaPlugin.getPlugin(MobMadness.class), world, game);
            case SKELETON -> new Scheleton(JavaPlugin.getPlugin(MobMadness.class), world, game);
            case JOCKEY_SKELETON -> new Jockey(JavaPlugin.getPlugin(MobMadness.class), world, game);
        };
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

    public TextComponent getNameComp() {
        return nameComp;
    }

    public TextColor getNameColor() {
        return nameColor;
    }
}
