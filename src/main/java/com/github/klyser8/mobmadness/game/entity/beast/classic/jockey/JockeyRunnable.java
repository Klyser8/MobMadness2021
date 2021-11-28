package com.github.klyser8.mobmadness.game.entity.beast.classic.jockey;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.beast.BeastRunnable;
import com.github.klyser8.mobmadness.game.entity.beast.classic.Spyder;
import com.github.klyser8.mobmadness.game.entity.soulstates.Hasted;
import com.github.klyser8.mobmadness.game.entity.soulstates.SoulStateType;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.MadUtils;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Spider;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class JockeyRunnable extends BeastRunnable<Jockey> {

    private int clock = 0;
    private Spider ride = null;
    private final Random random;
    private final MobMadness plugin;
    private final Game game;

    private final Sound jumpSound;
    private final Sound jumpBoneSound;
    private final Sound mountSound;
    private final Sound spiderConvertSound;

    public JockeyRunnable(Jockey jockeySkel, MobMadness plugin, Game game) {
        super(jockeySkel);
        random = new Random();
        this.plugin = plugin;
        this.game = game;
        jumpSound = new Sound("entity.ender_dragon.flap", 0.8f, 2.0f);
        jumpBoneSound = new Sound("entity.wither_skeleton.hurt", 1.0f, 1.5f);
        mountSound = new Sound("item.armor.equip_leather", 1.0f, 1.0f);
        spiderConvertSound = new Sound("entity.skeleton.converted_to_stray", 1.0f, 2.0f);
    }

    @Override
    public void run() {
        super.run();
        Mob mob = beast.getMob();
        if ((ride == null || mob.getPathfinder().getCurrentPath() == null) &&
                random.nextFloat() < 0.05 && mob.getVehicle() == null) {
            lookForSpider();
        }
        if (ride != null) {
            if (ride.isDead()) {
                ride = null;
            } else if (mob.getVehicle() == null) {
                moveTowardsSpider();
                if (ride.getLocation().distance(mob.getLocation()) < 2) {
                    mountSpider();
                    //End hasted soul state if it is present
                    Spyder spyder = (Spyder) game.getBeastMap().get(ride);
                    if (!spyder.hasSoulState(SoulStateType.HASTED)) {
                        new Hasted(plugin, game, spyder, 3, 2400);
                    }
                    ride.addPotionEffect(
                            new PotionEffect(PotionEffectType.REGENERATION, 2400, 1,
                                    true, true));
                    ride.addPotionEffect(
                            new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 0,
                                    true, true));
                }
            }
        }

        if (clock % 5 == 0) {
            beast.updateBossBar();
        }
        clock++;
    }

    //Looks for a spider by deselecting any entity. If it finds one, gets a hasted effect to run towards the spider
    private void lookForSpider() {
        //Skeleton will fail to find a spider if it is dazzled
        if (beast.hasSoulState(SoulStateType.DAZZLED)) return;
        Mob mob = beast.getMob();
        ride = (Spider) mob.getNearbyEntities(30, 10, 30).stream()
                .filter(spider -> spider instanceof Spider && mob.hasLineOfSight(spider))
                .findFirst().orElse(null);
        if (ride == null || !ride.getPassengers().isEmpty()) return;
        if (!beast.hasSoulState(SoulStateType.HASTED)) {
            new Hasted(plugin, game, beast, 3, 200);
        }
        mob.setMetadata(Beast.IGNORE_PLAYERS_TAG, new FixedMetadataValue(plugin, true));
    }

    private void moveTowardsSpider() {
        Mob mob = beast.getMob();
        mob.setTarget(null);
        mob.lookAt(ride);
        mob.getPathfinder().moveTo(ride);
        if (Math.abs(ride.getLocation().getY() - mob.getLocation().getY()) > 3
                && ride.getLocation().distance(mob.getLocation()) < 10
                && mob.isOnGround()) {
            if (beast.getGeneralCooldown() == 0) {
                jump();
            }
        }
    }

    private void mountSpider() {
        Mob mob = beast.getMob();
        Vector dir = MadUtils.getDirectionBetweenLocations(mob.getLocation(), ride.getLocation());
        for (double d = 0.1; d < mob.getLocation().distance(ride.getEyeLocation()); d += 0.1) {
            Location loc = mob.getLocation().clone();
            loc.add(dir.clone().multiply(d));
            mob.getWorld().spawnParticle(Particle.SPIT, loc, 2,
                    0.1, 0.1, 0.1, 0);
        }
        ride.addPassenger(mob);
        mob.removeMetadata(Beast.IGNORE_PLAYERS_TAG, plugin);
        mountSound.play(ride.getLocation(), SoundCategory.HOSTILE);
        Bukkit.getScheduler().runTaskLater(plugin,
                () -> {
                    spiderConvertSound.play(mob.getLocation(), SoundCategory.HOSTILE);
                    mob.getWorld().spawnParticle(Particle.SPELL_WITCH, ride.getLocation(), 50,
                            0.75, 0.25, 0.75, 0);
                }, 10);
    }

    private void jump() {
        Mob mob = beast.getMob();
        Vector dir = MadUtils.getDirectionBetweenLocations(mob.getLocation(), ride.getLocation());
        mob.setVelocity(mob.getVelocity().add(dir.normalize().add(new Vector(0, 0.3, 0)).multiply(
                ride.getLocation().distance(mob.getLocation()) / 7)));
        mob.getWorld().spawnParticle(Particle.SPIT, mob.getLocation(), 25,
                0.25, 0.33, 0.25, 0);
        jumpSound.play(mob.getLocation(), SoundCategory.HOSTILE);
        Bukkit.getScheduler().runTaskLater(plugin,
                () -> jumpBoneSound.play(mob.getLocation(), SoundCategory.HOSTILE), 3);
        beast.setGeneralCooldown(60);
    }
}
