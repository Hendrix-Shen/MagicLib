package top.hendrixshen.magiclib.language;

public class I18n {

    private I18n() {
    }


    public static String get(String key, Object... objects) {
        return MagicLanguageManager.INSTANCE.get(key, objects);
    }

    public static String getByCode(String code, String key, Object... objects) {
        return MagicLanguageManager.INSTANCE.getByCode(code, key, objects);
    }

    public static boolean exists(String key) {
        return MagicLanguageManager.INSTANCE.exists(key);
    }

    public static boolean exists(String code, String key) {
        return MagicLanguageManager.INSTANCE.exists(code, key);
    }
}
