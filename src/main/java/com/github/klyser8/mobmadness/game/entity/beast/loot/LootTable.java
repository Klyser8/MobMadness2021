package com.github.klyser8.mobmadness.game.entity.beast.loot;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows for creation of loot tables.
 */
public class LootTable {

    //A list containing what items are present in the loot table.
    private List<Entry> entries;

    public LootTable(List<Entry> entries) {
        this.entries = entries;
    }

    /**
     * Calculates which item will be created from the loot table.
     *
     * @return the chosen item.
     */
    public ItemStack calculate() {
        double random = Math.random();

        for (int i = 0; i < entries.size() - 1; i++) {
            Entry entry = entries.get(i + 1);
            if (entry.getChance() > random)
                return entries.get(i).getItem();
        }

        return entries.get(entries.size() - 1).getItem();
    }

    /**
     * Builder for loot tables.
     */
    public static class LootTableBuilder {

        private int totalWeight = 0;
        //List containing what items are present in the loot table currently.
        private List<Entry> entries = new ArrayList<>();

        /**
         * Adds an item to the entries list.
         *
         * @param item the itemstack to be added.
         * @param weight the weight of the item. The weight of an item changes the likelihood of it being chosen when
         *               the loot table is calculated.
         * @return this class.
         */
        public LootTableBuilder add(ItemStack item, int weight) {
            totalWeight += weight;
            entries.add(new Entry(item, weight));
            return this;
        }

        /**
         * Checks whether the loot table has been built yet or not. Checks the entries list and the total weight.
         *
         * @return true if entries.size is above 0 and totalweight is above 0.
         */
        public boolean isBuilt() {
            return entries.size() > 0 && totalWeight > 0;
        }

        /**
         * Builds the loot table.
         *
         * @return the built loot table, or null if the table cannot be built due to the list of entries is empty.
         */
        public LootTable build() {
            if (!isBuilt()) return null;

            double base = 0;
            for (Entry entry : entries) {
                double chance = getChance(base);
                entry.setChance(chance);
                base += entry.getWeight();
            }

            return new LootTable(entries);
        }

        /**
         * Converts the weight of the item to a double (from 0.0 to 1.0)
         *
         * @param weight the weight of the current item
         * @return a double from 0.0 to 1.0.
         */
        private double getChance(double weight) {
            return weight / totalWeight;
        }
    }

}
