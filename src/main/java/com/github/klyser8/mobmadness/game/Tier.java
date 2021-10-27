package com.github.klyser8.mobmadness.game;

/**
 * Tiers for Mob Madness. Each tier has an overall theme, and increases in difficulty.
 *
 */
public enum Tier {

    //Rudimentary, Classic.
    I (1, "&7I", 30, 0),
    //Ancient, Forgotten.
    II (2, "&2II", 60, 500),
    //Futuristic, Metallic.
    III (3, "&9III", 120, 1500),
    //Angelic, Heaven.
    IV (4, "&dIV", 200, 3000),
    //Infernal, hell.
    V (5, "&6V", 300, 5000),
    //Void.
    VI (6, "&cVI", 350, 7500);

    //The tier written as a number.
    private final int numericTier;
    //The tier, displayed and colored as a string.
    private final String tierString;
    //The default maximum weight for the tier. Will be affected by other modifiers.
    private final int defaultMaxWeight;
    //The lowest amount the avg score of the game needs to be to reach specified tier.
    private final int minScore;

    Tier(int numericTier, String tierString, int defaultMaxWeight, int minScore) {
        this.numericTier = numericTier;
        this.tierString = tierString;
        this.defaultMaxWeight = defaultMaxWeight;
        this.minScore = minScore;
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
