package top.hendrixshen.magiclib.impl.i18n.minecraft.translation;

import lombok.Getter;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/">Carpet-TIS-Addition</a>
 * <p>
 * With this you can use {@link TranslationContext#tr} freely in your target class
 */
@Getter
public class TranslationContext {
    private final Translator translator;

    protected TranslationContext(Translator translator) {
        this.translator = translator;
    }

    protected TranslationContext(String translationPath) {
        this(new Translator(translationPath));
    }

    protected MutableComponentCompat tr(String key, Object... args) {
        return this.translator.tr(key, args);
    }
}
