/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * TweakerMore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TweakerMore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TweakerMore.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.impl.malilib.config.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigResettable;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import fi.dy.masa.malilib.interfaces.IStringValue;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.mixin.malilib.accessor.WidgetSearchBarAccessor;
import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;
import top.hendrixshen.magiclib.util.serializable.JsonSaveAble;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/gui/TweakerMoreConfigGui.java">TweakerMore<a/>
 */
public class MagicConfigGui extends GuiConfigsBase {
    private final String identifier;
    private final MagicConfigManager configManager;
    private final Supplier<String> titleProvider;
    private final List<WidgetBase> hoveringWidgets = Lists.newArrayList();
    @Setter
    private WidgetSearchBar searchBar = null;

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
        this.hoveringWidgets.clear();
        this.setTitle(this.titleProvider.get());
        int x = 10;
        int y = 26;

        for (String category : this.configManager.getCategories()) {
            if (this.configManager.getContainers(category).stream()
                    .filter(this::filterUnsatisfiedConfig)
                    .noneMatch(this::isValidConfig)) {
                continue;
            }

            x += this.createNavigationButton(x, y, category);
        }

        x = this.width - 11;
        x = this.initSortingStrategyDropDownList(x) - 5;

        if (this.searchBar != null) {
            GuiTextFieldGeneric searchBox = ((WidgetSearchBarAccessor) this.searchBar).magiclib$getSearchBox();
            int deltaWidth = Math.max(50, x - this.searchBar.getX()) - this.searchBar.getWidth();
            this.searchBar.setWidth(this.searchBar.getWidth() + deltaWidth);
            searchBox.setWidth(searchBox.getWidth() + deltaWidth);
        }

