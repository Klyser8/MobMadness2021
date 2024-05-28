package com.github.klyser8.mobmadness.game.entity.beast;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.MadUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

/**
 * Listener used to handle beasts behavior.
 */
public class BeastListener implements Listener {

    private final MobMadness plugin;
    public BeastListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        if (!(event.getEntity() instanceof Mob victim)) return;
        Player killer = event.getEntity().getKiller();
        Game game = MadUtils.getEntityGame(plugin, killer);
        if (game == null) return;
        if (!event.getEntity().hasMetadata(Beast.BEAST_TYPE_TAG)) return;
        Beast<?> beast = game.getBeastMap().get(victim);
        beast.handleDeath();
    }

    @EventHandler
    public void onCreeperDamagePlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Creeper)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        if (!damager.hasMetadata(Beast.BEAST_TYPE_TAG)) return;
        double distance = event.getEntity().getLocation().distance(damager.getLocation()) < 1 ?
                1 : event.getEntity().getLocation().distance(damager.getLocation());
        BeastType type = BeastType.valueOf(damager.getMetadata(Beast.BEAST_TYPE_TAG).get(0).asString());
        event.setDamage(type.getDmg() / Math.max(1, distance / 2));
    }

    @EventHandler
    public void onTargetChange(EntityTargetEvent event) {
        if (event.getEntity() instanceof Monster && event.getTarget() instanceof Monster) {
            event.setCancelled(true);
        }
        if (event.getTarget() instanceof Player && event.getEntity().hasMetadata(Beast.IGNORE_PLAYERS_TAG)) {
            if (event.getEntity().getMetadata(Beast.IGNORE_PLAYERS_TAG).get(0).asBoolean()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler                                                           //Changes properties of arrows shots by mobs
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getProjectile() instanceof Arrow arrow)) return;
        if (event.getEntity() instanceof Skeleton skeleton) {
            if (!skeleton.hasMetadata(Beast.BEAST_TYPE_TAG)) return;
            if (skeleton.getMetadata(Beast.BEAST_TYPE_TAG).get(0).
                            asString().equalsIgnoreCase(BeastType.SKELETON.toString())) {
                arrow.setDamage(BeastType.SKELETON.getDmg());
            } else if (skeleton.getMetadata(Beast.BEAST_TYPE_TAG).get(0).
                    asString().equalsIgnoreCase(BeastType.JOCKEY_SKELETON.toString())) {
                arrow.setDamage(BeastType.JOCKEY_SKELETON.getDmg());
            }
        }
    }

    @EventHandler
    public void onBossDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Mob mob
                && mob.hasMetadata(Beast.BEAST_TYPE_TAG)
                && mob.hasMetadata(BossBeast.MM_BOSS_TAG)) {
            Game game = MadUtils.getEntityGame(plugin, (Mob) event.getEntity());
            if (game == null) return;
            if (game.getBeastMap().get(mob) instanceof BossBeast boss) {
                boss.handleDamage(event);
            }
        }
        /*if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
            if (event.getEntity() instanceof Skeleton skeleton) {
                if (skeleton.hasMetadata(Beast.MM_TYPE_TAG)
                        && skeleton.getMetadata(Beast.MM_TYPE_TAG).get(0).
                                asString().equalsIgnoreCase(BeastType.JOCKEY_SKELETON.toString())) {
                    event.setCancelled(true);
                }
            }
        }*/
    }

    @EventHandler                                                      //Prevents zombies from transforming into drowned
    public void onZombieConvert(EntityTransformEvent event) {
        if (event.getTransformReason() == EntityTransformEvent.TransformReason.DROWNED) {
            event.setCancelled(true);
        }
    }
}
