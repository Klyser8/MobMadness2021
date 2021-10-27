package com.github.klyser8.mobmadness.game.items.weapons;

public abstract class WeaponData {

    protected final MadWeapon weapon;
    protected double damage;

    public WeaponData(MadWeapon weapon) {
        this.weapon = weapon;
    }

    public double getDamage() {
        return damage;
    }
}
