package top.hendrixshen.magiclib.impl.i18n.minecraft.translation;

import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.fake.i18n.ServerPlayerLanguage;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.StyleAccessor;
import top.hendrixshen.magiclib.util.MiscUtil;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;

import java.util.List;

//#if MC > 11802
//$$ import net.minecraft.network.chat.contents.TranslatableContents;
//#endif

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/">Carpet-TIS-Addition</a>
 */
public class MagicTranslation {
    /**
     * key -> translated formatting string
     */
    @Nullable
    public static String getTranslationString(@NotNull String lang, String key) {
        return I18n.trByCode(lang.toLowerCase(), key);
    }

    public static @NotNull MutableComponentCompat translate(MutableComponentCompat text, String lang) {
        return MagicTranslation.translateComponent(text, lang);
    }

    public static @NotNull MutableComponentCompat translate(MutableComponentCompat text) {
        return MagicTranslation.translate(text, I18n.getCurrentLanguageCode());
    }

    public static @NotNull MutableComponentCompat translate(MutableComponentCompat text, ServerPlayer player) {
        return MagicTranslation.translate(text, ((ServerPlayerLanguage) player).magicLib$getLanguage());
    }

    private static @NotNull MutableComponentCompat translateComponent(@NotNull MutableComponentCompat text,
                                                                      @NotNull String lang) {
        return MutableComponentCompat.of(MagicTranslation.translateComponent(
                //#if MC > 11502
                (MutableComponent) text.get(),
                //#else
                //$$ (BaseComponent) text.get(),
                //#endif
                lang
        ));
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
        // quick scan to check if any required translation exists
        boolean[] translationRequired = new boolean[]{false};

        MagicTranslation.forEachTranslationComponent(text, lang, (txt, msgKeyString) -> {
            translationRequired[0] = true;
            return txt;
        });
        if (!translationRequired[0]) {
            return text;
        }

        // Make a copy of the text, and apply translation
        return MagicTranslation.forEachTranslationComponent(MiscUtil.cast(ComponentUtil.copy(text).get()), lang,
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
                        newText = MiscUtil.cast(ComponentUtil.format(msgKeyString, txtArgs).get());
                    } catch (IllegalArgumentException e) {
                        newText = MiscUtil.cast(ComponentUtil.format(msgKeyString, txtArgs).get());
                    }

                    // Migrating text data
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
            String msgKeyString = MagicTranslation.getTranslationString(lang, translatableText.getKey());

            if (msgKeyString == null && !lang.equals(I18n.DEFAULT_CODE)) {
                msgKeyString = MagicTranslation.getTranslationString(I18n.DEFAULT_CODE, translatableText.getKey());
            }

            text = modifier.apply(
                    //#if MC > 11802
                    //$$ text,
                    //#else
                    translatableText,
                    //#endif
                    msgKeyString
            );
        }

        // translate hover text
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

        // Translate sibling texts
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
