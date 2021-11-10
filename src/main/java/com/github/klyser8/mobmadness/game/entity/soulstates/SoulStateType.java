package com.github.klyser8.mobmadness.game.entity.soulstates;

import com.github.klyser8.mobmadness.game.entity.Soul;

public enum SoulStateType {

    //Marked entities take extra damage from the next arrow which hits them
    MARKED,
    //Dazzled entities cannot target players
    DAZZLED,
    //Hasted entities move 10% x level faster
    HASTED;


    /**
     * Checks if this Soul has a specified soul state or not. If it does, the Soul State's class is returned.
     *
     * @param type The type of soul state that should be looked for
     * @return the soul state's class, or null if it was not found
     */
    public static <T extends SoulState> T getSoulStateIfPresent(Soul soul, SoulStateType type) {
        return soul.getCurrentSoulStates().containsKey(type) ? (T) soul.getCurrentSoulStates().get(type) : null;
    }
}