        this.initBottomLine();
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        Comparator<ConfigContainer> nameComparator = Comparator.comparing(c -> c.getConfig().getName(),
                String::compareToIgnoreCase);
        List<MagicIConfigBase> configs = this.getCurrentContainers().stream()
                .filter(this::filterUnsatisfiedConfig)
                .filter(this::isValidConfig)
                .sorted(this.configManager.getGuiSetting().getSortingStrategy()
                        .getComparator().thenComparing(nameComparator))
                .map(ConfigContainer::getConfig)
                .collect(Collectors.toList());
        return ConfigOptionWrapper.createFor(configs);
    }

    public boolean hideUnAvailableConfigs() {
        return false;
    }

    public boolean isDebug() {
        return false;
    }

    public boolean isDevOnly() {
        return this.isDebug() && MagicLib.getInstance().getCurrentPlatform().isDevelopmentEnvironment();
    }

    // Dev & debug check.
    @SuppressWarnings("RedundantIfStatement")
    private boolean isValidConfig(@NotNull ConfigContainer configContainer) {
        // Hide debug configs unless debug mode on.
        if (configContainer.isDebugOnly() && !this.isDebug()) {
            return false;
        } else if (configContainer.isDevOnly() && !this.isDevOnly()) {
            // Hide dev only configs unless debug mode on and is dev env.
            return false;
        }

        return true;
    }

    private boolean filterUnsatisfiedConfig(ConfigContainer configContainer) {
        return !this.hideUnAvailableConfigs() || configContainer.isSatisfied();
    }

    private Collection<ConfigContainer> getCurrentContainers() {
        return this.configManager.getContainers(this.configManager.getGuiSetting().category);
    }

    private <T extends IStringValue> int initDropDownList(int x, @NotNull List<T> entries, T defaultValue,
                                                          Supplier<T> valueGetter, Consumer<T> valueSetter,
                                                          String hoverTextKey,
                                                          @NotNull Consumer<SelectorDropDownList<T>> postProcessor) {
        int y = this.getListY() + 3;
        int height = 16;
        int maxTextWidth = entries.stream().
                filter(Objects::nonNull).
                mapToInt(e -> this.getStringWidth(e.getStringValue())).
                max().orElse(-1);
        // constant 20 reference: fi.dy.masa.malilib.gui.widgets.WidgetDropDownList.getRequiredWidth
        int width = Math.max(maxTextWidth, 40) + 20;
        SelectorDropDownList<T> dd = new SelectorDropDownList<>(x - width, y, width, height,
                200, entries.size(), entries);
        dd.setEntryChangeListener(entry -> this.setDisplayParameter(valueGetter.get(), entry,
                () -> valueSetter.accept(entry), true));
        dd.setSelectedEntry(defaultValue);
        dd.setHoverText(hoverTextKey);
        postProcessor.accept(dd);
        this.addWidget(dd);
        this.hoveringWidgets.add(dd);
        return dd.getX();
    }

    private int createNavigationButton(int x, int y, String category) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20,
                I18n.tr(String.format("%s.config.category.%s.name", this.identifier, category)));
        button.setEnabled(!this.configManager.getGuiSetting().category.equals(category));
        String translatedHoverText = I18n.translateOrFallback(String.format("%s.config.category.%s.desc",
                this.identifier, category), null);

        if (translatedHoverText != null) {
            button.setHoverStrings(translatedHoverText);
        }

        this.addButton(button, (b, mb) -> this.setDisplayParameter(this.configManager.getGuiSetting().category, category,
                () -> this.configManager.getGuiSetting().setCategory(category), false));
        return button.getWidth() + 2;
    }

    private <T> void setDisplayParameter(T currentValue, T newValue, Runnable valueSetter, boolean keepSearchBar) {
        if (newValue != currentValue) {
            valueSetter.run();
            this.reDraw(keepSearchBar);
        }
    }

    private void initBottomLine() {
        int available = 0;
        int unavailable = 0;
        int modified = 0;

        for (ConfigContainer config : this.getCurrentContainers()) {
            if (this.isValidConfig(config) && config.isSatisfied()) {
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

    private int initSortingStrategyDropDownList(int x) {
        List<SortingStrategy> items = Arrays.asList(SortingStrategy.values());
        return this.initDropDownList(
                x, items, this.configManager.getGuiSetting().getSortingStrategy(),
                () -> this.configManager.getGuiSetting().getSortingStrategy(),
                strategy -> this.configManager.getGuiSetting().setSortingStrategy(strategy),
                "magiclib.config.gui.sorting_strategy.label_text",
                dd -> {
                }
        );
    }

    public void reDraw() {
        this.reDraw(true);
    }

    public void reDraw(boolean keepSearchBar) {
        // Storing search bar data.
        String previousSearchBarText = null;
        boolean previousSearchBoxFocus = false;

        if (keepSearchBar && this.searchBar != null && this.searchBar.isSearchOpen()) {
            previousSearchBarText = this.searchBar.getFilter();
            previousSearchBoxFocus = ((WidgetSearchBarAccessor) this.searchBar).magiclib$getSearchBox().isFocused();
        }

        super.removed();
        this.reCreateListWidget();

        // Restoring search bar data.
        if (this.searchBar != null && previousSearchBarText != null) {
            this.searchBar.setSearchOpen(true);
            ((WidgetSearchBarAccessor) this.searchBar).magiclib$getSearchBox().setValue(previousSearchBarText);
            ((WidgetSearchBarAccessor) this.searchBar).magiclib$getSearchBox().setFocused(previousSearchBoxFocus);
        }

        Objects.requireNonNull(this.getListWidget()).resetScrollbarPosition();
        this.initGui();
    }

    public void renderDropDownList(
            //#if MC > 11904
            //$$ GuiGraphics poseStackOrGuiGraphics,
            //#elseif MC > 11502
            PoseStack poseStackOrGuiGraphics,
            //#endif
            int mouseX, int mouseY
    ) {
        this.hoveringWidgets.forEach(widget -> widget.render(
                mouseX, mouseY, widget.isMouseOver(mouseX, mouseY)
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
        ));
    }

    @Getter
    @Setter
    public static class GuiSetting implements JsonSaveAble {
        private String category = Config.defaultCategory;
        private SortingStrategy sortingStrategy = SortingStrategy.ALPHABET;

        @Override
        public void dumpToJson(@NotNull JsonObject jsonObject) {
            jsonObject.addProperty("category", this.category);
            jsonObject.addProperty("sortingStrategy", this.sortingStrategy.name());
        }

        @Override
        public void loadFromJson(@NotNull JsonObject jsonObject) {
            this.category = jsonObject.get("category").getAsString();
            this.sortingStrategy = this.getEnumSafe(jsonObject, "sortingStrategy", this.sortingStrategy);
        }
    }

    @Getter
    public enum SortingStrategy implements IStringValue {
        ALPHABET((a, b) -> 0),
        MOST_RECENTLY_USED(Collections.reverseOrder(Comparator.comparingLong(c -> c.getStatistic().lastUsedTime))),
        MOST_COMMONLY_USED(Collections.reverseOrder(Comparator.comparingLong(c -> c.getStatistic().useAmount)));

        private final Comparator<ConfigContainer> comparator;

        SortingStrategy(Comparator<ConfigContainer> comparator) {
            this.comparator = comparator;
        }

        @Override
        public String getStringValue() {
            return I18n.tr("magiclib.config.gui.sorting_strategy." + this.name().toLowerCase());
        }
    }
}
