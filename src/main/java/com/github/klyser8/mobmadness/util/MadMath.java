package com.github.klyser8.mobmadness.util;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.bow.Bow;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Util class which stores static methods useful to perform math in multiple classes.
 * These methods are general as they can be mostly used outside of Minecraft as well.
 */
public class MadMath {

    /** Checks if a String is an integer.
     *
     * @param string any string
     * @return whether the string is an integer.
     */
    public boolean isInteger(String str) {
        try
        {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }


    /** Checks if a String is a double.
     *
     * @param string any string
     * @return whether the string is an double.
     */
    public static boolean isDouble(String string) {
        try
        {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    /** Returns an enum from a selection of enums.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        Random random = new Random();
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    /**
     * Gets a random rounded double between two numbers.
     * @param lowNum the bottom boundary
     * @param highNum the top boundary
     * @return the random number.
     */
    public static double randomDoubleBetween(Double lowNum, Double highNum) {
        Random random = new Random();
        return random.nextInt((int) (highNum - lowNum + 1)) + lowNum;
    }

    /**
     * Rounds to the provided amount of decimal places
     *
     * @param value the value to round
     * @param precision the amount of decimal places to round to
     * @return the rounded number
     */
    public double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    /**
     * Says hello to the chosen person
     *
     * @param toWho the name of the person
     */
    public void sayHello(String toWho) {
        System.out.println("Hello, " + toWho + "!");
    }

    
}
