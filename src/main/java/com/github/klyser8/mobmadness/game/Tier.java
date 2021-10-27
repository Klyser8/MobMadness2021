package com.github.klyser8.mobmadness.game;

/**
 * Tiers for Mob Madness. Each tier has an overall theme, and increases in difficulty.
 *
 */
public enum Tier {

    //Rudimentary, Classic.
    I (1, "&7I", 30),
    //Ancient, Forgotten.
    II (2, "&2II", 60),
    //Futuristic, Metallic.
    III (3, "&9III", 120),
    //Angelic, Heaven.
    IV (4, "&dIV", 200),
    //Infernal, hell.
    V (5, "&6V", 300),
    //Void.
    VI (6, "&cVI", 350);

    //The tier written as a number.
    private final int numericTier;
    //The tier, displayed and colored as a string.
    private final String tierString;
    //The default maximum weight for the tier. Will be affected by other modifiers.
    private final int defaultMaxWeight;

    Tier(int numericTier, String tierString, int defaultMaxWeight) {
        this.numericTier = numericTier;
        this.tierString = tierString;
        this.defaultMaxWeight = defaultMaxWeight;
    }

    public Integer getNumericTier() {
        return numericTier;
    }

    public String getTierString() {
        return tierString;
    }

    public int getDefaultMaxWeight() {
        return defaultMaxWeight;
    }

    public String getColorCode() {
        return tierString.substring(0, 2);
    }
}
