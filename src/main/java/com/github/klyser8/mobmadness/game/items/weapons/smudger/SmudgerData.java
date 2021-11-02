package com.github.klyser8.mobmadness.game.items.weapons.smudger;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.weapons.WeaponData;

public class SmudgerData extends WeaponData {

    protected final ItemType type;

    private int pellets;
    private int pelletSpread;
    private int pelletRange;
    private int cooldown;

    private int rgb;
    private double knockback;

    private double ultDamage;
    private int ultPellets;
    private int longCooldown;

    public SmudgerData(Smudger weapon, ItemType type) {
        super(weapon);
        this.type = type;
        setup();
    }

    private void setup() {
        switch (type) {
            case DEFECTIVE_SMUDGER:
                damage = 0.4;
                pellets = 20;
                pelletSpread = 30;
                pelletRange = 30;
                cooldown = 50;

                rgb = 100;
                knockback = 0.025;

                ultDamage = 0.75;
                ultPellets = 25;
                longCooldown = 160;
                break;
            case PLEASANT_SMUDGER:
                damage = 0.6;
                pellets = 20;
                pelletSpread = 30;
                pelletRange = 32;
                cooldown = 47;

                rgb = 80;
                knockback = 0.025;

                ultDamage = 1.2;
                ultPellets = 25;
                longCooldown = 156;
                break;
            case SILVER_PLATED_SMUDGER:
                damage = 0.9;
                pellets = 20;
                pelletSpread = 30;
                pelletRange = 34;
                cooldown = 44;
                rgb = 60;

                ultDamage = 1.95;
                ultPellets = 25;
                longCooldown = 152;
                break;
            case LUSTROUS_SMUDGER:
                damage = 1.3;
                pellets = 20;
                pelletSpread = 30;
                pelletRange = 36;
                cooldown = 41;

                rgb = 40;
                knockback = 0.025;

                ultDamage = 3.0;
                ultPellets = 25;
                longCooldown = 148;
                break;
            case DRACONIC_SMUDGER:
                damage = 1.8;
                pellets = 20;
                pelletSpread = 30;
                pelletRange = 38;
                cooldown = 38;

                rgb = 20;
                knockback = 0.025;

                ultDamage = 4.8;
                ultPellets = 25;
                longCooldown = 144;
                break;
            case POSSESSED_SMUDGER:
                damage = 2.4;
                pellets = 20;
                pelletSpread = 30;
                pelletRange = 40;
                cooldown = 35;

                rgb = 0;
                knockback = 0.025;

                ultDamage = 7.5;
                ultPellets = 25;
                longCooldown = 140;
                break;
        }
    }

    public int getPellets() {
        return pellets;
    }

    public int getPelletSpread() {
        return pelletSpread;
    }

    public int getPelletRange() {
        return pelletRange;
    }

    public int getCooldown() {
        return cooldown;
    }

    public double getUltDamage() {
        return ultDamage;
    }

    public int getUltPellets() {
        return ultPellets;
    }

    public int getLongCooldown() {
        return longCooldown;
    }

    public int getRgb() {
        return rgb;
    }

    public double getKnockback() {
        return knockback;
    }

    public void setKnockback(double knockback) {
        this.knockback = knockback;
    }
}
