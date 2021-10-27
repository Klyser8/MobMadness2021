package com.github.klyser8.mobmadness.game.items.equipment.shield;

import com.github.klyser8.mobmadness.game.items.ItemType;

public class ShieldData {

    protected final ItemType type;
    private int durability;
    private int maxDurability;


    public ShieldData(ItemType type) {
        this.type = type;
        setup();
    }

    private void setup() {
        switch (type) {
            case CROSS_SHIELD:
                maxDurability = 32;
                break;
            case MOSSY_SHIELD:
                maxDurability = 48;
                break;
            case PRISMARINE_SHIELD:
                maxDurability = 72;
                break;
            case PINK_LOTUS_SHIELD:
                maxDurability = 108;
                break;
            case GLORIOUS_SHIELD:
                maxDurability = 162;
                break;
            case WITHERS_SHIELD:
                maxDurability = 256;
                break;
        }
        durability = maxDurability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }
}
