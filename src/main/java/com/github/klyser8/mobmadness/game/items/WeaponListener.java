package com.github.klyser8.mobmadness.game.items;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.entity.beast.Beast;
import com.github.klyser8.mobmadness.game.items.equipment.shield.Shield;
import com.github.klyser8.mobmadness.game.items.weapons.bow.Bow;
import com.github.klyser8.mobmadness.game.items.weapons.crossbow.Crossbow;
import com.github.klyser8.mobmadness.game.items.weapons.rayler.Rayler;
import com.github.klyser8.mobmadness.game.items.weapons.scepter.Scepter;
import com.github.klyser8.mobmadness.game.items.weapons.smudger.Smudger;
import com.github.klyser8.mobmadness.game.items.weapons.sword.Sword;
import com.github.klyser8.mobmadness.game.items.weapons.trider.Trider;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.util.MadMath;
import com.github.klyser8.mobmadness.util.Sound;
import com.github.klyser8.mobmadness.util.MadUtils;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.github.klyser8.mobmadness.util.MadUtils.*;
import static me.mattstudios.mf.base.components.MfUtil.color;

/**
 * Event Listener for weapons.
 * Adds effects, changes damage and range of certain weapons and projectiles.
 * Also listens for Ultimate ability uses.
 *
 */
public class WeaponListener implements Listener {

