package com.github.klyser8.mobmadness.game.shop;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.misc.Compass;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.game.items.weapons.bow.Bow;
import com.github.klyser8.mobmadness.game.items.weapons.crossbow.Crossbow;
import com.github.klyser8.mobmadness.game.items.weapons.rayler.Rayler;
import com.github.klyser8.mobmadness.game.items.weapons.scepter.Scepter;
import com.github.klyser8.mobmadness.game.items.weapons.smudger.Smudger;
import com.github.klyser8.mobmadness.game.items.weapons.sword.Sword;
import com.github.klyser8.mobmadness.game.items.weapons.trider.Trider;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
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
        Game game = MadUtils.getEntityGame(plugin, player);
        if (game == null) return;
        Profile profile = game.getPlayers().get(player);
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
        Game game = MadUtils.getEntityGame(plugin, player);
        if (game == null) return;
        Profile profile = game.getPlayers().get(player);
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
            case OLD_SWORD, STONE_SWORD, BROADSWORD, BEAM_SWORD, GLEAMING_SWORD, EXECUTIONERS_SWORD
                    -> profile.setCurrentWeapon(new Sword(type, plugin, game));
            case FRAIL_BOW, SOLID_BOW, REINFORCED_BOW, PULSE_BOW, BLAZING_BOW, SHADOW_BOW
                    -> profile.setCurrentWeapon(new Bow(type, plugin, game));
            case FRAIL_CROSSBOW, STEADY_CROSSBOW, ENHANCED_CROSSBOW, BLASTING_CROSSBOW, FIERY_CROSSBOW,
                    CERBERUS_CROSSBOW
                    -> profile.setCurrentWeapon(new Crossbow(type, plugin, game));
            case WOODEN_SCEPTER, ANCIENT_SCEPTER, MANASTEEL_SCEPTER, CRYSTALLINE_SCEPTER, INFERNAL_SCEPTER,
                    ARMAGEDDON_SCEPTER
                    -> profile.setCurrentWeapon(new Scepter(type, plugin, game));
            case SLOW_RAYLER, IMPROVED_RAYLER, TITANS_RAYLER, VIRTUOUS_RAYLER, ERUPTING_RAYLER, JUDGEMENT_RAYLER
                    -> profile.setCurrentWeapon(new Rayler(type, plugin, game));
            case DEFECTIVE_SMUDGER, PLEASANT_SMUDGER, SILVER_PLATED_SMUDGER, LUSTROUS_SMUDGER, DRACONIC_SMUDGER,
                    POSSESSED_SMUDGER
                    -> profile.setCurrentWeapon(new Smudger(type, plugin, game));
            case IMPRUDENT_TRIDER, LOYAL_TRIDER, FERROUS_TRIDER, ANGELIC_TRIDER, HELLISH_TRIDER, MANIC_TRIDER
                    -> profile.setCurrentWeapon(new Trider(type, plugin, game));
        }
        profile.setXPMana(profile.getCurrentWeapon().getType().toString().contains("SCEPTER"));
        profile.stopCooldown();
    }

    /**
     * Checks if the player has an item with the weapon NBT tag.
     *
     * @param player the player to check
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
