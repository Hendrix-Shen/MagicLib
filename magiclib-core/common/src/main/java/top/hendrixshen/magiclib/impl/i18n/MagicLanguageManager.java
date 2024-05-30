package top.hendrixshen.magiclib.impl.i18n;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.i18n.LanguageProvider;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.impl.i18n.provider.FileLanguageProvider;
import top.hendrixshen.magiclib.impl.i18n.provider.JarLanguageProvider;
import top.hendrixshen.magiclib.util.MiscUtil;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MagicLanguageManager {
    public static final String DEFAULT_CODE = I18n.DEFAULT_CODE;
    @Getter(lazy = true)
    private static final MagicLanguageManager instance = new MagicLanguageManager();

    private final Map<String, String> currentLanguage = new ConcurrentHashMap<>();
    private final Map<String, Map<String, String>> language = Maps.newConcurrentMap();
    private final List<String> languageOrder = Lists.newArrayList();
    private final List<String> fallbackLanguage = Lists.newArrayList(DEFAULT_CODE);
    private final List<LanguageProvider> providers = Lists.newArrayList();

    @Getter
    private String currentCode = MiscUtil.getSystemLanguageCode();

    private MagicLanguageManager() {
        if (MagicLib.getInstance().getCurrentPlatform().getPlatformType()
                .matches(PlatformType.FABRIC_LIKE)) {
            this.providers.add(JarLanguageProvider.getInstance());
        }

        this.providers.add(FileLanguageProvider.getInstance());
        this.providers.forEach(LanguageProvider::init);
        this.init();
    }

    private void init() {
        // Recalculate language load order.
        this.languageOrder.addAll(this.fallbackLanguage);
        this.languageOrder.remove(this.currentCode);
        this.languageOrder.add(0, this.currentCode);
        // Disallow Default Language to be replaced.
        this.languageOrder.remove(MagicLanguageManager.DEFAULT_CODE);
        this.languageOrder.add(MagicLanguageManager.DEFAULT_CODE);
        this.updateFallbackLanguage();
        this.updateCurrentLanguage();
    }

    private @NotNull Map<String, String> getLanguage(String languageCode) {
        Map<String, String> result = this.language.get(languageCode);

        if (result == null) {
            result = Maps.newConcurrentMap();

            for (LanguageProvider provider : this.providers) {
                result.putAll(provider.getLanguage(languageCode));
            }

            this.language.put(languageCode, result);
        }

        return result;
    }

    private void updateFallbackLanguage() {
        this.languageOrder.forEach(this::getLanguage);
    }

    private void updateCurrentLanguage() {
        for (int i = this.languageOrder.size() - 1; i >= 0; i--) {
            String code = this.languageOrder.get(i);
            this.currentLanguage.putAll(this.getLanguage(code));
        }
    }

    public void reload() {
        this.currentLanguage.clear();
        this.language.clear();
        this.languageOrder.clear();
        this.providers.forEach(LanguageProvider::reload);
        this.init();
    }

    /**
     * Set current language code.
     *
     * @return True if language code updated.
     */
    public boolean setCurrentCode(String code) {
        if (!this.currentCode.equalsIgnoreCase(code)) {
            this.currentCode = code.toLowerCase();
            this.reload();
            return true;
        }

        return false;
    }

    public void setFallbackLanguage(@NotNull List<String> fallbackLanguage) {
        this.fallbackLanguage.clear();
        fallbackLanguage.forEach(code -> {
            if (!this.fallbackLanguage.contains(code.toLowerCase(Locale.ROOT))) {
                this.fallbackLanguage.add(code.toLowerCase(Locale.ROOT));
            }
        });
        this.fallbackLanguage.removeIf(code -> code.equals(MagicLanguageManager.DEFAULT_CODE));
        this.fallbackLanguage.add(MagicLanguageManager.DEFAULT_CODE);
        this.reload();
    }

    public void registerLanguageProvider(@NotNull LanguageProvider provider) {
        provider.init();
        this.providers.add(provider);
    }

    public String get(String key) {
        return this.currentLanguage.getOrDefault(key, key);
    }

    public String get(String key, Object... objects) {
        String translateValue = this.currentLanguage.getOrDefault(key, key);

        try {
            return String.format(translateValue, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + translateValue;
        }
    }

    public String getInCode(String code, String key) {
        return this.getLanguage(code).getOrDefault(key, key);
    }

    public String getInCode(String code, String key, Object... objects) {
        String translateValue = this.getInCode(code, key);

        try {
            return String.format(translateValue, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + translateValue;
        }
    }

    public String getByCode(String code, String key) {
        // Bypass code order calc.
        if (this.currentCode.equals(code)) {
            return this.get(key);
        } else if (MagicLanguageManager.DEFAULT_CODE.equals(code)) {
            return this.getLanguage(code).getOrDefault(key, key);
        }

        List<String> languageOrder = this.generateLanguageOrder(code);

        for (String languageCode : languageOrder) {
            String translateValue = this.getLanguage(languageCode).getOrDefault(key, key);

            if (!translateValue.equals(key)) {
                return translateValue;
            }
        }

        return key;
    }

    public String getByCode(String code, String key, Object... objects) {
        String translateValue = this.getByCode(code, key);

        try {
            return String.format(translateValue, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + translateValue;
        }
    }

    public boolean exists(String key) {
        return this.currentLanguage.containsKey(key);
    }

    public boolean exists(String code, String key) {
        return this.getLanguage(code).containsKey(key);
    }

    public boolean existsIn(String code, String key) {
        return this.generateLanguageOrder(code).stream()
                .anyMatch(c -> this.getLanguage(c).containsKey(key));
    }

    private @NotNull List<String> generateLanguageOrder(String code) {
        List<String> result = Lists.newArrayList(this.fallbackLanguage);
        result.remove(code);
        result.add(0, code);
        result.remove(MagicLanguageManager.DEFAULT_CODE);
        result.add(code);
        return result;
    }
}
