package com.github.klyser8.mobmadness.ui;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.Beast;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import static com.github.klyser8.mobmadness.util.ColorHelper.gradient;
import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Util class with methods designed to display information on the screen
 * This can be through scoreboards, action bars, player list and boss bars.
 */
public class UIHelper {

    public static void displayMobHpBar(MobMadness plugin, Beast beast, Player player, double dmg) {
        LivingEntity entity = beast.getMob();
        //The entity's current hp (Rounded up)
        int currentHP = (int) Math.ceil(entity.getHealth() - dmg);
        if (currentHP < 0) {
            currentHP = 0;
        }
        //The entity's max hp
        int maxHP = (int) Math.ceil(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        //The entity's missing hp. lastDMG is also subtracted to allow for damage display.
        if (dmg > entity.getHealth()) {
            dmg = entity.getHealth();
        }
        int missingHP;
        if (entity.getHealth() == 0) {
            missingHP = maxHP;
        } else {
            missingHP = (int) Math.ceil(maxHP - currentHP - dmg);
        }
        String hpSymbol = "|";
        TextComponent currentHpBar = (TextComponent) gradient(StringUtils.repeat(hpSymbol, currentHP),
                TextColor.color(255, 0, 0), TextColor.color(255 - currentHP, 0, currentHP));
        Component entityName = entity.customName() == null ? entity.name() : entity.customName();
        TextComponent component = (TextComponent) entityName.append(Component.text(" - "))
                .append(Component.text("[").color(NamedTextColor.DARK_RED))
                .append(currentHpBar)
                .append(Component.text(StringUtils.repeat(hpSymbol, (int) Math.floor(dmg))).color(NamedTextColor.WHITE))
                .append(Component.text(StringUtils.repeat(hpSymbol, missingHP)).color(NamedTextColor.DARK_GRAY))
                .append(Component.text("]").color(NamedTextColor.DARK_RED));
        player.sendActionBar(component);

        if (!entity.isDead()) {
            //Copy into effectively final variable to allow for use in scheduler
            double finalDmg = dmg;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                TextComponent finalComponent = (TextComponent) entityName.append(Component.text(" - "))
                        .append(Component.text("[").color(NamedTextColor.DARK_RED))
                        .append(currentHpBar)
                        .append(Component.text(StringUtils.repeat(hpSymbol, missingHP +
                                (int) Math.floor(finalDmg))).color(NamedTextColor.DARK_GRAY))
                        .append(Component.text("]").color(NamedTextColor.DARK_RED));
                player.sendActionBar(finalComponent);
            }, 10);
        }
    }

}
