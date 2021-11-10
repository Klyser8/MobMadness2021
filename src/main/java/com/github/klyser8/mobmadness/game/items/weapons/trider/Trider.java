package com.github.klyser8.mobmadness.game.items.weapons.trider;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.entity.soulstates.Dazzled;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.github.klyser8.mobmadness.util.MadUtils.getDirectionBetweenLocations;
import static me.mattstudios.mf.base.components.MfUtil.color;

public class Trider extends MadItem implements MadWeapon {

    private TriderData data;

    public static final String RANGED_DMG_TAG = "mm.ranged_damage";
    public static final String LOYALTY_WAIT_TAG = "mm.loyalty_wait";
    public static final String RANGE_TAG = "mm.range";

    public static final String ULT_DAZZLE_MAX_DURATION = "mm.ult.dazzle_max_duration";
    public static final String ULT_RADIUS_TAG = "mm.ult.radius";
    public static final String ULT_COOLDOWN_TAG = "mm.ult.cooldown";

    public static final String RIPTIDING_TAG = "mm.riptiding";

    private final Sound morphSound;
    private final Sound tideBreakerSplashSound;
    private final Sound tideBreakerExplodeSound;
    private final Sound fallOffWallSound;
    private final Sound returnSound;


    public Trider(ItemType type, MobMadness plugin, Game game) {
        super(type, new ItemStack(type.getMaterial()), plugin, game, true);
        setup();
        morphSound = new Sound("entity.glow_squid.squirt", 1, 1);
        tideBreakerSplashSound = new Sound("entity.axolotl.splash", 1, 0.5f);
        //Change sound pitch based on current weapon upgrade
        tideBreakerExplodeSound = new Sound("entity.glow_squid.death", 2.0f,
                (float) (1.45f - getRangeMultiplier()) * 1.3f);
        fallOffWallSound = new Sound("item.trident.return", 1.0f, 1.5f);
        returnSound = new Sound("item.trident.riptide_1", 1.0f, 1.5f);
    }

    @Override
    protected void setup() {
        createData(this);
        setTags(false, false, true,
                false, true, null);
        createLore();
        nbt.setBoolean(WEAPON_TAG, true);
        nbt.setDouble(DAMAGE_TAG, data.getDamage());
        nbt.setDouble(RANGED_DMG_TAG, data.getRangedDamage());
        nbt.setInteger(LOYALTY_WAIT_TAG, data.getLoyaltyWait());
        nbt.setDouble(RANGE_TAG, data.getRange());
        nbt.setDouble(ULT_DAZZLE_MAX_DURATION, data.getUltDazzleMaxDuration());
        nbt.setDouble(ULT_RADIUS_TAG, data.getUltTideBreakerRadius());
        nbt.setInteger(ULT_COOLDOWN_TAG, data.getUltCooldown());
    }

    private void createLore() {
        List<String> lore = new ArrayList<>();
        lore.add("&7Melee Damage: &f" + data.getDamage());
        lore.add("&7Ranged Damage: &f" + data.getRangedDamage());
        lore.add("&7Loyalty Wait: &f" + data.getLoyaltyWait() / 20 + "s");
        lore.add("&7Range: &f" + data.getRange() + " blocks");
        lore.add("&8--------------------------------");
        lore.add("&5Shift + Throw:&f You and the trident");
        lore.add("&fbecome one until you hit an entity or");
        lore.add("&fa wall.");
        lore.add("&fUpon collision, you &bDazzle &fenemies");
        lore.add("&fand re-gain control of yourself.");
        lore.add("");
        lore.add("&dDazzle max. duration: &f" + data.getUltDazzleMaxDuration() / 20 + "s");
        lore.add("&dRadius: &f" + data.getUltTideBreakerRadius() + " blocks");
        lore.add("&dCooldown: &f" + data.getUltCooldown() / 20 + "s");
        setLore(lore);
    }

