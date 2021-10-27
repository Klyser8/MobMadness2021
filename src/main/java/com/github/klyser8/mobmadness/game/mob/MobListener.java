package com.github.klyser8.mobmadness.game.mob;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.MadUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;

public class MobListener implements Listener {

    private final MobMadness plugin;
    public MobListener(MobMadness plugin) {
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
        if (!event.getEntity().hasMetadata(MadEntity.MM_TAG)) return;
        MadEntity entity = gameData.getMobMap().get(victim);
        entity.handleDeath();
    }

    @EventHandler
    public void onCreeperDamagePlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Creeper)) return;
        LivingEntity damager = (LivingEntity) event.getDamager();
        if (!damager.hasMetadata(MadEntity.MM_TAG)) return;
        double damage = damager.getMetadata(MadEntity.DAMAGE_TAG).get(0).asDouble() /
                event.getEntity().getLocation().distance(damager.getLocation());
        event.setDamage(damage);
    }

    @EventHandler
    public void onTargetChange(EntityTargetEvent event) {
        if (event.getEntity() instanceof Monster && event.getTarget() instanceof Monster) {
            event.setCancelled(true);
        }
    }

}
