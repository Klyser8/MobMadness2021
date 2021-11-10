package com.github.klyser8.mobmadness.game.entity;

import com.github.klyser8.mobmadness.game.entity.soulstates.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scoreboard.Team;

import java.util.Map;

/**
 * Represents an entity which possesses a soul.
 * Entity's with a soul may have Soul States applied to it.
 */
public interface Soul {

    /**
     * Gets a map containing the current soul state types and their respective soul state object.
     * @return a map containing the current soul states.
     */
    Map<SoulStateType, SoulState> getCurrentSoulStates();

    /**
     * Method used to return an instance of the entity which soul is attached to.
     * @return the entity of the soul
     */
    LivingEntity getLivingEntity();

}
