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
                damage = 4.0;
                radius = 1.5;
                projSpeed = 0.65;
                manaCost = 0.15f;
                cooldown = 20;

                ultDamage = 16;
                ultRadius = 4.5;
                ultProjSpeed = 1.75;
                ultManaCost = 0.725f;
                break;
            case ANCIENT_SCEPTER:
                damage = 6.0;
                radius = 1.6;
                projSpeed = 0.80;
                manaCost = 0.14f;
                cooldown = 18;

                ultDamage = 24;
                ultRadius = 5.0;
                ultProjSpeed = 1.90;
                ultManaCost = 0.700f;
                break;
            case MANASTEEL_SCEPTER:
                damage = 10.0;
                radius = 1.7;
                projSpeed = 0.95;
                manaCost = 0.13f;
                cooldown = 16;

                ultDamage = 40;
                ultRadius = 5.5;
                ultProjSpeed = 2.05;
                ultManaCost = 0.675f;
                break;
            case CRYSTALLINE_SCEPTER:
                damage = 15.0;
                radius = 1.8;
                projSpeed = 1.10;
                manaCost = 0.12f;
                cooldown = 14;

                ultDamage = 60;
                ultRadius = 6.0;
                ultProjSpeed = 2.20;
                ultManaCost = 0.650f;
                break;
            case INFERNAL_SCEPTER:
                damage = 21;
                radius = 1.9;
                projSpeed = 1.25;
                manaCost = 0.11f;
                cooldown = 12;

                ultDamage = 84;
                ultRadius = 6.5;
                ultProjSpeed = 2.35;
                ultManaCost = 0.625f;
                break;
            case ARMAGEDDON_SCEPTER:
                damage = 28;
                radius = 2.0;
                projSpeed = 1.40;
                manaCost = 0.10f;
                cooldown = 10;

                ultDamage = 112;
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
