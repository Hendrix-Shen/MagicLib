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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.game.malilib.Configs;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.impl.dependency.DependencyCheckResult;
import top.hendrixshen.magiclib.impl.malilib.config.statistic.ConfigStatistic;
import top.hendrixshen.magiclib.util.collect.InfoNode;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/config/TweakerMoreOption.java">TweakerMore<a/>
 */
public class ConfigContainer {
    private final Config configAnnotation;
    @Getter
    private final MagicIConfigBase config;
    private final List<DependenciesContainer<ConfigContainer>> dependencies;
    @Getter
    private final ConfigStatistic statistic;
    @Nullable
    protected Function<String, String> commentModifier = null;
    @Setter
    @Getter
    private boolean appendFooterFlag = true;

    public ConfigContainer(@NotNull Config configAnnotation, MagicIConfigBase config) {
        this.configAnnotation = configAnnotation;
        this.config = config;
        this.dependencies = Arrays.stream(this.configAnnotation.compositeDependencies().value())
                .map(deps -> DependenciesContainer.of(deps, this))
                .collect(Collectors.toList());
        this.statistic = new ConfigStatistic();
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
        return this.dependencies.stream().allMatch(DependenciesContainer::isSatisfied);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean shouldStatisticHotkey() {
        return this.configAnnotation.statistic().hotkey();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean shouldStatisticValueChange() {
        return this.configAnnotation.statistic().valueChanged();
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

        return comment;
    }

    private static void generateDependencyCheckMessage(
            @NotNull List<DependenciesContainer<ConfigContainer>> dependencies, InfoNode rootNode) {
        boolean first = true;
        boolean composite = false;
        InfoNode compositeNode = new InfoNode(null, GuiBase.TXT_GRAY +
                I18n.tr("magiclib.dependency.label.composite") + GuiBase.TXT_RST);

        for (DependenciesContainer<?> dependenciesContainer : dependencies) {
            boolean conflictSatisfied = dependenciesContainer.isConflictSatisfied();
            boolean requireSatisfied = dependenciesContainer.isRequireSatisfied();
            InfoNode orNode = null;

            if (first) {
                first = false;
            } else if (!conflictSatisfied || !requireSatisfied) {
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

            if (!conflictSatisfied) {
                InfoNode conflictNode = new InfoNode(orNode == null ? rootNode : orNode,
                        GuiBase.TXT_GRAY + I18n.tr("magiclib.dependency.label.conflict"));

                for (DependencyCheckResult result : dependenciesContainer.checkConflict()) {
                    new InfoNode(conflictNode, GuiBase.TXT_RED + result.getReason());
                }
            }

            if (!requireSatisfied) {
                InfoNode requireNode = new InfoNode(orNode == null ? rootNode : orNode,
                        GuiBase.TXT_GRAY + I18n.tr("magiclib.dependency.label.require"));

                for (DependencyCheckResult result : dependenciesContainer.checkRequire()) {
                    new InfoNode(requireNode, GuiBase.TXT_RED + result.getReason());
                }
            }
        }
    }
}
