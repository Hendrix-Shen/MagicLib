package top.hendrixshen.magiclib.gui;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.util.StringUtils;
import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.config.Option;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ConfigGui extends GuiConfigsBase {
    private static String tab;
    private final String identifier;
    private final Supplier<String> titleSupplier;

    private final ConfigManager configManager;

    public ConfigGui(String identifier, String defaultTab, ConfigManager configManager, String title) {
        this(identifier, defaultTab, configManager, () -> title);
    }

    public ConfigGui(String identifier, String defaultTab, ConfigManager configManager, Supplier<String> titleSupplier) {
        super(10, 50, identifier, null, titleSupplier.get());
        tab = defaultTab;
        this.identifier = identifier;
        this.configManager = configManager;
        this.titleSupplier = titleSupplier;
    }

    @Override
    public void removed() {
        super.removed();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();
        this.setTitle(this.titleSupplier.get());
        int x = 10;
        int y = 26;

        for (String category : configManager.getCategories()) {
            if (this.configManager.getOptionsByCategory(category).stream().noneMatch(Option::isEnabled)) {
                continue;
            }
            x += this.createNavigationButton(x, y, category);
        }
    }

    private int createNavigationButton(int x, int y, String category) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, StringUtils.translate(String.format("%s.gui.button.tab.%s", this.identifier, category)));
        button.setEnabled(!Objects.equals(category, tab) && this.configManager.getOptionsByCategory(category).stream().anyMatch(Option::isEnabled));
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
        for (Option option : this.configManager.getOptionsByCategory(tab)) {
            if (!option.isEnabled()) {
                continue;
            }
            configs.add(option.getConfig());
        }

        configs.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return ConfigOptionWrapper.createFor(configs);
    }
}
