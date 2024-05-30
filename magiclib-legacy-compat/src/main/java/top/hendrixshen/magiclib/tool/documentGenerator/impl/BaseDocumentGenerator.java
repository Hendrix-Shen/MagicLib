package top.hendrixshen.magiclib.tool.documentGenerator.impl;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.tool.documentGenerator.api.DocumentGenerator;

public abstract class BaseDocumentGenerator implements DocumentGenerator {
    @Getter
    @Setter
    private String currentLanguageCode = BaseDocumentGenerator.DEFAULT_LANGUAGE;
    @Getter
    private final String identifier;

    public BaseDocumentGenerator(@NotNull String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String tr(String key, Object... objects) {
        return I18n.trByCode(key, this.getCurrentLanguageCode(), key, objects);
    }

    @Override
    public String tr(String key) {
        return I18n.trByCode(key, this.getCurrentLanguageCode(), key);
    }
}
