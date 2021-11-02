package com.github.klyser8.mobmadness.game.items.misc;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.ColorHelper;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class LifeElixir extends MadItem {

    public static final int HP_RESTORED = 1;
    public static final int COOLDOWN = 10;

    private final Sound drinkSound;
    private final List<Sound> healthUpChime;
    public LifeElixir(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.LIFE_ELIXIR, new ItemStack(ItemType.LIFE_ELIXIR.getMaterial(), amount), plugin, gameData, true);
        healthUpChime = new ArrayList<>(Arrays.asList(
                new Sound("entity.experience_orb.pickup", 1.0f, 1.189207f),
                new Sound("entity.experience_orb.pickup", 1.0f, 1.587401f),
                new Sound("entity.experience_orb.pickup", 1.0f, 1.781797f)
        ));
        drinkSound = new Sound("entity.generic.drink", 0.5f, 1.25f);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Grans the user ").color(NamedTextColor.GRAY)
                        .append(Component.text("1 ").color(WHITE))
                        .append(Component.text("\u2764").color(RED)),
                Component.text("permanently").color(NamedTextColor.GRAY),
                Component.text("--------------------------------").color(NamedTextColor.DARK_GRAY),
                Component.text("You can only have up to 20 \u2764").color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true)
        ));

        setTags(true, false, true, false, false, null);
    }

    /**
     * Increases the maximum amount of heart containers the player can have by 1, for a total of maximum
     * 20 heart containers.
     *
     * @param player the consuming player
     */
    public void healthUp(Player player, ItemStack item) {
        if (player.getCooldown(type.getMaterial()) > 0) return;
        player.setCooldown(getItemStack().getType(), 30);
        Location particleLoc = player.getEyeLocation();
        World world = particleLoc.getWorld();
        if (player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() >= 40) return;
        drinkSound.play(player.getLocation(), SoundCategory.PLAYERS);
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i == 0) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 2);
                    player.setHealth(player.getHealth() + 2);
                    item.setAmount(item.getAmount() - 1);
                }
                healthUpChime.get(i).play(player.getLocation(), SoundCategory.NEUTRAL, player);
                if (i == 2) cancel();
                i++;
            }
        }.runTaskTimer(plugin, 5, 2);
    }
}
