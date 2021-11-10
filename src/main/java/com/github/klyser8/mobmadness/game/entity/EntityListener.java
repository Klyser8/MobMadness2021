package com.github.klyser8.mobmadness.game.entity;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.items.weapons.bow.Bow;
import com.github.klyser8.mobmadness.game.items.weapons.crossbow.Crossbow;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Listents to events related to both player, beast and misc entities.
 */
public class EntityListener implements Listener {

    private final MobMadness plugin;
    public EntityListener(MobMadness plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)  //Sets any damage dealt by arrows to the damage stored on the arrow
    public void onArrowDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        event.setDamage(arrow.getDamage());
    }

    @EventHandler(priority = EventPriority.HIGH)  //Sets any damage dealt by arrows to the damage stored on the arrow
    public void ccc(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        System.out.println(event.getDamage());
    }

}
