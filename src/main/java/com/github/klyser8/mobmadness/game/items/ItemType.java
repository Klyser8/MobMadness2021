package com.github.klyser8.mobmadness.game.items;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.consumables.*;
import com.github.klyser8.mobmadness.game.items.equipment.Chestplate;
import com.github.klyser8.mobmadness.game.items.equipment.Helmet;
import com.github.klyser8.mobmadness.game.items.equipment.Leggings;
import com.github.klyser8.mobmadness.game.items.equipment.shield.Shield;
import com.github.klyser8.mobmadness.game.items.misc.*;
import com.github.klyser8.mobmadness.game.items.weapons.bow.Bow;
import com.github.klyser8.mobmadness.game.items.weapons.crossbow.Crossbow;
import com.github.klyser8.mobmadness.game.items.weapons.railgun.Railgun;
import com.github.klyser8.mobmadness.game.items.weapons.smudger.Smudger;
import com.github.klyser8.mobmadness.game.items.weapons.scepter.Scepter;
import com.github.klyser8.mobmadness.game.items.weapons.sword.Sword;
import com.github.klyser8.mobmadness.game.items.weapons.trider.Trider;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ItemType {

    //Misc
    IRON_NUGGET(Material.IRON_NUGGET, "&9Iron Nugget"),
    GOLD_NUGGET(Material.GOLD_NUGGET, "&6Gold Nugget"),
    CRYSTAL(Material.PRISMARINE_CRYSTALS, "&bCrystal"),
    COMPASS(Material.COMPASS, "&bMob Tracker || Shop"),
    BLANK_ITEM(Material.BLACK_STAINED_GLASS, "&k"), //Material changes based on current tier.
    BLOCKED_ITEM(Material.BARRIER, "&4&uUnavailable"), //Used for locked items
    //Consumables
    LIFE_ELIXIR(Material.BEETROOT_SOUP, "&aLife Elixir"),
    AETHERS_BERRIES(Material.GLOW_BERRIES, "&eAether's Berries"),
    GREATER_HEALTH_POTION(Material.POTION, "&aGreater Health Potion"), //TODO implement
    HEALTH_POTION(Material.SPLASH_POTION, "&aHealth Brew"),
    ARROWS(Material.ARROW, "&aArrow"),
    RUNIC_ARROW(Material.SPECTRAL_ARROW, "&aRunic Arrow"), //TODO implement

    VEILING_POTION(Material.HONEY_BOTTLE, "&aVeiling Potion"),
    BARRICADE(Material.ANDESITE_WALL, "&aBarricade"),
    FROST_ORB(Material.SNOWBALL, "&aFrost Orb"), //TODO implement: Throws snowball, upon hitting an enemy freezes them. Once they die, they explode in ice shards damaging nearby enemies.
    RUGGED_ORB(Material.FIREWORK_STAR, "&aRugged Orb"), //TODO implement: Throws a rock, upon hitting an enemy does A LOT of damage. Upon hitting a block, it gets broken.
    DETONATION_ORB(Material.FIRE_CHARGE, "&aDetonation Orb"),
    VOID_ORB(Material.ENDER_PEARL, "&aVoid Orb"),
    COOKIE(Material.COOKIE, "&aCookie"),
    TOTEM_OF_UNDYING(Material.TOTEM_OF_UNDYING, "&aTotem of Undying"),
    THROWABLE_TNT(Material.TNT, "&aThrowable TNT"),
    BHG(Material.LINGERING_POTION, "&aBottled Healing Gas (B.H.G.)"),
    CHORUS_FRUIT(Material.CHORUS_FRUIT, "&aChorus Fruit"),
    ENDER_AID(Material.ENDER_EYE, "&aEnder Aid"), //TODO implement
    //Swords
    OLD_SWORD(Material.WOODEN_SWORD, "&7Old Sword"),
    STONE_SWORD(Material.STONE_SWORD, "&2Stone Sword"),
    BROADSWORD(Material.IRON_SWORD, "&9Broadsword"),
    BEAM_SWORD(Material.DIAMOND_SWORD, "&dBeam Sword"),
    GLEAMING_SWORD(Material.GOLDEN_SWORD, "&6Gleaming Sword"),
    EXECUTIONERS_SWORD(Material.NETHERITE_SWORD, "&cExecutioner's Sword"),
    //Shields
    CROSS_SHIELD(Material.SHIELD, "&7Cross Shield"),
    MOSSY_SHIELD(Material.SHIELD, "&2Mossy Shield"),
    PRISMARINE_SHIELD(Material.SHIELD, "&9Prismarine Shield"),
    PINK_LOTUS_SHIELD(Material.SHIELD, "&dPink Lotus Shield"),
    GLORIOUS_SHIELD(Material.SHIELD, "&6Glorious Shield"),
    WITHERS_SHIELD(Material.SHIELD, "&cWither's Shield"),
    //Bows
    FRAIL_BOW(Material.BOW, "&7Frail Bow"),
    SOLID_BOW(Material.BOW, "&2Solid Bow"),
    REINFORCED_BOW(Material.BOW, "&9Reinforced Bow"),
    PULSE_BOW(Material.BOW, "&dPulse Bow"),
    BLAZING_BOW(Material.BOW, "&6Blazing Bow"),
    SHADOW_BOW(Material.BOW, "&cShadow Bow"),
    //Crossbows
    FRAIL_CROSSBOW(Material.CROSSBOW, "&7Frail Crossbow"),
    STEADY_CROSSBOW(Material.CROSSBOW, "&2Steady Crossbow"),
    ENHANCED_CROSSBOW(Material.CROSSBOW, "&9Enhanced Crossbow"),
    BLASTING_CROSSBOW(Material.CROSSBOW, "&dBlasting Crossbow"),
    FIERY_CROSSBOW(Material.CROSSBOW, "&6Fiery Crossbow"),
    CERBERUS_CROSSBOW(Material.CROSSBOW, "&cCerberus' Crossbow"),
    //Scepters
    WOODEN_SCEPTER(Material.WOODEN_HOE, "&7Wooden Scepter"),
    ANCIENT_SCEPTER(Material.STONE_HOE, "&2Ancient Scepter"),
    MANASTEEL_SCEPTER(Material.IRON_HOE, "&9Manasteel Scepter"),
    CRYSTALLINE_SCEPTER(Material.DIAMOND_HOE, "&dCrystalline Scepter"),
    INFERNAL_SCEPTER(Material.GOLDEN_HOE, "&6Infernal Scepter"),
    ARMAGEDDON_SCEPTER(Material.NETHERITE_HOE, "&cArmageddon Scepter"),
    //Railguns
    SLOW_RAILGUN(Material.WOODEN_SHOVEL, "&7Slow Railgun"),
    IMPROVED_RAILGUN(Material.STONE_SHOVEL, "&2Improved Railgun"),
    TITANS_RAILGUN(Material.IRON_SHOVEL, "&9Titan's Railgun"),
    VIRTUOUS_RAILGUN(Material.DIAMOND_SHOVEL, "&dVirtuous Railgun"),
    ERUPTING_RAILGUN(Material.GOLDEN_SHOVEL, "&6Erupting Railgun"),
    JUDGEMENT_RAILGUN(Material.NETHERITE_SHOVEL, "&cJudgement Railgun"),
    //Smudger
    DEFECTIVE_SMUDGER(Material.WOODEN_AXE, "&7Defective Smudger"),
    PLEASANT_SMUDGER(Material.STONE_AXE, "&2Pleasant Smudger"),
    SILVER_PLATED_SMUDGER(Material.IRON_AXE, "&9Silver-Plated Smudger"),
    LUSTROUS_SMUDGER(Material.DIAMOND_AXE, "&dLustrous Smudger"),
    DRACONIC_SMUDGER(Material.GOLDEN_AXE, "&6Draconic Smudger"),
    POSSESSED_SMUDGER(Material.NETHERITE_AXE, "&cPossessed Smudger"),
    //Trider
    IMPRUDENT_TRIDER(Material.TRIDENT, "&7Imprudent Trider"),
    LOYAL_TRIDER(Material.TRIDENT, "&2Loyal Trider"),
    FERROUS_TRIDER(Material.TRIDENT, "&9Ferrous Trider"),
    ANGELIC_TRIDER(Material.TRIDENT, "&dAngelic Trider"),
    HELLISH_TRIDER(Material.TRIDENT, "&6Hellish Trider"),
    MANIC_TRIDER(Material.TRIDENT, "&cManic Trider"),
    //Leather armor
    LEATHER_HELMET(Material.LEATHER_HELMET, "&7Leather Helmet"),
    LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE, "&7Leather Chestplate"),
    LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS, "&7Leather Leggings"),
    LEATHER_BOOTS(Material.LEATHER_BOOTS, "&7Leather Boots"),
    //Chainmail armor
    CHAINMAIL_HELMET(Material.CHAINMAIL_HELMET, "&2Chainmail Helmet"),
    CHAINMAIL_CHESTPLATE(Material.CHAINMAIL_CHESTPLATE, "&2Chainmail Chestplate"),
    CHAINMAIL_LEGGINGS(Material.CHAINMAIL_LEGGINGS, "&2Chainmail Leggings"),
    CHAINMAIL_BOOTS(Material.CHAINMAIL_BOOTS, "&2Chainmail Boots"),
    //Iron armor
    IRON_HELMET(Material.IRON_HELMET, "&9Iron Helmet"),
    IRON_CHESTPLATE(Material.IRON_CHESTPLATE, "&9Iron Chestplate"),
    IRON_LEGGINGS(Material.IRON_LEGGINGS, "&9Iron Leggings"),
    IRON_BOOTS(Material.IRON_BOOTS, "&9Iron Boots"),
    //Diamond armor
    DIAMOND_HELMET(Material.DIAMOND_HELMET, "&dDiamond Helmet"),
    DIAMOND_CHESTPLATE(Material.DIAMOND_CHESTPLATE, "&dDiamond Chestplate"),
    DIAMOND_LEGGINGS(Material.DIAMOND_LEGGINGS, "&dDiamond Leggings"),
    DIAMOND_BOOTS(Material.DIAMOND_BOOTS, "&dDiamond Boots"),
    //Netherite armor
    NETHERITE_HELMET(Material.NETHERITE_HELMET, "&6Netherite Helmet"),
    NETHERITE_CHESTPLATE(Material.NETHERITE_CHESTPLATE, "&6Netherite Chestplate"),
    NETHERITE_LEGGINGS(Material.NETHERITE_LEGGINGS, "&6Netherite Leggings"),
    NETHERITE_BOOTS(Material.NETHERITE_BOOTS, "&6Netherite Boots");

    private final Material type;
    private final String name;
    ItemType(Material type, String name) {
        this.type = type;
        this.name = name;
    }

    public Material getMaterial() {
        return type;
    }

    /**
     * Creates a 'dummy' version of an item present in MobMadness.
     * This item is likely to not work in a game, as is for testing purposes only.
     *
     * @param plugin instance of the main class
     * @param gameData if applicable, data class of the game the item's for
     * @param type the type of item to create
     * @param amount amount of items to create
     * @return the MadItem, or null if no valid ItemType is provided.
     */
    public static MadItem createDummyItem(MobMadness plugin, GameData gameData, ItemType type, int amount) {
        switch (type) {
            case IRON_NUGGET: return new IronNugget(plugin, amount, gameData);
            case GOLD_NUGGET: return new GoldNugget(plugin, amount, gameData);
            case CRYSTAL: return new Crystal(plugin, amount, gameData);
            case COMPASS: return new Compass(plugin, gameData);
            case BLANK_ITEM:
                break;
            case BLOCKED_ITEM:
                break;
            case LIFE_ELIXIR: return new LifeElixir(plugin, amount, gameData);
            case AETHERS_BERRIES: return new AethersBerries(plugin, amount, gameData);
            case GREATER_HEALTH_POTION:
                break;
            case HEALTH_POTION: return new HealthPotion(plugin, gameData);
            case ARROWS: return new Arrows(plugin, amount, gameData);
            case RUNIC_ARROW:
                break;
            case COOKIE: return new Cookie(plugin, amount, gameData);
            case TOTEM_OF_UNDYING: return new TotemOfUndying(plugin, gameData);
            case THROWABLE_TNT: return new ThrowableTNT(plugin, amount, gameData);
            case BHG: return new BottledHealingGas(plugin,0, gameData);
            case CHORUS_FRUIT: return new ChorusFruit(plugin, amount, gameData);
            case ENDER_AID:
                break;
            case VOID_ORB: return new VoidOrb(plugin, amount, gameData);
            case VEILING_POTION: return new VeilingPotion(plugin, amount, gameData);
            case BARRICADE: return new Barricade(plugin, amount, gameData);
            case FROST_ORB:
                break;
            case RUGGED_ORB:
                break;
            case DETONATION_ORB: return new DetonationOrb(plugin, amount, gameData);
            //WEAPONS
            case OLD_SWORD: return new Sword(type, new ItemStack(Material.WOODEN_SWORD),
                    plugin, gameData);
            case STONE_SWORD: return new Sword(type, new ItemStack(Material.STONE_SWORD),
                    plugin, gameData);
            case BROADSWORD: return new Sword(type, new ItemStack(Material.IRON_SWORD),
                    plugin, gameData);
            case BEAM_SWORD: return new Sword(type, new ItemStack(Material.DIAMOND_SWORD),
                    plugin, gameData);
            case GLEAMING_SWORD: return new Sword(type, new ItemStack(Material.GOLDEN_SWORD),
                    plugin, gameData);
            case EXECUTIONERS_SWORD: return new Sword(type,
                    new ItemStack(Material.NETHERITE_SWORD), plugin, gameData);

            case CROSS_SHIELD:
            case PINK_LOTUS_SHIELD:
            case PRISMARINE_SHIELD:
            case MOSSY_SHIELD:
            case GLORIOUS_SHIELD:
            case WITHERS_SHIELD:
                return new Shield(type, plugin, gameData);

            case FRAIL_BOW:
            case SOLID_BOW:
            case REINFORCED_BOW:
            case PULSE_BOW:
            case BLAZING_BOW:
            case SHADOW_BOW:
                return new Bow(type, new ItemStack(Material.BOW), plugin, gameData);

            case FRAIL_CROSSBOW:
            case STEADY_CROSSBOW:
            case ENHANCED_CROSSBOW:
            case BLASTING_CROSSBOW:
            case FIERY_CROSSBOW:
            case CERBERUS_CROSSBOW:
                return new Crossbow(type, new ItemStack(Material.CROSSBOW),
                    plugin, gameData);

            case WOODEN_SCEPTER: return new Scepter(type, new ItemStack(Material.WOODEN_HOE),
                    plugin, gameData);
            case ANCIENT_SCEPTER: return new Scepter(type, new ItemStack(Material.STONE_HOE),
                    plugin, gameData);
            case MANASTEEL_SCEPTER: return new Scepter(type, new ItemStack(Material.IRON_HOE),
                    plugin, gameData);
            case CRYSTALLINE_SCEPTER: return new Scepter(type, new ItemStack(Material.DIAMOND_HOE),
                    plugin, gameData);
            case INFERNAL_SCEPTER: return new Scepter(type, new ItemStack(Material.GOLDEN_HOE),
                    plugin, gameData);
            case ARMAGEDDON_SCEPTER: return new Scepter(type, new ItemStack(Material.NETHERITE_HOE),
                    plugin, gameData);

            case SLOW_RAILGUN: return new Railgun(type, new ItemStack(Material.WOODEN_SHOVEL),
                    plugin, gameData);
            case IMPROVED_RAILGUN: return new Railgun(type, new ItemStack(Material.STONE_SHOVEL),
                    plugin, gameData);
            case TITANS_RAILGUN: return new Railgun(type, new ItemStack(Material.IRON_SHOVEL),
                    plugin, gameData);
            case VIRTUOUS_RAILGUN: return new Railgun(type, new ItemStack(Material.DIAMOND_SHOVEL),
                    plugin, gameData);
            case ERUPTING_RAILGUN: return new Railgun(type, new ItemStack(Material.GOLDEN_SHOVEL),
                    plugin, gameData);
            case JUDGEMENT_RAILGUN: return new Railgun(type, new ItemStack(Material.NETHERITE_SHOVEL),
                    plugin, gameData);

            case DEFECTIVE_SMUDGER: return new Smudger(type, new ItemStack(Material.WOODEN_AXE),
                    plugin, gameData);
            case PLEASANT_SMUDGER: return new Smudger(type, new ItemStack(Material.STONE_AXE),
                    plugin, gameData);
            case SILVER_PLATED_SMUDGER: return new Smudger(type, new ItemStack(Material.IRON_AXE),
                    plugin, gameData);
            case LUSTROUS_SMUDGER: return new Smudger(type, new ItemStack(Material.DIAMOND_AXE),
                    plugin, gameData);
            case DRACONIC_SMUDGER: return new Smudger(type, new ItemStack(Material.GOLDEN_AXE),
                    plugin, gameData);
            case POSSESSED_SMUDGER: return new Smudger(type, new ItemStack(Material.NETHERITE_AXE),
                    plugin, gameData);

            case IMPRUDENT_TRIDER:
            case LOYAL_TRIDER:
            case FERROUS_TRIDER:
            case ANGELIC_TRIDER:
            case HELLISH_TRIDER:
            case MANIC_TRIDER:
                return new Trider(type, new ItemStack(Material.TRIDENT), plugin, gameData);

            case LEATHER_HELMET: return new Helmet(plugin, gameData, type, new ItemStack(Material.LEATHER_HELMET));
            case LEATHER_CHESTPLATE:
                return new Chestplate(plugin, gameData, type, new ItemStack(Material.LEATHER_CHESTPLATE));
            case LEATHER_LEGGINGS:
                return new Leggings(plugin, gameData, type, new ItemStack(Material.LEATHER_LEGGINGS));
            case LEATHER_BOOTS: return new Leggings(plugin, gameData, type, new ItemStack(Material.LEATHER_BOOTS));

            case CHAINMAIL_HELMET: return new Helmet(plugin, gameData, type, new ItemStack(Material.CHAINMAIL_HELMET));
            case CHAINMAIL_CHESTPLATE:
                return new Chestplate(plugin, gameData, type, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            case CHAINMAIL_LEGGINGS:
                return new Leggings(plugin, gameData, type, new ItemStack(Material.CHAINMAIL_LEGGINGS));
            case CHAINMAIL_BOOTS: return new Leggings(plugin, gameData, type, new ItemStack(Material.CHAINMAIL_BOOTS));

            case IRON_HELMET: return new Helmet(plugin, gameData, type, new ItemStack(Material.IRON_HELMET));
            case IRON_CHESTPLATE:
                return new Chestplate(plugin, gameData, type, new ItemStack(Material.IRON_CHESTPLATE));
            case IRON_LEGGINGS: return new Leggings(plugin, gameData, type, new ItemStack(Material.IRON_LEGGINGS));
            case IRON_BOOTS: return new Leggings(plugin, gameData, type, new ItemStack(Material.IRON_BOOTS));

            case DIAMOND_HELMET: return new Helmet(plugin, gameData, type, new ItemStack(Material.DIAMOND_HELMET));
            case DIAMOND_CHESTPLATE:
                return new Chestplate(plugin, gameData, type, new ItemStack(Material.DIAMOND_CHESTPLATE));
            case DIAMOND_LEGGINGS:
                return new Leggings(plugin, gameData, type, new ItemStack(Material.DIAMOND_LEGGINGS));
            case DIAMOND_BOOTS: return new Leggings(plugin, gameData, type, new ItemStack(Material.DIAMOND_BOOTS));

            case NETHERITE_HELMET: return new Helmet(plugin, gameData, type, new ItemStack(Material.NETHERITE_HELMET));
            case NETHERITE_CHESTPLATE:
                return new Chestplate(plugin, gameData, type, new ItemStack(Material.NETHERITE_CHESTPLATE));
            case NETHERITE_LEGGINGS:
                return new Leggings(plugin, gameData, type, new ItemStack(Material.NETHERITE_LEGGINGS));
            case NETHERITE_BOOTS: return new Leggings(plugin, gameData, type, new ItemStack(Material.NETHERITE_BOOTS));
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
