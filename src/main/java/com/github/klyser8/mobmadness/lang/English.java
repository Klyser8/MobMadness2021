package com.github.klyser8.mobmadness.lang;

import com.github.klyser8.mobmadness.game.Tier;
import com.github.klyser8.mobmadness.game.world.MapType;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

/**
 * Language class storing messages sent to players.
 */
public enum English {

    ERROR_MAP_TAKEN(Component.text("The map you have selected is already in use!").color(NamedTextColor.RED)),
    ERROR_NO_GAME(Component.text("There is no ongoing game in this map!").color(NamedTextColor.RED)),
    ERROR_CANNOT_DROP_ITEM(Component.text("You cannot drop this item!").color(NamedTextColor.RED)),
    ERROR_INVENTORY_FULL(Component.text("Your inventory is full!").color(NamedTextColor.RED)),

    STOPPED_ALL_GAMES(Component.text("All games have been stopped.").color(NamedTextColor.GOLD)),
    STOPPED_GAME(Component.text("<maptype> ").color(NamedTextColor.WHITE)
            .append(Component.text(" has been stopped.").color(NamedTextColor.GOLD))),

    SIXTY_SECONDS_MESSAGE(Component.text("The game will start in ").color(NamedTextColor.YELLOW)
            .append(Component.text("60 ").color(NamedTextColor.WHITE))
            .append(Component.text("seconds!").color(NamedTextColor.YELLOW))),
    THIRTY_SECONDS_MESSAGE(Component.text("The game will start in ").color(NamedTextColor.GOLD)
            .append(Component.text("30 ").color(NamedTextColor.YELLOW))
            .append(Component.text("seconds!").color(NamedTextColor.GOLD))),
    TEN_SECONDS_MESSAGE(Component.text("The game will start in ").color(ColorHelper.ORANGE)
            .append(Component.text("10 ").color(NamedTextColor.GOLD))
            .append(Component.text("seconds!").color(ColorHelper.ORANGE))),
    COUNTDOWN_MESSAGE(Component.text("The game will start in ").color(NamedTextColor.RED)
            .append(Component.text("<time> ").color(ColorHelper.ORANGE))
            .append(Component.text("second(s)!").color(NamedTextColor.RED))),

    GAME_STARTED(Component.text("THE MADNESS HAS BEGUN!").color(ColorHelper.REAL_AQUA)
            .decoration(TextDecoration.BOLD, true)),
    TIER_INCREASE_TITLE(Component.text("New Game Tier!").color(NamedTextColor.YELLOW)
            .decoration(TextDecoration.BOLD, true)),
    TIER_INCREASE_SUBTITLE(Component.text("Tier reached: ").color(NamedTextColor.GOLD)
            .append(Component.text("<tier>").color(NamedTextColor.WHITE))),
    TIER_DECREASE_MSG(Component.text("The game tier has decreased to: ").color(NamedTextColor.RED)
            .decoration(TextDecoration.BOLD, true)
            .append(Component.text("<tier>").color(NamedTextColor.WHITE))),
    GAME_OVER(Component.text("GAME OVER! ").color(NamedTextColor.DARK_RED).decoration(TextDecoration.BOLD, true)
            .append(Component.text("<player> ").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false))
            .append(Component.text("died after the ").color(NamedTextColor.RED))
            .append(Component.text("Respawn Anchor ").color(ColorHelper.INDIGO))
            .append(Component.text("'s supply of ").color(NamedTextColor.RED))
            .append(Component.text("Aether's Berries ").color(ColorHelper.HONEYSACKLE))
            .append(Component.text("ran out").color(NamedTextColor.RED))),
    SPECIAL_ABILITY_READY(Component.text("Your weapon's special ability is ready").color(ColorHelper.INDIGO)),

    PLAYER_JOIN(Component.text("<player> ").color(NamedTextColor.WHITE)
            .append(Component.text("has joined the game").color(NamedTextColor.GREEN))),
    PLAYER_LEAVE(Component.text("<player> ").color(NamedTextColor.WHITE)
        .append(Component.text("has left the game").color(NamedTextColor.GOLD)));

    private final TextComponent comp;

    English(TextComponent comp) {
        this.comp = comp;
    }

    public TextComponent getComponent() {
        return comp;
    }

    public TextComponent getComponent(Player player) {
        return player == null ? comp : (TextComponent) comp.replaceText(TextReplacementConfig.builder().match("<player>")
                .replacement(player.getName()).build()).asComponent();
    }

    public TextComponent getComponent(MapType type) {
        return type == null ? comp : (TextComponent) comp.replaceText(TextReplacementConfig.builder().match("<maptype>")
                .replacement(type.getName()).build()).asComponent();
    }

    public TextComponent getComponent(int time) {
        return time == 0 ? comp : (TextComponent) comp.replaceText(TextReplacementConfig.builder().match("<time>")
                .replacement(String.valueOf(time)).build()).asComponent();
    }

    public TextComponent getComponent(Tier tier) {
        return (TextComponent) comp.replaceText(TextReplacementConfig.builder().match("<tier>")
                .replacement(tier.getTierComp().color(tier.getTierColor())).build()).asComponent();
    }
}
