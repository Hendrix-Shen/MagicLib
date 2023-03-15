package top.hendrixshen.magiclib.tool.documentGenerator.impl;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.language.api.I18n;
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
        String value;
        return (value = I18n.getByCode(this.getCurrentLanguageCode(), key, objects)).equals(key) ?
                I18n.getByCode(DocumentGenerator.DEFAULT_LANGUAGE, key, objects) : value;
    }

    @Override
    public String tr(String key) {
        String value;
        return (value = I18n.getByCode(this.getCurrentLanguageCode(), key)).equals(key) ?
                I18n.getByCode(DocumentGenerator.DEFAULT_LANGUAGE, key) : value;
    }
}
