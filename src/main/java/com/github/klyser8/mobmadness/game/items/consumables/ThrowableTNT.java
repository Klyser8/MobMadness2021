package com.github.klyser8.mobmadness.game.items.consumables;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.GameData;
import com.github.klyser8.mobmadness.util.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;

import static me.mattstudios.mf.base.components.MfUtil.color;

public class ThrowableTNT extends MadItem {

    public static final double VELOCITY_MULTIPLIER = 1.25;
    public static final int FUSE_TICKS = 60;

    private final Sound throwSound;
    public ThrowableTNT(MobMadness plugin, int amount, GameData gameData) {
        super(ItemType.THROWABLE_TNT, new ItemStack(ItemType.THROWABLE_TNT.getMaterial(), amount), plugin, gameData, true);
        throwSound = new Sound("entity.witch.throw", 1.0f, 0.75f);
        setup();
    }

    protected void setup() {
        setCompLore(Arrays.asList(
                Component.text("Light-weight TNT which can").color(NamedTextColor.GRAY),
                Component.text("be thrown by ").color(NamedTextColor.GRAY)
                        .append(Component.text("right-clicking.").color(NamedTextColor.WHITE)),
                Component.text("--------------------------------").color(NamedTextColor.DARK_GRAY),
                Component.text("The explosion does not damage").color(NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, true),
                Component.text("nearby players").color(NamedTextColor.DARK_GRAY)
        ));

        setTags(true, false, true, false, false, null);
    }

    /**
     * Spawns an instance of {@link ThrowableTNT} in front of the player.
     * After that, applies velocity to the TNT so that it is 'thrown' at the direction the player is looking.
     *
     * @param player the player tossing the tnt
     * @param item the ItemStack the TNT came from
     */
    public void toss(Player player, ItemStack item) {
        if (player.getCooldown(ItemType.THROWABLE_TNT.getMaterial()) > 0) return;
        throwSound.play(player.getEyeLocation(), SoundCategory.PLAYERS);
        World world = player.getWorld();
        TNTPrimed tnt = (TNTPrimed) world.spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
        tnt.setSource(player);
        tnt.setFuseTicks(FUSE_TICKS);
        tnt.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(VELOCITY_MULTIPLIER));
        tnt.setMetadata(MM_ENUM_TAG, new FixedMetadataValue(plugin, ItemType.THROWABLE_TNT.toString()));
        item.setAmount(item.getAmount() - 1);
        player.setCooldown(ItemType.THROWABLE_TNT.getMaterial(), FUSE_TICKS);
    }
}
