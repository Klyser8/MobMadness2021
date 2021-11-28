package com.github.klyser8.mobmadness.game.entity.player;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.ui.UIHelper;
import com.github.klyser8.mobmadness.util.ColorHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.github.klyser8.mobmadness.ui.UIHelper.createHpBar;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class ScoreboardRunnable implements Runnable {

    private final MobMadness plugin;
    private final Profile profile;
    private final Game game;
    private final Scoreboard board;
    public ScoreboardRunnable(MobMadness plugin, Profile profile) {
        this.plugin = plugin;
        this.profile = profile;
        game = profile.getCurrentGame();
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        //Objective name - Sets the 'title' of the scoreboard to the last value.
        Objective sideBar = board.registerNewObjective("sideBar", "",
                text("Respawn Charges: ").color(ColorHelper.INDIGO)
                        .append(text(game.getAnchorCharges()).color(ColorHelper.HONEYSACKLE))
                        .append(text("/").color(ColorHelper.METALLIC_BLUE))
                        .append(text("4").color(RED)));
        sideBar.setDisplaySlot(DisplaySlot.SIDEBAR);
/*        Score name = sideBar.getScore(ChatColor.GRAY + "Ubemubbe");
        name.setScore(1);*/
//        sideBar.getScore("").setScore(game.getPlayers().size());
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = new ArrayList<>(game.getPlayers().keySet()).get(i);
            addPlayerToScoreboard(player, i);
        }
        profile.getPlayer().setScoreboard(board);
    }

    @Override
    public void run() {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = new ArrayList<>(game.getPlayers().keySet()).get(i);
            String name = player.getName();
            if (board.getTeam(name) == null) {
                addPlayerToScoreboard(player, i);
            } else {
                Component[] bars = createHpBar(game.getPlayers().get(player), 0.0, true);
                if (bars == null) continue;
                //noinspection ConstantConditions
                board.getTeam(name).prefix(bars[0].append(text(" ")));
            }
            Objective obj = board.getObjective("sideBar");
            if (obj != null) {
                obj.displayName(text("Respawn Charges: ").color(ColorHelper.INDIGO)
                        .append(text(game.getAnchorCharges()).color(ColorHelper.HONEYSACKLE))
                        .append(text("/").color(ColorHelper.INDIGO))
                        .append(text("4").color(RED)));
            }
            player.sendPlayerListHeaderAndFooter(
                    text("Average score: ").color(ColorHelper.MID_GREEN)
                            .append(text(game.calculateAvgScore()).color(ColorHelper.HONEYSACKLE))
                            .append(text("\nBound: ").color(ColorHelper.METALLIC_BLUE))
                            .append(text(game.getBound()).color(ColorHelper.HONEYSACKLE)),
                    text("Weight: ").color(ColorHelper.ORANGE)
                            .append(text(game.getCurrentWeight()).color(ColorHelper.HONEYSACKLE))
                            .append(text("/").color(ColorHelper.ORANGE))
                            .append(text(game.getMaxWeight()).color(RED)));
        }
    }

    private void addPlayerToScoreboard(Player player, int score) {
        Objective group = board.getObjective("sideBar");
        if (group == null) {
            Bukkit.getLogger().log(Level.WARNING, "Scoreboard group is null!");
            return;
        }
        Team hpReader = board.registerNewTeam(player.getName());
        hpReader.addEntry(ChatColor.YELLOW + player.getName());
        Component[] bars = createHpBar(game.getPlayers().get(player), 0.0, true);
        Objective obj = board.getObjective("sideBar");
        if (obj != null) {
            obj.displayName(text("Respawn Charges: ").color(ColorHelper.INDIGO)
                    .append(text(game.getAnchorCharges()).color(ColorHelper.HONEYSACKLE))
                    .append(text("/").color(ColorHelper.METALLIC_BLUE))
                    .append(text("4").color(RED)));
        }
        if (bars != null) {
            hpReader.prefix(bars[0].append(text(" ").color(NamedTextColor.GRAY)));
            group.getScore(ChatColor.YELLOW + player.getName())
                    .setScore(score);
        }
    }
}
