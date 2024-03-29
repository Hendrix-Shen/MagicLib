package top.hendrixshen.magiclib.api.malilib.config.option;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;
import top.hendrixshen.magiclib.util.minecraft.StringUtil;

import java.util.Arrays;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
public interface EnumOptionEntry extends IConfigOptionListEntry {
    String name();

    int ordinal();

    EnumOptionEntry[] getAllValues();

    EnumOptionEntry getDefault();

    String getTranslationPrefix();

    @Override
    default String getStringValue() {
        return this.name().toLowerCase();
    }

    @Override
    default String getDisplayName() {
        return StringUtil.translateOrFallback(String.format("%s.value.%s", this.getTranslationPrefix(), this.name()),
                this.name());
    }

    @Override
    default IConfigOptionListEntry cycle(boolean forward) {
        int index = this.ordinal();
        EnumOptionEntry[] values = this.getAllValues();
        index += forward ? 1 : -1;
        index = (index + values.length) % values.length;
        return values[index];
    }

    @Override
    default IConfigOptionListEntry fromString(String value) {
        return Arrays.stream(this.getAllValues())
                .filter(o -> o.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseGet(this::getDefault);
    }
}
