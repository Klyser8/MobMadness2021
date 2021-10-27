package com.github.klyser8.mobmadness.game.items.weapons.sword;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.game.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Sword extends MadItem implements MadWeapon {

    private SwordData data;

    public static final String CRIT_DAMAGE_TAG = "mm.crit_damage";
    public static final String ULT_HEALING_TAG = "mm.ult.healing";
    public static final String ULT_RADIUS_TAG = "mm.ult.radius";
    public static final String ULT_DELAY_TAG = "mm.ult.delay";
    public static final String ULT_COOLDOWN_TAG = "mm.ult.cooldown";

    public Sword(ItemType type, ItemStack item, MobMadness plugin, GameData gameData) {
        super(type, item, plugin, gameData, true);
        setup();
    }

    @Override
    protected void setup() {
        createData(this);

        setTags(false, false, true,
                false, true, null);
        setDisplayName(color(type.getName()));
        createLore();
        nbt.setBoolean(WEAPON_TAG, true);
        nbt.setDouble(DAMAGE_TAG, data.getDamage());
        nbt.setDouble(CRIT_DAMAGE_TAG, data.getCritDamage());
        nbt.setDouble(ULT_HEALING_TAG, data.getUltHealing());
        nbt.setDouble(ULT_RADIUS_TAG, data.getUltRadius());
        nbt.setInteger(ULT_DELAY_TAG, data.getUltDelay());
        nbt.setInteger(ULT_COOLDOWN_TAG, data.getUltCooldown());
    }

    private void createLore() {
        List<String> lore = new ArrayList<>();
        lore.add("&7Damage: &f" + data.getDamage());
        lore.add("&7Crit. Damage: &f" + data.getCritDamage());
        lore.add("&8--------------------------------");
        lore.add("&5Shift + Right-Click:&f A beam appears");
        lore.add("&fat your location, healing players near");
        lore.add("&fit after a delay.");
        lore.add("");
        lore.add("&dTotal Healing: &f" + data.getUltHealing() + "&c \u2764");
        lore.add("&dRadius: &f" + data.getUltRadius() + " blocks");
        lore.add("&dDelay: &f" + data.getUltDelay() / 20 + "s");
        lore.add("&dCooldown: &f" + data.getUltCooldown() / 20 + "s");
        setLore(lore);
    }

    @Override
    public void createData(MadItem item) {
        data = new SwordData(this, type);
    }

    public void healingBeam(Profile profile) {
        Player player = profile.getPlayer();
        if (profile.isOnCooldown()) {
            cooldownError(profile.getPlayer());
            return;
        }
        new SwordUltRunnable(plugin, player, data).runTaskTimer(plugin, 0, 1);
        profile.startCooldown(data.getUltCooldown());
    }

    @Override
    public ItemType getType() {
        return type;
    }
}
