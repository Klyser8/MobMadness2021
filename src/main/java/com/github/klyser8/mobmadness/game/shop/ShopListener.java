package com.github.klyser8.mobmadness.game.shop;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.misc.Compass;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.game.items.weapons.bow.Bow;
import com.github.klyser8.mobmadness.game.items.weapons.crossbow.Crossbow;
import com.github.klyser8.mobmadness.game.items.weapons.railgun.Railgun;
import com.github.klyser8.mobmadness.game.items.weapons.scepter.Scepter;
import com.github.klyser8.mobmadness.game.items.weapons.smudger.Smudger;
import com.github.klyser8.mobmadness.game.items.weapons.sword.Sword;
import com.github.klyser8.mobmadness.game.items.weapons.trider.Trider;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.util.MadUtils;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.github.klyser8.mobmadness.util.MadUtils.isMMItem;

public class ShopListener implements Listener {

    private final MobMadness plugin;
    public ShopListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
//        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        Player player = event.getPlayer();
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        Profile profile = gameData.getPlayers().get(player);
        if (profile == null) return;
        ItemStack item = event.getItem();
        if (item == null) return;
        if (!isMMItem(item)) return;
        NBTItem nbt = new NBTItem(item);
        if (nbt.hasKey(Compass.OPEN_SHOP_TAG)) {
            profile.openPlayerShop();
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) return;
        ItemStack clickedItem = event.getCurrentItem();
        ItemStack boughtItem = null;
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        GameData gameData = MadUtils.getPlayerGame(plugin, player);
        if (gameData == null) return;
        Profile profile = gameData.getPlayers().get(player);
        if (profile == null) return;
        NBTItem nbt = new NBTItem(clickedItem);
        if (!nbt.hasKey(MadItem.CLICKABLE_TAG)) return;
        if (!nbt.getBoolean(MadItem.CLICKABLE_TAG)) {
            event.setCancelled(true);
            //Checks if the player currently has a weapon. If they do, before giving the player the new weapon
            //The old one is removed.
            if (nbt.getBoolean(MadWeapon.WEAPON_TAG)) {
                int weaponSlot = playerHasWeapon(player);
                if (weaponSlot != -1) {
                    player.getInventory().setItem(weaponSlot, null);
                }
            }
            boughtItem = profile.getPlayerShop().purchaseItem(event.getSlot());
        }
        if (boughtItem == null) return;
        if (!nbt.getBoolean(MadWeapon.WEAPON_TAG)) return;
        ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
        switch (type) {
            case OLD_SWORD:
            case STONE_SWORD:
            case BROADSWORD:
            case BEAM_SWORD:
            case GLEAMING_SWORD:
            case EXECUTIONERS_SWORD:
                profile.setCurrentWeapon(new Sword(type, boughtItem, plugin, gameData));
                break;
            case FRAIL_BOW:
            case SOLID_BOW:
            case REINFORCED_BOW:
            case PULSE_BOW:
            case BLAZING_BOW:
            case SHADOW_BOW:
                profile.setCurrentWeapon(new Bow(type, boughtItem, plugin, gameData));
                break;
            case FRAIL_CROSSBOW:
            case STEADY_CROSSBOW:
            case ENHANCED_CROSSBOW:
            case BLASTING_CROSSBOW:
            case FIERY_CROSSBOW:
            case CERBERUS_CROSSBOW:
                profile.setCurrentWeapon(new Crossbow(type, boughtItem, plugin, gameData));
                break;
            case WOODEN_SCEPTER:
            case ANCIENT_SCEPTER:
            case MANASTEEL_SCEPTER:
            case CRYSTALLINE_SCEPTER:
            case INFERNAL_SCEPTER:
            case ARMAGEDDON_SCEPTER:
                profile.setCurrentWeapon(new Scepter(type, boughtItem, plugin, gameData));
                break;
            case SLOW_RAILGUN:
            case IMPROVED_RAILGUN:
            case TITANS_RAILGUN:
            case VIRTUOUS_RAILGUN:
            case ERUPTING_RAILGUN:
            case JUDGEMENT_RAILGUN:
                profile.setCurrentWeapon(new Railgun(type, boughtItem, plugin, gameData));
                break;
            case DEFECTIVE_SMUDGER:
            case PLEASANT_SMUDGER:
            case SILVER_PLATED_SMUDGER:
            case LUSTROUS_SMUDGER:
            case DRACONIC_SMUDGER:
            case POSSESSED_SMUDGER:
                profile.setCurrentWeapon(new Smudger(type, boughtItem, plugin, gameData));
                break;
            case IMPRUDENT_TRIDER:
            case LOYAL_TRIDER:
            case FERROUS_TRIDER:
            case ANGELIC_TRIDER:
            case HELLISH_TRIDER:
            case MANIC_TRIDER:
                profile.setCurrentWeapon(new Trider(type, boughtItem, plugin, gameData));
                break;
        }
        profile.setXPMana(profile.getCurrentWeapon().getType().toString().contains("SCEPTER"));
        profile.stopCooldown();
    }

    /**
     * Checks if the player has an item with the weapon NBT tag.
     *
     * @param player
     * @return -1 if the player doesn't, otherwise returns the slot which the wepaon was found in.
     */
    private int playerHasWeapon(Player player) {
        int weaponSlot = -1;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack loopItem = player.getInventory().getItem(i);
            if (loopItem == null) continue;
            NBTItem loopNBT = new NBTItem(loopItem);
            if (loopNBT.getBoolean(MadWeapon.WEAPON_TAG)) {
                weaponSlot = i;
                break;
            }
        }
        return weaponSlot;
    }

}
