package com.github.klyser8.mobmadness.game.items.weapons.crossbow;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.weapons.WeaponData;
import com.github.klyser8.mobmadness.util.Ench;
import org.bukkit.enchantments.Enchantment;

public class CrossbowData extends WeaponData {

    protected final ItemType type;

    private double highDamage;
    private Ench loadEnch;
    private Ench pierceEnch;
    private Ench multiShot;

    private double ultDamage;
    private int ultPierceLevel;
    private int ultCooldown;

    public CrossbowData(Crossbow weapon, ItemType type) {
        super(weapon);
        this.type = type;
        setup();

    }

    private void setup() {
        switch (type) {
            case FRAIL_CROSSBOW -> {
                damage = 4.0;
                highDamage = 5.0;
                ultDamage = 7.0;
                ultPierceLevel = 3;
                ultCooldown = 400;
            }
            case STEADY_CROSSBOW -> {
                damage = 5.0;
                highDamage = 7.0;
                loadEnch = new Ench(Enchantment.QUICK_CHARGE, 1);
                pierceEnch = new Ench(Enchantment.PIERCING, 1);
                ultDamage = 9.5;
                ultPierceLevel = 5;
                ultCooldown = 380;
            }
            case ENHANCED_CROSSBOW -> {
                damage = 6.0;
                highDamage = 9.0;
                loadEnch = new Ench(Enchantment.QUICK_CHARGE, 1);
                pierceEnch = new Ench(Enchantment.PIERCING, 2);
                ultDamage = 12.0;
                ultPierceLevel = 7;
                ultCooldown = 360;
            }
            case BLASTING_CROSSBOW -> {
                damage = 8.0;
                highDamage = 11.0;
                loadEnch = new Ench(Enchantment.QUICK_CHARGE, 2);
                pierceEnch = new Ench(Enchantment.PIERCING, 3);
                ultDamage = 15.0;
                ultPierceLevel = 9;
                ultCooldown = 340;
            }
            case FIERY_CROSSBOW -> {
                damage = 11.0;
                highDamage = 15.0;
                loadEnch = new Ench(Enchantment.QUICK_CHARGE, 2);
                pierceEnch = new Ench(Enchantment.PIERCING, 4);
                ultDamage = 20.5;
                ultPierceLevel = 11;
                ultCooldown = 320;
            }
            case CERBERUS_CROSSBOW -> {
                damage = 15.0;
                highDamage = 19.0;
                loadEnch = new Ench(Enchantment.QUICK_CHARGE, 3);
                pierceEnch = new Ench(Enchantment.PIERCING, 5);
                multiShot = new Ench(Enchantment.MULTISHOT, 1);
                ultDamage = 26.5;
                ultPierceLevel = 13;
                ultCooldown = 300;
            }
        }
    }

    public double getHighDamage() {
        return highDamage;
    }

    public Ench getLoadEnch() {
        return loadEnch;
    }

    public Ench getPierceEnch() {
        return pierceEnch;
    }

    public Ench getMultiShot() {
        return multiShot;
    }

    public double getUltDamage() {
        return ultDamage;
    }

    public int getUltPierceLevel() {
        return ultPierceLevel;
    }

    public int getUltCooldown() {
        return ultCooldown;
    }
}
