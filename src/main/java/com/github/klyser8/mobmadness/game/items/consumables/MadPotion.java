package com.github.klyser8.mobmadness.game.items.consumables;

import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * Interface which any potion in MobMadness should extend.
 * The methods below listed are necessary to make a proper potion ItemStack.
 */
public interface MadPotion {

    void createPotionMeta();

    PotionMeta getPotionMeta();

    List<PotionEffect> getPotionEffects();

}
