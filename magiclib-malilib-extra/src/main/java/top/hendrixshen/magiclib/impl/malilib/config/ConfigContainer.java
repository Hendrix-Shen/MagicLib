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

package top.hendrixshen.magiclib.impl.malilib.config;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.gui.GuiBase;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.annotation.Statistic;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.game.malilib.Configs;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.impl.dependency.DependencyCheckResult;
import top.hendrixshen.magiclib.impl.malilib.config.comment.MarkProcessor;
import top.hendrixshen.magiclib.impl.malilib.config.comment.TagProcessor;
import top.hendrixshen.magiclib.impl.malilib.config.statistic.ConfigStatistic;
import top.hendrixshen.magiclib.util.DependencyUtil;
import top.hendrixshen.magiclib.util.collect.InfoNode;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/config/TweakerMoreOption.java">TweakerMore<a/>
 */
public class ConfigContainer {
    private final Config configAnnotation;
    @Nullable
    private final Statistic statisticAnnotation;
    @Getter
    @Nullable
    private final MagicConfigManager configManager;
    @Getter
    private final MagicIConfigBase config;
    private final List<DependenciesContainer<ConfigContainer>> dependencies;
    @Getter
    private final ConfigStatistic statistic;
    @Nullable
    private Function<String, String> commentModifier = null;
    @Setter
    @Getter
    private boolean appendFooterFlag = true;

    public static @NotNull ConfigContainer createIsolated(Field field) {
        return ConfigContainer.create(field, null);
    }

    public static @NotNull ConfigContainer createRegulated(Field field, MagicConfigManager configManager) {
        return ConfigContainer.create(field, configManager);
    }

    @SneakyThrows
    private static @NotNull ConfigContainer create(Field field, MagicConfigManager configManager) {
        return new ConfigContainer(ConfigContainer.assertFieldValidAnnotation(field),
                (MagicIConfigBase) ConfigContainer.assertFieldObjectValid(field).get(null), configManager);
    }

    public static boolean isValidField(@NotNull Field field) {
        return ConfigContainer.isValidFieldAnnotation(field) && ConfigContainer.isValidFieldObject(field);
    }

    public static boolean isValidFieldAnnotation(@NotNull Field field) {
        return field.isAnnotationPresent(Config.class);
    }

    public static boolean isValidFieldObject(Field field) {
        try {
            return field.get(null) instanceof MagicIConfigBase;
        } catch (IllegalAccessException | NullPointerException e) {
            return false;
        }
    }

    private static Field assertFieldValid(Field field) {
        return ConfigContainer.assertFieldObjectValid(ConfigContainer.assertFieldValidAnnotation(field));
    }

    private static Field assertFieldValidAnnotation(Field field) {
        if (!ConfigContainer.isValidFieldAnnotation(field)) {
            throw new IllegalArgumentException("Field " + field + " is not annotated with @Config!");
        }

        return field;
    }

    private static Field assertFieldObjectValid(Field field) {
        if (!ConfigContainer.isValidFieldObject(field)) {
            throw new IllegalArgumentException("Field " + field + " is not a valid MagicConfig field!");
        }

        return field;
    }

    @ApiStatus.Obsolete
    public ConfigContainer(@NotNull Config configAnnotation, @NotNull Field field, MagicIConfigBase config) {
        this(field, config, null);
    }

    private ConfigContainer(@NotNull Field field, MagicIConfigBase config, @Nullable MagicConfigManager configManager) {
        assert ConfigContainer.isValidFieldAnnotation(field);

        this.configAnnotation = field.getAnnotation(Config.class);
        this.config = config;
        this.statisticAnnotation = field.getAnnotation(Statistic.class);
        this.statistic = new ConfigStatistic();
        this.dependencies = DependencyUtil.parseDependencies(field, this);
        this.configManager = configManager;
    }

    public String getCategory() {
        return this.configAnnotation.category();
    }

    public String getName() {
        return this.config.getName();
    }

    public ImmutableList<DependenciesContainer<ConfigContainer>> getDependencies() {
        return ImmutableList.copyOf(this.dependencies);
    }

