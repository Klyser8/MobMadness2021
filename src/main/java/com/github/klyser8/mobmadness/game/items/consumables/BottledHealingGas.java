package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class BottledHealingGas extends MadItem implements MadPotion {

    protected final int price;
    public BottledHealingGas(MobMadness plugin, int price, GameData gameData) {
        super(ItemType.BHG, new ItemStack(ItemType.BHG.getMaterial(), 1), plugin, gameData, true);
        this.price = price;
        setup();
    }

    @Override
    protected void setup() {
        createPotionMeta();
        setDisplayName(color("&aBottled Healing Gas (B.H.G.)"));
        setLore(Arrays.asList(
                "&7Creates a healing area in the",
                "&7location it is thrown, healing",
                "&7up to &f15 &c\u2764"
        ));
        setTags(true, false, true,
                false, false, null);
    }

    @Override
    public void createPotionMeta() {
        PotionMeta meta = (PotionMeta) getItemStack().getItemMeta();
        meta.addCustomEffect(new PotionEffect(
                PotionEffectType.HEAL, 0, 2, false, true, false), true);
        meta.setColor(Color.fromRGB(255, 0, 157));
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
