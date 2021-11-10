package com.github.klyser8.mobmadness.game.entity.player;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.GameManager;
import com.github.klyser8.mobmadness.game.items.misc.Compass;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.ColorHelper;
import com.github.klyser8.mobmadness.util.ComponentHelper;
import com.github.klyser8.mobmadness.util.MadUtils;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.klyser8.mobmadness.ui.UIHelper.displayMobHpBar;
import static com.github.klyser8.mobmadness.util.MadUtils.*;
import static me.mattstudios.mf.base.components.MfUtil.color;

public class PlayerListener implements Listener {


    private final Sound gameOverSound;
    private final Sound deathSound;
    private final MobMadness plugin;
    public PlayerListener(MobMadness plugin) {
        this.plugin = plugin;
        gameOverSound = new Sound("entity.zombie.break_wooden_door", 10, 0.85f);
        deathSound = new Sound("block.respawn_anchor.deplete", 10, 1.0f);
//        gameOverSound = Sound.sound(Key.key("entity.zombie.break_wooden_door"), Sound.Source.PLAYER, 1.0f, 0.85f);
//        deathSound = Sound.sound(Key.key("block.respawn_anchor.deplete"), Sound.Source.PLAYER, 1.0f, 1.0f);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Location deathLoc = deadPlayer.getLocation();
        Game game = MadUtils.getEntityGame(plugin, deadPlayer);
        if (game == null) return;
        deadPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Profile.STARTING_HP);
        if (game.getAnchorCharges() > 0) {
            deathSound.play(deathLoc, SoundCategory.PLAYERS);
            handleRespawnCharges(game);
            deadPlayer.setLevel(deadPlayer.getLevel() / 4);
            deadPlayer.setGameMode(GameMode.SPECTATOR);
            TextComponent titleComp = Component.text("You died!").color(NamedTextColor.RED);
            TextComponent subtitleComp = Component.text("You will respawn in 30 seconds")
                    .color(ColorHelper.ORANGE);
//            deadPlayer.sendTitle(color("&cYou died!"), color("&eYou will respawn in 30 seconds."),
            ComponentHelper.sendPlayerTitle(deadPlayer, titleComp, subtitleComp, 0, 100, 20);
            new BukkitRunnable() {
                int seconds = 0;
                @Override
                public void run() {
                    if (seconds == 0) {
                        deadPlayer.teleport(deathLoc);
                    }
                    //allow player to be only within 5 blocks of their death location
                    if (deadPlayer.getLocation().distance(deathLoc) > 5) {
                        deadPlayer.teleport(deathLoc);
                    }
                    //After 30 seconds respawn. If the player is in survival though, respawn immediately
                    if ((seconds >= 30 && deadPlayer.getGameMode() == GameMode.SPECTATOR)
                            || deadPlayer.getGameMode() == GameMode.SURVIVAL) {
                        deadPlayer.teleport(game.getGameMap().getMapType().getSpawn());
                        clearInventory(deadPlayer);
                        addItemToSlot(deadPlayer, 8, new Compass(plugin, game).getItemStack());
                        addItemToSlot(deadPlayer, 17,
                                new IronNugget(plugin, GameManager.STARTING_NUGGETS, game).getItemStack());
                        deadPlayer.setGameMode(GameMode.SURVIVAL);
                        cancel();
                    }
                    seconds++;
                }
            }.runTaskTimer(plugin, 1, 20);
        } else {
            gameOverSound.play(deathLoc, SoundCategory.PLAYERS);
            deadPlayer.teleport(deadPlayer.getWorld().getSpawnLocation());
            for (Profile profile : game.getPlayers().values()) {
                Player loopPlayer = profile.getPlayer();
                loopPlayer.sendMessage(English.GAME_OVER.getComponent(deadPlayer));
            }
            plugin.getGameManager().stopGame(game.getGameMap().getMapType());
        }
    }

    @EventHandler //Display mob's health bar to whomever fighting the mob.
    // Updates the health bar even if the one hitting the mob isn't a player, as long as the mob has a killer set.
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player || !(event.getEntity() instanceof Mob mob)) return;
        if (mob.getKiller() == null) return;
        Player killer = mob.getKiller();
        Game game = getEntityGame(plugin, killer);
        if (game == null) return;
        if (game.getBeastMap().containsKey(mob)) {
            displayMobHpBar(plugin, game.getBeastMap().get(mob), killer, (int) Math.ceil(event.getFinalDamage()));
        }
    }

    @EventHandler                                                        //Display mob's health bar to the mob's damager
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile &&
                ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            damager = (Player) ((Projectile) event.getDamager()).getShooter();
        }
        if (damager == null) return;
        Game game = getEntityGame(plugin, damager);
        if (game == null) return;
        if (game.getBeastMap().containsKey(mob)) {
            displayMobHpBar(plugin, game.getBeastMap().get(mob), damager, (int) Math.ceil(event.getFinalDamage()));
        }
    }

/*    @EventHandler                                             //Sets a mob's killer whenever it gets hit by a player
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Mob)) return;
        ((Mob) event.getEntity()).setKiller((Player) event.getDamager());
    }*/

    private void handleRespawnCharges(Game game) {
        game.setAnchorCharges(game.getAnchorCharges() - 1);
        game.getRespawnAnchor().getWorld().spawnParticle(Particle.SPELL_INSTANT,
                game.getRespawnAnchor().getLocation().add(0.5, 0.75, 0.5), 25,
                0.25, 0, 0.25, 0);
    }
}
