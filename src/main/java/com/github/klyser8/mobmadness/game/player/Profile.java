package com.github.klyser8.mobmadness.game.player;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.shop.ShopInventory;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Class which stores player information regarding a Mob Madness game.
 */
public class Profile {

    //HP which players start with.
    public static final int STARTING_HP = 10;

    private final MobMadness plugin;

    //Instance of Player related to this player.
    private final Player player;
    //UUID of the player.
    private final UUID uuid;
    //The username of the player.
    private final String name;

    //The game the player is currently in.
    private final GameData currentGame;
    //The weapon the player's currently using.
    private MadWeapon currentWeapon;
    //Whether the XP bar is a mana bar or not.
    private boolean isXPMana = false;
    //Whether the ultimate is currently on cooldown or not
    private boolean isOnCooldown = false;
    //How many health ups a player has had. Usually achieved through Life Elixirs.
    private int healthUpAmount = 0;

    private ShopInventory playerShop;

    //Profile loop.
    private final ProfileRunnable runnable;

    public Profile(MobMadness plugin, Player player, GameData currentGame) {
        this.plugin = plugin;
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.currentGame = currentGame;
        runnable = new ProfileRunnable(this, currentGame);
        runnable.runTaskTimer(plugin, 0, ProfileRunnable.TICK_INTERVAL);
        player.setExp(0.99f);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(STARTING_HP);
        player.setHealth(Profile.STARTING_HP);
        playerShop = new ShopInventory(plugin, currentGame, this);
    }

    /**
     * Gets the {@link Player} object related to this profile.
     *
     * @return {@link #player}
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the {@link UUID} object related to this profile.
     *
     * @return {@link #uuid}
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets the name of the player
     *
     * @return {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an instance of {@link GameData} which the player is present in.
     *
     * @return {@link #currentGame}
     */
    public GameData getCurrentGame() {
        return currentGame;
    }

    /**
     * Returns whether the experience bar of the player is used to display one's mana, or one's ultimate cooldown.
     * Depends on the current weapon used by the player.
     *
     * @return true if the XP bar displays the player's Mana, false if it displays the ultimate cooldown
     */
    public boolean isXPMana() {
        return isXPMana;
    }

    /**
     * Sets whether the experience bar of the player should display mana or not.
     *
     * @param xpMana whether the XP bar should display mana
     */
    public void setXPMana(boolean xpMana) {
        this.isXPMana = xpMana;
        isOnCooldown = false;                                                   //Reset cooldown once this is updated
    }

    /**
     * Gets the current weapon the player is using.
     *
     * @return {@link #currentWeapon}
     */
    public MadWeapon getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     * Sets the weapon the player is currently using.
     *
     * @param currentWeapon the new weapon
     */
    public void setCurrentWeapon(MadWeapon currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    /**
     * Stops the profile runnable
     */
    public void stopProfileRunnable() {
        runnable.cancel();
    }

    /**
     * Starts an XP bar cooldown. The cooldown will be displayed on the XP bar, updated twice a second.
     *
     * @param ticks how long the cooldown should be in ticks
     */
    public void startCooldown(int ticks) {
        if (isOnCooldown) return;
        int period = 5;
        player.setExp(0.00f);
        isOnCooldown = true;
        new BukkitRunnable() {
            int clock = ticks / period;
            final float xpStep = 0.99f / clock;
            @Override
            public void run() {
                if (player.getExp() + xpStep < 1.00) {
                    player.setExp(player.getExp() + xpStep);
                }
                clock--;
                if (clock == 0) {
                    isOnCooldown = false;
                }
                if (!isOnCooldown) {
                    player.setExp(0.99f);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, period);
    }

    /**
     * Checks whether the player's current ultimate ability is on cooldown.
     *
     * @return whether the ultimate ability is on cooldown
     */
    public boolean isOnCooldown() {
        return isOnCooldown;
    }

    /**
     * Resets any player cooldown.
     */
    public void stopCooldown() {
        isOnCooldown = false;
    }

    /**
     * Opens the player's shop to the player.
     */
    public void openPlayerShop() {
        Sound sound = new Sound("entity.horse.saddle", 1.0f, 1.25f);
        sound.play(player.getLocation(), SoundCategory.PLAYERS, player);
        player.openInventory(playerShop.getShopInventory());
    }

    /**
     * Replaces the current instance of {@link ShopInventory} stored in {@link #playerShop} with a new one.
     */
    public void rerollShop() {
        playerShop = new ShopInventory(plugin, currentGame, this);
    }

    public ShopInventory getPlayerShop() {
        return playerShop;
    }

    public int getHealthUpAmount() {
        return healthUpAmount;
    }

    public void setHealthUpAmount(int healthUpAmount) {
        this.healthUpAmount = healthUpAmount;
    }
}
