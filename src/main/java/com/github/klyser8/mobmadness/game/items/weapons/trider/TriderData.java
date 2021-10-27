package com.github.klyser8.mobmadness.game.items.weapons.trider;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.weapons.WeaponData;

public class TriderData extends WeaponData {

    protected final ItemType type;

    private double rangedDamage;
    private int loyaltyWait;
    private double range; //80 blocks default

    private int ultDazzleMaxDuration;
    private double ultTideBreakerRadius;
    private int ultCooldown;

    public TriderData(Trider weapon, ItemType type) {
        super(weapon);
        this.type = type;
        setup();
    }

    private void setup() {
        switch (type) {
            case IMPRUDENT_TRIDER:
                damage = 4.0;
                rangedDamage = 5;
                loyaltyWait = 100;
                range = 30;

                ultDazzleMaxDuration = 100;
                ultTideBreakerRadius = 2.5;
                ultCooldown = 600;
                break;
            case LOYAL_TRIDER:
                damage = 5;
                rangedDamage = 6.5;
                loyaltyWait = 90;
                range = 32.5;

                ultDazzleMaxDuration = 120;
                ultTideBreakerRadius = 3.0;
                ultCooldown = 570;
                break;
            case FERROUS_TRIDER:
                damage = 6.5;
                rangedDamage = 8.5;
                loyaltyWait = 80;
                range = 35.0;

                ultDazzleMaxDuration = 140;
                ultTideBreakerRadius = 3.5;
                ultCooldown = 540;
                break;
            case ANGELIC_TRIDER:
                damage = 8.5;
                rangedDamage = 11;
                loyaltyWait = 70;
                range = 37.5;

                ultDazzleMaxDuration = 160;
                ultTideBreakerRadius = 4.0;
                ultCooldown = 510;
                break;
            case HELLISH_TRIDER:
                damage = 11;
                rangedDamage = 14;
                loyaltyWait = 60;
                range = 40;

                ultDazzleMaxDuration = 180;
                ultTideBreakerRadius = 4.5;
                ultCooldown = 480;
                break;
            case MANIC_TRIDER:
                damage = 14;
                rangedDamage = 17.5;
                loyaltyWait = 50;
                range = 42.5;

                ultDazzleMaxDuration = 200;
                ultTideBreakerRadius = 5.0;
                ultCooldown = 450;
                break;
        }
    }

    public double getRangedDamage() {
        return rangedDamage;
    }

    public int getLoyaltyWait() {
        return loyaltyWait;
    }

    public double getRange() {
        return range;
    }

    public double getUltDazzleMaxDuration() {
        return ultDazzleMaxDuration;
    }

    public double getUltTideBreakerRadius() {
        return ultTideBreakerRadius;
    }

    public int getUltCooldown() {
        return ultCooldown;
    }

    public ItemType getType() {
        return type;
    }
}
