package com.github.klyser8.mobmadness.util;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;

public class ColorConstant implements TextColor {

    public static final int MID_GREEN_VALUE = 0x14c414;
    public static final int INDIGO_VALUE = 0xae93cf;
    public static final int FUCHSIA_VALUE = 0xd10870;
    public static final int ORANGE_VALUE = 0xff7700;
    public static final int LIME_VALUE = 0xfbff00;
    public static final int REAL_AQUA_VALUE = 0x00ffc3;
    public static final int BROWN_VALUE = 0x754400;
    public static final int LIGHT_GRAY_VALUE = 0xc9c9c9;

    public static final ColorConstant MID_GREEN = new ColorConstant("mid_green", MID_GREEN_VALUE);
    public static final ColorConstant INDIGO = new ColorConstant("indigo", INDIGO_VALUE);
    public static final ColorConstant FUCHSIA = new ColorConstant("fuchsia", FUCHSIA_VALUE);
    public static final ColorConstant ORANGE = new ColorConstant("orange", ORANGE_VALUE);
    public static final ColorConstant LIME = new ColorConstant("lime", LIME_VALUE);
    public static final ColorConstant REAL_AQUA = new ColorConstant("real_aqua", REAL_AQUA_VALUE);
    public static final ColorConstant BROWN = new ColorConstant("brown", BROWN_VALUE);
    public static final ColorConstant LIGHT_GRAY = new ColorConstant("light_gray", LIGHT_GRAY_VALUE);

    @Override
    public int value() {
        return value;
    }

    private final String name;
    private final int value;
    private final HSVLike hsv;

    private ColorConstant(final String name, final int value) {
        this.name = name;
        this.value = value;
        this.hsv = HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }
}