    public void throwTrident(Trident trident) {
        World world = trident.getWorld();
        Player shooter = (Player) trident.getShooter();
        if (shooter == null) return;
        new BukkitRunnable() {

            int groundClock = 0;

            @Override
            public void run() {
                //Mimic crit arrow trail!
                for (int i = 0; i < 4; i++) {
                    world.spawnParticle(Particle.SCRAPE, trident.getLocation().add(
                            trident.getVelocity().getX() * (double)i / 4.0,
                            trident.getVelocity().getY() * (double)i / 4.0,
                            trident.getVelocity().getZ() * (double)i / 4.0), 1,
                            0.01, 0.01, 0.01, 0.25);
                }
                if (groundClock == data.getLoyaltyWait()
                        && shooter.getGameMode() == GameMode.SURVIVAL
                        && !shooter.getInventory().contains(Material.TRIDENT)) {
                    returnTrident();
                }
                //Despawn trident if player has died!
                if (shooter.getGameMode() == GameMode.SPECTATOR) {
                    trident.remove();
                }
                if (trident.isDead()) {
                    cancel();
                }
                if (trident.isInBlock()) {
                    groundClock++;
                }
            }

            /**
             * Returns the trident to the player, with added particle effects and sounds.
             */
            private void returnTrident() {
                shooter.getInventory().addItem(trident.getItem());
                Vector direction = getDirectionBetweenLocations(
                        trident.getLocation(), shooter.getEyeLocation()).normalize();
                fallOffWallSound.play(trident.getLocation(), SoundCategory.NEUTRAL);
                returnSound.play(shooter.getLocation(), SoundCategory.PLAYERS, shooter);
                world.spawnParticle(Particle.GLOW_SQUID_INK, trident.getLocation(), 5,
                        0.1, 0.1, 0.1, 0.01);
                for (double d = 0.1; d < trident.getLocation().distance(shooter.getEyeLocation()); d += 0.1) {
                    Location loc = trident.getLocation().clone();
                    loc.add(direction.clone().multiply(d));
                    shooter.spawnParticle(Particle.SCRAPE, loc, 1,
                            0.1, 0.1, 0.1, 0.1);
                    shooter.spawnParticle(Particle.COMPOSTER, loc, 1,
                            0.1, 0.1, 0.1, 0);
                }
                trident.remove();
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    private void calculateTrident(Consumer<Trident> consumer) {
//        consumer.accept(consumer);
    }

    //particles for trident: 1. Glow 2. Glow_squid_ink 3. scrape
    public void riptide(Profile profile, Trident trident) {
        Player rider = profile.getPlayer();
        //If riptide is on cooldown then:
        if (profile.isOnCooldown()) {
            //If rider is in survival, give cooldown error
            if (rider.getGameMode() != GameMode.CREATIVE) {
                cooldownError(rider);
                throwTrident(trident);
                return;
            }
        }
        trident.setMetadata(RIPTIDING_TAG, new FixedMetadataValue(plugin, true));
        World world = rider.getWorld();
        Location origin = rider.getEyeLocation();
        morphSound.play(origin, SoundCategory.PLAYERS);
        rider.hidePlayer(plugin, rider);
        trident.addPassenger(rider);
        world.spawnParticle(Particle.GLOW_SQUID_INK, origin, 100, 0.25, 0.25, 0.5, 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                world.spawnParticle(Particle.GLOW, trident.getLocation(), 5, 0, 0, 0);
                world.spawnParticle(Particle.DOLPHIN, trident.getLocation(), 10, 0.5, 0.5, 0.5);
                world.spawnParticle(Particle.SCRAPE, trident.getLocation(), 5,
                        0.1, 0.1, 0.1, 1);
                if (!trident.hasMetadata(RIPTIDING_TAG)
                        || !trident.getMetadata(RIPTIDING_TAG).get(0).asBoolean()
                        || trident.isDead()) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        profile.startCooldown(data.getUltCooldown());
    }

    public void tideBreaker(Profile profile, Trident trident, org.bukkit.entity.Entity hitEntity) {
        trident.removeMetadata(RIPTIDING_TAG, plugin);
        trident.eject();
        Player rider = profile.getPlayer();
        World world = rider.getWorld();
        Location landingLoc = trident.getLocation();
        rider.showPlayer(plugin, rider);
        world.spawnParticle(Particle.GLOW, landingLoc, (int) (40 * data.getUltTideBreakerRadius()),
                (data.getUltTideBreakerRadius() / 5),
                (data.getUltTideBreakerRadius() / 5),
                (data.getUltTideBreakerRadius() / 5));
        world.spawnParticle(Particle.SCRAPE, landingLoc, (int) (50 * data.getUltTideBreakerRadius()),
                0, 0, 0, 10);
        world.spawnParticle(Particle.SCRAPE, landingLoc, (int) (50 * data.getUltTideBreakerRadius()),
                0.2 + (data.getUltTideBreakerRadius() / 5),
                0.2 + (data.getUltTideBreakerRadius() / 5),
                0.2 + (data.getUltTideBreakerRadius() / 5),
                0.1);
        world.spawnParticle(Particle.FIREWORKS_SPARK, landingLoc, (int) (50 * data.getUltTideBreakerRadius()),
                0.2 + (data.getUltTideBreakerRadius() / 5),
                0.2 + (data.getUltTideBreakerRadius() / 5),
                0.2 + (data.getUltTideBreakerRadius() / 5),
                0.1);
        tideBreakerSplashSound.play(landingLoc, SoundCategory.PLAYERS);
        tideBreakerExplodeSound.play(landingLoc, SoundCategory.PLAYERS);
        for (LivingEntity mob : landingLoc.getNearbyLivingEntities(data.getUltTideBreakerRadius(),
                data.getUltTideBreakerRadius(), data.getUltTideBreakerRadius())) {
            if (game.getBeastMap().containsKey(mob)) {
                Beast beast = game.getBeastMap().get(mob);
                double modifiedDistance = mob.getEyeLocation().distance(landingLoc) / 3;
                if (modifiedDistance < 1) {
                    modifiedDistance = 1;
                }
                int dazzleDuration = (int) (data.getUltDazzleMaxDuration() / modifiedDistance);
                double damage = data.getRangedDamage() / (modifiedDistance * 2);
                new Dazzled(plugin, game, beast, dazzleDuration);
                //avoid damaging the same entity twice.
                if (hitEntity == null || !hitEntity.getUniqueId().equals(mob.getUniqueId())) {
                    mob.damage(damage, trident);
                }
            }
        }
    }

    @Override
    public void createData(MadItem item) {
        data = new TriderData(this, type);
    }

    @Override
    public ItemType getType() {
        return type;
    }

    public double getRangeMultiplier() {
        return switch (type) {
            case IMPRUDENT_TRIDER -> 0.38;
            case LOYAL_TRIDER -> 0.41;
            case FERROUS_TRIDER -> 0.44;
            case ANGELIC_TRIDER -> 0.48;
            case HELLISH_TRIDER -> 0.51;
            case MANIC_TRIDER -> 0.54;
            default -> 1.0;
        };
    }
}
