package com.github.klyser8.mobmadness.game.items.weapons.crossbow;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.util.MadMath;
import com.github.klyser8.mobmadness.util.MadUtils;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Crossbow extends MadItem implements MadWeapon {

    private CrossbowData data;

    public static final String HIGH_DAMAGE_TAG = "mm.high_damage";
    public static final String LOAD_SPEED_TAG = "mm.load_speed";
    public static final String PIERCE_LEVEL_TAG = "mm.pierce_level";
    public static final String MULTISHOT_TAG = "mm.multishot";
    public static final String ENCHANTED_TAG = "mm.enchanted";
    public static final String ULT_DAMAGE_TAG = "mm.ult.damage";
    public static final String ULT_RADIUS_TAG = "mm.ult.radius";
    public static final String ULT_COOLDOWN_TAG = "mm.ult.cooldown";

    private final Sound portalIn;
    private final Sound portalOut;
    private final Sound rocketSound;

    public Crossbow(ItemType type, MobMadness plugin, Game game) {
        super(type, new ItemStack(type.getMaterial()), plugin, game, true);
        this.portalIn = new Sound("block.respawn_anchor.charge", 1.0f, 2.0f);
        this.portalOut = new Sound("block.respawn_anchor.deplete", 1.0f, 1.0f);
        this.rocketSound = new Sound("entity.ender_eye.launch", 1.0f, 1.0f);
        setup();
    }

    @Override
    protected void setup() {
        createData(this);
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
        arrow.setVelocity(arrow.getVelocity().multiply(0.25));
        arrow.setDamage(data.getUltDamage());
        arrow.setPierceLevel(data.getUltPierceLevel());
        new BukkitRunnable() {
            int lifetime = 0;
            double oldPitch = 0;
            @Override
            public void run() {
                lifetime++;
                if (arrow.isInBlock() || arrow.isDead() || lifetime > 600) {
                    cancel();   //Stop homing if arrow's dead/old
                }
                Location arrowLoc = arrow.getLocation();
                world.spawnParticle(Particle.SPELL_WITCH, arrowLoc, 5, 0, 0, 0);
                Beast target = pickTarget();
                if (target == null || lifetime < 20) return;
                rocketSound.play(arrowLoc, SoundCategory.AMBIENT);
                Vector velocity = arrow.getVelocity();
                Location targetLocation = target.getMob().getEyeLocation();
                velocity.add(targetLocation.subtract(arrowLoc).toVector().normalize().multiply(0.5));
                arrow.setVelocity(velocity.normalize().multiply(0.5));
            }

            //Picks a target for the arrow to home onto.
            private Beast pickTarget() {
                List<Beast> mobList = game.getBeastMap().values().stream().toList();
                if (mobList.isEmpty()) {
                    return null;
                }
                mobList.removeIf(beast -> beast.getMob().hasMetadata(Beast.PIERCED_TAG) &&
                        beast.getMob().getMetadata(Beast.PIERCED_TAG).get(0).asString()
                                .equals(arrow.getUniqueId().toString()));
                return MadUtils.getClosestVisibleBeast(arrow, mobList);
            }
        }.runTaskTimer(plugin, 0, 1);
        if (profile.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            profile.startCooldown(data.getUltCooldown());
        }
    }

    @Override
    public ItemType getType() {
        return type;
    }
}
