package com.github.klyser8.mobmadness.game.items.weapons.scepter;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.weapons.WeaponData;

public class ScepterData extends WeaponData {

    protected final ItemType type;

    private double radius;
    private double projSpeed;
    private float manaCost;
    private int cooldown;

    private double ultDamage;
    private double ultRadius;
    private double ultProjSpeed;
    private float ultManaCost;

    public ScepterData(Scepter weapon, ItemType type) {
        super(weapon);
        this.type = type;
        setup();
    }

    private void setup() {
        switch (type) {
            case WOODEN_SCEPTER:
                damage = 3.0;
                radius = 1.5;
                projSpeed = 0.65;
                manaCost = 0.2f;
                cooldown = 20;

                ultDamage = 12;
                ultRadius = 4.5;
                ultProjSpeed = 1.75;
                ultManaCost = 0.725f;
                break;
            case ANCIENT_SCEPTER:
                damage = 5.0;
                radius = 1.6;
                projSpeed = 0.80;
                manaCost = 0.19f;
                cooldown = 18;

                ultDamage = 20;
                ultRadius = 5.0;
                ultProjSpeed = 1.90;
                ultManaCost = 0.700f;
                break;
            case MANASTEEL_SCEPTER:
                damage = 8.0;
                radius = 1.7;
                projSpeed = 0.95;
                manaCost = 0.18f;
                cooldown = 16;

                ultDamage = 32;
                ultRadius = 5.5;
                ultProjSpeed = 2.05;
                ultManaCost = 0.675f;
                break;
            case CRYSTALLINE_SCEPTER:
                damage = 12;
                radius = 1.8;
                projSpeed = 1.10;
                manaCost = 0.17f;
                cooldown = 14;

                ultDamage = 48;
                ultRadius = 6.0;
                ultProjSpeed = 2.20;
                ultManaCost = 0.650f;
                break;
            case INFERNAL_SCEPTER:
                damage = 17;
                radius = 1.9;
                projSpeed = 1.25;
                manaCost = 0.16f;
                cooldown = 12;

                ultDamage = 68;
                ultRadius = 6.5;
                ultProjSpeed = 2.35;
                ultManaCost = 0.625f;
                break;
            case ARMAGEDDON_SCEPTER:
                damage = 23;
                radius = 2.0;
                projSpeed = 1.40;
                manaCost = 0.15f;
                cooldown = 10;

                ultDamage = 92;
                ultRadius = 7.0;
                ultProjSpeed = 2.50;
                ultManaCost = 0.600f;
                break;
        }
    }

    public double getRadius() {
        return radius;
    }

    public double getProjSpeed() {
        return projSpeed;
    }

    public float getManaCost() {
        return manaCost;
    }

    public int getCooldown() {
        return cooldown;
    }

    public double getUltDamage() {
        return ultDamage;
    }

    public double getUltRadius() {
        return ultRadius;
    }

    public double getUltProjSpeed() {
        return ultProjSpeed;
    }

    public float getUltManaCost() {
        return ultManaCost;
    }
}
