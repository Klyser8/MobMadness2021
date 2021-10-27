package com.github.klyser8.mobmadness.game.items;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import de.tr7zw.nbtapi.NBTItem;
import me.mattstudios.mf.base.components.MfUtil;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Abstract class which any item found in MobMadness extends.
 * Contains tag constants, which are used to store data of items.
 */
public abstract class MadItem {

    //GENERAL TAGS
    //NBT tag storing enum value for MM item
    public static final String MM_ENUM_TAG = "mm.enum";
    //What UUID the item contains.
    public static final String UUID_TAG = "mm.uuid";
    //Whether an item can or cannot be dropped.
    public static final String DROP_TAG = "mm.drop";
    //Whether the item can be 'consigned' to other players.
    public static final String CONSIGN_TAG = "mm.consign";
    //Whether the item is unbreakable.
    public static final String UNBREAKING_TAG = "mm.unbreakable";
    //What the price of the item is.
    public static final String PRICE_TAG = "mm.price";
    //Whether the item should persist after a game reset.
    public static final String PERSIST_TAG = "mm.persist";
    //What sound the item should make when picked up
    public static final String PICKUP_SOUND_TAG = "mm.pickup_sound";
    //Only one item with this tag can be present in an inventory.
    public static final String UNIQUE_TAG = "mm.unique";

    //Current durability of the item
    public static final String CURRENT_DURABILITY_TAG = "mm.current_durability";
    //Maximum durability of the item
    public static final String MAX_DURABILITY_TAG = "mm.max_durability";

    //SHOP TAGS

    //Any item with this tag will open the shop unique to the player
    public static final String SHOP_TAG = "mm.shop.open";
    //Whether the item clickable or not.
    public static final String CLICKABLE_TAG = "mm.shop.clickable";
    //Whether the item is on sale.
    public static final String ON_SALE_TAG = "mm.shop.on_sale";
    //Price for the item in iron nuggets.
    public static final String IRON_PRICE_TAG = "mm.shop.iron";
    //Price for the item in gold nuggets.
    public static final String GOLD_PRICE_TAG = "mm.shop.gold";

    //WEAPON TAGS

    public static final String DAMAGE_TAG = "mm.weapon.damage";

    protected final Sound errorSound;

    protected final NBTItem nbt;
    protected final MobMadness plugin;
    protected final GameData gameData;
    protected final ItemType type;

    protected MadItem(ItemType type, ItemStack item, MobMadness plugin, GameData gameData, boolean hideAttr) {
        this.type = type;
        nbt = new NBTItem(item);
        this.plugin = plugin;
        this.gameData = gameData;
        errorSound = new Sound("block.note_block.bass", 1.0f, 1.0f);
        if (hideAttr) {
            nbt.getItem().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        nbt.setString(MM_ENUM_TAG, type.toString());
    }

    /**
     * Abstract method child classes should override. Sets up the item so it's ready for use.
     */
    protected abstract void setup();

    /**
     * Gets the NBT of the item. Used to add any additional tags.
     *
     * @return {@link NBTItem}
     */
    public NBTItem getNbt() {
        return nbt;
    }

    public ItemStack getItemStack() {
        return nbt.getItem();
    }

    /**
     * Creates an exact copy of the item in the class.
     *
     * @return a copy of {@link #nbt#getItemStack()}
     */
    public ItemStack cloneItem() {
        return nbt.getItem().clone();
    }

    /**
     * Gets the type of item this is
     *
     * @return {@link ItemType}
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Gets the display name of the item, through the nbt tag.
     *
     * @return String, containing the display name of the item
     */
    public String getDisplayName() {
        return nbt.getItem().getItemMeta().getDisplayName();
    }

    /**
     * Changes the display name of the item to the provided string
     *
     * @param name the new display name
     */
    public void setDisplayName(String name) {
        ItemMeta meta = nbt.getItem().getItemMeta();
        meta.setDisplayName(name);
        nbt.getItem().setItemMeta(meta);
    }

    /**
     * Returns a list of strings, containing each line of the item's lore
     *
     * @return the lore of the item
     */
    public List<String> getLore() {
        return nbt.getItem().getItemMeta().getLore();
    }

    /**
     * Replaces the lore of the item to the provided one.
     * Additionally, it parses any color codes provided in each line with the use of
     * {@link MfUtil#color(String)}
     *
     * @param lore the new lore
     */
    public void setLore(List<String> lore) {
        ItemMeta meta = nbt.getItem().getItemMeta();
        List<String> coloredLore = new ArrayList<>();
        for (String line : lore) {
            coloredLore.add(color(line));
        }
        meta.setLore(coloredLore);
        nbt.getItem().setItemMeta(meta);
    }

    /**
     * Gets the enchantments found on the item
     *
     * @return a map containing the enchantments present, and their level
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return nbt.getItem().getItemMeta().getEnchants();
    }

    /**
     * Sets the current enchantments of the item.
     *
     * @param enchants a map containing enchantments and their respective level
     */
    public void setEnchantments(Map<Enchantment, Integer> enchants) {
        ItemMeta meta = nbt.getItem().getItemMeta();
        Map<Enchantment, Integer> currentEnchants = meta.getEnchants();
        for (Enchantment ench : currentEnchants.keySet()) {                              //Remove enchantments from meta
            meta.removeEnchant(ench);
        }
        for (Enchantment ench : enchants.keySet()) {                                          //Add enchantments to meta
            meta.addEnchant(ench, enchants.get(ench), true);
        }
        nbt.getItem().setItemMeta(meta);
    }

    /**
     * Sets NBT tags up for the item. These can be retrieved with new NBTItem(item).get...
     * @param drop Whether the item should be droppable or not
     * @param consign Whether the item can be given to another player through right-clicking.
     * @param persist Whether the item should not be removed from the inventory after a match.
     * @param unique Whether the item's tag can be present only once in the inventory.
     * @param tags Any additional String tag which may be useful.
     */
    protected void setTags(Boolean drop, Boolean consign, Boolean unbreaking, Boolean persist,
                           Boolean unique, @Nullable Map<String, String> tags) {
        nbt.setBoolean(DROP_TAG, drop);
        nbt.setBoolean(CONSIGN_TAG, consign);
        nbt.setBoolean(UNBREAKING_TAG, unbreaking);
        nbt.setBoolean(PERSIST_TAG, persist);
        nbt.setBoolean(UNIQUE_TAG, unique);

        if (tags == null) return;
        for (String tag : tags.keySet()) {
            nbt.setString(tag, tags.get(tag));
        }
    }

    /**
     * Plays an error sound and sends an error message related to the ultimate being on cooldown still.
     *
     * @param player who to send the error to
     */
    protected void cooldownError(Player player) {
        errorSound.play(player.getLocation(), SoundCategory.MASTER, player);
        player.sendMessage(color("&cYour ultimate is still on cooldown!"));
    }
}
