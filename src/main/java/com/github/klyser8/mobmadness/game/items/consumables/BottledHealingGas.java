package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        setCompLore(Arrays.asList(
                Component.text("Creates a healing area in the").color(NamedTextColor.GRAY),
                Component.text("location it is thrown, healing").color(NamedTextColor.GRAY),
                Component.text("up to ").color(NamedTextColor.GRAY)
                        .append(Component.text("15 ")).color(NamedTextColor.GRAY)
                        .append(Component.text("\u2764")).color(NamedTextColor.RED)
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
