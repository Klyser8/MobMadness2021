package com.github.klyser8.mobmadness.game.items.weapons.crossbow;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.game.mob.MadEntity;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.game.player.Profile;
import com.github.klyser8.mobmadness.util.MadMath;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.github.klyser8.mobmadness.util.MadUtils.createEllipse;
import static com.github.klyser8.mobmadness.util.MadUtils.isBlockPassThrough;
import static me.mattstudios.mf.base.components.MfUtil.color;

public class Crossbow extends MadItem implements MadWeapon {

    private CrossbowData data;

    public static final String HIGH_DAMAGE_TAG = "mm.high_damage";
    public static final String LOAD_SPEED_TAG = "mm.load_speed";
    public static final String PIERCE_LEVEL_TAG = "mm.pierce_level";
    public static final String MULTISHOT_TAG = "mm.multishot";
    public static final String ULT_DAMAGE_TAG = "mm.ult.damage";
    public static final String ULT_RADIUS_TAG = "mm.ult.radius";
    public static final String ULT_COOLDOWN_TAG = "mm.ult.cooldown";

    private final Sound portalIn;
    private final Sound portalOut;
    private final Sound rocketSound;

    public Crossbow(ItemType type, ItemStack item, MobMadness plugin, GameData gameData) {
        super(type, item, plugin, gameData, true);
        this.portalIn = new Sound("block.respawn_anchor.charge", 1.0f, 2.0f);
        this.portalOut = new Sound("block.respawn_anchor.deplete", 1.0f, 1.0f);
        this.rocketSound = new Sound("entity.ender_eye.launch", 1.0f, 1.0f);
        setup();
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
        nbt.setDouble(HIGH_DAMAGE_TAG, data.getHighDamage());
        nbt.setObject(LOAD_SPEED_TAG, data.getLoadEnch());
        nbt.setObject(PIERCE_LEVEL_TAG, data.getPierceEnch());
        nbt.setObject(MULTISHOT_TAG, data.getMultiShot());
        nbt.setDouble(ULT_DAMAGE_TAG, data.getUltDamage());
        nbt.setInteger(ULT_RADIUS_TAG, data.getUltPierceLevel());
        nbt.setInteger(ULT_COOLDOWN_TAG, data.getUltCooldown());
    }

    private void createLore() {
        List<String> lore = new ArrayList<>();
        double loadSpeed = 1.25;
        double pierceLevel = 0;
        if (data.getLoadEnch() != null) {
            loadSpeed -= MadMath.round(data.getLoadEnch().getLevel() * 0.25, 2);
        }
        if (data.getPierceEnch() != null) {
            pierceLevel += Math.round(data.getPierceEnch().getLevel());
        }
        lore.add("&7Damage: &f" + data.getDamage() + "-" + data.getHighDamage());
        lore.add("&7Loading time: &f" + loadSpeed + "s");
        lore.add("&7Max. Range: &f130 blocks");
        lore.add("&7Pierce Level: &f" + pierceLevel);
        lore.add("&8--------------------------------");
        lore.add("&5Shift-Right Click: &fFires an ");
        lore.add("&d&oenchanted&f arrow, which has homing &");
        lore.add("&fand teleporting abilities.");
        lore.add("&d&oEnchanted&7&o: possesses reality");
        lore.add("&7&owarping abilities.");
        lore.add("");
        lore.add("&dUlt. Damage: &f" + data.getUltDamage());
        lore.add("&dUlt. Pierce Level: &f" + data.getUltPierceLevel());
        lore.add("&dUlt. Cooldown: &f" + data.getUltCooldown() / 20 + "s");
        setLore(lore);
    }

    @Override
    public void createData(MadItem item) {
        data = new CrossbowData(this, type);
    }

