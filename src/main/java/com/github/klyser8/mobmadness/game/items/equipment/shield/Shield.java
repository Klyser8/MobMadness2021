package com.github.klyser8.mobmadness.game.items.equipment.shield;

import com.github.klyser8.mobmadness.MobMadness;
import com.github.klyser8.mobmadness.game.items.ItemType;
import com.github.klyser8.mobmadness.game.items.MadItem;
import com.github.klyser8.mobmadness.storage.Game;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.mattstudios.mf.base.components.MfUtil.color;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class Shield extends MadItem { //TODO fix shield durability not going down when hit by an arrow

    private final ShieldData data;
    public static final int BASE_MAX_DURABILITY = 337;

    public Shield(ItemType type, MobMadness plugin, Game game) {
        super(type, new ItemStack(Material.SHIELD), plugin, game, true);
        data = new ShieldData(type);
        setup();
    }

    @Override
    protected void setup() {
        createLore();
        createShieldPatterns();
        nbt.setInteger(MAX_DURABILITY_TAG, data.getMaxDurability());
        nbt.setInteger(CURRENT_DURABILITY_TAG, data.getDurability());
    }

    private void createLore() {
        setCompLore(List.of(
                Component.text("Durability: ").color(GRAY)
                        .append(Component.text(data.getDurability()).color(YELLOW))
                        .append(Component.text("/").color(GRAY))
                        .append(Component.text(data.getMaxDurability()).color(WHITE))
        ));
    }


    private void createShieldPatterns() {
        List<Pattern> patterns = new ArrayList<>();
        switch (type) {
            case CROSS_SHIELD -> {
                patterns.add(new Pattern(DyeColor.GRAY, PatternType.BASE));
                patterns.add(new Pattern(DyeColor.LIGHT_GRAY, PatternType.STRIPE_MIDDLE));
                patterns.add(new Pattern(DyeColor.LIGHT_GRAY, PatternType.STRIPE_CENTER));
                patterns.add(new Pattern(DyeColor.GRAY, PatternType.STRAIGHT_CROSS));
            }
            case MOSSY_SHIELD -> {
                patterns.add(new Pattern(DyeColor.GREEN, PatternType.BASE));
                patterns.add(new Pattern(DyeColor.LIME, PatternType.TRIANGLE_BOTTOM));
                patterns.add(new Pattern(DyeColor.LIME, PatternType.TRIANGLE_TOP));
                patterns.add(new Pattern(DyeColor.GREEN, PatternType.TRIANGLE_BOTTOM));
                patterns.add(new Pattern(DyeColor.GREEN, PatternType.TRIANGLE_TOP));
                patterns.add(new Pattern(DyeColor.LIME, PatternType.RHOMBUS_MIDDLE));
                patterns.add(new Pattern(DyeColor.GREEN, PatternType.RHOMBUS_MIDDLE));
            }
            case PRISMARINE_SHIELD -> {
                patterns.add(new Pattern(DyeColor.CYAN, PatternType.BASE));
                patterns.add(new Pattern(DyeColor.BLUE, PatternType.BORDER));
                patterns.add(new Pattern(DyeColor.BLUE, PatternType.TRIANGLE_TOP));
                patterns.add(new Pattern(DyeColor.BLUE, PatternType.TRIANGLE_BOTTOM));
                patterns.add(new Pattern(DyeColor.CYAN, PatternType.RHOMBUS_MIDDLE));
                patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRAIGHT_CROSS));
            }
            case PINK_LOTUS_SHIELD -> {
                patterns.add(new Pattern(DyeColor.MAGENTA, PatternType.BASE));
                patterns.add(new Pattern(DyeColor.PURPLE, PatternType.GRADIENT));
                patterns.add(new Pattern(DyeColor.PINK, PatternType.FLOWER));
                patterns.add(new Pattern(DyeColor.PINK, PatternType.GRADIENT_UP));
            }
            case GLORIOUS_SHIELD -> {
                patterns.add(new Pattern(DyeColor.ORANGE, PatternType.BASE));
                patterns.add(new Pattern(DyeColor.BROWN, PatternType.GRADIENT_UP));
                patterns.add(new Pattern(DyeColor.YELLOW, PatternType.GRADIENT));
                patterns.add(new Pattern(DyeColor.BROWN, PatternType.CIRCLE_MIDDLE));
                patterns.add(new Pattern(DyeColor.ORANGE, PatternType.BORDER));
            }
            case WITHERS_SHIELD -> {
                patterns.add(new Pattern(DyeColor.RED, PatternType.BASE));
                patterns.add(new Pattern(DyeColor.BLACK, PatternType.SKULL));
                patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_BOTTOM));
                patterns.add(new Pattern(DyeColor.BLACK, PatternType.TRIANGLES_TOP));
            }
        }
        ItemMeta itemMeta = nbt.getItem().getItemMeta();
        BlockStateMeta blockMeta = (BlockStateMeta) itemMeta;
        Banner banner = (Banner) blockMeta.getBlockState();
        banner.setPatterns(patterns);
        banner.update();
        blockMeta.setBlockState(banner);
        blockMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        nbt.getItem().setItemMeta(blockMeta);
    }
}
