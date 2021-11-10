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
import com.github.klyser8.mobmadness.game.items.weapons.rayler.Rayler;
import com.github.klyser8.mobmadness.game.items.weapons.smudger.Smudger;
import com.github.klyser8.mobmadness.game.items.weapons.scepter.Scepter;
import com.github.klyser8.mobmadness.game.items.weapons.sword.Sword;
import com.github.klyser8.mobmadness.game.items.weapons.trider.Trider;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

public enum ItemType {

    //Misc
    IRON_NUGGET(Material.IRON_NUGGET, Component.text("Iron Nugget")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.METALLIC_BLUE),
    GOLD_NUGGET(Material.GOLD_NUGGET, Component.text("Gold Nugget")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    CRYSTAL(Material.PRISMARINE_CRYSTALS, Component.text("Crystal")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.INDIGO),
    COMPASS(Material.COMPASS, Component.text("Mob Tracker || Shop")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.EGGPLANT),
    BLANK_ITEM(Material.BLACK_STAINED_GLASS, Component.text("")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.WHITE), //Material changes based on current tier.
    BLOCKED_ITEM(Material.BARRIER, Component.text("Unavailable")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.DARK_RED), //Used for locked items
    //Consumables
    LIFE_ELIXIR(Material.BEETROOT_SOUP, Component.text("Life Elixir")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.FUCHSIA),
    AETHERS_BERRIES(Material.GLOW_BERRIES, Component.text("Aether's Berries")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.HONEYSACKLE),
    GREATER_HEALTH_POTION(Material.POTION, Component.text("Greater Health Potion")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME), //TODO implement
    HEALTH_POTION(Material.SPLASH_POTION, Component.text("Health Potion")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    ARROWS(Material.ARROW, Component.text("Arrow")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    RUNIC_ARROW(Material.SPECTRAL_ARROW, Component.text("Runic Arrow")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME), //TODO implement

    VEILING_POTION(Material.HONEY_BOTTLE, Component.text("Veiling Potion")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    BARRICADE(Material.ANDESITE_WALL, Component.text("Barricade")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    FROST_ORB(Material.SNOWBALL, Component.text("Frost Orb")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME), //TODO implement: Throws snowball, upon hitting an enemy freezes them. Once they die, they explode in ice shards damaging nearby enemies.
    RUGGED_ORB(Material.FIREWORK_STAR, Component.text("Rugged Orb")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME), //TODO implement: Throws a rock, upon hitting an enemy does A LOT of damage. Upon hitting a block, it gets broken.
    DETONATION_ORB(Material.FIRE_CHARGE, Component.text("Detonation Orb")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    VOID_ORB(Material.ENDER_PEARL, Component.text("Void Orb")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    COOKIE(Material.COOKIE, Component.text("Cookie")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    TOTEM_OF_UNDYING(Material.TOTEM_OF_UNDYING, Component.text("Totem of Undying")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    THROWABLE_TNT(Material.TNT, Component.text("Throwable TNT")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    BHG(Material.LINGERING_POTION, Component.text("Bottled Healing Gas (B.H.G.)")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    CHORUS_FRUIT(Material.CHORUS_FRUIT, Component.text("Chorus Fruit")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME),
    ENDER_AID(Material.ENDER_EYE, Component.text("Ender Aid")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.LIME), //TODO implement
    //Swords
    OLD_SWORD(Material.WOODEN_SWORD, Component.text("Old Sword")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    STONE_SWORD(Material.STONE_SWORD, Component.text("Stone Sword")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    BROADSWORD(Material.IRON_SWORD, Component.text("Broadsword")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    BEAM_SWORD(Material.DIAMOND_SWORD, Component.text("Beam Sword")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    GLEAMING_SWORD(Material.GOLDEN_SWORD, Component.text("Gleaming Sword")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    EXECUTIONERS_SWORD(Material.NETHERITE_SWORD, Component.text("Executioner's Sword")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    //Shields
    CROSS_SHIELD(Material.SHIELD, Component.text("Cross Shield")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    MOSSY_SHIELD(Material.SHIELD, Component.text("Mossy Shield")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    PRISMARINE_SHIELD(Material.SHIELD, Component.text("Prismarine Shield")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    PINK_LOTUS_SHIELD(Material.SHIELD, Component.text("Pink Lotus Shield")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    GLORIOUS_SHIELD(Material.SHIELD, Component.text("Glorious Shield")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    WITHERS_SHIELD(Material.SHIELD, Component.text("Wither's Shield")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.RED),
    //Bows
    FRAIL_BOW(Material.BOW, Component.text("Frail Bow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    SOLID_BOW(Material.BOW, Component.text("Solid Bow")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    REINFORCED_BOW(Material.BOW, Component.text("Reinforced Bow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    PULSE_BOW(Material.BOW, Component.text("Pulse Bow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    BLAZING_BOW(Material.BOW, Component.text("Blazing Bow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    SHADOW_BOW(Material.BOW, Component.text("Shadow Bow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.RED),
    //Crossbows
    FRAIL_CROSSBOW(Material.CROSSBOW, Component.text("Frail Crossbow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    STEADY_CROSSBOW(Material.CROSSBOW, Component.text("Steady Crossbow")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    ENHANCED_CROSSBOW(Material.CROSSBOW, Component.text("Enhanced Crossbow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    BLASTING_CROSSBOW(Material.CROSSBOW, Component.text("Blasting Crossbow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    FIERY_CROSSBOW(Material.CROSSBOW, Component.text("Fiery Crossbow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    CERBERUS_CROSSBOW(Material.CROSSBOW, Component.text("Cerberus' Crossbow")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.RED),
    //Scepters
    WOODEN_SCEPTER(Material.WOODEN_HOE, Component.text("Wooden Scepter")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    ANCIENT_SCEPTER(Material.STONE_HOE, Component.text("Ancient Scepter")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    MANASTEEL_SCEPTER(Material.IRON_HOE, Component.text("Manasteel Scepter")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    CRYSTALLINE_SCEPTER(Material.DIAMOND_HOE, Component.text("Crystalline Scepter")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    INFERNAL_SCEPTER(Material.GOLDEN_HOE, Component.text("Infernal Scepter")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    ARMAGEDDON_SCEPTER(Material.NETHERITE_HOE, Component.text("Armageddon Scepter")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.RED),
    //Railguns
    SLOW_RAYLER(Material.WOODEN_SHOVEL, Component.text("Slow Rayler")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    IMPROVED_RAYLER(Material.STONE_SHOVEL, Component.text("Improved Rayler")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    TITANS_RAYLER(Material.IRON_SHOVEL, Component.text("Titan's Rayler")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    VIRTUOUS_RAYLER(Material.DIAMOND_SHOVEL, Component.text("Virtuous Rayler")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    ERUPTING_RAYLER(Material.GOLDEN_SHOVEL, Component.text("Erupting Rayler")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    JUDGEMENT_RAYLER(Material.NETHERITE_SHOVEL, Component.text("Judgement Rayler")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.RED),
    //Smudger
    DEFECTIVE_SMUDGER(Material.WOODEN_AXE, Component.text("Defective Smudger")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    PLEASANT_SMUDGER(Material.STONE_AXE, Component.text("Pleasant Smudger")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    SILVER_PLATED_SMUDGER(Material.IRON_AXE, Component.text("Silver-Plated Smudger")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    LUSTROUS_SMUDGER(Material.DIAMOND_AXE, Component.text("Lustrous Smudger")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    DRACONIC_SMUDGER(Material.GOLDEN_AXE, Component.text("Draconic Smudger")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    POSSESSED_SMUDGER(Material.NETHERITE_AXE, Component.text("Possessed Smudger")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.RED),
    //Trider
    IMPRUDENT_TRIDER(Material.TRIDENT, Component.text("Imprudent Trider")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    LOYAL_TRIDER(Material.TRIDENT, Component.text("Loyal Trider")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    FERROUS_TRIDER(Material.TRIDENT, Component.text("Ferrous Trider")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    ANGELIC_TRIDER(Material.TRIDENT, Component.text("Angelic Trider")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    HELLISH_TRIDER(Material.TRIDENT, Component.text("Hellish Trider")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    MANIC_TRIDER(Material.TRIDENT, Component.text("Manic Trider")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.RED),
    //Leather armor
    LEATHER_HELMET(Material.LEATHER_HELMET, Component.text("Leather Helmet")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    LEATHER_CHESTPLATE(Material.LEATHER_CHESTPLATE, Component.text("Leather Chestplate")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    LEATHER_LEGGINGS(Material.LEATHER_LEGGINGS, Component.text("Leather Leggings")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    LEATHER_BOOTS(Material.LEATHER_BOOTS, Component.text("Leather Boots")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GRAY),
    //Chainmail armor
    CHAINMAIL_HELMET(Material.CHAINMAIL_HELMET, Component.text("Chainmail Helmet")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    CHAINMAIL_CHESTPLATE(Material.CHAINMAIL_CHESTPLATE, Component.text("Chainmail Chestplate")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    CHAINMAIL_LEGGINGS(Material.CHAINMAIL_LEGGINGS, Component.text("Chainmail Leggings")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    CHAINMAIL_BOOTS(Material.CHAINMAIL_BOOTS, Component.text("Chainmail Boots")
            .decoration(TextDecoration.ITALIC, false), ColorHelper.MID_GREEN),
    //Iron armor
    IRON_HELMET(Material.IRON_HELMET, Component.text("Iron Helmet")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    IRON_CHESTPLATE(Material.IRON_CHESTPLATE, Component.text("Iron Chestplate")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    IRON_LEGGINGS(Material.IRON_LEGGINGS, Component.text("Iron Leggings")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    IRON_BOOTS(Material.IRON_BOOTS, Component.text("Iron Boots")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.BLUE),
    //Diamond armor
    DIAMOND_HELMET(Material.DIAMOND_HELMET, Component.text("Diamond Helmet")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    DIAMOND_CHESTPLATE(Material.DIAMOND_CHESTPLATE, Component.text("Diamond Chestplate")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    DIAMOND_LEGGINGS(Material.DIAMOND_LEGGINGS, Component.text("Diamond Leggings")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    DIAMOND_BOOTS(Material.DIAMOND_BOOTS, Component.text("Diamond Boots")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.LIGHT_PURPLE),
    //Netherite armor
    NETHERITE_HELMET(Material.NETHERITE_HELMET, Component.text("Netherite Helmet")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    NETHERITE_CHESTPLATE(Material.NETHERITE_CHESTPLATE, Component.text("Netherite Chestplate")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    NETHERITE_LEGGINGS(Material.NETHERITE_LEGGINGS, Component.text("Netherite Leggings")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD),
    NETHERITE_BOOTS(Material.NETHERITE_BOOTS, Component.text("Netherite Boots")
            .decoration(TextDecoration.ITALIC, false), NamedTextColor.GOLD);

    private final Material type;
    private final TextComponent comp;
    private final TextColor color;
    ItemType(Material type, TextComponent name, TextColor color) {
        this.type = type;
        this.comp = name;
        this.color = color;
    }

    public Material getMaterial() {
        return type;
    }

    /**
     * Creates a 'dummy' version of an item present in MobMadness.
     * This item is likely to not work in a game, as is for testing purposes only.
     *
     * @param plugin instance of the main class
     * @param game if applicable, data class of the game the item's for
     * @param type the type of item to create
     * @param amount amount of items to create
     * @return the MadItem, or null if no valid ItemType is provided.
     */
    public static MadItem createDummyItem(MobMadness plugin, Game game, ItemType type, int amount) {
        switch (type) {
            case IRON_NUGGET: return new IronNugget(plugin, amount, game);
            case GOLD_NUGGET: return new GoldNugget(plugin, amount, game);
            case CRYSTAL: return new Crystal(plugin, amount, game);
            case COMPASS: return new Compass(plugin, game);
            case BLANK_ITEM:
                break;
            case BLOCKED_ITEM:
                break;
            case LIFE_ELIXIR: return new LifeElixir(plugin, amount, game);
            case AETHERS_BERRIES: return new AethersBerries(plugin, amount, game);
            case GREATER_HEALTH_POTION:
                break;
            case HEALTH_POTION: return new HealthPotion(plugin, game);
            case ARROWS: return new Arrows(plugin, amount, game);
            case RUNIC_ARROW:
                break;
            case COOKIE: return new Cookie(plugin, amount, game);
            case TOTEM_OF_UNDYING: return new TotemOfUndying(plugin, game);
            case THROWABLE_TNT: return new ThrowableTNT(plugin, amount, game);
            case BHG: return new BottledHealingGas(plugin,0, game);
            case CHORUS_FRUIT: return new ChorusFruit(plugin, amount, game);
            case ENDER_AID:
                break;
            case VOID_ORB: return new VoidOrb(plugin, amount, game);
            case VEILING_POTION: return new VeilingPotion(plugin, amount, game);
            case BARRICADE: return new Barricade(plugin, amount, game);
            case FROST_ORB:
                break;
            case RUGGED_ORB:
                break;
            case DETONATION_ORB: return new DetonationOrb(plugin, amount, game);
            //WEAPONS
            case OLD_SWORD: return new Sword(type,
                    plugin, game);
            case STONE_SWORD: return new Sword(type,
                    plugin, game);
            case BROADSWORD: return new Sword(type,
                    plugin, game);
            case BEAM_SWORD: return new Sword(type,
                    plugin, game);
            case GLEAMING_SWORD: return new Sword(type,
                    plugin, game);
            case EXECUTIONERS_SWORD: return new Sword(type,
                    plugin, game);

            case CROSS_SHIELD:
            case PINK_LOTUS_SHIELD:
            case PRISMARINE_SHIELD:
            case MOSSY_SHIELD:
            case GLORIOUS_SHIELD:
            case WITHERS_SHIELD:
                return new Shield(type, plugin, game);

            case FRAIL_BOW:
            case SOLID_BOW:
            case REINFORCED_BOW:
            case PULSE_BOW:
            case BLAZING_BOW:
            case SHADOW_BOW:
                return new Bow(type, plugin, game);

            case FRAIL_CROSSBOW:
            case STEADY_CROSSBOW:
            case ENHANCED_CROSSBOW:
            case BLASTING_CROSSBOW:
            case FIERY_CROSSBOW:
            case CERBERUS_CROSSBOW:
                return new Crossbow(type,
                        plugin, game);

            case WOODEN_SCEPTER: return new Scepter(type,
                    plugin, game);
            case ANCIENT_SCEPTER: return new Scepter(type,
                    plugin, game);
            case MANASTEEL_SCEPTER: return new Scepter(type,
                    plugin, game);
            case CRYSTALLINE_SCEPTER: return new Scepter(type,
                    plugin, game);
            case INFERNAL_SCEPTER: return new Scepter(type,
                    plugin, game);
            case ARMAGEDDON_SCEPTER: return new Scepter(type,
                    plugin, game);

            case SLOW_RAYLER: return new Rayler(type,
                    plugin, game);
            case IMPROVED_RAYLER: return new Rayler(type,
                    plugin, game);
            case TITANS_RAYLER: return new Rayler(type,
                    plugin, game);
            case VIRTUOUS_RAYLER: return new Rayler(type,
                    plugin, game);
            case ERUPTING_RAYLER: return new Rayler(type,
                    plugin, game);
            case JUDGEMENT_RAYLER: return new Rayler(type,
                    plugin, game);

            case DEFECTIVE_SMUDGER: return new Smudger(type,
                    plugin, game);
            case PLEASANT_SMUDGER: return new Smudger(type,
                    plugin, game);
            case SILVER_PLATED_SMUDGER: return new Smudger(type,
                    plugin, game);
            case LUSTROUS_SMUDGER: return new Smudger(type,
                    plugin, game);
            case DRACONIC_SMUDGER: return new Smudger(type,
                    plugin, game);
            case POSSESSED_SMUDGER: return new Smudger(type,
                    plugin, game);

            case IMPRUDENT_TRIDER:
            case LOYAL_TRIDER:
            case FERROUS_TRIDER:
            case ANGELIC_TRIDER:
            case HELLISH_TRIDER:
            case MANIC_TRIDER:
                return new Trider(type, plugin, game);

            case LEATHER_HELMET: return new Helmet(plugin, game, type);
            case LEATHER_CHESTPLATE:
                return new Chestplate(plugin, game, type);
            case LEATHER_LEGGINGS:
                return new Leggings(plugin, game, type);
            case LEATHER_BOOTS: return new Leggings(plugin, game, type);

            case CHAINMAIL_HELMET: return new Helmet(plugin, game, type);
            case CHAINMAIL_CHESTPLATE:
                return new Chestplate(plugin, game, type);
            case CHAINMAIL_LEGGINGS:
                return new Leggings(plugin, game, type);
            case CHAINMAIL_BOOTS: return new Leggings(plugin, game, type);

            case IRON_HELMET: return new Helmet(plugin, game, type);
            case IRON_CHESTPLATE:
                return new Chestplate(plugin, game, type);
            case IRON_LEGGINGS: return new Leggings(plugin, game, type);
            case IRON_BOOTS: return new Leggings(plugin, game, type);

            case DIAMOND_HELMET: return new Helmet(plugin, game, type);
            case DIAMOND_CHESTPLATE:
                return new Chestplate(plugin, game, type);
            case DIAMOND_LEGGINGS:
                return new Leggings(plugin, game, type);
            case DIAMOND_BOOTS: return new Leggings(plugin, game, type);

            case NETHERITE_HELMET: return new Helmet(plugin, game, type);
            case NETHERITE_CHESTPLATE:
                return new Chestplate(plugin, game, type);
            case NETHERITE_LEGGINGS:
                return new Leggings(plugin, game, type);
            case NETHERITE_BOOTS: return new Leggings(plugin, game, type);
        }
        return null;
    }

    public TextComponent getComp() {
        return comp;
    }

    public TextColor getColor() {
        return color;
    }
}
