package com.github.klyser8.mobmadness.game.items.weapons.railgun;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.weapons.WeaponData;

public class RailgunData extends WeaponData {

    protected final ItemType type;
    private int chargeLevel = 0;

    private double piercePower;
    private int chargeTime;
    private int cooldown;
    private float chargePitch;

    private double ultDamage;
    private int ultMasking;
    private int ultMaskingDuration;
    private double ultWidth;
    private int ultCooldown;

    public RailgunData(Railgun weapon, ItemType type) {
        super(weapon);
        this.type = type;
        setup();
    }

    private void setup() {
        switch (type) {
            case SLOW_RAILGUN:
                damage = 8.0;
                piercePower = 0.65;
                chargeTime = 20;
                cooldown = 125;
                chargePitch = 1.0f;

                ultDamage = 4;
                ultMasking = 4;
                ultMaskingDuration = 200;
                ultWidth = 2.5;
                ultCooldown = 1400;
                break;
            case IMPROVED_RAILGUN:
                damage = 14.0;
                piercePower = 0.70;
                chargeTime = 19;
                cooldown = 120;
                chargePitch = 1.1f;

                ultDamage = 8;
                ultMasking = 6;
                ultMaskingDuration = 200;
                ultWidth = 3.0;
                ultCooldown = 1320;
                break;
            case TITANS_RAILGUN:
                damage = 22.0;
                piercePower = 0.75;
                chargeTime = 18;
                cooldown = 115;
                chargePitch = 1.2f;

                ultDamage = 14;
                ultMasking = 8;
                ultMaskingDuration = 200;
                ultWidth = 3.5;
                ultCooldown = 1240;
                break;
            case VIRTUOUS_RAILGUN:
                damage = 36.0;
                piercePower = 0.80;
                chargeTime = 17;
                cooldown = 110;
                chargePitch = 1.3f;

                ultDamage = 22;
                ultMasking = 10;
                ultMaskingDuration = 200;
                ultWidth = 4.0;
                ultCooldown = 1160;
                break;
            case ERUPTING_RAILGUN:
                damage = 58.0;
                piercePower = 0.85;
                chargeTime = 16;
                cooldown = 105;
                chargePitch = 1.4f;

                ultDamage = 32;
                ultMasking = 12;
                ultMaskingDuration = 200;
                ultWidth = 4.5;
                ultCooldown = 1080;
                break;
            case JUDGEMENT_RAILGUN:
                damage = 88;
                piercePower = 0.90;
                chargeTime = 15;
                cooldown = 100;
                chargePitch = 1.5f;

                ultDamage = 44;
                ultMasking = 12;
                ultMaskingDuration = 200;
                ultWidth = 5.0;
                ultCooldown = 1000;
                break;
        }
    }

    public float getChargePitch() {
        return chargePitch;
    }

    public int getUltMaskingDuration() {
        return ultMaskingDuration;
    }

    public double getPiercePower() {
        return piercePower;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    public int getCooldown() {
        return cooldown;
    }

    public double getUltDamage() {
        return ultDamage;
    }

    public int getUltMasking() {
        return ultMasking;
    }

    public double getUltWidth() {
        return ultWidth;
    }

    public int getUltCooldown() {
        return ultCooldown;
    }

    public int getChargeLevel() {
        return chargeLevel;
    }

    public void setChargeLevel(int chargeLevel) {
        this.chargeLevel = chargeLevel;
    }
}
