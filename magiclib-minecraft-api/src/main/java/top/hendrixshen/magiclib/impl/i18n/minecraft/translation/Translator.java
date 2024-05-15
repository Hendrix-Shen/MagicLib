package top.hendrixshen.magiclib.impl.i18n.minecraft.translation;

import com.google.common.base.Strings;
import lombok.Getter;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/">Carpet-TIS-Addition</a>
 */
@Getter
public class Translator {
    private final String translationPath;

    public Translator(String translationPath) {
        if (
                Strings.isNullOrEmpty(translationPath) ||
                        translationPath.startsWith(".") ||
                        translationPath.endsWith(".")) {
            throw new RuntimeException("Invalid translation path: " + translationPath);
        }

        this.translationPath = translationPath;
    }

    public Translator getDerivedTranslator(String derivedName) {
        return new Translator(this.translationPath + "." + derivedName);
    }

    public MutableComponentCompat tr(String key, Object... args) {
        return ComponentUtil.tr(key, args);
    }
}
