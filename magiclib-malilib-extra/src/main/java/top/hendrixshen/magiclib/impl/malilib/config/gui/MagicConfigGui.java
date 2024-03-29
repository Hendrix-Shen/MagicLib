package top.hendrixshen.magiclib.impl.malilib.config.gui;

import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigResettable;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.util.minecraft.RenderUtil;
import top.hendrixshen.magiclib.util.minecraft.StringUtil;
import top.hendrixshen.magiclib.util.serializable.JsonSaveAble;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class MagicConfigGui extends GuiConfigsBase {
    private final String identifier;
    private final MagicConfigManager configManager;
    private final Supplier<String> titleProvider;

    public MagicConfigGui(String identifier, MagicConfigManager configManager,
                          String title) {
        this(identifier, configManager, () -> title);
    }

    public MagicConfigGui(String identifier, MagicConfigManager configManager,
                          @NotNull Supplier<String> titleProvider) {
        super(10, 50, identifier, null, titleProvider.get());
        this.identifier = identifier;
        this.configManager = configManager;
        this.titleProvider = titleProvider;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();
        this.setTitle(this.titleProvider.get());
        int x = 10;
        int y = 26;

        for (String category : this.configManager.getCategories()) {
            if (this.configManager.getContainers(category).stream().noneMatch(ConfigContainer::isSatisfied)) {
                continue;
            }

            x += this.createNavigationButton(x, y, category);
        }

        this.initBottomLine();
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        Comparator<ConfigContainer> nameComparator = Comparator.comparing(c -> c.getConfig().getName(),
                String::compareToIgnoreCase);
        List<MagicIConfigBase> configs = this.getCurrentContainers().stream()
                .filter(ConfigContainer::isSatisfied)
                .sorted(nameComparator)
                .map(ConfigContainer::getConfig)
                .collect(Collectors.toList());
        return ConfigOptionWrapper.createFor(configs);
    }

    private Collection<ConfigContainer> getCurrentContainers() {
        return this.configManager.getContainers(this.configManager.getGuiSetting().category);
    }

    private int createNavigationButton(int x, int y, String category) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20,
                I18n.tr(String.format("%s.config.category.%s.name", this.identifier, category)));
        button.setEnabled(!this.configManager.getGuiSetting().category.equals(category));
        button.setHoverStrings(StringUtil.translateOrFallback(String.format("%s.config.category.%s.desc",
                this.identifier, category), category));
        this.addButton(button, (b, mb) -> this.tabButtonCallback(this.configManager.getGuiSetting().category, category,
                () -> this.configManager.getGuiSetting().setCategory(category)));
        return button.getWidth() + 2;
    }

    private <T> void tabButtonCallback(T currentValue, T newValue, Runnable valueSetter) {
        if (newValue != currentValue) {
            valueSetter.run();
            this.reDraw();
        }
    }

    private void initBottomLine() {
        int available = 0;
        int unavailable = 0;
        int modified = 0;

        for (ConfigContainer config : this.getCurrentContainers()) {
            if (config.isSatisfied()) {
                if (config.getConfig() instanceof IConfigResettable &&
                        ((IConfigResettable) config.getConfig()).isModified()) {
                    modified++;
                }

                available++;
            } else {
                unavailable++;
            }
        }

        int total = available + unavailable;
        String stats = I18n.tr("magiclib.config.gui.bottom_line.stat", total, available, unavailable, modified);
        int width = RenderUtil.getRenderWidth(stats);
        int height = RenderUtil.TEXT_HEIGHT;
        int x = 10;
        int y = this.height - height - GuiBase.TOP;
        this.addLabel(x, y, width, height, 0xFFAAAAAA, stats);
    }

    public void reDraw() {
        this.reCreateListWidget();
        Objects.requireNonNull(this.getListWidget()).resetScrollbarPosition();
        this.initGui();
    }

    @Getter
    @Setter
    public static class GuiSetting implements JsonSaveAble {
        private String category = Config.defaultCategory;

        @Override
        public void dumpToJson(@NotNull JsonObject jsonObject) {
            jsonObject.addProperty("category", this.category);
        }

        @Override
        public void loadFromJson(@NotNull JsonObject jsonObject) {
            this.category = jsonObject.get("category").getAsString();
        }
    }
}
