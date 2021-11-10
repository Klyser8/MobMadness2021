package com.github.klyser8.mobmadness.game.items.weapons.bow;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.weapons.WeaponData;

public class BowData extends WeaponData {

    protected final ItemType type;

    private double highDamage;

    private int ultMarkDuration;
    private int ultCooldown;

    public BowData(Bow weapon, ItemType type) {
        super(weapon);
        this.type = type;
        setup();
    }

    private void setup() {
        switch (type) {
            case FRAIL_BOW -> {
                damage = 3.0;
                highDamage = 5.0;
                ultMarkDuration = 200;
                ultCooldown = 800;
            }
            case SOLID_BOW -> {
                damage = 6.0;
                highDamage = 10.0;
                ultMarkDuration = 240;
                ultCooldown = 740;
            }
            case REINFORCED_BOW -> {
                damage = 11.0;
                highDamage = 16.0;
                ultMarkDuration = 280;
                ultCooldown = 680;
            }
            case PULSE_BOW -> {
                damage = 18.0;
                highDamage = 25.0;
                ultMarkDuration = 320;
                ultCooldown = 620;
            }
            case BLAZING_BOW -> {
                damage = 26.0;
                highDamage = 35.0;
                ultMarkDuration = 360;
                ultCooldown = 560;
            }
            case SHADOW_BOW -> {
                damage = 35.0;
                highDamage = 46.0;
                ultMarkDuration = 400;
                ultCooldown = 500;
            }
        }
    }

    /**
     * Gets the highest possible damage the Bow can deal.
     *
     * @return {@link #highDamage}
     */
    public double getHighDamage() {
        return highDamage;
    }

    /**
     * Gets the duration of the mark on an enemy.
     *
     * @return {@link #ultMarkDuration}
     */
    public int getUltMarkDuration() {
        return ultMarkDuration;
    }

    /**
     * Gets the cooldown of the ultimate ability.
     *
     * @return {@link #ultCooldown}
     */
    public int getUltCooldown() {
        return ultCooldown;
    }
}
