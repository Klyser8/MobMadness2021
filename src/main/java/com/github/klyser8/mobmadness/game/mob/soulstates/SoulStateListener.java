package com.github.klyser8.mobmadness.game.mob.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.storage.GameData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import static com.github.klyser8.mobmadness.util.MadUtils.getPlayerGame;

/**
 * Listens to events for soul states to use to apply their effects.
 */
public class SoulStateListener implements Listener {

    private final MobMadness plugin;
    public SoulStateListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler                                     //Prevent mobs with the "Dazzled" soul state from damaging players
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof LivingEntity)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player victim = (Player) entity;
            GameData gameData = getPlayerGame(plugin, victim);
            if (gameData == null) return;
            MadEntity madMob = gameData.getMobMap().get(damager);
            if (madMob != null) {
                if (/*damager.hasMetadata(Dazzled.DAZZLED_TAG)*/
                        madMob.getCurrentSoulStates().containsKey(SoulStateType.DAZZLED)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler      //Prevents skeletons from shooting their bows towards players when under the "dazzled" soul state
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) event.getEntity();
            if (event.getProjectile() instanceof Arrow && skeleton.getTarget() instanceof Player) {
                LivingEntity shooter = (LivingEntity) ((Arrow) event.getProjectile()).getShooter();
                Player victim = (Player) skeleton.getTarget();
                GameData gameData = getPlayerGame(plugin, victim);
                if (gameData == null) return;
                if (shooter == null) return;
                MadEntity madMob = gameData.getMobMap().get(shooter);
                if (madMob != null) {
                    if (/*shooter.hasMetadata(Dazzled.DAZZLED_TAG)*/
                            madMob.getCurrentSoulStates().containsKey(SoulStateType.DAZZLED)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