    private final MobMadness plugin;
    private final Random random;
    public WeaponListener(MobMadness plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    @EventHandler                          //Checks whether the player matches certain criteria to cast the sword's ult.
    public void onUltUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!player.isSneaking()) return;
        ItemStack item = event.getItem();
        Game game = MadUtils.getEntityGame(plugin, player);
        if (isItemInvalid(game, item)) return;
        Profile profile = game.getPlayers().get(player);
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
        if (nbt.getString(MadItem.MM_ENUM_TAG).contains("SWORD")) {
            event.setCancelled(true);
            ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
            if (!(profile.getCurrentWeapon() instanceof Sword) || profile.getCurrentWeapon().getType() != type) {
                profile.setCurrentWeapon(new Sword(type, plugin, game));
            }
            ((Sword) profile.getCurrentWeapon()).healingBeam(profile);
        }
    }

    @EventHandler
    public void onWeaponRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() != null && event.getItem().getType() == Material.WOODEN_PICKAXE) {
            PacketContainer container1 = new PacketContainer(PacketType.Play.Server.ANIMATION);
            container1.getIntegers().write(0, event.getPlayer().getEntityId());
            container1.getIntegers().write(1, 0);
            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(event.getPlayer(), container1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        ItemStack item = event.getItem();
        Game game = MadUtils.getEntityGame(plugin, player);
        if (isItemInvalid(game, item)) return;
        NBTItem nbt = new NBTItem(item);
        Profile profile = game.getPlayers().get(player);
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
        if (nbt.getString(MadItem.MM_ENUM_TAG).contains("SCEPTER")) {
            event.setCancelled(true);
            ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
            if (!(profile.getCurrentWeapon() instanceof Scepter) || profile.getCurrentWeapon().getType() != type) {
                profile.setCurrentWeapon(new Scepter(type, plugin, game));
            }
            if (player.isSneaking()) {
                ((Scepter) profile.getCurrentWeapon()).meteor(profile);
            } else {
                ((Scepter) profile.getCurrentWeapon()).shoot(profile);
            }
        } else if (nbt.getString(MadItem.MM_ENUM_TAG).contains("RAYLER")) {
            event.setCancelled(true);
            ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
            if (!(profile.getCurrentWeapon() instanceof Rayler) || profile.getCurrentWeapon().getType() != type) {
                profile.setCurrentWeapon(new Rayler(type, plugin, game));
            }
            if (player.isSneaking()) {
                ((Rayler) profile.getCurrentWeapon()).shootGleam(profile);
            } else {
                ((Rayler) profile.getCurrentWeapon()).charge(player);
            }
        } else if (nbt.getString(MadItem.MM_ENUM_TAG).contains("SMUDGER")) {
            event.setCancelled(true);
            ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
            if (!(profile.getCurrentWeapon() instanceof Smudger) || profile.getCurrentWeapon().getType() != type) {
                profile.setCurrentWeapon(new Smudger(type, plugin, game));
            }
            ((Smudger) profile.getCurrentWeapon()).shoot(profile);
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        ItemStack item = event.getItem();
        Game game = MadUtils.getEntityGame(plugin, player);
        if (isItemInvalid(game, item)) return;
        Profile profile = game.getPlayers().get(player);
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
        event.setCancelled(true);
        if (nbt.getString(MadItem.MM_ENUM_TAG).contains("_BOW")) {
            ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
            if (!(profile.getCurrentWeapon() instanceof Bow) || profile.getCurrentWeapon().getType() != type) {
                profile.setCurrentWeapon(new Bow(type, plugin, game));
            }
            ((Bow) profile.getCurrentWeapon()).cripplingDash(profile);
        }
    }

    @EventHandler
    public void onBowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getProjectile();
        Player player = (Player) event.getEntity();
        ItemStack item = event.getBow();
        Game game = MadUtils.getEntityGame(plugin, player);
        if (isItemInvalid(game, item)) return;
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
        if (nbt.getString(MadItem.MM_ENUM_TAG).contains("_BOW")) {
            arrow.setVelocity(arrow.getVelocity().multiply(0.81));              //Set max travel distance of arrow to 80
        }
        double damage = MadMath.randomDoubleBetween(nbt.getDouble(MadItem.DAMAGE_TAG),
                ((nbt.getDouble(Bow.HIGH_DAMAGE_TAG))));
        arrow.setDamage(damage * event.getForce());
    }

    @EventHandler
    public void onCrossbowShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getProjectile() instanceof Arrow)) return;
        Arrow arrow = (Arrow) event.getProjectile();
        Player player = (Player) event.getEntity();
        ItemStack item = event.getBow();
        Game game = MadUtils.getEntityGame(plugin, player);
        if (isItemInvalid(game, item)) return;
        Profile profile = game.getPlayers().get(player);
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
        if (nbt.getString(MadItem.MM_ENUM_TAG).contains("CROSSBOW")) {
            if (!player.isSneaking()) {
                double damage = MadMath.randomDoubleBetween(nbt.getDouble(Crossbow.DAMAGE_TAG),
                        nbt.getDouble(Crossbow.HIGH_DAMAGE_TAG));
                arrow.setDamage(damage);
                return;
            }
            arrow.setMetadata(Crossbow.ENCHANTED_TAG, new FixedMetadataValue(plugin, true));
            ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
            if (!(profile.getCurrentWeapon() instanceof Crossbow) || profile.getCurrentWeapon().getType() != type) {
                profile.setCurrentWeapon(new Crossbow(type, plugin, game));
            }
            ((Crossbow) profile.getCurrentWeapon()).shootArrow(profile, arrow);
        }
    }

    @EventHandler
    public void onTridentThrow(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Trident tridentEntity
                && event.getEntity().getShooter() instanceof Player player) {
            ItemStack item = tridentEntity.getItem();
            World world = player.getWorld();
            Game game = getEntityGame(plugin, player);
            if (game == null) return;
            if (isItemInvalid(game, item)) return;
            NBTItem nbt = new NBTItem(item);
            Profile profile = game.getPlayers().get(player);
            if (nbt.getString(MadItem.MM_ENUM_TAG).contains("TRIDER")) {
                ItemType type = ItemType.valueOf(nbt.getString(MadItem.MM_ENUM_TAG));
                if (!(profile.getCurrentWeapon() instanceof Trider) || profile.getCurrentWeapon().getType() != type) {
                    profile.setCurrentWeapon(new Trider(type, plugin, game));
                }
                Trider trider = ((Trider) profile.getCurrentWeapon());
                //Set the range of the trider to the appropriate one.
                tridentEntity.setVelocity(tridentEntity.getVelocity().multiply(trider.getRangeMultiplier()));
                if (player.isSneaking()) {
                    trider.riptide(profile, tridentEntity);
                } else {
                    trider.throwTrident(tridentEntity);
                }
            }
        }
    }

    @EventHandler
    public void onTridentDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof Trident) {

            Trident trident = (Trident) event.getDismounted();
            if (trident.hasMetadata(Trider.RIPTIDING_TAG)
                && trident.getMetadata(Trider.RIPTIDING_TAG).get(0).asBoolean()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler                                                                          //Sets trider's ranged damage
    public void onTridentDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Trident) || !(event.getEntity() instanceof LivingEntity)) return;
        LivingEntity entity = (LivingEntity) event.getEntity();
        Trident trident = (Trident) event.getDamager();
        ItemStack item = trident.getItem();
        Player shooter = (Player) trident.getShooter();
        if (shooter == null) return;
        Game game = MadUtils.getEntityGame(plugin, shooter);
        if (game == null) return;
        if (isItemInvalid(game, item)) return;
        Profile profile = game.getPlayers().get(shooter);
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey(MadItem.MM_ENUM_TAG) ||
                (trident.hasMetadata(Trider.RIPTIDING_TAG)
                && trident.getMetadata(Trider.RIPTIDING_TAG).get(0).asBoolean())) return;
        event.setDamage(nbt.getDouble(Trider.RANGED_DMG_TAG));
        entity.setKiller(shooter);
    }

    @EventHandler
    public void onTridentCollide(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Trident) {
            Trident trident = (Trident) event.getEntity();
            ItemStack item = trident.getItem();
            if (!(trident.getShooter() instanceof Player)) {
                return;
            }
            Player shooter = (Player) trident.getShooter();
            Game game = MadUtils.getEntityGame(plugin, shooter);
            if (game == null) return;
            if (isItemInvalid(game, item)) return;
            Profile profile = game.getPlayers().get(shooter);
            NBTItem nbt = new NBTItem(item);
            if (!nbt.hasKey(MadItem.MM_ENUM_TAG)) return;
            if (trident.getPassengers().contains(shooter)) {
                Trider trider = (Trider) profile.getCurrentWeapon();
                trider.tideBreaker(profile, trident, event.getHitEntity());
            }
        }
    }

    @EventHandler                                                                                   //Sword damage logic
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();
        Game game = MadUtils.getEntityGame(plugin, player);
        if (isItemInvalid(game, item)) return;
        NBTItem nbt = new NBTItem(item);
        String enumStr = nbt.getString(MadItem.MM_ENUM_TAG);
        if (enumStr.contains("SWORD")) {
            if (event.isCritical()) {
                event.setDamage(nbt.getDouble(Sword.CRIT_DAMAGE_TAG));
            } else {
                event.setDamage(nbt.getDouble(Sword.DAMAGE_TAG) * player.getAttackCooldown());
            }
        } else if (enumStr.contains("TRIDER")) {
            event.setDamage(nbt.getDouble(Trider.DAMAGE_TAG) * player.getAttackCooldown());
        }
    }

    @EventHandler                                                                              //Shield durability logic
    public void onItemDamage(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Game game = MadUtils.getEntityGame(plugin, player);
        if (isItemInvalid(game, item)) return;
        NBTItem nbt = new NBTItem(item);
        String enumStr = nbt.getString(MadItem.MM_ENUM_TAG);
        ItemMeta meta = nbt.getItem().getItemMeta();
        if (meta == null || nbt.getBoolean(MadItem.UNBREAKING_TAG)) return;
        handleDurabilityLore(meta, nbt);
        if (enumStr.contains("SHIELD")) {
            if (nbt.getItem().getItemMeta() instanceof Damageable) {
                Damageable damageable = (Damageable) nbt.getItem().getItemMeta();
                int durabilityStep = Shield.BASE_MAX_DURABILITY / nbt.getInteger(Shield.MAX_DURABILITY_TAG);
                damageable.setDamage(durabilityStep *
                        (nbt.getInteger(Shield.MAX_DURABILITY_TAG) - nbt.getInteger(Shield.CURRENT_DURABILITY_TAG)));
                nbt.getItem().setItemMeta(damageable);
                nbt.setInteger(Shield.CURRENT_DURABILITY_TAG,
                        nbt.getInteger(Shield.CURRENT_DURABILITY_TAG) - event.getDamage());
            }
        }
        nbt.applyNBT(item);
    }

    @EventHandler                                                                              //Scepter fireballs logic
    public void onFireballHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Fireball)) return;
        Location fireballLoc = event.getEntity().getLocation();
        World world = fireballLoc.getWorld();
        Fireball fireball = (Fireball) event.getEntity();
        Player shooter = (Player) fireball.getShooter();
        org.bukkit.entity.Entity hitEntity = event.getHitEntity();
        double radius = fireball.getMetadata(Scepter.RADIUS_TAG).get(0).asDouble();
        double damage = fireball.getMetadata(Scepter.DAMAGE_TAG).get(0).asDouble();
        if (fireball instanceof SmallFireball) {
            new Sound("entity.generic.explode", 1.0f, (float) (3.0f - radius))
                    .play(fireballLoc, SoundCategory.PLAYERS);
            world.spawnParticle(Particle.EXPLOSION_LARGE, fireballLoc, 1,
                    0, 0, 0, 0, null, true);
        } else {
            playMeteorEffects(world, fireballLoc, radius);
        }
        List<Beast> beasts = getBeastsInArea(plugin, fireballLoc, radius, radius, radius);
        if (beasts.isEmpty()) return;
        if (hitEntity instanceof LivingEntity) {
            ((LivingEntity) hitEntity).damage(damage, shooter);
        }
        for (Beast beast : beasts) {
            Mob mob = beast.getMob();
            if (mob.equals(hitEntity) || mob instanceof Player) continue;
            launchEntitiesFireball(mob, fireballLoc, fireball, damage, shooter);
        }
    }

    /**
     * Checks if the item is invalid.
     * For an item to be invalid, it has to either:
     * - Fail a null check
     * - Not be part of any ongoing game
     * - Fail the {@link MadUtils#isMMItem(ItemStack)} check.
     *
     * @param game the game the item should be present in.
     * @param item the item to check
     * @return True if it succeeds all the checks above + isMMItem(), false otherwise.
     */
    private boolean isItemInvalid(Game game, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;
        if (game == null) return true;
        return !isMMItem(item);
    }

    /**
     * Checks whether a player's attack is a critical hit or not.
     * If a player is currently falling and is not on the ground, this will return true.
     *
     * @param player the player to check
     * @return true if the attack's a critical hit, false otherwise.
     * @deprecated since {@link EntityDamageByEntityEvent#isCritical()} exists
     */
    @Deprecated
    private boolean isAttackCritical(Player player) {
        return player.getFallDistance() > 0 && !player.isOnGround() && player.getAttackCooldown() == 1.0;
    }

    /**
     * Plays sound and particle effects for the Ultimate: Meteor.
     *
     * @param world the world to play the effects on
     * @param loc the location to play the effects at
     * @param radius the radius at which the effects should play
     */
    private void playMeteorEffects(World world, Location loc, double radius) {
        new Sound("entity.lightning_bolt.impact", 3.0f, (float) (2.0f - (radius / 0.5)))
                .play(loc, SoundCategory.PLAYERS);
        world.spawnParticle(Particle.EXPLOSION_LARGE, loc.add(0, 0.5, 0), (int) (radius * 7),
                radius / 3, radius / 3, radius / 3, 0);
        world.spawnParticle(Particle.LAVA, loc.add(0, 0.5, 0), (int) (radius * 20),
                radius / 3, radius / 3, radius / 3, 0);
        world.spawnParticle(Particle.FLAME, loc.add(0, 0.5, 0), (int) (radius * 20),
                radius / 3, radius / 3, radius / 3, 0.25);
        world.spawnParticle(Particle.SMOKE_LARGE, loc.add(0, 0.5, 0), (int) (radius * 20),
                radius / 3, radius / 3, radius / 3, 0.1);
    }

    /**
     * Launches the provided entity a certain distance based off the provided information.
     *
     * @param mob the mob to launch
     * @param fireballLoc the location at which the fireball landed
     * @param fireball the fireball entity
     * @param damage the base damage to be dealt
     * @param shooter the player who shot the fireball
     */
    private void launchEntitiesFireball(
            LivingEntity mob, Location fireballLoc, Fireball fireball, double damage, Player shooter) {
        double distance = fireballLoc.distance(mob.getLocation());
        if (distance < 1) return;
        double finalDmg = damage / distance;
        mob.damage(finalDmg, shooter);
        if (fireball instanceof SmallFireball) return;
        //Meteor code
        if (mob.equals(shooter)) return;
        Vector unitVector = mob.getLocation().toVector().subtract(
                fireballLoc.add(0, -1, 0).toVector()).normalize();
        mob.setVelocity(unitVector.multiply(2.5 - (fireballLoc.distance(mob.getLocation()) / 4)));
    }

    private void handleDurabilityLore(ItemMeta meta, NBTItem nbt) {
        List<String> lore = meta.getLore();
        if (lore != null) {
            int lineIndex = 0;
            String renewedLine = "&7Durability: &6" + nbt.getInteger(MadItem.CURRENT_DURABILITY_TAG) + "&7/" +
                    nbt.getInteger(MadItem.MAX_DURABILITY_TAG);
            while (lineIndex < Objects.requireNonNull(lore).size()) {
                if (lore.get(lineIndex).contains("Durability: ")) {
                    break;
                }
                lineIndex++;
            }
            lore.set(lineIndex, renewedLine);
            meta.setLore(color(lore));
            nbt.getItem().setItemMeta(meta);
        }
    }
}
