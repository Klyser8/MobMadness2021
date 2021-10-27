package com.github.klyser8.mobmadness.game.items.weapons.sword;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SwordUltRunnable extends BukkitRunnable {


    private final World world;
    private final Sound ultSound;
    private final Sound beamSound;
    private final Sound healSound;
    private final Location ultLocation;
    private final Location beamLoc;
    private double t = Math.PI / 4;

    private boolean isHealingDone = false;

    private final MobMadness plugin;
    private final Player player;
    private final SwordData data;
    public SwordUltRunnable(MobMadness plugin, Player caster, SwordData data) {
        this.plugin = plugin;
        this.player = caster;
        this.data = data;
        this.world = caster.getWorld();
        this.ultSound = new Sound("block.conduit.deactivate", 2.0f, 1.25f);
        this.beamSound = new Sound("block.conduit.activate", 3.0f, 1.0f);
        this.healSound = new Sound("entity.player.swim", 1.0f, 1.5f);
        this.ultLocation = caster.getLocation();
        this.beamLoc = caster.getLocation().add(0, 50, 0);
    }

    @Override
    public void run() {
        if (beamLoc.getY() > ultLocation.getY() + 3) {
            createBeamEffect();
        } else {
            if (isHealingDone) {
                createDome(ultLocation);
                return;
            }
            List<Player> nearbyPlayers = (List<Player>) ultLocation.getNearbyPlayers(data.getUltRadius() / 2);
            if (nearbyPlayers.isEmpty()) {
                isHealingDone = true;
                return;
            }
            ultSound.play(ultLocation, SoundCategory.PLAYERS);
            double healing = Math.ceil(data.getUltHealing() / nearbyPlayers.size());
            for (Player hitPlayer : nearbyPlayers) {
                healPlayers(hitPlayer, healing);
            }
            isHealingDone = true;
        }
        beamLoc.add(0, -1.125, 0);
    }

    /**
     * Creates the beam effect of the ultimate.
     */
    private void createBeamEffect() {
        beamSound.play(beamLoc, SoundCategory.AMBIENT);
        world.spawnParticle(Particle.END_ROD, beamLoc, 40,
                0.125, 0.3, 0.125, 0, null, true);
        world.spawnParticle(Particle.REDSTONE, ultLocation, 10,
                data.getUltRadius() / 4, 0.1, data.getUltRadius() / 4, 0,
                new Particle.DustOptions(Color.LIME, 1), true);
    }

    /**
     * Creates the dome effect of the ultimate.
     * @param loc the start location of the dome
     */
    private void createDome(Location loc) {
        t = t + 0.1 * Math.PI;
        for (double var = 0; var <= 2 * Math.PI; var += Math.PI / 32) {
            double x = (t * data.getUltRadius() / 6) * cos(var); //Changing contents of cos: shape of circle
            double y = 2 * Math.exp(-0.05 * t) * sin(t) + 1.5;
            double z = (t * data.getUltRadius() / 6) * sin(var); //Changing contents of sin: shape of circle
            loc.add(x, y, z);
            world.spawnParticle(Particle.COMPOSTER, loc, 1, 0.2, 0.2, 0.2, 0, null, true);
            world.spawnParticle(Particle.BUBBLE_POP, loc, 1, 0.2, 0.2, 0.2, 0, null, true);
            loc.subtract(x, y, z);
            if (t > 5) cancel();
        }
    }

    /**
     * Heals the player by the provided amount. Works only if he has missing HP and he is not dead.
     * @param hitPlayer the player who's hit by the ability.
     * @param healing the amount to heal him by
     */
    private void healPlayers(Player hitPlayer, double healing) {
        if (player.isDead()
                || player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            return;
        }
        double hpMissing = (hitPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - hitPlayer.getHealth());
        double hpOld = hitPlayer.getHealth();
        if (healing > hpMissing) {
            hitPlayer.setHealth(hitPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        } else {
            hitPlayer.setHealth(hitPlayer.getHealth() + healing);
        }
        double healedAmount = hitPlayer.getHealth() - hpOld;
        world.spawnParticle(Particle.HEART, hitPlayer.getLocation().add(0, 1, 0), (int) Math.round(healedAmount) * 2,
                0.25, 0.5, 0.25, 0, null, true);
        healSound.play(hitPlayer.getLocation(), SoundCategory.PLAYERS);
    }
}
