package com.github.klyser8.mobmadness.ui;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class UIEvents implements Listener {

    int time = 0;
    Entity oldEntity = null;

    /*@EventHandler
    public void onServerTick(ServerTickStartEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Entity entity = player.getTargetEntity(20);
            if (entity == null) {
                return;
            }
            if (!(entity instanceof LivingEntity)) return;
            LivingEntity mob = (LivingEntity) entity;
            if (oldEntity == null) oldEntity = mob;
            if (oldEntity != mob) {
                time = 0;
//                GlowAPI.setGlowing(mob, GlowAPI.Color.NONE, player);
                oldEntity = mob;
                player.sendActionBar("...");
            }
            if (time < 20) {
                player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 0.25f, 2.0f);
//                GlowAPI.setGlowing(mob, GlowAPI.Color.AQUA, player);
                time++;
            } else {
                player.sendActionBar(color("HP: ") + mob.getHealth() + "/" + mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }
        }
    }*/

}
