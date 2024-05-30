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

import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.fake.i18n.ServerPlayerLanguage;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.StyleAccessor;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;

import java.util.List;

//#if MC > 11802
//$$ import net.minecraft.network.chat.contents.TranslatableContents;
//#endif

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/2733a1dfa4978374e7422376486b5c291ebb1bbc/src/main/java/carpettisaddition/translations/TranslationContext.java">Carpet-TIS-Addition</a>
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

    private static
    //#if MC > 11502
    MutableComponent
    //#else
    //$$ BaseComponent
    //#endif
    translateComponent(
            //#if MC > 11502
            MutableComponent text,
            //#else
            //$$ BaseComponent text,
            //#endif
            @NotNull String lang
    ) {
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

                    //#if MC > 11502
                    MutableComponent newText;
                    //#else
                    //$$ BaseComponent newText;
                    //#endif

                    try {
                        newText = ComponentUtil.format(msgKeyString, txtArgs).get();
                    } catch (IllegalArgumentException e) {
                        newText = ComponentUtil.simple(msgKeyString).get();
                    }

                    // Migrating text data.
                    newText.getSiblings().addAll(txt.getSiblings());
                    newText.setStyle(txt.getStyle());

                    return newText;
                });
    }

    private static @NotNull
    //#if MC > 11502
    MutableComponent
    //#else
    //$$ BaseComponent
    //#endif
    forEachTranslationComponent(
            //#if MC > 11502
            MutableComponent text,
            //#else
            //$$ BaseComponent text,
            //#endif
            @NotNull String lang,
            ComponentModifier modifier
    ) {
        if (ComponentUtil.getTextContent(text) instanceof
                //#if MC > 11802
                //$$ TranslatableContents
                //#else
                TranslatableComponent
            //#endif
        ) {
            //#if MC > 11802
            //$$ TranslatableContents translatableText = (TranslatableContents) ComponentUtil.getTextContent(text);
            //#else
            TranslatableComponent translatableText = (TranslatableComponent) ComponentUtil.getTextContent(text);
            //#endif

            // Translate arguments
            Object[] args = translatableText.getArgs();

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];

                if (arg instanceof
                        //#if MC > 11502
                        MutableComponent
                    //#else
                    //$$ BaseComponent
                    //#endif
                ) {
                    //#if MC > 11502
                    MutableComponent newText = MagicTranslation.forEachTranslationComponent((MutableComponent) arg,
                            lang, modifier);
                    //#else
                    //$$ BaseComponent newText = MagicTranslation.forEachTranslationComponent((BaseComponent) arg,
                    //$$         lang, modifier);
                    //#endif

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
        HoverEvent hoverEvent = ((StyleAccessor) text.getStyle()).getHoverEvent();

        if (hoverEvent != null) {
            //#if MC > 11502
            Object hoverText = hoverEvent.getValue(hoverEvent.getAction());

            if (hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT && hoverText instanceof
                    //#if MC > 11502
                    MutableComponent
                //#else
                //$$ BaseComponent
                //#endif
            ) {
                //#if MC > 11502
                MutableComponent newText = MagicTranslation.forEachTranslationComponent((MutableComponent) hoverText,
                        lang, modifier);
                //#else
                //$$ BaseComponent newText = MagicTranslation.forEachTranslationComponent((BaseComponent) hoverText,
                //$$         lang, modifier);
                //$$
                //#endif

                if (newText != hoverText) {
                    text.setStyle(text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, newText)));
                }
            }
            //#else
            //$$ Component hoverText = hoverEvent.getValue();
            //$$ BaseComponent newText = MagicTranslation.forEachTranslationComponent((BaseComponent) hoverText,
            //$$         lang, modifier);
            //$$
            //$$ if (newText != hoverText) {
            //$$     text.getStyle().setHoverEvent(new HoverEvent(hoverEvent.getAction(), newText));
            //$$ }
            //#endif
        }

        // Translate sibling texts.
        List<Component> siblings = text.getSiblings();

        for (int i = 0; i < siblings.size(); i++) {
            Component sibling = siblings.get(i);
            //#if MC > 11502
            MutableComponent newText = MagicTranslation.forEachTranslationComponent((MutableComponent) sibling,
                    lang, modifier);
            //#else
            //$$ BaseComponent newText = MagicTranslation.forEachTranslationComponent((BaseComponent) sibling,
            //$$         lang, modifier);
            //#endif

            if (newText != sibling) {
                siblings.set(i, newText);
            }
        }

        return text;
    }

    @FunctionalInterface
    private interface ComponentModifier {
        //#if MC > 11502
        MutableComponent
        //#else
        //$$ BaseComponent
        //#endif

        apply(
                //#if MC > 11802
                //$$ MutableComponent translatableText,
                //#else
                TranslatableComponent translatableText,
                //#endif
                @Nullable String msgKeyString
        );
    }
}
