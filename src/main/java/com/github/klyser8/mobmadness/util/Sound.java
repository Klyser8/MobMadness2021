package com.github.klyser8.mobmadness.util;

import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * Utility class, used to create Sound objects.
 * These sounds can be played to specific players, or to everyone in a specific location of the world.
 */
public class Sound {

    private final String soundName;
    private float pitch;
    private float volume;

    public Sound(String name, float volume, float pitch) {
        this.soundName = name;
        this.pitch = pitch;
        this.volume = volume;
    }

    /**
     * Gets the name of name of the sound.
     *
     * @return the name of the sound
     */
    public String getSoundName() {
        return soundName;
    }

    /**
     * Gets the pitch of the sound, between 0.5f and 2.0f
     *
     * @return the pitch of the sound
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch of the sound.
     * Pitch must be between 0.5f and 2.0f.
     *
     * @param pitch the new pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Gets the volume of the sound.
     * The volume of the sound dictates the distance the sound can be heard from.
     *
     * @return the volume of the sound
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Sets the volume of the sound.
     * Values below 1.0f will decrease the volume of the sound, while values above will increase the distance
     * at which the sound can be heard by (Volume * 16).
     *
     * @param volume the volume to play the sound at
     */
    public void setVolume(float volume) {
        this.volume = volume;
    }

    /**
     * Plays the sound at a location, through the specified sound category, to a set of players.
     *
     * @param loc the location of the sound
     * @param category the category of the sound
     * @param players the players to play the sound to
     */
    public void play(Location loc, SoundCategory category, Set<Player> players) {
        for (Player player : players) {
            player.playSound(loc, soundName, category, volume, pitch);
        }
    }

    /**
     * Plays the sound at a location, through the specified sound category, to a list of players.
     *
     * @param loc the location of the sound
     * @param category the category of the sound
     * @param players the players to play the sound to
     */
    public void play(Location loc, SoundCategory category, List<Player> players) {
        for (Player player : players) {
            player.playSound(loc, soundName, category, volume, pitch);
        }
    }

    /**
     * Plays the sound at a location, to a list of players.
     *
     * @param loc the location of the sound
     * @param players the players to play the sound to
     */
    public void play(Location loc, List<Player> players) {
        for (Player player : players) {
            player.playSound(loc, soundName, volume, pitch);
        }
    }

    /**
     * Plays the sound at a location, to a set of players.
     *
     * @param loc the location of the sound
     * @param players the players to play the sound to
     */
    public void play(Location loc, Set<Player> players) {
        for (Player player : players) {
            player.playSound(loc, soundName, volume, pitch);
        }
    }

    /**
     * Plays the sound at a location, through the specified sound category, to a player.
     *
     * @param loc the location of the sound
     * @param category the category of the sound
     * @param player the player to play the sound to
     */
    public void play(Location loc, SoundCategory category, Player player) {
        player.playSound(loc, soundName, category, volume, pitch);
    }

    /**
     * Plays the sound at a location, to a player.
     *
     * @param loc the location of the sound
     * @param player the player to play the sound to
     */
    public void play(Location loc, Player player) {
        player.playSound(loc, soundName, volume, pitch);
    }

    /**
     * Plays the sound at a location, through the specified sound category.
     *
     * @param loc the location of the sound
     * @param category the category of the sound
     */
    public void play(Location loc, SoundCategory category) {
        if (loc.getWorld() == null) return;
        loc.getWorld().playSound(loc, soundName, category, volume, pitch);
    }

    /**
     * Plays the sound at a location.
     *
     * @param loc the location of the sound
     */
    public void play(Location loc) {
        if (loc.getWorld() == null) return;
        loc.getWorld().playSound(loc, soundName, volume, pitch);
    }
}
