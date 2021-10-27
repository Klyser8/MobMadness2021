package com.github.klyser8.mobmadness.game.items.weapons.sword;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.weapons.WeaponData;

public class SwordData extends WeaponData {

    protected final ItemType type;

    private double critDamage;

    private double ultHealing;
    private double ultRadius;
    private int ultDelay;
    private int ultCooldown;

    public SwordData(Sword weapon, ItemType type) {
        super(weapon);
        this.type = type;
        setup();
    }

    private void setup() {
        switch (type) {
            case OLD_SWORD:
                damage = 3.0;
                critDamage = 4.5;
                ultHealing = 6.0;
                ultRadius = 2.5;
                ultDelay = 40;
                ultCooldown = 700;
                break;
            case STONE_SWORD:
                damage = 4.0;
                critDamage = 6.0;
                ultHealing = 9.0;
                ultRadius = 3.0;
                ultDelay = 40;
                ultCooldown = 660;
                break;
            case BROADSWORD:
                damage = 6.0;
                critDamage = 9.0;
                ultHealing = 12.0;
                ultRadius = 3.5;
                ultDelay = 40;
                ultCooldown = 620;
                break;
            case BEAM_SWORD:
                damage = 9.0;
                critDamage = 13.5;
                ultHealing = 15.0;
                ultRadius = 4.0;
                ultDelay = 40;
                ultCooldown = 580;
                break;
            case GLEAMING_SWORD:
                damage = 13.0;
                critDamage = 19.5;
                ultHealing = 18.0;
                ultRadius = 4.5;
                ultDelay = 40;
                ultCooldown = 540;
                break;
            case EXECUTIONERS_SWORD:
                damage = 18.0;
                critDamage = 27.0;
                ultHealing = 21.0;
                ultRadius = 5.0;
                ultDelay = 40;
                ultCooldown = 500;
                break;
        }
    }

    public ItemType getType() {
        return type;
    }

    public double getCritDamage() {
        return critDamage;
    }

    public double getUltHealing() {
        return ultHealing;
    }

    public double getUltRadius() {
        return ultRadius;
    }

    public int getUltDelay() {
        return ultDelay;
    }

    public int getUltCooldown() {
        return ultCooldown;
    }
}
