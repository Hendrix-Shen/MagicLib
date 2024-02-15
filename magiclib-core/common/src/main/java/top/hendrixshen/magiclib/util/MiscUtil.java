package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MiscUtil {
    public static @NotNull String getSystemLanguageCode() {
        return Locale.getDefault().toString().toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
