package com.github.klyser8.mobmadness.game.items.weapons.scepter;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.game.mob.Beast;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.util.MadMath;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Scepter extends MadItem implements MadWeapon {

    private ScepterData data;

    public static final String RADIUS_TAG = "mm.radius";
    public static final String PROJECTILE_SPEED_TAG = "mm.proj_speed";
    public static final String MANA_COST_TAG = "mm.mana_cost";
    public static final String COOLDOWN_TAG = "mm.cooldown";
    public static final String ULT_DAMAGE_TAG = "mm.ult.damage";
    public static final String ULT_RADIUS_TAG = "mm.ult.radius";
    public static final String ULT_PROJECTILE_SPEED_TAG = "mm.ult.proj_speed";
    public static final String ULT_MANA_COST_TAG = "mm.ult.mana_cost";

    private final Sound shootSound;
    private final Sound ultSound;

    public Scepter(ItemType type, ItemStack item, MobMadness plugin, GameData gameData) {
        super(type, item, plugin, gameData, true);
        setup();
        shootSound = new Sound("entity.blaze.shoot", 1, 1.0f + data.getCooldown() / 20f);
        ultSound = new Sound("entity.blaze.shoot", 1, 1.0f + (data.getCooldown() / 20f) - 0.5f);
    }

    @Override
    protected void setup() {
        createData(this);
        setTags(false, false, true,
                false, true, null);
        createLore();
        nbt.setBoolean(WEAPON_TAG, true);
        nbt.setDouble(DAMAGE_TAG, data.getDamage());
        nbt.setDouble(RADIUS_TAG, data.getRadius());
        nbt.setDouble(PROJECTILE_SPEED_TAG, data.getProjSpeed());
        nbt.setFloat(MANA_COST_TAG, data.getManaCost());
        nbt.setInteger(COOLDOWN_TAG, data.getCooldown());
        nbt.setDouble(ULT_DAMAGE_TAG, data.getUltDamage());
        nbt.setDouble(ULT_RADIUS_TAG, data.getUltRadius());
        nbt.setDouble(ULT_PROJECTILE_SPEED_TAG, data.getUltProjSpeed());
        nbt.setFloat(ULT_MANA_COST_TAG, data.getUltManaCost());
    }

    private void createLore() {
        List<String> lore = new ArrayList<>();
        lore.add("&7Damage: &f" + data.getDamage());
        lore.add("&7Damage Radius: &f" + data.getRadius() + " blocks");
        lore.add("&7Mana Cost: &f" + data.getManaCost()+ "&3\u27E1");
        lore.add("&7Proj. Speed: &f" + Math.round(data.getProjSpeed() * 28) + " blocks/s");
        lore.add("&7Cooldown: &f" + MadMath.round(data.getCooldown() / 20f, 1) + "s");
        lore.add("&8--------------------------------");
        lore.add("&5Shift + Right-Click: &fShoots a meteor");
        lore.add("&fwhich damages and knocks away all");
        lore.add("&fenemies hit.");
        lore.add("");
        lore.add("&dDamage: &f" + data.getUltDamage());
        lore.add("&dMana cost: &f" + data.getUltManaCost() * 100 + " &3\u27E1");
        lore.add("&dProj. Speed: &f" + Math.round(data.getUltProjSpeed() * 28) + " blocks/s");
        lore.add("&dMeteor Radius: &f" + data.getUltRadius() + " blocks");
        setLore(lore);
    }

    public void shoot(Profile profile) {
        Player shooter = profile.getPlayer();
        World world = shooter.getWorld();
        Location eyeLoc = shooter.getEyeLocation();
        if (shooter.getCooldown(type.getMaterial()) > 0 || shooter.getExp() < data.getManaCost()) {
            return;
        }
        shooter.setCooldown(type.getMaterial(), data.getCooldown());
        SmallFireball fireball = (SmallFireball) world.spawnEntity(
                eyeLoc.add(eyeLoc.getDirection().multiply(0.5)), EntityType.SMALL_FIREBALL);
        fireball.setVelocity(eyeLoc.getDirection().normalize().multiply(data.getProjSpeed()));
        shootSound.play(eyeLoc, SoundCategory.PLAYERS);
        fireball.setShooter(shooter);
        fireball.setIsIncendiary(false);
        fireball.setMetadata(Beast.DAMAGE_TAG, new FixedMetadataValue(plugin, data.getDamage()));
        fireball.setMetadata(RADIUS_TAG, new FixedMetadataValue(plugin, data.getRadius()));
        setMana(profile, data.getManaCost());
    }

    public void meteor(Profile profile) {
        Player shooter = profile.getPlayer();
        World world = shooter.getWorld();
        Location eyeLoc = shooter.getEyeLocation();
        if (shooter.getExp() < data.getUltManaCost() || shooter.getCooldown(type.getMaterial()) > 0) {
            errorSound.play(shooter.getLocation(), SoundCategory.MASTER, shooter);
            return;
        }
        shooter.setVelocity(shooter.getLocation().getDirection().normalize().multiply(-1.075));
        shooter.setCooldown(type.getMaterial(), data.getCooldown());
        Fireball fireball = (Fireball) world.spawnEntity(
                eyeLoc.add(eyeLoc.getDirection().multiply(0.5)), EntityType.FIREBALL);
        fireball.setVelocity(eyeLoc.getDirection().normalize().multiply(data.getUltProjSpeed()));
        ultSound.play(eyeLoc, SoundCategory.PLAYERS);
        fireball.setShooter(shooter);
        fireball.setIsIncendiary(false);
        fireball.setMetadata(Beast.DAMAGE_TAG, new FixedMetadataValue(plugin, data.getUltDamage()));
        fireball.setMetadata(RADIUS_TAG, new FixedMetadataValue(plugin, data.getUltRadius()));
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fireball.isDead()) cancel();
                world.spawnParticle(Particle.FLAME, fireball.getLocation().add(0, 0.25, 0.), 10,
                        0.1, 0.1, 0.1, 0.01);
            }
        }.runTaskTimer(plugin, 3, 1);
        setMana(profile, data.getUltManaCost());
    }

    @Override
    public void createData(MadItem item) {
        data = new ScepterData(this, type);
    }

    @Override
    public ItemType getType() {
        return type;
    }

    /**
     * Removes XP from the player's XP bar to match their current mana.
     *
     * @param profile the profile who's mana should be changed
     * @param amount the amount of mana to take away.
     */
    private void setMana(Profile profile, float amount) {
        Player player = profile.getPlayer();
        if (player.getExp() >= amount) {
            player.setExp(player.getExp() - amount);
        }
    }
}
