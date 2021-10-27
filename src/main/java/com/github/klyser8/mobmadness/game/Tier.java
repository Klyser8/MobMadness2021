package com.github.klyser8.mobmadness.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

/**
 * Tiers for Mob Madness. Each tier has an overall theme, and increases in difficulty.
 *
 */
public enum Tier {

    //Rudimentary, Classic.
    I (1, Component.text("I"), NamedTextColor.GRAY, 30, 0),
    //Ancient, Forgotten.
    II (2, Component.text("II"), NamedTextColor.DARK_GREEN, 60, 500),
    //Futuristic, Metallic.
    III (3, Component.text("III"), NamedTextColor.DARK_AQUA, 120, 1500),
    //Angelic, Heaven.
    IV (4, Component.text("IV"), NamedTextColor.LIGHT_PURPLE, 200, 3000),
    //Infernal, hell.
    V (5, Component.text("V"), NamedTextColor.GOLD, 300, 5000),
    //Void.
    VI (6, Component.text("VI"), NamedTextColor.RED, 350, 7500);

    //The tier written as a number.
    private final int numericTier;
    //The tier, displayed as a string.
    private final TextComponent tierComp;
    //The tier's color.
    private final TextColor color;
    //The default maximum weight for the tier. Will be affected by other modifiers.
    private final int defaultMaxWeight;
    //The lowest amount the avg score of the game needs to be to reach specified tier.
    private final int minScore;

    Tier(int numericTier, TextComponent tierComp, TextColor color, int defaultMaxWeight, int minScore) {
        this.numericTier = numericTier;
        this.tierComp = tierComp;
        this.color = color;
        this.defaultMaxWeight = defaultMaxWeight;
        this.minScore = minScore;
    }

    public Integer getNumericTier() {
        return numericTier;
    }

    public TextComponent getTierComp() {
        return tierComp;
    }

    public int getDefaultMaxWeight() {
        return defaultMaxWeight;
    }

    public TextColor getTierColor() {
        return color;
    }

    public int getMinScore() {
        return minScore;
    }

    /**
     * Returns a tier enum based on the score provided.
     *
     * @param score
     * @return
     */
    public static Tier getTierUsingScore(int score) {
        if (score < I.minScore) {
            return I;
        } else if (score < II.minScore) {
            return II;
        } else if (score < III.minScore) {
            return III;
        } else if (score < IV.minScore) {
            return IV;
        } else if (score < V.minScore) {
            return V;
        } else {
            return VI;
        }
    }
}
