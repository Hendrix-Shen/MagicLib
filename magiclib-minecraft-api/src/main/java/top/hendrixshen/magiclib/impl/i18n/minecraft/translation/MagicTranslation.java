/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.impl.i18n.minecraft.translation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11802
//$$ import net.minecraft.network.chat.MutableComponent;
//$$ import net.minecraft.network.chat.contents.TranslatableContents;
//#else
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.BaseComponent;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.fake.i18n.ServerPlayerLanguage;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.StyleAccessor;
import top.hendrixshen.magiclib.util.CommonUtil;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;

import java.util.List;

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/be45c2589ad8e6cd9063dca71920ad8a873fc050/src/main/java/carpettisaddition/translations/TISAdditionTranslations.java">Carpet-TIS-Addition</a>.
 */
public class MagicTranslation {
    public static @NotNull MutableComponentCompat translate(MutableComponentCompat text) {
        return MagicTranslation.translate(text, I18n.getCurrentLanguageCode());
    }

    public static @NotNull MutableComponentCompat translate(MutableComponentCompat text, String lang) {
        return MagicTranslation.translateComponent(text, lang);
    }

    public static @NotNull MutableComponentCompat translate(MutableComponentCompat text, ServerPlayer player) {
        return MagicTranslation.translate(text, ((ServerPlayerLanguage) player).magicLib$getLanguage());
    }

    private static @NotNull MutableComponentCompat translateComponent(@NotNull MutableComponentCompat text,
                                                                      @NotNull String lang) {
        return MutableComponentCompat.of(MagicTranslation.translateComponent(text.get(), lang));
    }

    private static BaseComponent translateComponent(BaseComponent text, @NotNull String lang) {
        // Quick scan to check if any required translation exists.
        boolean[] translationRequired = new boolean[]{false};

        MagicTranslation.forEachTranslationComponent(text, lang, (txt, msgKeyString) -> {
            translationRequired[0] = true;
            return txt;
        });

        if (!translationRequired[0]) {
            return text;
        }

        // Make a copy of the text, and apply translation.
        return MagicTranslation.forEachTranslationComponent(ComponentUtil.copy(text), lang,
                (txt, msgKeyString) -> {
                    //#if MC > 11802
                    //$$ TranslatableContents content = (TranslatableContents) txt.getContents();
                    //$$ String txtKey = content.getKey();
                    //$$ Object[] txtArgs = content.getArgs();
                    //#else
                    String txtKey = txt.getKey();
                    Object[] txtArgs = txt.getArgs();
                    //#endif

                    if (msgKeyString == null) {
                        MagicLib.getLogger().warn("MagicTranslation: Unknown translation key {}", txtKey);
                        return txt;
                    }

                    BaseComponent newText;

                    try {
                        newText = ComponentUtil.format(msgKeyString, txtArgs);
                    } catch (IllegalArgumentException e) {
                        newText = ComponentUtil.simple(msgKeyString);
                    }

                    // Migrating text data.
                    newText.getSiblings().addAll(txt.getSiblings());
                    newText.setStyle(txt.getStyle());

                    return newText;
                });
    }

    private static @NotNull BaseComponent forEachTranslationComponent(
            BaseComponent text, @NotNull String lang, ComponentModifier modifier) {
        // CHECKSTYLE.OFF: OperatorWrap
        // @formatter:off
        if (ComponentUtil.getTextContent(text) instanceof
                //#if MC > 11802
                //$$ TranslatableContents
                //#else
                TranslatableComponent
                //#endif
        ) {
            // @formatter:on
            // CHECKSTYLE.ON: OperatorWrap
            //#if MC > 11802
            //$$ TranslatableContents translatableText = (TranslatableContents) ComponentUtil.getTextContent(text);
            //#else
            TranslatableComponent translatableText = (TranslatableComponent) ComponentUtil.getTextContent(text);
            //#endif

            // Translate arguments
            Object[] args = translatableText.getArgs();

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];

                if (arg instanceof BaseComponent) {
                    BaseComponent newText = MagicTranslation.forEachTranslationComponent((BaseComponent) arg,
                            lang, modifier);

                    if (newText != arg) {
                        args[i] = newText;
                    }
                }
            }

            // Do translation logic.
            if (HookTranslationManager.getInstance().isNamespaceRegistered(translatableText.getKey())) {
                String msgKeyString = I18n.trByCode(lang, translatableText.getKey());

                text = modifier.apply(
                        //#if MC > 11802
                        //$$ text,
                        //#else
                        translatableText,
                        //#endif
                        msgKeyString
                );
            }
        }

        // Translate hover text.
        // In mc1.21.9+, Style is a final class, so we need to cast it to Object first.
        @SuppressWarnings("RedundantCast")
        HoverEvent hoverEvent = ((StyleAccessor) (Object) text.getStyle()).getHoverEvent();

        if (hoverEvent != null) {
            BaseComponent oldHoverText = CommonUtil.make(() -> {
                //#if MC > 12104
                //$$ if (hoverEvent instanceof HoverEvent.ShowText(Component hoverEventText) && hoverEventText instanceof MutableComponent) {
                //$$     return (MutableComponent) hoverEventText;
                //$$ }
                //#elseif MC > 11502
                Object hoverEventText = hoverEvent.getValue(hoverEvent.getAction());

                if (hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT && hoverEventText instanceof BaseComponent) {
                    return (BaseComponent) hoverEventText;
                }
                //#else
                //$$ Component hoverEventText = hoverEvent.getValue();
                //$$
                //$$ if (hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT && hoverEventText instanceof BaseComponent) {
                //$$     return (BaseComponent) hoverEventText;
                //$$ }
                //#endif

                return null;
            });

            if (oldHoverText != null) {
                BaseComponent newHoverText = MagicTranslation.forEachTranslationComponent(oldHoverText, lang, modifier);

                if (newHoverText != oldHoverText) {
                    ComponentUtil.hover(text, newHoverText);
                }
            }
        }

        // Translate sibling texts.
        List<Component> siblings = text.getSiblings();

        for (int i = 0; i < siblings.size(); i++) {
            Component sibling = siblings.get(i);
            BaseComponent newText = MagicTranslation.forEachTranslationComponent((BaseComponent) sibling,
                    lang, modifier);

            if (newText != sibling) {
                siblings.set(i, newText);
            }
        }

        return text;
    }

    @FunctionalInterface
    private interface ComponentModifier {
        BaseComponent apply(
                //#if MC > 11802
                //$$ MutableComponent translatableText,
                //#else
                TranslatableComponent translatableText,
                //#endif
                @Nullable String msgKeyString
        );
    }
}
