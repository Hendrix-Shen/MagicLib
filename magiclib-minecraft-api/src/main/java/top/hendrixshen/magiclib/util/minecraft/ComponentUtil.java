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

package top.hendrixshen.magiclib.util.minecraft;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.*;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.dimension.DimensionWrapper;
import top.hendrixshen.magiclib.impl.i18n.minecraft.translation.Translator;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.TranslatableComponentAccessor;
import top.hendrixshen.magiclib.util.MiscUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

//#if MC > 12005
//$$ import net.minecraft.core.Holder;
//#endif

//#if MC > 11802
//$$ import net.minecraft.network.chat.contents.TranslatableContents;
//$$ import net.minecraft.network.chat.contents.TranslatableFormatException;
//#endif

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/2733a1dfa4978374e7422376486b5c291ebb1bbc/src/main/java/carpettisaddition/utils/IdentifierUtil.java">Carpet-TIS-Addition</a>
 */
public class ComponentUtil {
    private static final Translator translator = new Translator("magiclib.util.minecraft");

    /*
     * ----------------------------
     *    Text Factories - Utils
     * ----------------------------
     */

    public static
    //#if MC > 11802
    //$$ ComponentContents
    //#elseif MC > 11502
    MutableComponent
    //#else
    //$$ BaseComponent
    //#endif
    getTextContent(
            //#if MC > 11502
            @NotNull MutableComponent text
            //#else
            //$$ @NotNull BaseComponent text
            //#endif
    ) {
        //#if MC > 11802
        //$$ return text.getContents();
        //#else
        return text;
        //#endif
    }

    /*
     * ----------------------------
     *    Component Factories - Utils
     * ----------------------------
     */

    private static final ImmutableMap<DyeColor, Consumer<MutableComponentCompat>> DYE_COLOR_APPLIER = Util.make(() -> {
        Map<DyeColor, ChatFormatting> map = Maps.newHashMap();
        map.put(DyeColor.WHITE, ChatFormatting.WHITE);
        map.put(DyeColor.LIGHT_GRAY, ChatFormatting.GRAY);
        map.put(DyeColor.GRAY, ChatFormatting.DARK_GRAY);
        map.put(DyeColor.BLACK, ChatFormatting.BLACK);
        map.put(DyeColor.RED, ChatFormatting.RED);
        map.put(DyeColor.YELLOW, ChatFormatting.YELLOW);
        map.put(DyeColor.LIME, ChatFormatting.GREEN);
        map.put(DyeColor.GREEN, ChatFormatting.DARK_GREEN);
        map.put(DyeColor.CYAN, ChatFormatting.DARK_AQUA);
        map.put(DyeColor.LIGHT_BLUE, ChatFormatting.AQUA);
        map.put(DyeColor.BLUE, ChatFormatting.DARK_BLUE);
        map.put(DyeColor.PURPLE, ChatFormatting.DARK_PURPLE);
        map.put(DyeColor.MAGENTA, ChatFormatting.LIGHT_PURPLE);

        //#if MC < 11600
        //$$ map.put(DyeColor.BROWN, ChatFormatting.DARK_RED);
        //$$ map.put(DyeColor.PINK, ChatFormatting.RED);
        //$$ map.put(DyeColor.ORANGE, ChatFormatting.GOLD);
        //#endif

        ImmutableMap.Builder<DyeColor, Consumer<MutableComponentCompat>> builder = new ImmutableMap.Builder<>();
        map.forEach((dyeColor, fmt) -> builder.put(dyeColor, text -> ComponentUtil.formatting(text, fmt)));
        //#if MC > 11502
        Arrays.stream(DyeColor.values())
                .filter(dyeColor -> !map.containsKey(dyeColor))
                .forEach(dyeColor -> builder.put(dyeColor, text ->
                        text.setStyle(text.getStyle().withColor(TextColor.fromRgb(dyeColor.getTextColor())))));
        //#endif
        return builder.build();
    });

