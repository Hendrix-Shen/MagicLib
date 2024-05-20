package top.hendrixshen.magiclib.util.minecraft.render;

import net.minecraft.client.Minecraft;

//#if MC > 11502
import com.google.common.collect.Lists;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.StringDecomposer;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.StringSplitterAccessor;

import java.util.List;
//#endif

public class TextRenderUtil {
    //#if MC > 11502
    public static @NotNull FormattedCharSequence string2formattedCharSequence(String string) {
        return visitor -> StringDecomposer.iterateFormatted(string, Style.EMPTY, visitor);
    }

    public static @NotNull String formattedCharSequence2string(@NotNull FormattedCharSequence text) {
        StringBuilder builder = new StringBuilder();
        text.accept((index, style, codePoint) -> {
            builder.append((char) codePoint);
            return true;
        });
        return builder.toString();
    }

    public static FormattedCharSequence trim(@NotNull FormattedCharSequence text, int maxWidth,
                                             PostTrimModifier<FormattedCharSequence> postTrimModifier) {
        Font font = Minecraft.getInstance().font;
        StringSplitter.WidthProvider widthRetriever = ((StringSplitterAccessor) font.getSplitter())
                .magiclib$widthProvider();
        List<Triple<Integer, Style, Integer>> elements = Lists.newArrayList();
        MutableFloat width = new MutableFloat(0);
        boolean hasTrimmed = text.accept((index, style, codePoint) -> {
            width.add(widthRetriever.getWidth(codePoint, style));
            boolean ok = width.getValue() <= maxWidth;

            if (ok) {
                elements.add(Triple.of(index, style, codePoint));
            }

            return ok;
        });

        FormattedCharSequence trimmedText = formattedCharSink -> {
            for (Triple<Integer, Style, Integer> element : elements) {
                if (!formattedCharSink.accept(element.getLeft(), element.getMiddle(), element.getRight())) {
                    return false;
                }
            }

            return true;
        };

        if (hasTrimmed) {
            trimmedText = postTrimModifier.modify(trimmedText);
        }

        return trimmedText;
    }

    public static FormattedCharSequence trim(FormattedCharSequence text, int maxWidth) {
        return TextRenderUtil.trim(text, maxWidth, t -> t);
    }
    //#endif

    public static String trim(String text, int maxWidth, PostTrimModifier<String> postTrimModifier) {
        Minecraft mc = Minecraft.getInstance();
        //#if MC > 11502
        String trimmedText = mc.font.plainSubstrByWidth(text, maxWidth);
        //#else
        //$$ String trimmedText = mc.font.substrByWidth(text, maxWidth, false);
        //#endif

        if (trimmedText.length() < text.length()) {
            trimmedText = postTrimModifier.modify(trimmedText);
        }

        return trimmedText;
    }

    public static String trim(String text, int maxWidth) {
        return TextRenderUtil.trim(text, maxWidth, t -> t);
    }

    @FunctionalInterface
    public interface PostTrimModifier<T> {
        T modify(T trimmedText);
    }
}
