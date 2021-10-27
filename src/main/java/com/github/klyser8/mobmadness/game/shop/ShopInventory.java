package com.github.klyser8.mobmadness.game.shop;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.Tier;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.consumables.*;
import com.github.klyser8.mobmadness.game.items.equipment.Boots;
import com.github.klyser8.mobmadness.game.items.equipment.Chestplate;
import com.github.klyser8.mobmadness.game.items.equipment.Helmet;
import com.github.klyser8.mobmadness.game.items.equipment.Leggings;
import com.github.klyser8.mobmadness.game.items.equipment.shield.Shield;
import com.github.klyser8.mobmadness.game.items.menu.BlankItem;
import com.github.klyser8.mobmadness.game.items.misc.*;
import com.github.klyser8.mobmadness.game.items.weapons.bow.Bow;
import com.github.klyser8.mobmadness.game.items.weapons.crossbow.Crossbow;
import com.github.klyser8.mobmadness.game.items.weapons.railgun.Railgun;
import com.github.klyser8.mobmadness.game.items.weapons.scepter.Scepter;
import com.github.klyser8.mobmadness.game.items.weapons.smudger.Smudger;
import com.github.klyser8.mobmadness.game.items.weapons.sword.Sword;
import com.github.klyser8.mobmadness.game.items.weapons.trider.Trider;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import de.tr7zw.nbtapi.NBTItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class ShopInventory {

    //Item prices (in iron nuggets)

    //Misc
    public static final int IRON_NUGGET_VALUE = 1;
    public static final int GOLD_NUGGET_VALUE = 64;
    public static final int CRYSTAL_VALUE = 4096;
    //Consumables
    public static final int LIFE_ELIXIR_VALUE = 4; //times 2 per additional heart container //TODO price should reset once player dies
    public static final int AETHER_BERRIES_VALUE = 16; //see formula below
    public static final int HEALTH_POTION_VALUE = 4;
    public static final int VEILING_POTION_VALUE = 10; //TODO remove empty bottle after consuming
    public static final int ARROWS_VALUE = 1; //2 Arrows //TODO when a thrown arrow is picked up, it loses all NBT
    public static final int RUNIC_ARROW_VALUE = 1; //1 Arrow

    public static final int COOKIE_VALUE = 1;
    public static final int TOTEM_OF_UNDYING_VALUE = 128;
    public static final int THROWABLE_TNT_VALUE = 12;
    public static final int BHG_VALUE = 32;
    public static final int CHORUS_FRUIT_VALUE = 16;
    public static final int ENDER_AID_VALUE = 16;
    public static final int VOID_ORB_VALUE = 12;
    public static final int BARRICADE_VALUE = 10;
    public static final int DETONATION_ORB_VALUE = 12;
    //Swords
    public static final int OLD_SWORD_VALUE = 16;
    public static final int STONE_SWORD_VALUE = 32;
    public static final int BROADSWORD_VALUE = 64;
    public static final int BEAM_SWORD_VALUE = 128;
    public static final int GLEAMING_SWORD_VALUE = 256;
    public static final int EXECUTIONERS_SWORD_VALUE = 512;
    //Shields
    public static final int CROSS_SHIELD_VALUE = 16;
    public static final int MOSSY_SHIELD_VALUE = 32;
    public static final int PRISMARINE_SHIELD_VALUE = 64;
    public static final int PINK_LOTUS_SHIELD_VALUE = 128;
    public static final int GLORIOUS_SHIELD_VALUE = 256;
    public static final int WITHERS_SHIELD_VALUE = 512;
    //Bows
    public static final int FRAIL_BOW_VALUE = 16;
    public static final int SOLID_BOW_VALUE = 32;
    public static final int REINFORCED_BOW_VALUE = 64;
    public static final int PULSE_BOW_VALUE = 128;
    public static final int BLAZING_BOW_VALUE = 256;
    public static final int SHADOW_BOW_VALUE = 512;
    //Crossbows
    public static final int FRAIL_CROSSBOW_VALUE = 16;
    public static final int STEADY_CROSSBOW_VALUE = 32;
    public static final int ENHANCED_CROSSBOW_VALUE = 64;
    public static final int BLASTING_CROSSBOW_VALUE = 128;
    public static final int FIERY_CROSSBOW_VALUE = 256;
    public static final int CERBERUS_CROSSBOW_VALUE = 512;
    //Scepters
    public static final int WOODEN_SCEPTER_VALUE = 32;
    public static final int ANCIENT_SCEPTER_VALUE = 64;
    public static final int MANASTEEL_SCEPTER_VALUE = 128;
    public static final int CRYSTALLINE_SCEPTER_VALUE = 256;
    public static final int INFERNAL_SCEPTER_VALUE = 512;
    public static final int ARMAGEDDON_SCEPTER_VALUE = 1024;
    //Railguns
    public static final int SLOW_RAILGUN_VALUE = 32;
    public static final int IMPROVED_RAILGUN_VALUE = 64;
    public static final int TITANS_RAILGUN_VALUE = 128;
    public static final int VIRTUOUS_RAILGUN_VALUE = 256;
    public static final int ERUPTING_RAILGUN_VALUE = 512;
    public static final int JUDGEMENT_RAILGUN_VALUE = 1024;
    //Smudgers
    public static final int DEFECTIVE_SMUDGER_VALUE = 32;
    public static final int PLEASANT_SMUDGER_VALUE = 64;
    public static final int SILVER_PLATED_SMUDGER_VALUE = 128;
    public static final int LUSTROUS_SMUDGER_VALUE = 256;
    public static final int DRACONIC_SMUDGER_VALUE = 512;
    public static final int POSSESSED_SMUDGER_VALUE = 1024;
    //Tridents
    public static final int IMPRUDENT_TRIDER_VALUE = 24;
    public static final int LOYAL_TRIDER_VALUE = 48;
    public static final int FERROUS_TRIDER_VALUE = 96;
    public static final int ANGELIC_TRIDER_VALUE = 192;
    public static final int HELLISH_TRIDER_VALUE = 384;
    public static final int MANIC_TRIDER_VALUE = 768;

    //Helmets & Boots
    public static final int LEATHER_SMALL = 6;
    public static final int LEATHER_MAIN = 12;
    public static final int CHAINMAIL_SMALL = 12;
    public static final int CHAINMAIL_MAIN = 24;
    public static final int IRON_SMALL = 24;
    public static final int IRON_MAIN = 48;
    public static final int DIAMOND_SMALL = 64;
    public static final int DIAMOND_MAIN = 128;
    public static final int NETHERITE_SMALL = 160;
    public static final int NETHERITE_MAIN = 320;

    private final Inventory shop;
    private final MobMadness plugin;
    private final GameData gameData;
    private final Profile profile;

    private final Random random;

    private Map<Integer, ItemStack> weapons;
    private Map<Integer, ItemStack> armors;
    private Map<ItemType, MadItem> consumables;

    private final MadItem blankItem;
    private final Sound brokeSound;
    private final Sound purchaseSound;

    public ShopInventory(MobMadness plugin, GameData gameData, Profile profile) {
        this.plugin = plugin;
        this.gameData = gameData;
        this.profile = profile;
        random = new Random();
        consumables = new HashMap<>();
        Tier tier = gameData.getTier();
        TextComponent invComp = Component.text("Tier ").color(tier.getTierColor())
                .append(tier.getTierComp().decoration(TextDecoration.BOLD, true)
                .append(Component.text(" shop").color(tier.getTierColor())
                        .decoration(TextDecoration.BOLD, false)));
        shop = Bukkit.createInventory(null, 54, invComp);
        blankItem = new BlankItem(plugin, gameData);
        brokeSound = new Sound("entity.item.break", 0.5f, 2.0f);
        purchaseSound = new Sound("block.note_block.pling", 0.5f, 2.0f);
        populateWeapons();
        populateConsumables();
        populateArmor();
        createContents();
    }

    private void createContents() {
        //Add weapons to the shop
        for (int i : weapons.keySet()) {
            shop.setItem(i, weapons.get(i));
        }
        //Add armors to the shop
        for (int i : armors.keySet()) {
            shop.setItem(i, armors.get(i));
        }
        //Add consumables to the shop
        shop.setItem(22, createShopItem(new HealthPotion(plugin, gameData)));
        shop.setItem(33, createShopItem(new LifeElixir(plugin, 1, gameData)));
        shop.setItem(42, createShopItem(new AethersBerries(plugin, 1, gameData)));
        shop.setItem(44, createShopItem(new Arrows(plugin, 8, gameData)));
        //Add random consumables
        int slot = 17;
        for (MadItem item : consumables.values()) {
            shop.setItem(slot, createShopItem(item));
            slot += 9;
        }
        //Add armors to the shop

        //Add OTHER to the shop
        shop.setItem(15, createShopItem(new IronNugget(plugin, 64, gameData)));
        shop.setItem(24, createShopItem(new GoldNugget(plugin, 1, gameData)));
        for (int i = 0; i < shop.getSize(); i++) {
            if (shop.getItem(i) == null) {
                shop.setItem(i, blankItem.getItemStack());
            }
        }
    }

    /**
     * Purchases an item from the shop.
     *
     * @param slot the slot the item is in
     * @return the ItemStack purchased. Returns null if the purchase failed or if the item clicked isn't on sale.
     */
    public ItemStack purchaseItem(int slot) {
        Player player = profile.getPlayer();
        //Take nuggets away from player
        ItemStack item = shop.getItem(slot);
        if (item == null) return null;
        NBTItem nbt = new NBTItem(item);
        int ironPrice = nbt.getInteger(MadItem.IRON_PRICE_TAG);
        int goldPrice = nbt.getInteger(MadItem.GOLD_PRICE_TAG);
        if (!takeNuggets(ironPrice, goldPrice)) {
            brokeSound.play(player.getLocation(), SoundCategory.MASTER, player);
            return null;
        }
        purchaseSound.play(player.getLocation(), SoundCategory.PLAYERS, player);
        //If the item is a life elixir, change the price of the following elixir.
        if (ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG)) == ItemType.LIFE_ELIXIR) {
            profile.setHealthUpAmount(profile.getHealthUpAmount() + 1);
            shop.setItem(slot, createShopItem(new LifeElixir(plugin, 1, gameData)));
        }
        //If the item is Aether's Berries, change the price of the following berries.
        if (ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG)) == ItemType.AETHERS_BERRIES) {
            shop.setItem(slot, createShopItem(new AethersBerries(plugin, 1, gameData)));
        }
        //Give the item to the player
        item = prepareItem(item);
        if (item == null) return null;
        if (!player.getInventory().addItem(item).isEmpty()) {
            brokeSound.play(player.getLocation(), SoundCategory.MASTER, player);
            player.sendMessage(English.ERROR_INVENTORY_FULL.getComponent());
        }
        return item;
    }

    public Inventory getShopInventory() {
        return shop;
    }

    private ItemStack createShopItem(MadItem item) {
        NBTItem nbt = item.getNbt();
        nbt.setBoolean(MadItem.ON_SALE_TAG, true);
        nbt.setBoolean(MadItem.CLICKABLE_TAG, false);
        int ironPrice = 0;
        int goldPrice = 0;
        switch (item.getType()) {
            case IRON_NUGGET:
//                amount = 64;
                ironPrice = IRON_NUGGET_VALUE;
                break;
            case GOLD_NUGGET:
                ironPrice = GOLD_NUGGET_VALUE;
                break;
            case HEALTH_POTION:
                ironPrice = HEALTH_POTION_VALUE;
                break;
            case LIFE_ELIXIR:
                ironPrice = calculateLifeElixirPrice();
                break;
            case AETHERS_BERRIES:
                ironPrice = calculateAetherBerriesPrice();
                break;
            case CRYSTAL:
                ironPrice = CRYSTAL_VALUE;
                break;
            case ARROWS:
                ironPrice = ARROWS_VALUE;
                break;

            case VOID_ORB:
                ironPrice = VOID_ORB_VALUE - (item.getItemStack().getAmount() - 1);
                break;
            case ENDER_AID:
                ironPrice = ENDER_AID_VALUE;
                break;
            case BHG:
                ironPrice = BHG_VALUE;
                break;
            case TOTEM_OF_UNDYING:
                ironPrice = TOTEM_OF_UNDYING_VALUE;
                break;
            case RUNIC_ARROW:
                ironPrice = RUNIC_ARROW_VALUE;
                break;
            case COOKIE:
                ironPrice = COOKIE_VALUE;
                break;
            case VEILING_POTION:
                ironPrice = VEILING_POTION_VALUE - (item.getItemStack().getAmount() - 1);
                break;
            case THROWABLE_TNT:
                ironPrice = THROWABLE_TNT_VALUE - (item.getItemStack().getAmount() - 1);
                break;
            case CHORUS_FRUIT:
                ironPrice = CHORUS_FRUIT_VALUE;
                break;
            case BARRICADE:
                ironPrice = BARRICADE_VALUE;
                break;
            case DETONATION_ORB:
                ironPrice = DETONATION_ORB_VALUE - (item.getItemStack().getAmount() - 1);
                break;

            case OLD_SWORD:
                ironPrice = OLD_SWORD_VALUE;
                break;
            case STONE_SWORD:
                ironPrice = STONE_SWORD_VALUE;
                break;
            case BROADSWORD:
                ironPrice = BROADSWORD_VALUE;
                break;
            case BEAM_SWORD:
                ironPrice = BEAM_SWORD_VALUE;
                break;
            case GLEAMING_SWORD:
                ironPrice = GLEAMING_SWORD_VALUE;
                break;
            case EXECUTIONERS_SWORD:
                ironPrice = EXECUTIONERS_SWORD_VALUE;
                break;
            case FRAIL_BOW:
                ironPrice = FRAIL_BOW_VALUE;
                break;
            case SOLID_BOW:
                ironPrice = SOLID_BOW_VALUE;
                break;
            case REINFORCED_BOW:
                ironPrice = REINFORCED_BOW_VALUE;
                break;
            case PULSE_BOW:
                ironPrice = PULSE_BOW_VALUE;
                break;
            case BLAZING_BOW:
                ironPrice = BLAZING_BOW_VALUE;
                break;
            case SHADOW_BOW:
                ironPrice = SHADOW_BOW_VALUE;
                break;
            case FRAIL_CROSSBOW:
                ironPrice = FRAIL_CROSSBOW_VALUE;
                break;
            case STEADY_CROSSBOW:
                ironPrice = STEADY_CROSSBOW_VALUE;
                break;
            case ENHANCED_CROSSBOW:
                ironPrice = ENHANCED_CROSSBOW_VALUE;
                break;
            case BLASTING_CROSSBOW:
                ironPrice = BLASTING_CROSSBOW_VALUE;
                break;
            case FIERY_CROSSBOW:
                ironPrice = FIERY_CROSSBOW_VALUE;
                break;
            case CERBERUS_CROSSBOW:
                ironPrice = CERBERUS_CROSSBOW_VALUE;
                break;
            case WOODEN_SCEPTER:
                ironPrice = WOODEN_SCEPTER_VALUE;
                break;
            case ANCIENT_SCEPTER:
                ironPrice = ANCIENT_SCEPTER_VALUE;
                break;
            case MANASTEEL_SCEPTER:
                ironPrice = MANASTEEL_SCEPTER_VALUE;
                break;
            case CRYSTALLINE_SCEPTER:
                ironPrice = CRYSTALLINE_SCEPTER_VALUE;
                break;
            case INFERNAL_SCEPTER:
                ironPrice = INFERNAL_SCEPTER_VALUE;
                break;
            case ARMAGEDDON_SCEPTER:
                ironPrice = ARMAGEDDON_SCEPTER_VALUE;
                break;
            case SLOW_RAILGUN:
                ironPrice = SLOW_RAILGUN_VALUE;
                break;
            case IMPROVED_RAILGUN:
                ironPrice = IMPROVED_RAILGUN_VALUE;
                break;
            case TITANS_RAILGUN:
                ironPrice = TITANS_RAILGUN_VALUE;
                break;
            case VIRTUOUS_RAILGUN:
                ironPrice = VIRTUOUS_RAILGUN_VALUE;
                break;
            case ERUPTING_RAILGUN:
                ironPrice = ERUPTING_RAILGUN_VALUE;
                break;
            case JUDGEMENT_RAILGUN:
                ironPrice = JUDGEMENT_RAILGUN_VALUE;
                break;
            case DEFECTIVE_SMUDGER:
                ironPrice = DEFECTIVE_SMUDGER_VALUE;
                break;
            case PLEASANT_SMUDGER:
                ironPrice = PLEASANT_SMUDGER_VALUE;
                break;
            case SILVER_PLATED_SMUDGER:
                ironPrice = SILVER_PLATED_SMUDGER_VALUE;
                break;
            case LUSTROUS_SMUDGER:
                ironPrice = LUSTROUS_SMUDGER_VALUE;
                break;
            case DRACONIC_SMUDGER:
                ironPrice = DRACONIC_SMUDGER_VALUE;
                break;
            case POSSESSED_SMUDGER:
                ironPrice = POSSESSED_SMUDGER_VALUE;
                break;
            case IMPRUDENT_TRIDER:
                ironPrice = IMPRUDENT_TRIDER_VALUE;
                break;
            case LOYAL_TRIDER:
                ironPrice = LOYAL_TRIDER_VALUE;
                break;
            case FERROUS_TRIDER:
                ironPrice = FERROUS_TRIDER_VALUE;
                break;
            case ANGELIC_TRIDER:
                ironPrice = ANGELIC_TRIDER_VALUE;
                break;
            case HELLISH_TRIDER:
                ironPrice = HELLISH_TRIDER_VALUE;
                break;
            case MANIC_TRIDER:
                ironPrice = MANIC_TRIDER_VALUE;
                break;
            case CROSS_SHIELD:
                ironPrice = CROSS_SHIELD_VALUE;
                break;
            case MOSSY_SHIELD:
                ironPrice = MOSSY_SHIELD_VALUE;
                break;
            case PRISMARINE_SHIELD:
                ironPrice = PRISMARINE_SHIELD_VALUE;
                break;
            case PINK_LOTUS_SHIELD:
                ironPrice = PINK_LOTUS_SHIELD_VALUE;
                break;
            case GLORIOUS_SHIELD:
                ironPrice = GLORIOUS_SHIELD_VALUE;
                break;
            case WITHERS_SHIELD:
                ironPrice = WITHERS_SHIELD_VALUE;
                break;
            case LEATHER_HELMET:
            case LEATHER_BOOTS:
                ironPrice = LEATHER_SMALL;
                break;
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
                ironPrice = LEATHER_MAIN;
                break;
            case CHAINMAIL_HELMET:
            case CHAINMAIL_BOOTS:
                ironPrice = CHAINMAIL_SMALL;
                break;
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
                ironPrice = CHAINMAIL_MAIN;
                break;
            case IRON_HELMET:
            case IRON_BOOTS:
                ironPrice = IRON_SMALL;
                break;
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
                ironPrice = IRON_MAIN;
                break;
            case DIAMOND_HELMET:
            case DIAMOND_BOOTS:
                ironPrice = DIAMOND_SMALL;
                break;
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
                ironPrice = DIAMOND_MAIN;
                break;
            case NETHERITE_HELMET:
            case NETHERITE_BOOTS:
                ironPrice = NETHERITE_SMALL;
                break;
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
                ironPrice = NETHERITE_MAIN;
                break;
        }
        ironPrice *= nbt.getItem().getAmount();
        if (item.getType() == ItemType.ARROWS) {
            ironPrice /= 2;
        }
        if (ironPrice >= 64 && item.getType() != ItemType.GOLD_NUGGET) {
            goldPrice = ironPrice / GOLD_NUGGET_VALUE/*(int) Math.floor((double) ironPrice / GOLD_NUGGET_VALUE)*/;
            ironPrice = ironPrice % GOLD_NUGGET_VALUE;
        }
        List<String> lore;
        //Create a lore if it doesn't exist, otherwise just modify the current one.
        if (nbt.getItem().getLore() == null) {
            lore = new ArrayList<>();
        } else {
            lore = nbt.getItem().getLore();
        }
        lore.add(color("&8--------------------------------"));
        if (goldPrice > 0) {
            nbt.setInteger(MadItem.GOLD_PRICE_TAG, goldPrice);
            lore.add(color("&e" + goldPrice + "&6 Gold Nuggets"));
        }
        if (ironPrice > 0) {
            nbt.setInteger(MadItem.IRON_PRICE_TAG, ironPrice);
            lore.add(color("&b" + ironPrice + "&9 Iron Nuggets"));
        }
        nbt.getItem().setLore(lore);
        return nbt.getItem();
    }

    private void populateWeapons() {
        Tier tier = gameData.getTier();
        switch (tier) {
            case I:
                weapons = Map.of(
                        9, createShopItem(new Sword(ItemType.OLD_SWORD,
                                new ItemStack(Material.WOODEN_SWORD), plugin, gameData)),
                        10, createShopItem(new Shield(ItemType.CROSS_SHIELD, plugin, gameData)),
                        18, createShopItem(new Bow(ItemType.FRAIL_BOW,
                                new ItemStack(Material.BOW), plugin, gameData)),
                        19, createShopItem(new Crossbow(ItemType.FRAIL_CROSSBOW,
                                new ItemStack(Material.CROSSBOW), plugin, gameData)),
                        27, createShopItem(new Smudger(ItemType.DEFECTIVE_SMUDGER,
                                new ItemStack(Material.WOODEN_AXE), plugin, gameData)),
                        28, createShopItem(new  Railgun(ItemType.SLOW_RAILGUN,
                                new ItemStack(Material.WOODEN_SHOVEL), plugin, gameData)),
                        36, createShopItem(new Scepter(ItemType.WOODEN_SCEPTER,
                                new ItemStack(Material.WOODEN_HOE), plugin, gameData)),
                        37, createShopItem(new Trider(ItemType.IMPRUDENT_TRIDER,
                                new ItemStack(Material.TRIDENT), plugin, gameData))
                );
                break;
            case II:
                weapons = Map.of(
                        9, createShopItem(new Sword(ItemType.STONE_SWORD,
                                new ItemStack(Material.STONE_SWORD), plugin, gameData)),
                        10, createShopItem(new Shield(ItemType.MOSSY_SHIELD, plugin, gameData)),
                        18, createShopItem(new Bow(ItemType.SOLID_BOW,
                                new ItemStack(Material.BOW), plugin, gameData)),
                        19, createShopItem(new Crossbow(ItemType.STEADY_CROSSBOW,
                                new ItemStack(Material.CROSSBOW), plugin, gameData)),
                        27, createShopItem(new Smudger(ItemType.PLEASANT_SMUDGER,
                                new ItemStack(Material.WOODEN_AXE), plugin, gameData)),
                        28, createShopItem(new  Railgun(ItemType.IMPROVED_RAILGUN,
                                new ItemStack(Material.WOODEN_SHOVEL), plugin, gameData)),
                        36, createShopItem(new Scepter(ItemType.ANCIENT_SCEPTER,
                                new ItemStack(Material.WOODEN_HOE), plugin, gameData)),
                        37, createShopItem(new Trider(ItemType.LOYAL_TRIDER,
                                new ItemStack(Material.TRIDENT), plugin, gameData))
                );
                break;
            case III:
                weapons = Map.of(
                        9, createShopItem(new Sword(ItemType.BROADSWORD,
                                new ItemStack(Material.STONE_SWORD), plugin, gameData)),
                        10, createShopItem(new Shield(ItemType.PRISMARINE_SHIELD, plugin, gameData)),
                        18, createShopItem(new Bow(ItemType.REINFORCED_BOW,
                                new ItemStack(Material.BOW), plugin, gameData)),
                        19, createShopItem(new Crossbow(ItemType.ENHANCED_CROSSBOW,
                                new ItemStack(Material.CROSSBOW), plugin, gameData)),
                        27, createShopItem(new Smudger(ItemType.SILVER_PLATED_SMUDGER,
                                new ItemStack(Material.WOODEN_AXE), plugin, gameData)),
                        28, createShopItem(new  Railgun(ItemType.TITANS_RAILGUN,
                                new ItemStack(Material.WOODEN_SHOVEL), plugin, gameData)),
                        36, createShopItem(new Scepter(ItemType.MANASTEEL_SCEPTER,
                                new ItemStack(Material.WOODEN_HOE), plugin, gameData)),
                        37, createShopItem(new Trider(ItemType.FERROUS_TRIDER,
                                new ItemStack(Material.TRIDENT), plugin, gameData))
                );
                break;
            case IV:
                weapons = Map.of(
                        9, createShopItem(new Sword(ItemType.BEAM_SWORD,
                                new ItemStack(Material.STONE_SWORD), plugin, gameData)),
                        10, createShopItem(new Shield(ItemType.PINK_LOTUS_SHIELD, plugin, gameData)),
                        18, createShopItem(new Bow(ItemType.PULSE_BOW,
                                new ItemStack(Material.BOW), plugin, gameData)),
                        19, createShopItem(new Crossbow(ItemType.BLASTING_CROSSBOW,
                                new ItemStack(Material.CROSSBOW), plugin, gameData)),
                        27, createShopItem(new Smudger(ItemType.LUSTROUS_SMUDGER,
                                new ItemStack(Material.WOODEN_AXE), plugin, gameData)),
                        28, createShopItem(new  Railgun(ItemType.VIRTUOUS_RAILGUN,
                                new ItemStack(Material.WOODEN_SHOVEL), plugin, gameData)),
                        36, createShopItem(new Scepter(ItemType.CRYSTALLINE_SCEPTER,
                                new ItemStack(Material.WOODEN_HOE), plugin, gameData)),
                        37, createShopItem(new Trider(ItemType.ANGELIC_TRIDER,
                                new ItemStack(Material.TRIDENT), plugin, gameData))
                );
                break;
            case V:
                weapons = Map.of(
                        9, createShopItem(new Sword(ItemType.GLEAMING_SWORD,
                                new ItemStack(Material.STONE_SWORD), plugin, gameData)),
                        10, createShopItem(new Shield(ItemType.GLORIOUS_SHIELD, plugin, gameData)),
                        18, createShopItem(new Bow(ItemType.BLAZING_BOW,
                                new ItemStack(Material.BOW), plugin, gameData)),
                        19, createShopItem(new Crossbow(ItemType.FIERY_CROSSBOW,
                                new ItemStack(Material.CROSSBOW), plugin, gameData)),
                        27, createShopItem(new Smudger(ItemType.DRACONIC_SMUDGER,
                                new ItemStack(Material.WOODEN_AXE), plugin, gameData)),
                        28, createShopItem(new  Railgun(ItemType.ERUPTING_RAILGUN,
                                new ItemStack(Material.WOODEN_SHOVEL), plugin, gameData)),
                        36, createShopItem(new Scepter(ItemType.INFERNAL_SCEPTER,
                                new ItemStack(Material.WOODEN_HOE), plugin, gameData)),
                        37, createShopItem(new Trider(ItemType.HELLISH_TRIDER,
                                new ItemStack(Material.TRIDENT), plugin, gameData))
                );
                break;
            case VI:
                weapons = Map.of(
                        9, createShopItem(new Sword(ItemType.EXECUTIONERS_SWORD,
                                new ItemStack(Material.STONE_SWORD), plugin, gameData)),
                        10, createShopItem(new Sword(ItemType.WITHERS_SHIELD,
                                new ItemStack(Material.SHIELD), plugin, gameData)),
                        18, createShopItem(new Bow(ItemType.SHADOW_BOW,
                                new ItemStack(Material.BOW), plugin, gameData)),
                        19, createShopItem(new Crossbow(ItemType.CERBERUS_CROSSBOW,
                                new ItemStack(Material.CROSSBOW), plugin, gameData)),
                        27, createShopItem(new Smudger(ItemType.POSSESSED_SMUDGER,
                                new ItemStack(Material.WOODEN_AXE), plugin, gameData)),
                        28, createShopItem(new  Railgun(ItemType.JUDGEMENT_RAILGUN,
                                new ItemStack(Material.WOODEN_SHOVEL), plugin, gameData)),
                        36, createShopItem(new Scepter(ItemType.ARMAGEDDON_SCEPTER,
                                new ItemStack(Material.WOODEN_HOE), plugin, gameData)),
                        37, createShopItem(new Trider(ItemType.MANIC_TRIDER,
                                new ItemStack(Material.TRIDENT), plugin, gameData))
                );
                break;
        }
    }

    private void populateArmor() {
        Tier tier = gameData.getTier();
        switch (tier) {
            case I:
                armors = Map.of(
                        12, createShopItem(new Helmet(plugin, gameData, ItemType.LEATHER_HELMET,
                                new ItemStack(Material.LEATHER_HELMET))),
                        21, createShopItem(new Chestplate(plugin, gameData, ItemType.LEATHER_CHESTPLATE,
                                new ItemStack(Material.LEATHER_CHESTPLATE))),
                        30, createShopItem(new Leggings(plugin, gameData, ItemType.LEATHER_LEGGINGS,
                                new ItemStack(Material.LEATHER_LEGGINGS))),
                        39, createShopItem(new Boots(plugin, gameData, ItemType.LEATHER_BOOTS,
                                new ItemStack(Material.LEATHER_BOOTS)))
                );
                break;
            case II:
                armors = Map.of(
                        12, createShopItem(new Helmet(plugin, gameData, ItemType.CHAINMAIL_HELMET,
                                new ItemStack(Material.CHAINMAIL_HELMET))),
                        21, createShopItem(new Chestplate(plugin, gameData, ItemType.CHAINMAIL_CHESTPLATE,
                                new ItemStack(Material.CHAINMAIL_CHESTPLATE))),
                        30, createShopItem(new Leggings(plugin, gameData, ItemType.CHAINMAIL_LEGGINGS,
                                new ItemStack(Material.CHAINMAIL_LEGGINGS))),
                        39, createShopItem(new Boots(plugin, gameData, ItemType.CHAINMAIL_BOOTS,
                                new ItemStack(Material.CHAINMAIL_BOOTS)))
                );
                break;
            case III:
                armors = Map.of(
                        12, createShopItem(new Helmet(plugin, gameData, ItemType.IRON_HELMET,
                                new ItemStack(Material.IRON_HELMET))),
                        21, createShopItem(new Chestplate(plugin, gameData, ItemType.IRON_CHESTPLATE,
                                new ItemStack(Material.IRON_CHESTPLATE))),
                        30, createShopItem(new Leggings(plugin, gameData, ItemType.IRON_LEGGINGS,
                                new ItemStack(Material.IRON_LEGGINGS))),
                        39, createShopItem(new Boots(plugin, gameData, ItemType.IRON_BOOTS,
                                new ItemStack(Material.IRON_BOOTS)))
                );
                break;
            case IV:
                armors = Map.of(
                        12, createShopItem(new Helmet(plugin, gameData, ItemType.DIAMOND_HELMET,
                                new ItemStack(Material.DIAMOND_HELMET))),
                        21, createShopItem(new Chestplate(plugin, gameData, ItemType.DIAMOND_CHESTPLATE,
                                new ItemStack(Material.DIAMOND_CHESTPLATE))),
                        30, createShopItem(new Leggings(plugin, gameData, ItemType.DIAMOND_LEGGINGS,
                                new ItemStack(Material.DIAMOND_LEGGINGS))),
                        39, createShopItem(new Boots(plugin, gameData, ItemType.DIAMOND_BOOTS,
                                new ItemStack(Material.DIAMOND_BOOTS)))
                );
                break;
            case V:
            case VI:
                armors = Map.of(
                        12, createShopItem(new Helmet(plugin, gameData, ItemType.NETHERITE_HELMET,
                                new ItemStack(Material.NETHERITE_HELMET))),
                        21, createShopItem(new Chestplate(plugin, gameData, ItemType.NETHERITE_CHESTPLATE,
                                new ItemStack(Material.NETHERITE_CHESTPLATE))),
                        30, createShopItem(new Leggings(plugin, gameData, ItemType.NETHERITE_LEGGINGS,
                                new ItemStack(Material.NETHERITE_LEGGINGS))),
                        39, createShopItem(new Boots(plugin, gameData, ItemType.NETHERITE_BOOTS,
                                new ItemStack(Material.NETHERITE_BOOTS)))
                );
                break;
        }
    }

    private void populateConsumables() {
        consumables.clear();
        while (consumables.size() < 3){
            int amount;
            int chance = random.nextInt(17);
            switch (chance) {
                case 0:
                    consumables.put(ItemType.TOTEM_OF_UNDYING, new TotemOfUndying(plugin, gameData));
                    break;
                case 1:
                case 2:
                    amount = (random.nextInt(3) + 1) + gameData.getTier().getNumericTier() / 2;
                    consumables.put(ItemType.VOID_ORB, new VoidOrb(plugin, amount, gameData));
                    break;
                case 3:
                case 4:
                    amount = (random.nextInt(3) + 1) + gameData.getTier().getNumericTier() / 2;
                    consumables.put(ItemType.DETONATION_ORB, new DetonationOrb(plugin, amount, gameData));
                    break;
                case 5:
                case 6:
                    amount = (random.nextInt(2) + 1) + gameData.getTier().getNumericTier() / 2;
                    consumables.put(ItemType.THROWABLE_TNT, new ThrowableTNT(plugin, amount, gameData));
                    break;
                case 7:
                case 8:
                    amount = 1;
                    consumables.put(ItemType.BHG, new BottledHealingGas(plugin, amount, gameData));
                    break;
                case 9:
                case 10:
                    amount = 1 + (gameData.getTier().getNumericTier() / 3);
                    consumables.put(ItemType.CHORUS_FRUIT, new ChorusFruit(plugin, amount, gameData));
                    break;
                case 11:
                case 12:
                    amount = (random.nextInt(3) + 5) + gameData.getTier().getNumericTier() * 2;
                    consumables.put(ItemType.COOKIE, new Cookie(plugin, amount, gameData));
                    break;
                case 13:
                case 14:
                    amount = (random.nextInt(2) + 1) + gameData.getTier().getNumericTier() / 3;
                    consumables.put(ItemType.VEILING_POTION, new VeilingPotion(plugin, amount, gameData));
                    break;
                case 15:
                case 16:
                    amount = gameData.getTier().getNumericTier();
                    consumables.put(ItemType.BARRICADE, new Barricade(plugin, amount, gameData));
                    break;
            }
        }
    }

    /**
     * Takes the required amount of nuggets from the player.
     *
     * @param ironPrice
     * @param goldPrice
     * @return true if the player has enough of each nuggets, false otherwise
     */
    private boolean takeNuggets(int ironPrice, int goldPrice) {
        boolean transactionSuccess = false;
        Player player = profile.getPlayer();
        int ironAmount = 0;
        int goldAmount = 0;
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item == null) continue;
            if (item.getType() == Material.IRON_NUGGET) {
                ironAmount += item.getAmount();
            } else if (item.getType() == Material.GOLD_NUGGET) {
                goldAmount += item.getAmount();
            }
        }
        if (ironAmount < ironPrice || goldAmount < goldPrice) {
            return false;
        }
        if (ironPrice > 0) {
            MadItem ironStack = new IronNugget(plugin, ironPrice, gameData);
            inv.removeItemAnySlot(ironStack.getItemStack());
            transactionSuccess = true;
        }
        if (goldPrice > 0) {
            MadItem goldStack = new GoldNugget(plugin, goldPrice, gameData);
            inv.removeItemAnySlot(goldStack.getItemStack());
            transactionSuccess = true;
        }
        return transactionSuccess;
    }

    /**
     * Removes all shop tags from an item and unnecessary lore from it as well, then returns it.
     *
     * @param item The item to give the player
     * @return the prepared item.
     */
    private ItemStack prepareItem(ItemStack item) {
        NBTItem nbt = new NBTItem(item);
        List<String> lore = item.getLore();
        if (lore == null) return null;
        lore.remove(lore.size() - 1);
        Player player = profile.getPlayer();
        if (nbt.getInteger(MadItem.GOLD_PRICE_TAG) > 0) {
            lore.remove(lore.size() - 1);
        }
        nbt.removeKey(MadItem.GOLD_PRICE_TAG);
        if (nbt.getInteger(MadItem.IRON_PRICE_TAG) > 0) {
            lore.remove(lore.size() - 1);
        }
        nbt.removeKey(MadItem.IRON_PRICE_TAG);
        nbt.removeKey(MadItem.SHOP_TAG);
        nbt.removeKey(MadItem.ON_SALE_TAG);
        nbt.removeKey(MadItem.CLICKABLE_TAG);
        nbt.getItem().setLore(lore);
        return nbt.getItem();
    }

    /**
     * Calculates the price for a Life Elixir. This is based on how many health ups the player has had so far.
     *
     * @return the price for a life elixir in iron nuggets.
     */
    private int calculateLifeElixirPrice() {
        switch (profile.getHealthUpAmount()) {
            default:
                return LIFE_ELIXIR_VALUE;
            case 0:
                return LIFE_ELIXIR_VALUE * 2; //8
            case 1:
                return LIFE_ELIXIR_VALUE * 4; //16
            case 2:
                return LIFE_ELIXIR_VALUE * 8; //32
            case 3:
                return LIFE_ELIXIR_VALUE * 12; //48
            case 4:
                return LIFE_ELIXIR_VALUE * 16; //64 (1)
            case 5:
                return LIFE_ELIXIR_VALUE * 24; //96 (1, 32)
            case 6:
                return LIFE_ELIXIR_VALUE * 32; //128 (2)
            case 7:
                return LIFE_ELIXIR_VALUE * 48; //192 (3)
            case 8:
                return LIFE_ELIXIR_VALUE * 64; //256 (4)
            case 9:
                return LIFE_ELIXIR_VALUE * 80; //320 (5)
            case 10:
                return LIFE_ELIXIR_VALUE * 128; //512 (8)
            case 11:
                return LIFE_ELIXIR_VALUE * 192; //768 (12)
            case 12:
                return LIFE_ELIXIR_VALUE * 288; //1152 (18)
            case 13:
                return LIFE_ELIXIR_VALUE * 384; //1536 (24)
            case 14:
                return LIFE_ELIXIR_VALUE * 512; //2048 (32)
            case 15:
                return LIFE_ELIXIR_VALUE * 4096; //No. NO!
        }
    }

    private int calculateAetherBerriesPrice() {
        int berryCount = 0;
        for (Profile profile : gameData.getPlayers().values()) {
            Player player = profile.getPlayer();
            for (int slot = 0; slot < player.getInventory().getSize(); slot++) {
                ItemStack item = player.getInventory().getItem(slot);
                if (item != null && item.getType() == Material.GLOW_BERRIES) {
                    berryCount+= item.getAmount();
                }
            }
        }
        return (int) (AETHER_BERRIES_VALUE * (Math.pow(2, berryCount + gameData.getAnchorCharges() + 1))
                        * gameData.getTier().getNumericTier());
    }
}
