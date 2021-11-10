package com.github.klyser8.mobmadness.game.entity.soulstates;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.Soul;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.MadUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import static com.github.klyser8.mobmadness.util.MadUtils.getEntityGame;

/**
 * Listens to events for soul states to use to apply their effects.
 */
public class SoulStateListener implements Listener {

    private final MobMadness plugin;
    public SoulStateListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof Mob targeter)) return;
        if (event.getTarget() instanceof Player victim) {
            Game game = getEntityGame(plugin, victim);
            if (game == null) return;
            Beast madMob = game.getBeastMap().get(targeter);
            if (madMob != null) {
                if (madMob.getCurrentSoulStates().containsKey(SoulStateType.DAZZLED)) {
                    event.setTarget(null);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onArrowDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && event.getDamager() instanceof Arrow arrow) {
            Game game = MadUtils.getEntityGame(plugin, entity);
            if (game == null) return;
            Soul soul;
            if (event.getEntity() instanceof Player player) {
                soul = game.getPlayers().get(player);
            } else {
                soul = game.getBeastMap().get(((Mob) event.getEntity()));
            }
            Marked marked = SoulStateType.getSoulStateIfPresent(soul, SoulStateType.MARKED);
            if (marked == null) return;
            marked.handleDamage(event);
        }
    }



}
