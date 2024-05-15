package top.hendrixshen.magiclib.util.minecraft;

import net.minecraft.commands.CommandSource;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;

import java.text.NumberFormat;

public class StringUtil {
    public static String translateOrFallback(String key, String fallback) {
        String translated = I18n.tr(key);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }

    public static String translateOrFallback(String key, String fallback, Object... object) {
        String translated = I18n.tr(key, object);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }

    public static String translateOrFallback(CommandSource source, String key, String fallback) {
        String translated = I18n.tr(source, key);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }

    public static String translateOrFallback(CommandSource source, String key, String fallback, Object... object) {
        String translated = I18n.tr(source, key, object);

        if (!key.equals(translated)) {
            return translated;
        }

        return fallback;
    }

    public static String fractionDigit(double value, int digit) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(digit);
        nf.setMaximumFractionDigits(digit);
        return nf.format(value);
    }
}
