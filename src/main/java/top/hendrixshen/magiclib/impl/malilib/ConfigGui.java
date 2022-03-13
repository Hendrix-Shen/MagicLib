package top.hendrixshen.magiclib.impl.malilib;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import top.hendrixshen.magiclib.util.FabricUtil;
import top.hendrixshen.magiclib.util.malilib.ConfigManager;
import top.hendrixshen.magiclib.util.malilib.Option;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigGui extends GuiConfigsBase {
    private final String identifier;
    private final ConfigManager configManager;
    private static String tab;

    public ConfigGui(String identifier, String defaultTab, ConfigManager configManager) {
        super(10, 50, identifier, null, StringUtils.translate(String.format("%s.gui.title", identifier)));
        tab = defaultTab;
        this.identifier = identifier;
        this.configManager = configManager;
    }

    public ConfigGui(String identifier, String defaultTab, ConfigManager configManager, String title) {
        super(10, 50, identifier, null, title);
        tab = defaultTab;
        this.identifier = identifier;
        this.configManager = configManager;
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        int x = 10;
        int y = 26;

        for (String category : configManager.getCategories()) {
            x += this.createNavigationButton(x, y, category);
        }

        Set<String> possibleTypes = this.configManager.getOptionsByCategory(tab).stream().map(Option::getCategory).collect(Collectors.toSet());
        List<String> items = this.configManager.getCategories().stream().filter(possibleTypes::contains).collect(Collectors.toList());
        items.add(0, null);
    }

    private int createNavigationButton(int x, int y, String category) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, StringUtils.translate(String.format("%s.gui.button.tab.%s", this.identifier, category)));
        button.setEnabled(!Objects.equals(category, tab));
        this.addButton(button, (b, mouseButton) -> {
            tab = category;
            this.reDraw();
        });
        return button.getWidth() + 2;
    }

    public void reDraw() {
        this.reCreateListWidget();
        Objects.requireNonNull(this.getListWidget()).resetScrollbarPosition();
        this.initGui();
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<IConfigBase> configs = Lists.newArrayList();
        List<ConfigBooleanHotkeyed> compatConfigs = Lists.newArrayList();
        for (Option option : this.configManager.getOptionsByCategory(tab)) {
            if (!option.isEnabled() && this.configManager.isHideDisabled()) {
                continue;
            }
            if (option.isDebug() && this.configManager.isHideDebug()) {
                continue;
            }
            if (option.isDevOnly() && !FabricUtil.isDevelopmentEnvironment() && this.configManager.isHideDevOnly()) {
                continue;
            }
            if (!option.isMatchedMinecraftVersion() && this.configManager.isHideUnmatchedMinecraftVersion()) {
                continue;
            }
            if (this.configManager.getCompatCategories().contains(tab)) {
                compatConfigs.add((ConfigBooleanHotkeyed) option.getConfig());
                continue;
            }
            configs.add(option.getConfig());
        }

        if (!compatConfigs.isEmpty()) {
            compatConfigs.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
            return ConfigOptionWrapper.createFor(ConfigUtils.createConfigWrapperForType(ConfigType.HOTKEY, compatConfigs));
        }

        configs.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return ConfigOptionWrapper.createFor(configs);
    }
}
