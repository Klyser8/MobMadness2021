package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.consumables.MadPotion;
import com.github.klyser8.mobmadness.storage.GameData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class HealthPotion extends MadItem implements MadPotion {

    public HealthPotion(MobMadness plugin, GameData gameData) {
        super(ItemType.HEALTH_POTION, new ItemStack(ItemType.HEALTH_POTION.getMaterial(), 1), plugin, gameData, true);
        setup();
    }

    @Override
    protected void setup() {
        createPotionMeta();
        setLore(Arrays.asList(
                "&7Heals you and nearby allies",
                "&7by up to &f2 &c\u2764",
                "&8--------------------------------",
                "&8&oAmount healed based on distance",
                "&8&ofrom the potion."
        ));
        setTags(true, false, true,
                false, false, null);
    }

    @Override
    public void createPotionMeta() {
        PotionMeta meta = (PotionMeta) getItemStack().getItemMeta();
        meta.addCustomEffect(new PotionEffect(
                PotionEffectType.HEAL, 0, 0, false, true, false), true);
        meta.setColor(Color.fromRGB(255, 0, 0));
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        nbt.getItem().setItemMeta(meta);
    }

    @Override
    public PotionMeta getPotionMeta() {
        return (PotionMeta) getItemStack().getItemMeta();
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return getPotionMeta().getCustomEffects();
    }
}
