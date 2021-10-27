package com.github.klyser8.mobmadness.game.items.weapons;

import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import org.bukkit.entity.Player;

public interface MadWeapon {

    String WEAPON_TAG = "mm.weapon";

    void createData(MadItem item);

    ItemType getType();

}
