package com.github.klyser8.mobmadness.game.items.weapons.railgun;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.github.klyser8.mobmadness.util.MadUtils.getDirectionBetweenLocations;
import static com.github.klyser8.mobmadness.util.MadUtils.isBlockPassThrough;
import static me.mattstudios.mf.base.components.MfUtil.color;

public class Railgun extends MadItem implements MadWeapon {

    private RailgunData data;
    private final Sound chargeSound;
    private final Sound cancelSound;
    private final Sound shootSound;
    private final Sound raySound;
    private final Sound hitSound;

    private final Sound ultShootSound;
    private final Sound ultMaskSound;
    private final Sound ultDamageSound;

    public static final String PIERCE_POWER_TAG = "mm.pierce_power";
    public static final String CHARGE_TIME_TAG = "mm.charge_time";
    public static final String COOLDOWN_TAG = "mm.cooldown";
    public static final String ULT_DAMAGE_TAG = "mm.ult.damage";
    public static final String ULT_MASKING_TAG = "mm.ult.masking";
    public static final String ULT_WIDTH_TAG = "mm.ult.width";
    public static final String ULT_COOLDOWN_TAG = "mm.ult.cooldown";

    public Railgun(ItemType type, ItemStack item, MobMadness plugin, GameData gameData) {
        super(type, item, plugin, gameData, true);
        setup();
        this.chargeSound = new Sound("entity.illusioner.prepare_blindness", 1.0f, data.getChargePitch());
        this.cancelSound = new Sound("entity.spider.death", 1.0f, 2.0f);
        this.shootSound = new Sound("entity.illusioner.prepare_mirror", 1.0f, 2.0f);
        this.raySound = new Sound("entity.bat.takeoff", 0.01f, 2.0f);
        this.hitSound = new Sound("entity.player.attack.crit", 1.0f, 2.0f);
        this.ultShootSound = new Sound("item.trident.riptide_3", 1.0f, 1.0f);
        this.ultMaskSound = new Sound("block.enchantment_table.use", 1.0f, 1.0f);
        this.ultDamageSound = new Sound("entity.evoker.cast_spell", 0.8f, 2.0f);
    }

    @Override
    protected void setup() {
        createData(this);

        setDisplayName(color(type.getName()));
        createLore();
        setTags(false, false, true,
                false, true, null);
        nbt.setBoolean(WEAPON_TAG, true);
        nbt.setDouble(DAMAGE_TAG, data.getDamage());
        nbt.setDouble(PIERCE_POWER_TAG, data.getPiercePower());
        nbt.setInteger(CHARGE_TIME_TAG, data.getChargeTime());
        nbt.setInteger(COOLDOWN_TAG, data.getCooldown());
        nbt.setDouble(ULT_DAMAGE_TAG, data.getUltDamage());
        nbt.setInteger(ULT_MASKING_TAG, data.getUltMasking());
        nbt.setDouble(ULT_WIDTH_TAG, data.getUltWidth());
        nbt.setInteger(ULT_COOLDOWN_TAG, data.getUltCooldown());
    }

    private void createLore() {
        List<String> lore = new ArrayList<>();
        lore.add("&7Damage: &f" + data.getDamage());
        lore.add("&7Piercing Power: &f" + Math.round(data.getPiercePower() * 100) + "%");
        lore.add("&7Charge-up Time: &f" + data.getChargeTime() / 20.0 + "s");
        lore.add("&7Cooldown: &f" + data.getCooldown() / 20.0 + "s");
        lore.add("&8--------------------------------");
        lore.add("&5Shift Right-Click:&f shoots a gleam");
        lore.add("&fwhich damages + knocks back enemies");
        lore.add("&fand provides hit allies with");
        lore.add("&ftemporary &6runic hearts&f.");
        lore.add("&6&oRunic Hearts&7: &ohearts which");
        lore.add("&7&owhen lost cannot be regenerated.");
        lore.add("");
        lore.add("&dDamage: &f" + data.getUltDamage());
        lore.add("&dRunic Hearts: &f" + data.getUltMasking() / 2 + "&6 \u2764");
        lore.add("&dCooldown: &f" + data.getUltCooldown() / 20 + "s");
        lore.add("&dWidth: &f" + data.getUltWidth() + " blocks");
        setLore(lore);
    }

