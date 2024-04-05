package top.hendrixshen.magiclib.api.malilib.config.gui;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigOptionList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.gui.GuiBase;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 *
 * <p>
 * See class {@link fi.dy.masa.malilib.gui.button.ConfigButtonOptionList} for the implementation details.
 */
public interface ConfigButtonOptionListHovering {
    void magiclib$setEnableValueHovering();

    default List<String> magiclib$makeHoveringLines(@NotNull IConfigOptionList config) {
        List<String> lines = Lists.newArrayList();
        IConfigOptionListEntry head = config.getDefaultOptionListValue();
        IConfigOptionListEntry current = config.getOptionListValue();
        int cnt = 0;

        // in case it loops forever
        final int MAX_ENTRIES = 128;
        lines.add("magiclib.config.gui.element.config_button_option_list_hovering.title");
        List<IConfigOptionListEntry> entries = Lists.newArrayList();
        boolean allEnum = true;

        for (IConfigOptionListEntry entry = head;
             (cnt == 0 || entry != head) && cnt < MAX_ENTRIES && entry != null;
             entry = entry.cycle(true)) {
            cnt++;
            entries.add(entry);
            allEnum &= entry instanceof Enum<?>;
        }

        if (allEnum) {
            entries.sort(Comparator.comparingInt(e -> ((Enum<?>) e).ordinal()));
        }

        for (IConfigOptionListEntry entry : entries) {
            String line = "  " + entry.getDisplayName();

            if (entry.equals(current)) {
                line += GuiBase.TXT_DARK_GRAY + "     <---" + GuiBase.TXT_RST;
            }

            lines.add(line);
        }

        return lines;
    }
}
