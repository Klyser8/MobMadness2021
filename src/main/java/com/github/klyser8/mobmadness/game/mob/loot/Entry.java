package com.github.klyser8.mobmadness.game.mob.loot;

import org.bukkit.inventory.ItemStack;

public class Entry {

    private ItemStack item;
    private int weight;
    private double chance;

    public Entry(ItemStack item, int weight) {
        this.item = item;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public double getChance() {
        return chance;
    }

    public ItemStack getItem() {
        return item.clone();
    }
}
