package com.github.klyser8.mobmadness.game.items.weapons.bow;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.game.items.weapons.MadWeapon;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.game.entity.player.Profile;
import com.github.klyser8.mobmadness.util.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.github.klyser8.mobmadness.util.ColorHelper.MINERAL_GREEN;
import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Bow extends MadItem implements MadWeapon {

    private BowData data;

    public static final String HIGH_DAMAGE_TAG = "mm.high_damage";
    public static final String ULT_COOLDOWN = "mm.ult.cooldown";

    private final Sound dashSound;

    public Bow(ItemType type, MobMadness plugin, Game game) {
        super(type, new ItemStack(type.getMaterial()), plugin, game, true);
        dashSound = new Sound("entity.player.attack_knockback", 1.0f, 0.5f);
        setup();
    }

    @Override
    protected void setup() {
        createData(this);
        setTags(false, false, true,
                false, true, null);
        createLore();
        nbt.setBoolean(WEAPON_TAG, true);
        nbt.setDouble(DAMAGE_TAG, data.getDamage());
        nbt.setDouble(HIGH_DAMAGE_TAG, data.getHighDamage());
        nbt.setInteger(ULT_COOLDOWN, data.getUltCooldown());
    }

    private void createLore() {
        /*List<String> lore = new ArrayList<>();
        lore.add("&7Max Damage: &f" + data.getDamage() + "-" + data.getHighDamage());
        lore.add("&7Max Range: &f80 blocks");
        lore.add("&8--------------------------------");
        lore.add("&5Left-Click: &fAllows the user to dash");
        lore.add("&fin a direction, tossing any enemy hit.");
        lore.add("&fUpon tossing an enemy, you <mineral green> Mark it.");
        lore.add("");
        lore.add("&dCooldown: &f" + data.getUltCooldown() / 20 + "s");
        lore.add("&dMark Duration: &f" + data.getUltMarkDuration() / 20 + "s");
        setLore(lore);*/
        setCompLore(List.of(
                text("Damage: ").color(GRAY)
                        .append(text(data.getDamage() + "-" + data.getHighDamage()).color(WHITE)),
                text("Max Range: ").color(GRAY)
                        .append(text("80 blocks").color(WHITE)),
                text("--------------------------------").color(DARK_GRAY),
                text("Left-Click: ").color(DARK_PURPLE)
                        .append(text("Allows the user to dash").color(WHITE)),
                text("in a direction, tossing any enemy hit.").color(WHITE),
                text("Upon tossing an enemy, you").color(WHITE)
                        .append(text(" Mark ").color(MINERAL_GREEN))
                        .append(text("it").color(WHITE)),
                text(""),
                text("Mark Duration: ").color(LIGHT_PURPLE)
                        .append(text(data.getUltMarkDuration() / 20 + "s").color(WHITE)),
                text("Cooldown: ").color(LIGHT_PURPLE)
                        .append(text(data.getUltCooldown() / 20).color(WHITE))
                        .append(text("s").color(WHITE))
        ));
    }

    @Override
    public void createData(MadItem item) {
        data = new BowData(this, type);
    }

    /**
     * Casts the ultimate ability "Crippling Dash".
     *
     * @param profile the profile of the caster
     */
    public void cripplingDash(Profile profile) {
        Player player = profile.getPlayer();
        if (profile.isOnCooldown()) {
            cooldownError(player);                          //If the cooldown of the player is higher than 0, send error
            return;
        }
        player.setInvulnerable(true);                                //The player should be invulnerable during the dash
        player.setVelocity(player.getLocation().getDirection().normalize().add(
                new Vector(0, 0.15, 0)).multiply(2));
        dashSound.play(player.getLocation(), SoundCategory.PLAYERS);
        new BowUltRunnable(plugin, player, data).runTaskTimer(plugin, 0, 1);
        profile.startCooldown(data.getUltCooldown());
    }

    @Override
    public ItemType getType() {
        return type;
    }
}
