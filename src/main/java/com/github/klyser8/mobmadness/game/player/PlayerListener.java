package com.github.klyser8.mobmadness.game.player;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.GameManager;
import com.github.klyser8.mobmadness.game.items.misc.Compass;
import com.github.klyser8.mobmadness.game.items.misc.IronNugget;
import com.github.klyser8.mobmadness.lang.English;
import com.github.klyser8.mobmadness.storage.GameData;
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
        GameData gameData = MadUtils.getPlayerGame(plugin, deadPlayer);
        if (gameData == null) return;
        deadPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Profile.STARTING_HP);
        if (gameData.getAnchorCharges() > 0) {
            deathSound.play(deathLoc, SoundCategory.PLAYERS);
            handleRespawnCharges(gameData);
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
                    if (seconds == 30 && deadPlayer.getGameMode() == GameMode.SPECTATOR) {
                        deadPlayer.teleport(gameData.getGameMap().getMapType().getSpawn());
                        clearInventory(deadPlayer);
                        addItemToSlot(deadPlayer, 8, new Compass(plugin, gameData).getItemStack());
                        addItemToSlot(deadPlayer, 17,
                                new IronNugget(plugin, GameManager.STARTING_NUGGETS, gameData).getItemStack());
                        deadPlayer.setGameMode(GameMode.SURVIVAL);
                        cancel();
                    }
                    seconds++;
                }
            }.runTaskTimer(plugin, 1, 20);
        } else {
            gameOverSound.play(deathLoc, SoundCategory.PLAYERS);
            deadPlayer.teleport(deadPlayer.getWorld().getSpawnLocation());
            for (Profile profile : gameData.getPlayers().values()) {
                Player loopPlayer = profile.getPlayer();
                loopPlayer.sendMessage(English.GAME_OVER.getComponent(deadPlayer));
            }
            plugin.getGameManager().stopGame(gameData.getGameMap().getMapType());
        }
    }

    @EventHandler //Display mob's health bar to whomever fighting the mob.
    // Updates the health bar even if the one hitting the mob isn't a player, as long as the mob has a killer set.
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player || !(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        if (entity.getKiller() == null) return;
        Player killer = entity.getKiller();
        GameData gameData = getPlayerGame(plugin, killer);
        if (gameData == null) return;
        if (gameData.getMobMap().containsKey(entity)) {
            displayMobHpBar(plugin, gameData.getMobMap().get(entity), killer, (int) Math.ceil(event.getFinalDamage()));
        }
    }

    @EventHandler                                                        //Display mob's health bar to the mob's damager
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player || !(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile &&
                ((Projectile) event.getDamager()).getShooter() instanceof Player) {
            damager = entity.getKiller();
        }
        if (damager == null) return;
        GameData gameData = getPlayerGame(plugin, damager);
        if (gameData == null) return;
        if (gameData.getMobMap().containsKey(entity)) {
            displayMobHpBar(plugin, gameData.getMobMap().get(entity), damager, (int) Math.ceil(event.getFinalDamage()));
        }
    }

    @EventHandler                                             //Sets an entity's killer whenever it gets hit by a player
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        ((LivingEntity) event.getEntity()).setKiller((Player) event.getDamager());
    }

    private void handleRespawnCharges(GameData gameData) {
        gameData.setAnchorCharges(gameData.getAnchorCharges() - 1);
        gameData.getRespawnAnchor().getWorld().spawnParticle(Particle.SPELL_INSTANT,
                gameData.getRespawnAnchor().getLocation().add(0.5, 0.75, 0.5), 25,
                0.25, 0, 0.25, 0);
    }
}
