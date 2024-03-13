package top.hendrixshen.magiclib.api.i18n.minecraft;

import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;
import top.hendrixshen.magiclib.api.fake.i18n.ServerPlayerLanguage;

public class I18n extends top.hendrixshen.magiclib.api.i18n.I18n {
    public static String tr(CommandSource source, String key) {
        if (source instanceof ServerPlayer) {
            String code = ((ServerPlayerLanguage) source).magicLib$getLanguage();
            return I18n.trByCode(code, key);
        }

        return I18n.tr(key);
    }
    public static String tr(CommandSource source, String key, Object... objects) {
        if (source instanceof ServerPlayer) {
            String code = ((ServerPlayerLanguage) source).magicLib$getLanguage();
            return I18n.trByCode(code, key, objects);
        }

        return I18n.tr(key, objects);
    }
}
