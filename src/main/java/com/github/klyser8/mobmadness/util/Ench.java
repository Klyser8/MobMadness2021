package com.github.klyser8.mobmadness.util;

import org.bukkit.enchantments.Enchantment;


/**
 * Util class to store enchantment data.
 */
public class Ench {

    private final Enchantment enchantment;
    private final int level;
    public Ench(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    /**
     * Gets the current enchantment.
     * @return instance of {@link Enchantment}
     */
    public Enchantment getEnchantment() {
        return enchantment;
    }

    /**
     * Gets the current level of the enchantment
     * @return the level of the enchantment (int)
     */
    public int getLevel() {
        return level;
    }
}