    public static @NotNull MutableComponentCompat compose(Object @NotNull ... objects) {
        MutableComponentCompat literal = ComponentCompat.literal("");

        for (Object o : objects) {
            if (o instanceof MutableComponentCompat) {
                literal.append((MutableComponentCompat) o);
            } else if (o instanceof
                    //#if MC > 11502
                    MutableComponent
                //#else
                //$$ BaseComponent
                //#endif
            ) {
                literal.append(MutableComponentCompat.of(MiscUtil.cast(o)));
            } else {
                literal.append(o.toString());
            }
        }

        return literal;
    }

    // Simple Component
    public static @NotNull MutableComponentCompat simple(@NotNull Object text) {
        return ComponentCompat.literal(text.toString());
    }

    // Simple Component with formatting
    public static @NotNull MutableComponentCompat simple(Object text, ChatFormatting... chatFormattings) {
        return ComponentUtil.formatting(ComponentUtil.simple(text), chatFormattings);
    }

    public static @NotNull MutableComponentCompat newLine() {
        return ComponentUtil.simple("\n");
    }

    public static MutableComponentCompat colored(MutableComponentCompat text, DyeColor value) {
        Consumer<MutableComponentCompat> consumer = ComponentUtil.DYE_COLOR_APPLIER.get(value);

        if (consumer != null) {
            consumer.accept(text);
        }

        return text;
    }

    public static MutableComponentCompat colored(MutableComponentCompat text, Object value) {
        ChatFormatting color = null;

        if (Boolean.TRUE.equals(value)) {
            color = ChatFormatting.GREEN;
        } else if (Boolean.FALSE.equals(value)) {
            color = ChatFormatting.RED;
        }

        if (value instanceof Number) {
            color = ChatFormatting.GOLD;
        }

        if (color != null) {
            ComponentUtil.formatting(text, color);
        }

        return text;
    }

    public static MutableComponentCompat colored(Object value) {
        return ComponentUtil.colored(ComponentUtil.simple(value), value);
    }

    public static MutableComponentCompat property(Property<?> property, Object value) {
        return ComponentUtil.colored(ComponentUtil.simple(TextUtil.property(property, value)), value);
    }

    public static @NotNull MutableComponentCompat tr(String key, Object... args) {
        return ComponentCompat.translatable(key, args);
    }

    public static @NotNull MutableComponentCompat fancy(MutableComponentCompat displayText,
                                                        MutableComponentCompat hoverText,
                                                        ClickEventCompat clickEvent) {
        MutableComponentCompat text = ComponentUtil.copy(displayText);

        if (hoverText != null) {
            ComponentUtil.hover(text, hoverText);
        }

        if (clickEvent != null) {
            ComponentUtil.click(text, clickEvent);
        }

        return text;
    }

    public static @NotNull MutableComponentCompat join(MutableComponentCompat joiner,
                                                       @NotNull Iterable<MutableComponentCompat> items) {
        MutableComponentCompat text = ComponentUtil.simple("");
        boolean first = true;

        for (MutableComponentCompat item : items) {
            if (!first) {
                text.append(joiner);
            }

            first = false;
            text.append(item);
        }

        return text;
    }

    public static @NotNull MutableComponentCompat join(MutableComponentCompat joiner, MutableComponentCompat... items) {
        return ComponentUtil.join(joiner, Arrays.asList(items));
    }