    /**
     * Casts the ultimate ability "Enchanted Shot".
     *
     * @param profile the profile of the caster
     * @param arrow the arrow shot
     */
    public void shootArrow(Profile profile, Arrow arrow) {
        Player shooter = profile.getPlayer();
        if (profile.isOnCooldown()) {
            cooldownError(shooter);                         //If the cooldown of the player is higher than 0, send error
            return;
        }
        World world = shooter.getWorld();
        arrow.setCritical(false);                                   //Remove critical particles from the enchanted arrow
        arrow.setGravity(false);                                               //Arrow should not be affected by gravity
        arrow.setVelocity(arrow.getVelocity().multiply(0.2));
        arrow.setDamage(data.getUltDamage());
        new BukkitRunnable() {
            int lifetime = 0;
            double oldPitch = 0;
            @Override
            public void run() {
                lifetime++;
                if (arrow.isInBlock() || arrow.isDead() || lifetime > 600) cancel();   //Stop homing if arrow's dead/old
                Location arrowLoc = arrow.getLocation();
                world.spawnParticle(Particle.SPELL_WITCH, arrowLoc, 5, 0, 0, 0);
                LivingEntity target = pickTarget(arrow.getLocation());
                if (target == null || lifetime < 20) return;
                rocketSound.play(arrowLoc, SoundCategory.AMBIENT);
                Vector velocity = arrow.getVelocity();
                Location targetLocation = target.getEyeLocation();
                velocity.add(targetLocation.subtract(arrowLoc).toVector().normalize().multiply(0.22));
                arrow.setVelocity(velocity.normalize().multiply(0.5));
                if (target.hasLineOfSight(arrow)) return;                   //If arrow can reach mob, it should not warp
                Location portalLoc = createPortal(arrowLoc, arrow.getVelocity().normalize());
                if (portalLoc == null || Math.abs(oldPitch - arrowLoc.getPitch()) > 1) {
                    oldPitch = arrowLoc.getPitch();
                    return;
                }
                Location tpLoc = target.getEyeLocation().add(0, 10, 0);
                createPortalParticles(portalLoc);                           //Create particles at entrance of the portal
                createPortalParticles(tpLoc);                                   //Create particles at exit of the portal
                if (arrowLoc.distance(portalLoc) < 1) {
                    portalIn.play(arrowLoc, SoundCategory.AMBIENT);
                    world.spawnParticle(Particle.FLASH, portalLoc, 1, 0, 0, 0, 0);
                    arrow.teleport(tpLoc);
                    createPortalParticles(tpLoc);
                    portalOut.play(tpLoc, SoundCategory.AMBIENT);
                } //FIXME multiple arrows hitting same target result in infinite homing bug!
            }

            //Create portal particles at the provided location
            private void createPortalParticles(Location portalLoc) {
                world.spawnParticle(Particle.REDSTONE, portalLoc.add(0, 0, 0), 15,
                        0.25, 0.25, 0.25, 0,
                        new Particle.DustOptions(Color.fromRGB(116, 4, 196), 1), false);
                List<Location> ellipseLocs = createEllipse(portalLoc.add(0, -0.5, 0));
                for (Location loc : ellipseLocs) {
                    world.spawnParticle(Particle.REDSTONE, loc, 1, 0, 0, 0, 0,
                            new Particle.DustOptions(Color.fromRGB(183, 2, 219), 1), false);
                }
            }

            //Creates a portal, based on the provided location of the arrow and direction
            private Location createPortal(Location arrowLoc, Vector direction) {
                for (int i = 1; i < 10; i++) {
                    Vector directionClone = direction.clone();
                    Location locClone = arrowLoc.clone();
                    directionClone.multiply(i);
                    locClone.add(directionClone);
                    if (isBlockPassThrough(locClone.getBlock().getType())) {
                        continue;
                    }
                    return arrowLoc.clone().add(direction.multiply(i - 0.5));
                }
                return null;
            }

            //Picks a target for the arrow to home onto.
            private LivingEntity pickTarget(Location arrowLoc) {
                List<LivingEntity> entityList = new ArrayList<>();
                for (LivingEntity nearbyEntity : arrowLoc.getNearbyLivingEntities(50, 50, 50)) {
                    if (nearbyEntity instanceof Player) continue;
                    if (nearbyEntity.hasMetadata(MadEntity.PIERCED_TAG) &&
                            nearbyEntity.getMetadata(MadEntity.PIERCED_TAG).get(0).asString()
                                    .equals(arrow.getUniqueId().toString())) continue;
                    entityList.add(nearbyEntity);
                }
                LivingEntity target = null;
                double distance = 1000;
                for (LivingEntity mob : entityList) {
                    if (distance < mob.getLocation().distance(arrowLoc) || mob.isDead()) continue;
                    distance = mob.getLocation().distance(arrowLoc);
                    target = mob;
                }
                return target;
            }
        }.runTaskTimer(plugin, 0, 1);
        profile.startCooldown(data.getUltCooldown());
    }

    @Override
    public ItemType getType() {
        return type;
    }
}
