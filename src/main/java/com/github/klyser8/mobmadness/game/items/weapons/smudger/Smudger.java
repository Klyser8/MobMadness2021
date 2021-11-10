package com.github.klyser8.mobmadness.game.items.weapons.smudger;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.klyser8.mobmadness.util.MadUtils.*;
import static me.mattstudios.mf.base.components.MfUtil.color;

public class Smudger extends MadItem implements MadWeapon {

    private SmudgerData data;
    private final Random random;

    public static final String PELLETS_TAG = "mm.pellets";
    public static final String PELLET_SPREAD_TAG = "mm.pellet_spread";
    public static final String PELLET_RANGE_TAG = "mm.pellet_range";
    public static final String COOLDOWN_TAG = "mm.cooldown";
    public static final String ULT_DAMAGE_TAG = "mm.ult.damage";
    public static final String ULT_PELLETS_TAG = "mm.ult.pellets";
    public static final String ULT_COOLDOWN_TAG = "mm.ult.cooldown";

    private final Sound shotSound;
    private final Sound reverbShotSound;
    private final Sound ultShotSound;
    private final Sound ultReverbShotSound;
    private final Sound hitSound;

    public Smudger(ItemType type, MobMadness plugin, Game game) {
        super(type, new ItemStack(type.getMaterial()), plugin, game, true);
        this.random = new Random();
        setup();
        this.shotSound = new Sound("entity.dragon_fireball.explode", 1.5f, 1.6f);
        this.reverbShotSound = new Sound("entity.firework_rocket.large_blast", 1.5f, 0.6f);
        this.ultShotSound = new Sound("entity.dragon_fireball.explode", 1.5f, 1.4f);
        this.ultReverbShotSound = new Sound("entity.lightning_bolt.thunder", 2.5f, 2.0f);
        this.hitSound = new Sound("item.flintandsteel.use", 1.0f, 2.0f);
    }

    @Override
    protected void setup() {
        createData(this);
        setTags(false, false, true,
                false, true, null);
        createLore();
        nbt.setBoolean(WEAPON_TAG, true);
        nbt.setDouble(DAMAGE_TAG, data.getDamage());
        nbt.setInteger(PELLETS_TAG, data.getPellets());
        nbt.setInteger(PELLET_SPREAD_TAG, data.getPelletSpread());
        nbt.setInteger(PELLET_RANGE_TAG, data.getPelletRange());
        nbt.setInteger(COOLDOWN_TAG, data.getCooldown());
        nbt.setDouble(ULT_DAMAGE_TAG, data.getUltDamage());
        nbt.setInteger(ULT_PELLETS_TAG, data.getUltPellets());
        nbt.setInteger(ULT_COOLDOWN_TAG, data.getLongCooldown());
    }

    private void createLore() {
        List<String> lore = new ArrayList<>();
        lore.add("&7Pellet Damage: &f" + data.getDamage());
        lore.add("&7Max Damage: &f" + data.getDamage() * data.getPellets());
        lore.add("&7Pellets Shot: &f" + data.getPellets());
        lore.add("&7Cooldown: &f" + data.getCooldown() / 20 + "s");
        lore.add("&7Reload time: &f" + data.getLongCooldown() / 20 + "s");
        lore.add("&8----------------------------");
        lore.add("&5Passive: &fEvery third shot");
        lore.add("&ffires more pellets which");
        lore.add("&fdeal extra damage.");
        lore.add("");
        lore.add("&dPellet Damage: &f" + data.getUltDamage());
        lore.add("&dMax Damage: &f" + data.getUltDamage() * data.getUltPellets());
        lore.add("&dPellets Shot: &f" + data.getUltPellets());
        setLore(lore);
    }

