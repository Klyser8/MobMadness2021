package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ItemGlow;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class VeilingPotion extends MadItem {

    public static final int RUNIC_HEART_AMOUNT = 4;
    public static final int COOLDOWN = 600;

    private final Sound drinkSound;
    public VeilingPotion(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.VEILING_POTION, new ItemStack(ItemType.VEILING_POTION.getMaterial(), amount), plugin, gameData, true);
        drinkSound = new Sound("block.enchantment_table.use", 1.0f, 1.0f);
        ItemGlow glow = new ItemGlow(new NamespacedKey(plugin, ItemGlow.key));
        Map<Enchantment, Integer> enchants = new HashMap<>();
        enchants.put(glow, 1);
        setEnchantments(enchants);
        setup();
    }

    protected void setup() {
        setLore(Arrays.asList(
                "&7Generates&f " + RUNIC_HEART_AMOUNT / 2 + "&6 \u2764&7 upon",
                "&7consumption."
        ));

        setTags(true, false, true, false, false, null);
    }

    public void drink(Profile profile, ItemStack potion) {
        Player player = profile.getPlayer();
        player.setAbsorptionAmount(player.getAbsorptionAmount() + RUNIC_HEART_AMOUNT);
        drinkSound.play(player.getLocation(), SoundCategory.PLAYERS);
        player.setCooldown(potion.getType(), COOLDOWN);
    }
}