    public static @NotNull MutableComponentCompat format(String formatter, Object... args) {
        TranslatableComponentAccessor dummy = (TranslatableComponentAccessor) (
                ComponentUtil.tr(formatter, args).get()
                //#if MC > 11802
                //$$ .getContents()
                //#endif
        );

        try {
            //#if MC > 11701
            //$$ List<FormattedText> segments = Lists.newArrayList();
            //$$ dummy.magiclib$invokeDecomposeTemplate(formatter, segments::add);
            //#else
            dummy.magiclib$getDecomposedParts().clear();
            dummy.magiclib$invokeDecomposeTemplate(formatter);
            //#endif

            return ComponentUtil.compose(
                    //#if MC > 11502
                    //#if MC > 11701
                    //$$ segments.
                    //#else
                    dummy.magiclib$getDecomposedParts().
                            //#endif
                                    stream().map(formattedText -> {
                        if (formattedText instanceof
                                //#if MC > 11802
                                //$$ Component
                                //#else
                                MutableComponent
                            //#endif
                        ) {
                            //#if MC > 11802
                            //$$ return (Component) formattedText;
                            //#else
                            return formattedText;
                            //#endif

                        }

                        return ComponentUtil.simple(formattedText.getString());
                    }).toArray()
                    //#else
                    //$$ dummy.magiclib$getDecomposedParts().toArray(new Object[0])
                    //#endif
            );
        } catch (TranslatableFormatException e) {
            throw new IllegalArgumentException(formatter);
        }
    }

    /*
     * -------------------------------
     *    Component Factories - Advanced
     * -------------------------------
     */

