package com.github.klyser8.mobmadness.game.items.weapons.bow;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.mob.Beast;
import com.github.klyser8.mobmadness.util.Sound;
import com.github.klyser8.mobmadness.util.MadUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BowUltRunnable extends BukkitRunnable {

    private final MobMadness plugin;
    private final Player player;
    private final World world;
    private final BowData bowData;
    private final Sound hitSound;
    public BowUltRunnable(MobMadness plugin, Player player, BowData bowData) {
        this.plugin = plugin;
        this.player = player;
        this.world = player.getWorld();
        this.bowData = bowData;
        hitSound = new Sound("entity.iron_golem.attack", 1.0f, 0.5f);
    }

    @Override
    public void run() {
        Location loc = player.getLocation();
        if (player.getVelocity().length() < 0.11) {                  //Any velocity below this, means the dash has ended
            player.setInvulnerable(false);
            cancel();
        }
        world.spawnParticle(Particle.SPIT, player.getLocation().add(0, 1, 0), (int) bowData.getDamage(),
                0.2, 0.5, 0.2, 0);
        List<LivingEntity> mobs = MadUtils.getMobsInArea(loc, 2.0, 2.0, 2.0);
        if (mobs.isEmpty()) {
            return;
        }
        tossEnemies(mobs);
    }

    private void tossEnemies(List<LivingEntity> mobs) {               //Throws any mob hit by the dash behind the caster
        for (LivingEntity mob : mobs) {
            if (mob.hasMetadata(Beast.MARKED_TAG)) continue;
            mob.setMetadata(Beast.MARKED_TAG,
                    new FixedMetadataValue(plugin, bowData.getUltMarkDuration()));                   //Marks the hit mob
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, bowData.getUltMarkDuration(), 0,
                    false, false, false));
            mob.setVelocity(player.getVelocity().multiply(-1).setY(mob.getVelocity().getY() + 1)); //Tosses mob backward
            hitSound.play(mob.getEyeLocation(), SoundCategory.PLAYERS);
            mob.setKiller(player);
            runMark(mob);
        }
    }

    private void runMark(LivingEntity mob) {                                 //Plays particle effects for marked enemies
        new BukkitRunnable() {
            @Override
            public void run() {
                int markDuration = mob.getMetadata(Beast.MARKED_TAG).get(0).asInt();
                if (markDuration == 0 || mob.isDead()) {
                    mob.removeMetadata(Beast.MARKED_TAG, plugin);
                    mob.removePotionEffect(PotionEffectType.SLOW);
                    cancel();
                    return;
                }
                if (markDuration % 5 == 0) {
                    world.spawnParticle(Particle.SNEEZE, mob.getEyeLocation(), 5,
                            0.25, 0.5 ,0.25, 0.025);
                }
                mob.setMetadata(Beast.MARKED_TAG, new FixedMetadataValue(plugin, markDuration - 1));
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
