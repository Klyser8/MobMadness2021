package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class Cookie extends MadItem {

    public static final int HP_RESTORED = 1;
    public static final int COOLDOWN = 10;

    private final Sound eatSound;
    public Cookie(MobMadness plugin, int amount, Game game) {
        super(ItemType.COOKIE, new ItemStack(ItemType.COOKIE.getMaterial(), amount), plugin, game, true);
        eatSound = new Sound("entity.generic.eat", 0.5f, 1.25f);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Instantly restores ").color(NamedTextColor.GRAY)
                        .append(Component.text("0.5 ").color(NamedTextColor.WHITE))
                        .append(Component.text("\u2764").color(NamedTextColor.RED)),
                Component.text("upon consumption").color(NamedTextColor.GRAY)
        ));

        setTags(true, false, true, false, false, null);
    }

    /**
     * Heals the player who consumes the cookie by 1 (half a heart).
     * Only works if the item is off cooldown.
     *
     * @param player the consuming player
     * @param cookie the cookie being eaten
     */
    public void eat(Player player, ItemStack cookie) {
        if (player.getCooldown(type.getMaterial()) > 0) return;
        Location particleLoc = player.getEyeLocation();
        World world = particleLoc.getWorld();
        int hp = (int) player.getHealth();
        if (hp + HP_RESTORED <= Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()) {
            player.setHealth(hp + HP_RESTORED);
            eatSound.play(player.getLocation(), SoundCategory.PLAYERS);
            player.setCooldown(type.getMaterial(), COOLDOWN);
            world.spawnParticle(Particle.HEART, particleLoc, HP_RESTORED,
                    0.2, 0.2, 0.2, 0, null, true);
            cookie.setAmount(cookie.getAmount() - 1);
        }
    }
}