    public boolean isDebugOnly() {
        return this.configAnnotation.debugOnly();
    }

    public boolean isDevOnly() {
        return this.configAnnotation.devOnly();
    }

    public boolean isSatisfied() {
        return this.dependencies.stream().anyMatch(DependenciesContainer::isSatisfied);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean shouldStatisticHotkey() {
        if (this.statisticAnnotation == null) {
            return true;
        }

        return this.statisticAnnotation.hotkey();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean shouldStatisticValueChange() {
        if (this.statisticAnnotation == null) {
            return true;
        }

        return this.statisticAnnotation.valueChanged();
    }

    public void setCommentModifier(@Nullable Function<String, String> commentModifier) {
        this.commentModifier = commentModifier;
    }

    public String modifyComment(String comment) {
        if (this.commentModifier != null) {
            comment = this.commentModifier.apply(comment);
        }

        if (this.appendFooterFlag) {
            if (!this.dependencies.stream().allMatch(DependenciesContainer::isSatisfied)) {
                InfoNode rootNode = new InfoNode(null,
                        GuiBase.TXT_GRAY + I18n.tr("magiclib.config.gui.dependencies_footer"));
                ConfigContainer.generateDependencyCheckMessage(this.dependencies, rootNode);
                comment += "\n" + rootNode.toString().replaceAll("\t", " ");
            }

            List<String> lines = this.statistic.getDisplayLines();

            if (Configs.debug.getBooleanValue()) {
                comment += "\n" + GuiBase.TXT_GRAY + I18n.tr("magiclib.config.gui.statistic.title") + GuiBase.TXT_RST
                        + "\n" + Joiner.on('\n').join(lines.stream().
                        map(line -> GuiBase.TXT_DARK_GRAY + " " + GuiBase.TXT_GRAY + line + GuiBase.TXT_RST).
                        toArray()
                );
            }
        }

        comment = TagProcessor.processReferences(this, comment);
        comment = MarkProcessor.processMarks(comment);


        return comment;
    }

    private static void generateDependencyCheckMessage(
            @NotNull List<DependenciesContainer<ConfigContainer>> dependencies, InfoNode rootNode) {
        boolean first = true;
        boolean composite = false;
        InfoNode compositeNode = new InfoNode(null, GuiBase.TXT_GRAY +
                I18n.tr("magiclib.dependency.label.composite") + GuiBase.TXT_RST);

        for (DependenciesContainer<?> dependenciesContainer : dependencies) {
            List<DependencyCheckResult> conflict = dependenciesContainer.checkConflict();
            List<DependencyCheckResult> require = dependenciesContainer.checkRequire();
            InfoNode orNode = null;

            if (first) {
                first = false;
            } else if (!conflict.isEmpty() || !require.isEmpty()) {
                if (!composite) {
                    for (InfoNode child : rootNode.getChildren()) {
                        child.moveTo(compositeNode);
                    }

                    compositeNode.moveTo(rootNode);
                    composite = true;
                }

                orNode = new InfoNode(rootNode, GuiBase.TXT_GRAY +
                        I18n.tr("magiclib.dependency.label.or") + GuiBase.TXT_RST);
            }

            if (!conflict.isEmpty()) {
                InfoNode conflictNode = new InfoNode(orNode == null ? rootNode : orNode,
                        GuiBase.TXT_GRAY + I18n.tr("magiclib.dependency.label.conflict"));

                for (DependencyCheckResult result : conflict) {
                    new InfoNode(conflictNode, (result.isSuccess() ? GuiBase.TXT_GREEN : GuiBase.TXT_RED) +
                            result.getReason());
                }
            }

            if (!require.isEmpty()) {
                InfoNode requireNode = new InfoNode(orNode == null ? rootNode : orNode,
                        GuiBase.TXT_GRAY + I18n.tr("magiclib.dependency.label.require"));

                for (DependencyCheckResult result : require) {
                    new InfoNode(requireNode, (result.isSuccess() ? GuiBase.TXT_GREEN : GuiBase.TXT_RED) +
                            result.getReason());
                }
            }
        }
    }
}