    public void charge(Player shooter) {
        if (shooter.getCooldown(type.getMaterial()) > 0) {
            return;
        }
        Location loc = shooter.getLocation();
        shooter.setCooldown(type.getMaterial(), data.getCooldown());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (data.getChargeLevel() == 0) chargeSound.play(loc, SoundCategory.PLAYERS, shooter);
                if (data.getChargeLevel() < data.getChargeTime()) {
                    data.setChargeLevel(data.getChargeLevel() + 1);
                    if (!shooter.getInventory().getItemInMainHand().equals(nbt.getItem())) {
                        data.setChargeLevel(0);
                        shooter.stopSound(chargeSound.getSoundName(), SoundCategory.PLAYERS);
                        shooter.setCooldown(type.getMaterial(), 0);
                        cancelSound.play(loc, SoundCategory.PLAYERS, shooter);
                        cancel();
                    }
                } else {
                    shootRay(shooter);
                    data.setChargeLevel(0);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void shootRay(Player shooter) {
        Location origin = shooter.getEyeLocation();
        World world = origin.getWorld();
        List<LivingEntity> mobsHit = new ArrayList<>();
        Location targetLoc = origin.clone().add(origin.getDirection().multiply(128));
        Vector direction = getDirectionBetweenLocations(origin, targetLoc).normalize();
        shooter.setVelocity(origin.getDirection().normalize().multiply(-0.1));
        shootSound.play(origin, SoundCategory.PLAYERS);
        for (double d = 0.1; d < origin.distance(targetLoc); d += 0.1) {
            Location loc = origin.clone();
            loc.add(direction.clone().multiply(d));
            world.spawnParticle(Particle.END_ROD, loc, 1,
                    0, 0, 0, 0, null, true);
            if (((int) d) % 1.0 == 0) {
                if (!isBlockPassThrough(loc.getBlock().getType())) {
                    break;
                }
            }
            if (((int) d) % 5 == 0) {
                raySound.play(loc, SoundCategory.AMBIENT);
            }
            List<LivingEntity> mobs = (List<LivingEntity>) loc.getNearbyLivingEntities(0.25);
            for (LivingEntity mob : mobs) {
                if (mob instanceof Player || mobsHit.contains(mob)) continue;
                double damage = data.getDamage() * (Math.pow(data.getPiercePower(), mobsHit.size()));
                mob.damage(damage, shooter);
                mob.setVelocity(direction.clone().add(new Vector(0, 0.2, 0)).multiply(1.1));
                mobsHit.add(mob);
                world.spawnParticle(Particle.FIREWORKS_SPARK, loc, (int) damage / 4, 0, 0, 0, 0.025);
            }
        }
    }

    public void shootGleam(Profile profile) {
        Player shooter = profile.getPlayer();
        if (profile.isOnCooldown()) {
            cooldownError(shooter);
            return;
        }
        if (shooter.getCooldown(type.getMaterial()) > 0) return;
        World world = shooter.getWorld();
        Location origin = shooter.getEyeLocation();
        Location targetLoc = origin.clone().add(origin.getDirection().multiply(256));
        Vector direction = getDirectionBetweenLocations(origin, targetLoc).normalize();
        ultShootSound.play(origin, SoundCategory.PLAYERS);
        for (double d = 1; d < origin.distance(targetLoc); d += data.getUltWidth()) {
            Location loc = origin.clone();
            loc.add(direction.clone().multiply(d));
            world.spawnParticle(Particle.REDSTONE, loc, (int) (data.getUltWidth() * 30),
                    data.getUltWidth() / 2, data.getUltWidth() / 2, data.getUltWidth() / 2, 1,
                    new Particle.DustOptions(Color.fromRGB(140, 240, 255), 1), false);
            world.spawnParticle(Particle.REDSTONE, loc, (int) (data.getUltWidth() * 30),
                    data.getUltWidth() / 2, data.getUltWidth() / 2, data.getUltWidth() / 2, 1,
                    new Particle.DustOptions(Color.fromRGB(217, 250, 255), 1), false);
            List<LivingEntity> mobs = (List<LivingEntity>) loc.getNearbyLivingEntities(data.getUltWidth());
            if (mobs.isEmpty()) continue;
            for (LivingEntity entity : mobs) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    gleamMaskPlayer(player);
                } else {
                    entity.damage(data.getUltDamage(), shooter);
                    entity.setVelocity(direction.clone().add(new Vector(0, 0.15, 0)).multiply(1.25));
                }
            }
        }
        profile.startCooldown(data.getUltCooldown());
    }

    @Override
    public void createData(MadItem item) {
        data = new RailgunData(this, type);
    }

    @Override
    public ItemType getType() {
        return type;
    }

    private void gleamMaskPlayer(Player player) {
        World world = player.getWorld();
        ultMaskSound.play(player.getLocation(), SoundCategory.PLAYERS);
        world.spawnParticle(Particle.VILLAGER_HAPPY, player.getLocation().add(0, 1, 0), 25,
                0.25, 0.5, 0.25);
        world.spawnParticle(Particle.CRIT_MAGIC, player.getLocation().add(0, 1, 0), 30,
                0.25, 0.5, 0.25, 0);
        player.setAbsorptionAmount(player.getAbsorptionAmount() + data.getUltMasking());
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.getAbsorptionAmount() >= data.getUltMasking()) {
                player.setAbsorptionAmount(player.getAbsorptionAmount() - data.getUltMasking());
            } else {
                player.setAbsorptionAmount(0);
            }
        }, data.getUltMaskingDuration());
    }
}
