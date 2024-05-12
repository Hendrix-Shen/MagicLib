package top.hendrixshen.magiclib.malilib.impl.gui;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.ConfigOption;
import top.hendrixshen.magiclib.util.RenderUtil;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public class ConfigGui extends GuiConfigsBase {
    private String tab;
    private final String identifier;
    private final Supplier<String> titleSupplier;
    private final ConfigManager configManager;

    public ConfigGui(String identifier, String defaultTab, ConfigManager configManager, String title) {
        this(identifier, defaultTab, configManager, () -> title);
    }

    public ConfigGui(String identifier, String defaultTab, ConfigManager configManager, @NotNull Supplier<String> titleSupplier) {
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
        this.initBottomLine();

        for (String category : configManager.getCategories()) {
            if (this.configManager.getOptionsByCategory(category).stream().noneMatch(ConfigOption::isEnabled)) {
                continue;
            }

            x += this.createNavigationButton(x, y, category);
        }
    }

    private void initBottomLine() {
        int available = 0;
        int unavailable = 0;
        int modified = 0;

        for (ConfigOption option : this.configManager.getAllOptions()) {
            if (option.isEnabled()) {
                if (option.getConfig().isModified()) {
                    modified++;
                }

                available++;
            } else {
                unavailable++;
            }
        }

        int total = available + unavailable;
        String stats = I18n.tr("magiclib.gui.bottom_line.stat", total, available, unavailable, modified);
        int width = RenderUtil.getRenderWidth(stats);
        int height = RenderUtil.TEXT_HEIGHT;
        int x = 10;
        int y = this.height - height - GuiBase.TOP;
        this.addLabel(x, y, width, height, 0xFFAAAAAA, stats);
    }

    private int createNavigationButton(int x, int y, String category) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, I18n.tr(String.format("%s.gui.button.tab.%s", this.identifier, category)));
        button.setEnabled(!Objects.equals(category, tab) && this.configManager.getOptionsByCategory(category).stream().anyMatch(ConfigOption::isEnabled));

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

        for (ConfigOption option : this.configManager.getOptionsByCategory(tab)) {
            if (!option.isEnabled()) {
                continue;
            }

            configs.add(option.getConfig());
        }

        configs.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return ConfigOptionWrapper.createFor(configs);
    }
}
