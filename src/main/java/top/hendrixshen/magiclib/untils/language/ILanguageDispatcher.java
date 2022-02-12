package top.hendrixshen.magiclib.untils.language;

public interface ILanguageDispatcher {
    /**
     * Register the localised language for your mod.
     *
     * @param modId    Your mod Identifier.
     * @param language Localised language code.
     */
    void register(String modId, String language);
}
