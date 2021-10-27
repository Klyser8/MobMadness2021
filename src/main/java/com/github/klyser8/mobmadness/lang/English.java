package com.github.klyser8.mobmadness.lang;

import org.bukkit.entity.Player;

/**
 * Language class storing messages sent to players.
 */
public enum English {

    ERROR_MAP_TAKEN("&cThe map you have selected is already in use!"),
    ERROR_NO_GAME("&cThere is no ongoing game in this map!"),
    ERROR_CANNOT_DROP_ITEM("&cYou cannot drop this item!"),
    ERROR_INVENTORY_FULL("&cYour inventory is full!"),

    STOPPED_ALL_GAMES("&6All games have been stopped."),
    STOPPED_GAME("&f<game>&6 has been stopped."),

    SIXTY_SECONDS_MESSAGE("&eThe game will start in&f 60&e seconds!"),
    THIRTY_SECONDS_MESSAGE("&eThe game will start in&f 30&e seconds!"),
    TEN_SECONDS_MESSAGE("&6The game will start in&e 10&6 seconds!"),
    COUNTDOWN_MESSAGE("&cThe game will start in&f <time>&c second(s)!"),

    GAME_STARTED("&b&lTHE MADNESS HAS BEGUN!"),
    GAME_OVER("&4lGAME OVER!&f <player> &c died after the &5Respawn Anchor&c's supply " +
            "of &eAether's Berries &cran out."),

    PLAYER_JOIN("&f<player>&a has joined the game."),
    PLAYER_LEAVE("&f<player>&6 has left the game.");

    private final String msg;

    English(String msg) {
        this.msg = msg;
    }

    public String getMsg(Player player) {
        return player == null ? msg : msg.replace("<player>", player.getName());
    }
}