    public static @NotNull MutableComponentCompat bool(boolean bool) {
        return ComponentUtil.simple(String.valueOf(bool), bool ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    private static MutableComponentCompat getTeleportHint(MutableComponentCompat dest) {
        return ComponentUtil.translator.tr("teleport_hint.hover", dest);
    }

    private static @NotNull MutableComponentCompat coordinate(@Nullable DimensionWrapper dim, String posStr, String command) {
        MutableComponentCompat hoverText = ComponentUtil.simple("");
        hoverText.append(ComponentUtil.getTeleportHint(ComponentUtil.simple(posStr)));

        if (dim != null) {
            hoverText.append("\n");
            hoverText.append(ComponentUtil.translator.tr("teleport_hint.dimension"));
            hoverText.append(": ");
            hoverText.append(ComponentUtil.dimension(dim));
        }

        return ComponentUtil.fancy(ComponentUtil.simple(posStr), hoverText, ClickEventCompat.of(ClickEvent.Action.SUGGEST_COMMAND, command));
    }

    public static @NotNull MutableComponentCompat coordinate(Vec3 pos, DimensionWrapper dim) {
        return ComponentUtil.coordinate(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull MutableComponentCompat coordinate(Vec3i pos, DimensionWrapper dim) {
        return ComponentUtil.coordinate(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull MutableComponentCompat coordinate(ChunkPos pos, DimensionWrapper dim) {
        return ComponentUtil.coordinate(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull MutableComponentCompat coordinate(Vec3 pos) {
        return ComponentUtil.coordinate(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    public static @NotNull MutableComponentCompat coordinate(Vec3i pos) {
        return ComponentUtil.coordinate(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    public static @NotNull MutableComponentCompat coordinate(ChunkPos pos) {
        return ComponentUtil.coordinate(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    private static @NotNull MutableComponentCompat vector(String displayText, String detailedText) {
        return ComponentUtil.fancy(ComponentUtil.simple(displayText),
                ComponentUtil.simple(detailedText), ClickEventCompat.of(ClickEvent.Action.SUGGEST_COMMAND, detailedText));
    }

    public static @NotNull MutableComponentCompat vector(Vec3 vec) {
        return ComponentUtil.vector(TextUtil.vector(vec), TextUtil.vector(vec, 6));
    }

    public static @NotNull MutableComponentCompat entityType(@NotNull EntityType<?> entityType) {
        return MutableComponentCompat.of(MiscUtil.cast(entityType.getDescription()));
    }

    public static @NotNull MutableComponentCompat entityType(@NotNull Entity entity) {
        return ComponentUtil.entityType(entity.getType());
    }

    public static @NotNull MutableComponentCompat entity(Entity entity) {
        MutableComponentCompat entityBaseName = ComponentUtil.entityType(entity);
        MutableComponentCompat entityDisplayName = MutableComponentCompat.of(MiscUtil.cast(entity.getName()));
        MutableComponentCompat hoverText = ComponentUtil.compose(ComponentUtil.translator.tr("entity_type", entityBaseName, ComponentUtil.simple(EntityType.getKey(entity.getType()).toString())), ComponentUtil.newLine(), ComponentUtil.getTeleportHint(entityDisplayName));
        return ComponentUtil.fancy(entityDisplayName, hoverText, ClickEventCompat.of(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(entity)));
    }

    public static @NotNull MutableComponentCompat attribute(@NotNull Attribute attribute) {
        return ComponentUtil.tr(
                //#if MC > 11600
                attribute.getDescriptionId()
                //#else
                //$$ "attribute.name." + attribute.getName()
                //#endif
        );
    }

    //#if MC > 12004
    //$$ public static MutableComponentCompat attribute(Holder<Attribute> attribute) {
    //$$     return ComponentUtil.attribute(attribute.value());
    //$$ }
    //#endif

    private static final ImmutableMap<DimensionWrapper, MutableComponentCompat> DIMENSION_NAME = ImmutableMap.of(
            DimensionWrapper.OVERWORLD, ComponentUtil.tr(
                    //#if MC > 11802
                    //$$ "flat_world_preset.minecraft.overworld"
                    //#else
                    "createWorld.customize.preset.overworld"
                    //#endif
            ), DimensionWrapper.NETHER, ComponentUtil.tr("advancements.nether.root.title"),
            DimensionWrapper.THE_END, ComponentUtil.tr("advancements.end.root.title"));

    public static MutableComponentCompat dimension(DimensionWrapper dim) {
        MutableComponentCompat dimText = ComponentUtil.DIMENSION_NAME.get(dim);
        return dimText != null ? ComponentUtil.copy(dimText) : ComponentUtil.simple(dim.getResourceLocationString());
    }

    public static @NotNull MutableComponentCompat getColoredDimensionSymbol(@NotNull DimensionWrapper dimensionType) {
        if (dimensionType.equals(DimensionWrapper.OVERWORLD)) {
            return ComponentUtil.simple("O", ChatFormatting.DARK_GREEN);
        }

        if (dimensionType.equals(DimensionWrapper.NETHER)) {
            return ComponentUtil.simple("N", ChatFormatting.DARK_RED);
        }

        if (dimensionType.equals(DimensionWrapper.THE_END)) {
            return ComponentUtil.simple("E", ChatFormatting.DARK_PURPLE);
        }

        return ComponentUtil.simple(dimensionType.getResourceLocationString().toUpperCase().substring(0, 1));
    }

    public static @NotNull MutableComponentCompat block(@NotNull Block block) {
        return ComponentUtil.hover(ComponentUtil.tr(block.getDescriptionId()), ComponentUtil.simple(TextUtil.block(block)));
    }

    public static @NotNull MutableComponentCompat block(@NotNull BlockState blockState) {
        List<MutableComponentCompat> hovers = Lists.newArrayList();
        hovers.add(ComponentUtil.simple(TextUtil.block(blockState.getBlock())));

        for (Property<?> property : blockState.getProperties()) {
            hovers.add(ComponentUtil.compose(ComponentUtil.simple(property.getName()), ComponentUtil.simple(" : ").withStyle(style -> style.withColor(ChatFormatting.GRAY)), ComponentUtil.property(property, blockState.getValue(property))));
        }
        return ComponentUtil.fancy(ComponentUtil.block(blockState.getBlock()), ComponentUtil.join(ComponentUtil.newLine(), hovers.toArray(new MutableComponentCompat[0])), ClickEventCompat.of(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.block(blockState)));
    }

    public static @NotNull MutableComponentCompat fluid(@NotNull Fluid fluid) {
        return ComponentUtil.hover(ComponentUtil.block(fluid.defaultFluidState().createLegacyBlock().getBlock()), ComponentUtil.simple(ResourceLocationUtil.id(fluid).toString()));
    }

    public static @NotNull MutableComponentCompat fluid(@NotNull FluidState fluid) {
        return ComponentUtil.fluid(fluid.getType());
    }

    public static @NotNull MutableComponentCompat blockEntity(@NotNull BlockEntity blockEntity) {
        ResourceLocation id = ResourceLocationUtil.id(blockEntity.getType());
        return ComponentUtil.simple(id != null ? id.toString() : // vanilla block entity
                blockEntity.getClass().getSimpleName()  // modded block entity, assuming the class name is not obfuscated
        );
    }

    public static @NotNull MutableComponentCompat item(@NotNull Item item) {
        return ComponentUtil.tr(item.getDescriptionId());
    }

    public static MutableComponentCompat color(@NotNull DyeColor color) {
        return ComponentUtil.translator.tr("color." + color.getName().toLowerCase());
    }

    /*
     * --------------------
     *    Text Modifiers
     * --------------------
     */

    public static @NotNull MutableComponentCompat hover(@NotNull MutableComponentCompat text, HoverEventCompat hoverEvent) {
        text.withStyle(style -> style.withHoverEvent(hoverEvent));
        return text;
    }

    public static @NotNull MutableComponentCompat hover(MutableComponentCompat text, MutableComponentCompat hoverText) {
        return ComponentUtil.hover(text, HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT, hoverText));
    }

    public static @NotNull MutableComponentCompat click(@NotNull MutableComponentCompat text, ClickEventCompat clickEvent) {
        text.withStyle(style -> style.withClickEvent(clickEvent));
        return text;
    }

    public static @NotNull MutableComponentCompat formatting(@NotNull MutableComponentCompat text, ChatFormatting... formattings) {
        text.withStyle(formattings);
        return text;
    }

    public static @NotNull MutableComponentCompat style(@NotNull MutableComponentCompat text, StyleCompat style) {
        text.setStyle(style);
        return text;
    }

    public static @NotNull MutableComponentCompat copy(@NotNull MutableComponentCompat text) {
        return MutableComponentCompat.of(ComponentUtil.copy(text.get()));
    }

    public static @NotNull
    //#if MC > 11502
    MutableComponent
    //#else
    //$$ BaseComponent
    //#endif
    copy(
            //#if MC > 11502
            @NotNull MutableComponent text
            //#else
            //$$ @NotNull BaseComponent text
            //#endif
    ) {
        //#if MC > 11502
        MutableComponent copied;
        //#else
        //$$ BaseComponent copied;
        //#endif

        //#if MC > 11802
        //$$ copied = text.copy();
        //#elseif MC > 11502
        copied = text.copy();
        //#else
        //$$ copied = (BaseComponent) text.deepCopy();
        //#endif

        // mc1.16+ doesn't make a copy of args of a TranslatableText,
        // so we need to copy that by ourselves.
        //#if MC > 11502
        if (ComponentUtil.getTextContent(copied) instanceof
                //#if MC > 11802
                //$$ TranslatableContents
                //#else
                TranslatableComponent
            //#endif
        ) {
            //#if MC > 11802
            //$$ TranslatableContents translatableText = (TranslatableContents) ComponentUtil.getTextContent(copied);
            //#else
            TranslatableComponent translatableText = (TranslatableComponent) ComponentUtil.getTextContent(copied);
            //#endif
            Object[] args = translatableText.getArgs().clone();

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof
                        //#if MC > 11502
                        MutableComponent
                    //#else
                    //$$ BaseComponent
                    //#endif
                ) {
                    args[i] = ComponentUtil.copy(
                            //#if MC > 11502
                            (MutableComponent) args[i]
                            //#else
                            //$$ (BaseComponent) args[i]
                            //#endif
                    );
                }
            }

            ((TranslatableComponentAccessor) translatableText).magiclib$setArgs(args);
        }
        //#endif

        return copied;
    }
}
