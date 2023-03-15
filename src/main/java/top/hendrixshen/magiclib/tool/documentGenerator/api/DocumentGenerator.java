package top.hendrixshen.magiclib.tool.documentGenerator.api;

public interface DocumentGenerator {
    String DEFAULT_LANGUAGE = "en_us";

    String tr(String key);

    String tr(String key, Object... objects);

    void generateDocument();
}
