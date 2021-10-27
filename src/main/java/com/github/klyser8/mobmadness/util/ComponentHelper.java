package com.github.klyser8.mobmadness.util;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;

import java.time.Duration;

/**
 * Util class to help with sending text components
 */
public class ComponentHelper {

    /**
     * Util method to ease creating titles and sending them to players.
     *
     * @param player the player to send the title to
     * @param titleComp the text component containing the title string
     * @param subTitleComp the text component containing the subtitle string
     * @param fadeIn how long the title should take fade in (in ticks)
     * @param stay how long the title should stay on the screen (in ticks)
     * @param fadeOut how long the title should take to fade out (in ticks)
     */
    public static void sendPlayerTitle(Player player, TextComponent titleComp, TextComponent subTitleComp,
                                       int fadeIn, int stay, int fadeOut) {
        Title title = Title.title(titleComp, subTitleComp, Title.Times.of(
                Duration.ofMillis(50L * fadeIn), Duration.ofMillis(50L * stay), Duration.ofMillis(50L * fadeOut)));
        player.showTitle(title);
    }

}