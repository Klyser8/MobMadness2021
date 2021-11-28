package com.github.klyser8.mobmadness.ui;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.Soul;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.beast.BossBeast;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import static com.github.klyser8.mobmadness.util.ColorHelper.gradient;
import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.Component.text;

/**
 * Util class with methods designed to display information on the screen
 * This can be through scoreboards, action bars, player list and boss bars.
 */
public class UIHelper {

    public static final String HP_SYMBOL = "|";


    /**
     * Creates two components displaying two different HP bars.
     * The first, displays the Soul's current health + the last damage taken.
     * The second, just returns the Soul's current health.
     * If the hit entity is a boss, the health bar is not displayed as bosses use a {@link BossBar} themselves.
     *
     * @param soul the soul whose HP should be displayed
     * @param dmg the damage dealt to the beast
     * @param friendly whether the hp bar displayed is for a friendly entity or not.
     * @return An array containing two components
     */
    @SuppressWarnings("ConstantConditions")
    public static Component[] createHpBar(Soul soul, double dmg, boolean friendly) {
        if (soul instanceof BossBeast) return null;
        final Component[] bars = new Component[2];
        LivingEntity entity = soul.getLivingEntity();
        int currentHP = (int) Math.max(Math.ceil(entity.getHealth() - dmg), 0);
        int maxHP = (int) Math.ceil(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        int runicHP = (int) Math.ceil(entity.getAbsorptionAmount());
        //The entity's missing hp. lastDMG is also subtracted to allow for damage display.
        dmg = Math.min(dmg, entity.getHealth());
        //If entity's HP is 0, missingHP = maxHp
        int missingHP = entity.getHealth() == 0 ? maxHP : (int) Math.ceil(maxHP - currentHP - dmg);
        if (friendly) {
            currentHP = (int) Math.floor(currentHP / 2.0);
            missingHP = (int) Math.ceil(missingHP / 2.0);
            runicHP = (int) Math.ceil(runicHP / 2.0);
        }
        if (friendly) {
            TextColor color = NamedTextColor.GREEN;
            if (entity.isInvulnerable()) {
                color = TextColor.color(255, 251, 209);
            } else if (entity.getFireTicks() > 0) {
                color = TextColor.color(Math.min(color.red() + 100, 255), Math.max(color.green() - 100, 0), color.blue());
            }
            if (runicHP == 0) {
                bars[0] = text("<").color(NamedTextColor.DARK_GREEN)
                        .append(text(StringUtils.repeat(HP_SYMBOL, currentHP)).color(color))
                        .append(text(StringUtils.repeat(HP_SYMBOL, missingHP)).color(NamedTextColor.DARK_GRAY))
                        .append(text(">").color(NamedTextColor.DARK_GREEN));
            } else {
                bars[0] = text("<").color(NamedTextColor.DARK_GREEN)
                        .append(text(StringUtils.repeat(HP_SYMBOL, currentHP)).color(color))
                        .append(text(StringUtils.repeat(HP_SYMBOL, runicHP)).color(NamedTextColor.GOLD))
                        .append(text(StringUtils.repeat(HP_SYMBOL, Math.max(0, missingHP - runicHP))).color(NamedTextColor.DARK_GRAY))
                        .append(text(">").color(NamedTextColor.DARK_GREEN));
            }
        } else {
            //Add hp bar + dmg indicator
            Component entityName = entity.customName() == null ? entity.name() : entity.customName();
            bars[0] = entityName.append(text(" - "))
                    .append(text("[").color(NamedTextColor.DARK_RED))
                    .append(gradient(StringUtils.repeat(HP_SYMBOL, currentHP),
                            TextColor.color(255, 0, 0), TextColor.color(255 - currentHP, 0, currentHP)))
                    .append(text(StringUtils.repeat(HP_SYMBOL, (int) Math.floor(dmg))).color(NamedTextColor.WHITE))
                    .append(text(StringUtils.repeat(HP_SYMBOL, missingHP)).color(NamedTextColor.DARK_GRAY))
                    .append(text("]").color(NamedTextColor.DARK_RED));

            //Add hp bar
            bars[1] = entityName.append(text(" - "))
                    .append(text("[").color(NamedTextColor.DARK_RED))
                    .append(gradient(StringUtils.repeat(HP_SYMBOL, currentHP),
                            TextColor.color(255, 0, 0), TextColor.color(255 - currentHP, 0, currentHP)))
                    .append(text(StringUtils.repeat(HP_SYMBOL, missingHP +
                            (int) Math.floor(dmg))).color(NamedTextColor.DARK_GRAY))
                    .append(text("]").color(NamedTextColor.DARK_RED));
        }
        return bars;
    }


    /*public static Component[] createHpBar(MobMadness plugin, Soul soul, double dmg, boolean showName,
                                          @Nullable TextColor hpColor) {
        if (soul instanceof BossBeast) return null;
        final Component[] bars = new Component[2];
        LivingEntity entity = soul.getLivingEntity();
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
        TextComponent currentHpBar;
        if (hpColor == null) {
            currentHpBar = (TextComponent) gradient(StringUtils.repeat(hpSymbol, currentHP),
                    TextColor.color(255, 0, 0), TextColor.color(255 - currentHP, 0, currentHP));
        } else {
            currentHpBar = text(StringUtils.repeat(hpSymbol, currentHP)).color(hpColor);
        }
        TextComponent dmgDisplayComponent = text("");
        Component entityName = entity.customName() == null ? entity.name() : entity.customName();
        if (showName) {
            if (entityName != null) {
                dmgDisplayComponent = (TextComponent) entityName.append(text(" - "));
            }
        }

        dmgDisplayComponent = dmgDisplayComponent.append(text("[").color(NamedTextColor.DARK_RED))
                .append(currentHpBar)
                .append(text(StringUtils.repeat(hpSymbol, (int) Math.floor(dmg))).color(NamedTextColor.WHITE))
                .append(text(StringUtils.repeat(hpSymbol, missingHP)).color(NamedTextColor.DARK_GRAY))
                .append(text("]").color(NamedTextColor.DARK_RED));
        bars[0] = dmgDisplayComponent;
//        System.out.println(bars[0]);
//        player.sendActionBar(dmgDisplayComponent);

        if (!entity.isDead()) {
            TextComponent finalHpComponent = text("");
            if (showName) {
                if (entityName != null) {
                    finalHpComponent = (TextComponent) entityName.append(text(" - "));
                }
            }
            finalHpComponent = finalHpComponent.append(text("[").color(NamedTextColor.DARK_RED))
                    .append(currentHpBar)
                    .append(text(StringUtils.repeat(hpSymbol, missingHP +
                            (int) Math.floor(dmg))).color(NamedTextColor.DARK_GRAY))
                    .append(text("]").color(NamedTextColor.DARK_RED));
            bars[1] = finalHpComponent;
//                player.sendActionBar(finalHpComponent);
        }
        return bars;
    }*/
}