    public void shoot(Profile profile) {
        Player shooter = profile.getPlayer();
        if (shooter.getCooldown(type.getMaterial()) > 0 || profile.isOnCooldown()) return;
        List<LivingEntity> hitMobs = new ArrayList<>();
        Location origin = shooter.getEyeLocation().add(shooter.getEyeLocation().getDirection().multiply(0.2));
        Location targetLoc = origin.clone().add(origin.getDirection().multiply(data.getPelletRange()));
        shooter.setCooldown(type.getMaterial(), data.getCooldown());
        shooter.setVelocity(origin.getDirection().normalize().multiply(-0.1));
        //Play shoot sound
        playShotSound(shooter);
        for (int shot = 0; shot < data.getPellets(); shot++) {
            Location modTargetLoc = targetLoc.clone();
            //Semi random loc
            modTargetLoc.add((random.nextInt(data.getPelletSpread() * 5) - data.getPelletSpread() * 2.5) / 10,
                    (random.nextInt((int) (data.getPelletSpread() * 2.5)) - data.getPelletSpread() * 1.25) / 10,
                    (random.nextInt(data.getPelletSpread() * 5) - data.getPelletSpread() * 2.5) / 10);
            Vector modDirection = getDirectionBetweenLocations(origin, modTargetLoc).normalize();
            for (double d = 0.1; d < origin.distance(modTargetLoc); d += 0.2) {
                Location loc = origin.clone();
                loc.add(modDirection.clone().multiply(d));
                float size = 0.5f;
                size = createParticles(shooter, loc, size);
                if (!isBlockPassThrough(loc.getBlock().getType())) break;
                LivingEntity mob = getClosestMob(loc, size, size, size); //FIXME This avoids players!
                if (mob == null) continue;
                if (!hitMobs.contains(mob)) hitMobs.add(mob);
                hitSound.play(loc, SoundCategory.PLAYERS);
                //Damage and Knockback logic
                calculateDamage(shooter, mob);
                break;
            }
        }
        for (LivingEntity mob : hitMobs) {
            damageMob(shooter, mob, targetLoc);
        }
        setCurrentProjectile(profile);
    }

    @Override
    public void createData(MadItem item) {
        data = new SmudgerData(this, type);
    }

    @Override
    public ItemType getType() {
        return type;
    }

    private void playShotSound(Player shooter) {
        Location origin = shooter.getEyeLocation();
        if (isThirdShot(shooter)) {
            ultShotSound.play(origin, SoundCategory.PLAYERS);
            ultReverbShotSound.play(origin, SoundCategory.PLAYERS);
        } else {
            shotSound.play(origin, SoundCategory.PLAYERS);
            reverbShotSound.play(origin, SoundCategory.PLAYERS);
        }
    }

    private float createParticles(Player shooter, Location loc, float size) {
        World world = loc.getWorld();
        int rgb = random.nextInt(150) + data.getRgb();
        world.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0,
                new Particle.DustOptions(Color.fromRGB(rgb, rgb, rgb), size), true);
        if (isThirdShot(shooter)) {
            size = 0.7f;
            if (random.nextBoolean()) {
                world.spawnParticle(Particle.REDSTONE, loc, 1, 0.1, 0.1, 0.1, 0,
                        new Particle.DustOptions(Color.fromRGB(255, 238, 0), size), true);
            }
        }
        return size;
    }

    private void calculateDamage(Player shooter, LivingEntity mob) {
        double pelletDmg = data.getDamage();
        double pelletKnockback = data.getKnockback();
        if (isThirdShot(shooter)) {
            pelletDmg = data.getUltDamage();
            pelletKnockback *=2;
        }
        double currentDmg = pelletDmg;
        double currentKnockback = pelletKnockback;
        if (mob.hasMetadata(Beast.PELLETS_DMG_TAG) && mob.hasMetadata(Beast.PELLETS_KNOCKBACK_TAG)) {
            currentDmg = mob.getMetadata(Beast.PELLETS_DMG_TAG).get(0).asDouble();
            currentDmg += pelletDmg;
            currentKnockback = mob.getMetadata(Beast.PELLETS_KNOCKBACK_TAG).get(0).asDouble();
            currentKnockback += pelletKnockback;
        }
        mob.setMetadata(Beast.PELLETS_DMG_TAG, new FixedMetadataValue(plugin, currentDmg));
        mob.setMetadata(Beast.PELLETS_KNOCKBACK_TAG, new FixedMetadataValue(plugin, currentKnockback));
    }

    private void damageMob(Player shooter, LivingEntity mob, Location targetLoc) {
        Vector direction = getDirectionBetweenLocations(shooter.getEyeLocation(), targetLoc).normalize();
        double damage = mob.getMetadata(Beast.PELLETS_DMG_TAG).get(0).asDouble();
        double knockback = mob.getMetadata(Beast.PELLETS_KNOCKBACK_TAG).get(0).asDouble();
        mob.damage(damage, shooter);
        mob.setVelocity(mob.getVelocity().add(direction.multiply(1 + knockback)));
        mob.removeMetadata(Beast.PELLETS_DMG_TAG, plugin);
        mob.removeMetadata(Beast.PELLETS_KNOCKBACK_TAG, plugin);
    }

    private boolean isThirdShot(Player shooter) {
        return shooter.getExp() < 0.34;
    }

    private void setCurrentProjectile(Profile profile) {
        Player player = profile.getPlayer();
        if (isThirdShot(player)) {
            player.setExp(0.00f);
            profile.startCooldown(data.getLongCooldown());
        } else {
            player.setExp(player.getExp() - 0.33f);
        }
    }
}
