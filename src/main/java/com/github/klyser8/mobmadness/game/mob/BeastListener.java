package com.github.klyser8.mobmadness.game.mob;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class BeastListener implements Listener {

    private final MobMadness plugin;
    public BeastListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        LivingEntity victim = event.getEntity();
        Player killer = event.getEntity().getKiller();
        GameData gameData = MadUtils.getPlayerGame(plugin, killer);
        if (gameData == null) return;
        Profile profile = gameData.getPlayers().get(killer);
        if (!event.getEntity().hasMetadata(Beast.MM_TYPE_TAG)) return;
        Beast beast = gameData.getMobMap().get(victim);
        beast.handleDeath();
    }

    @EventHandler
    public void onCreeperDamagePlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Creeper)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        if (!damager.hasMetadata(Beast.MM_TYPE_TAG)) return;
        double distance = event.getEntity().getLocation().distance(damager.getLocation()) < 1 ?
                1 : event.getEntity().getLocation().distance(damager.getLocation());
        double damage = damager.getMetadata(Beast.DAMAGE_TAG).get(0).asDouble() / Math.max(1, distance / 2);
        event.setDamage(damage);
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
        if (!(event.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getProjectile();
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (!skeleton.hasMetadata(Beast.MM_TYPE_TAG)) return;
            if (skeleton.getMetadata(Beast.MM_TYPE_TAG).get(0).
                            asString().equalsIgnoreCase(BeastType.SKELETON.toString())) {
                arrow.setDamage(BeastType.SKELETON.getDmg());
            } else if (skeleton.getMetadata(Beast.MM_TYPE_TAG).get(0).
                    asString().equalsIgnoreCase(BeastType.JOCKEY_SKELETON.toString())) {
                arrow.setDamage(BeastType.JOCKEY_SKELETON.getDmg());
            }
        }
    }

    @EventHandler
    public void onBossDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Mob mob
                && mob.hasMetadata(Beast.MM_TYPE_TAG)
                && mob.hasMetadata(BossBeast.MM_BOSS_TAG)) {
            GameData gameData = MadUtils.getBeastGame(plugin, (Mob) event.getEntity());
            if (gameData == null) return;
            if (gameData.getMobMap().get(mob) instanceof BossBeast boss) {
                System.out.println("Damage before: " + event.getDamage());
                boss.handleDamage(event);
                System.out.println("Damage after: " + event.getDamage());
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

}
