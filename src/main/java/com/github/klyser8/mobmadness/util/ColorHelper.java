package com.github.klyser8.mobmadness.util;

import com.github.klyser8.mobmadness.MobMadness;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;

public class ColorHelper implements TextColor {

    private static final int MID_GREEN_VALUE = 0x14c414;
    private static final int INDIGO_VALUE = 0xae93cf;
    private static final int FUCHSIA_VALUE = 0xd10870;
    private static final int ORANGE_VALUE = 0xff7700;
    private static final int LIME_VALUE = 0xc8ff00;
    private static final int REAL_AQUA_VALUE = 0x00ffc3;
    private static final int BROWN_VALUE = 0x754400;
    private static final int LIGHT_GRAY_VALUE = 0xc9c9c9;
    private static final int HONEYSACKLE_VALUE = 0xedfc84;
    private static final int EGGPLANT_VALUE = 0x91438a;
    private static final int METALLIC_BLUE_VALUE = 0x4f70a8;
    private static final int MINERAL_GREEN_VALUE = 0x316649;

    public static final ColorHelper MID_GREEN = new ColorHelper("mid_green", MID_GREEN_VALUE);
    public static final ColorHelper INDIGO = new ColorHelper("indigo", INDIGO_VALUE);
    public static final ColorHelper FUCHSIA = new ColorHelper("fuchsia", FUCHSIA_VALUE);
    public static final ColorHelper ORANGE = new ColorHelper("orange", ORANGE_VALUE);
    public static final ColorHelper LIME = new ColorHelper("lime", LIME_VALUE);
    public static final ColorHelper REAL_AQUA = new ColorHelper("real_aqua", REAL_AQUA_VALUE);
    public static final ColorHelper BROWN = new ColorHelper("brown", BROWN_VALUE);
    public static final ColorHelper LIGHT_GRAY = new ColorHelper("light_gray", LIGHT_GRAY_VALUE);
    public static final ColorHelper HONEYSACKLE = new ColorHelper("honeysackle", HONEYSACKLE_VALUE);
    public static final ColorHelper EGGPLANT = new ColorHelper("eggplant", EGGPLANT_VALUE);
    public static final ColorHelper METALLIC_BLUE = new ColorHelper("metallic_blue", METALLIC_BLUE_VALUE);
    public static final ColorHelper MINERAL_GREEN = new ColorHelper("mineral_green", MINERAL_GREEN_VALUE);

    private static final String RAW_GRADIENT_HEX_REGEX = "<\\$#[A-Fa-f0-9]{6}>";

    @Override
    public int value() {
        return value;
    }

    private final String name;
    private final int value;
    private final HSVLike hsv;

    private ColorHelper(final String name, final int value) {
        this.name = name;
        this.value = value;
        this.hsv = HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }

    /**
     * Applies gradients to the provided string
     *
     * Originally from Iridescent
     *
     * @param msg the string to parse
     * @return the parsed string
     */
    public static Component gradient(String msg, TextColor fromColor, TextColor toColor) {
        //Puts a $ sign between each character, removes the last $ sign and inserts one at the beginning.
/*        msg = msg.replaceAll(".", "$0\\$");
        msg = "$" + msg;
        msg = msg.substring(0, msg.length() - 1);*/
        float fromRed = fromColor.red();
        float fromGreen = fromColor.green();
        float fromBlue = fromColor.blue();
        float toRed = toColor.red();
        float toGreen = toColor.green();
        float toBlue = toColor.blue();

        //How much each value of the color should change by, every time the gradient is applied.
        float stepRed = (fromRed - toRed) / msg.length();
        float stepGreen = (fromGreen - toGreen) / msg.length();
        float stepBlue = (fromBlue - toBlue) / msg.length();
        TextComponent component = Component.text("");
//        List<TextColor> colors = new ArrayList<>();
        for (int i = 0; i < msg.length(); i++) {
            /*colors.add(TextColor.color(fromRed + stepRed * i,
                                    fromGreen + stepGreen * i,
                                    fromBlue + stepBlue * i));*/
            component = component.append(Component.text(msg.charAt(i)).color(TextColor.color(

                    (int) (fromRed - stepRed * i),
                    (int) (fromGreen - stepGreen * i),
                    (int) (fromBlue - stepBlue * i))));
        }
        return component;
    }
}
