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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.*;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.dimension.DimensionWrapper;
import top.hendrixshen.magiclib.impl.i18n.minecraft.translation.Translator;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.DyeColorAccessor;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.TranslatableComponentAccessor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    //#else MC > 11502
    BaseComponent
    //#endif
    getTextContent(BaseComponent text) {
        //#if MC > 11802
        //$$ return text.getContents();
        //#else
        return text;
        //#endif
    }

    public static
    //#if MC > 11802
    //$$ ComponentContents
    //#else MC > 11502
    BaseComponent
    //#endif
    getTextContent(@NotNull MutableComponentCompat mutableComponentCompat) {
        return ComponentUtil.getTextContent(mutableComponentCompat.get());
    }

    /*
     * ----------------------------
     *    Component Factories - Utils
     * ----------------------------
     */

    private static final ImmutableMap<DyeColor, Consumer<BaseComponent>> DYE_COLOR_APPLIER = Util.make(() -> {
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

        ImmutableMap.Builder<DyeColor, Consumer<BaseComponent>> builder = new ImmutableMap.Builder<>();
        map.forEach((dyeColor, fmt) -> builder.put(dyeColor, text -> ComponentUtil.formatting(text, fmt)));
        //#if MC > 11502
        Arrays.stream(DyeColor.values())
                .filter(dyeColor -> !map.containsKey(dyeColor))
                .forEach(dyeColor -> builder.put(dyeColor, text -> {
                    TextColor color = TextColor.fromRgb(((DyeColorAccessor) (Object) dyeColor).magiclib$getTextColor());
                    text.setStyle(text.getStyle().withColor(color));
                }));
        //#endif
        return builder.build();
    });

    public static @NotNull BaseComponent compose(Object @NotNull ... objects) {
        BaseComponent literal = ComponentUtil.empty();

        for (Object o : objects) {
            if (o instanceof MutableComponentCompat) {
                literal.append(((MutableComponentCompat) o).get());
            } else if (o instanceof BaseComponent) {
                literal.append((BaseComponent) o);
            } else {
                literal.append(o.toString());
            }
        }

        return literal;
    }

    public static @NotNull MutableComponentCompat composeCompat(Object @NotNull ... objects) {
        return MutableComponentCompat.of(ComponentUtil.compose(objects));
    }

    @NotNull
    public static BaseComponent simple(@NotNull Object text) {
        return ComponentCompat.literal(text.toString());
    }

    public static @NotNull MutableComponentCompat simpleCompat(@NotNull Object text) {
        return ComponentCompat.literalCompat(text.toString());
    }

    public static @NotNull BaseComponent simple(Object text, ChatFormatting... chatFormattings) {
        return ComponentUtil.formatting(ComponentUtil.simple(text), chatFormattings);
    }

    public static @NotNull MutableComponentCompat simpleCompat(Object text, ChatFormatting... chatFormattings) {
        return ComponentUtil.formattingCompat(ComponentUtil.simpleCompat(text), chatFormattings);
    }

    public static @NotNull BaseComponent empty() {
        return ComponentUtil.simple("");
    }

    public static @NotNull MutableComponentCompat emptyCompat() {
        return ComponentUtil.simpleCompat("");
    }


    public static @NotNull BaseComponent newLine() {
        return ComponentUtil.simple("\n");
    }

    public static @NotNull MutableComponentCompat newLineCompat() {
        return ComponentUtil.simpleCompat("\n");
    }

    public static BaseComponent colored(BaseComponent text, DyeColor value) {
        Consumer<BaseComponent> consumer = ComponentUtil.DYE_COLOR_APPLIER.get(value);

        if (consumer != null) {
            consumer.accept(text);
        }

        return text;
    }

    public static @NotNull MutableComponentCompat coloredCompat(@NotNull MutableComponentCompat text, DyeColor value) {
        ComponentUtil.colored(text.get());
        return text;
    }

    public static BaseComponent colored(BaseComponent text, Object value) {
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

    public static @NotNull MutableComponentCompat coloredCompat(@NotNull MutableComponentCompat text, Object value) {
        ComponentUtil.colored(text.get(), value);
        return text;
    }

    public static BaseComponent colored(Object value) {
        return ComponentUtil.colored(ComponentUtil.simple(value), value);
    }

    public static @NotNull MutableComponentCompat coloredCompat(Object value) {
        return ComponentUtil.coloredCompat(ComponentUtil.simpleCompat(value), value);
    }

    public static BaseComponent property(Property<?> property, Object value) {
        return ComponentUtil.colored(ComponentUtil.simple(TextUtil.property(property, value)), value);
    }

    public static MutableComponentCompat propertyCompat(Property<?> property, Object value) {
        return ComponentUtil.coloredCompat(ComponentUtil.simpleCompat(TextUtil.property(property, value)), value);
    }

    public static @NotNull BaseComponent tr(String key, Object @NotNull ... args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ComponentCompat) {
                args[i] = ((ComponentCompat) args[i]).get();
            }
        }

        return ComponentCompat.translatable(key, args);
    }

    public static @NotNull MutableComponentCompat trCompat(String key, Object @NotNull ... args) {
        return MutableComponentCompat.of(ComponentUtil.tr(key, args));
    }

    public static @NotNull BaseComponent fancy(BaseComponent displayText, BaseComponent hoverText,
                                               ClickEvent clickEvent) {
        BaseComponent text = ComponentUtil.copy(displayText);

        if (hoverText != null) {
            ComponentUtil.hover(text, hoverText);
        }

        if (clickEvent != null) {
            ComponentUtil.click(text, clickEvent);
        }

        return text;
    }

    public static @NotNull MutableComponentCompat fancyCompat(
            @NotNull MutableComponentCompat displayText, MutableComponentCompat hoverText, ClickEventCompat clickEvent) {
        return MutableComponentCompat.of(ComponentUtil.fancy(displayText.get(),
                hoverText == null ? null : hoverText.get(), clickEvent == null ? null : clickEvent.get()));
    }

    public static @NotNull BaseComponent join(BaseComponent joiner, @NotNull Iterable<BaseComponent> items) {
        BaseComponent text = ComponentUtil.empty();
        boolean first = true;

        for (BaseComponent item : items) {
            if (!first) {
                text.append(joiner);
            }

            first = false;
            text.append(item);
        }

        return text;
    }

    public static @NotNull MutableComponentCompat joinCompat(
            MutableComponentCompat joiner, @NotNull Iterable<MutableComponentCompat> items) {
        MutableComponentCompat text = ComponentUtil.emptyCompat();
        ComponentUtil.join(text.get(), StreamSupport.stream(items.spliterator(), false)
                .map(MutableComponentCompat::get)
                .collect(Collectors.toList()));
        return text;
    }

    public static @NotNull BaseComponent join(BaseComponent joiner, BaseComponent... items) {
        return ComponentUtil.join(joiner, Arrays.asList(items));
    }

    public static @NotNull MutableComponentCompat joinCompat(MutableComponentCompat joiner,
                                                             MutableComponentCompat... items) {
        return ComponentUtil.joinCompat(joiner, Arrays.asList(items));
    }

    public static @NotNull BaseComponent joinLines(Iterable<BaseComponent> items) {
        return ComponentUtil.join(ComponentUtil.newLine(), items);
    }

    public static @NotNull MutableComponentCompat joinLinesCompat(Iterable<MutableComponentCompat> items) {
        return ComponentUtil.joinCompat(ComponentUtil.newLineCompat(), items);
    }

    public static @NotNull BaseComponent joinLines(BaseComponent... items) {
        return ComponentUtil.join(ComponentUtil.newLine(), items);
    }

    public static @NotNull MutableComponentCompat joinLinesCompat(MutableComponentCompat... items) {
        return ComponentUtil.joinCompat(ComponentUtil.newLineCompat(), items);
    }

    public static @NotNull BaseComponent format(String formatter, Object... args) {
        TranslatableComponentAccessor dummy = (TranslatableComponentAccessor) (
                ComponentUtil.tr(formatter, args)
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
                    //$$ segments
                    //#else
                    dummy.magiclib$getDecomposedParts()
                            //#endif
                            .stream().map(formattedText -> {
                                if (formattedText instanceof
                                        //#if MC > 11802
                                        //$$ Component
                                        //#else
                                        BaseComponent
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

    public static @NotNull MutableComponentCompat formatCompat(String formatter, Object... args) {
        return MutableComponentCompat.of(ComponentUtil.format(formatter, args));
    }

    /*
     * -------------------------------
     *    Component Factories - Advanced
     * -------------------------------
     */

    public static @NotNull BaseComponent bool(boolean bool) {
        return ComponentUtil.simple(String.valueOf(bool), bool ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    public static @NotNull MutableComponentCompat boolCompat(boolean bool) {
        return MutableComponentCompat.of(ComponentUtil.bool(bool));
    }

    private static BaseComponent getTeleportHint(BaseComponent dest) {
        return ComponentUtil.translator.tr("teleport_hint.hover", dest);
    }

    private static MutableComponentCompat getTeleportHintCompat(MutableComponentCompat dest) {
        return ComponentUtil.translator.trCompat("teleport_hint.hover", dest);
    }

    private static @NotNull BaseComponent coordinate(@Nullable DimensionWrapper dim, String posStr, String command) {
        BaseComponent hoverText = ComponentUtil.empty();
        hoverText.append(ComponentUtil.getTeleportHint(ComponentUtil.simple(posStr)));

        if (dim != null) {
            hoverText.append("\n");
            hoverText.append(ComponentUtil.translator.tr("teleport_hint.dimension"));
            hoverText.append(": ");
            hoverText.append(ComponentUtil.dimension(dim));
        }

        return ComponentUtil.fancy(ComponentUtil.simple(posStr), hoverText,
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
    }

    private static @NotNull MutableComponentCompat coordinateCompat(@Nullable DimensionWrapper dim, String posStr, String command) {
        return MutableComponentCompat.of(ComponentUtil.coordinate(dim, posStr, command));
    }

    public static @NotNull BaseComponent coordinate(Vec3 pos, DimensionWrapper dim) {
        return ComponentUtil.coordinate(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull MutableComponentCompat coordinateCompat(Vec3 pos, DimensionWrapper dim) {
        return ComponentUtil.coordinateCompat(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull BaseComponent coordinate(Vec3i pos, DimensionWrapper dim) {
        return ComponentUtil.coordinate(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull MutableComponentCompat coordinateCompat(Vec3i pos, DimensionWrapper dim) {
        return ComponentUtil.coordinateCompat(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull BaseComponent coordinate(ChunkPos pos, DimensionWrapper dim) {
        return ComponentUtil.coordinate(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull MutableComponentCompat coordinateCompat(ChunkPos pos, DimensionWrapper dim) {
        return ComponentUtil.coordinateCompat(dim, TextUtil.coordinate(pos), TextUtil.tp(pos, dim));
    }

    public static @NotNull BaseComponent coordinate(Vec3 pos) {
        return ComponentUtil.coordinate(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    public static @NotNull MutableComponentCompat coordinateCompat(Vec3 pos) {
        return ComponentUtil.coordinateCompat(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    public static @NotNull BaseComponent coordinate(Vec3i pos) {
        return ComponentUtil.coordinate(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    public static @NotNull MutableComponentCompat coordinateCompat(Vec3i pos) {
        return ComponentUtil.coordinateCompat(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    public static @NotNull BaseComponent coordinate(ChunkPos pos) {
        return ComponentUtil.coordinate(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    public static @NotNull MutableComponentCompat coordinateCompat(ChunkPos pos) {
        return ComponentUtil.coordinateCompat(null, TextUtil.coordinate(pos), TextUtil.tp(pos));
    }

    private static @NotNull BaseComponent vector(String displayText, String detailedText) {
        return ComponentUtil.fancy(ComponentUtil.simple(displayText), ComponentUtil.simple(detailedText),
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, detailedText));
    }

    public static @NotNull BaseComponent vector(Vec3 vec) {
        return ComponentUtil.vector(TextUtil.vector(vec), TextUtil.vector(vec, 6));
    }

    public static @NotNull MutableComponentCompat vectorCompat(Vec3 vec) {
        return MutableComponentCompat.of(ComponentUtil.vector(vec));
    }

    public static @NotNull BaseComponent entityType(@NotNull EntityType<?> entityType) {
        return (BaseComponent) entityType.getDescription();
    }

    public static @NotNull MutableComponentCompat entityTypeCompat(@NotNull EntityType<?> entityType) {
        return MutableComponentCompat.of(ComponentUtil.entityType(entityType));
    }

    public static @NotNull BaseComponent entityType(@NotNull Entity entity) {
        return ComponentUtil.entityType(entity.getType());
    }

    public static @NotNull MutableComponentCompat entityTypeCompat(@NotNull Entity entity) {
        return ComponentUtil.entityTypeCompat(entity.getType());
    }

    public static @NotNull BaseComponent entity(Entity entity) {
        BaseComponent entityBaseName = ComponentUtil.entityType(entity);
        BaseComponent entityDisplayName = (BaseComponent) entity.getName();
        BaseComponent hoverText = ComponentUtil.compose(ComponentUtil.translator.tr("entity_type",
                entityBaseName, ComponentUtil.simple(EntityType.getKey(entity.getType()).toString())),
                ComponentUtil.newLine(), ComponentUtil.getTeleportHint(entityDisplayName));
        return ComponentUtil.fancy(entityDisplayName, hoverText, new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                TextUtil.tp(entity)));
    }

    public static @NotNull MutableComponentCompat entityCompat(Entity entity) {
        return MutableComponentCompat.of(ComponentUtil.entity(entity));
    }

    public static @NotNull BaseComponent attribute(@NotNull Attribute attribute) {
        return ComponentUtil.tr(
                //#if MC > 11600
                attribute.getDescriptionId()
                //#else
                //$$ "attribute.name." + attribute.getName()
                //#endif
        );
    }

    public static @NotNull MutableComponentCompat attributeCompat(@NotNull Attribute attribute) {
        return MutableComponentCompat.of(ComponentUtil.attribute(attribute));
    }

    //#if MC > 12004
    //$$ public static MutableComponent attribute(Holder<Attribute> attribute) {
    //$$     return ComponentUtil.attribute(attribute.value());
    //$$ }
    //$$
    //$$ public static MutableComponentCompat attributeCompat(Holder<Attribute> attribute) {
    //$$     return MutableComponentCompat.of(ComponentUtil.attribute(attribute));
    //$$ }
    //#endif

    private static final ImmutableMap<DimensionWrapper, BaseComponent> DIMENSION_NAME = ImmutableMap.of(
            DimensionWrapper.OVERWORLD, ComponentUtil.tr(
                    //#if MC > 11802
                    //$$ "flat_world_preset.minecraft.overworld"
                    //#else
                    "createWorld.customize.preset.overworld"
                    //#endif
            ), DimensionWrapper.NETHER, ComponentUtil.tr("advancements.nether.root.title"),
            DimensionWrapper.THE_END, ComponentUtil.tr("advancements.end.root.title"));

    public static BaseComponent dimension(DimensionWrapper dim) {
        BaseComponent dimText = ComponentUtil.DIMENSION_NAME.get(dim);
        return dimText != null ? ComponentUtil.copy(dimText) : ComponentUtil.simple(dim.getResourceLocationString());
    }

    public static MutableComponentCompat dimensionCompat(DimensionWrapper dim) {
        return MutableComponentCompat.of(ComponentUtil.dimension(dim));
    }

    public static @NotNull BaseComponent getColoredDimensionSymbol(@NotNull DimensionWrapper dimensionType) {
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

    public static @NotNull MutableComponentCompat getColoredDimensionSymbolCompat(@NotNull DimensionWrapper dimensionType) {
        return MutableComponentCompat.of(ComponentUtil.getColoredDimensionSymbol(dimensionType));
    }

    public static @NotNull BaseComponent block(@NotNull Block block) {
        return ComponentUtil.hover(ComponentUtil.tr(block.getDescriptionId()), ComponentUtil.simple(TextUtil.block(block)));
    }

    public static @NotNull MutableComponentCompat blockCompat(@NotNull Block block) {
        return MutableComponentCompat.of(ComponentUtil.block(block));
    }

    public static @NotNull BaseComponent block(@NotNull BlockState blockState) {
        List<BaseComponent> hovers = Lists.newArrayList();
        hovers.add(ComponentUtil.simple(TextUtil.block(blockState.getBlock())));

        for (Property<?> property : blockState.getProperties()) {
            hovers.add(ComponentUtil.compose(
                    ComponentUtil.simple(property.getName()),
                    ComponentUtil.simple(" : ").withStyle(style -> style.withColor(ChatFormatting.GRAY)),
                    ComponentUtil.property(property, blockState.getValue(property))));
        }
        return ComponentUtil.fancy(ComponentUtil.block(blockState.getBlock()),
                ComponentUtil.join(ComponentUtil.newLine(), hovers.toArray(new BaseComponent[0])),
                new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.block(blockState)));
    }

    public static @NotNull MutableComponentCompat blockCompat(@NotNull BlockState blockState) {
        return MutableComponentCompat.of(ComponentUtil.block(blockState));
    }

    public static @NotNull BaseComponent fluid(@NotNull Fluid fluid) {
        return ComponentUtil.hover(ComponentUtil.block(fluid.defaultFluidState().createLegacyBlock().getBlock()),
                ComponentUtil.simple(ResourceLocationUtil.id(fluid).toString()));
    }

    public static @NotNull MutableComponentCompat fluidCompat(@NotNull Fluid fluid) {
        return MutableComponentCompat.of(ComponentUtil.fluid(fluid));
    }

    public static @NotNull BaseComponent fluid(@NotNull FluidState fluid) {
        return ComponentUtil.fluid(fluid.getType());
    }

    public static @NotNull MutableComponentCompat fluidCompat(@NotNull FluidState fluid) {
        return ComponentUtil.fluidCompat(fluid.getType());
    }

    public static @NotNull BaseComponent blockEntity(@NotNull BlockEntity blockEntity) {
        ResourceLocation id = ResourceLocationUtil.id(blockEntity.getType());
        return ComponentUtil.simple(id != null ? id.toString() : // vanilla block entity
                blockEntity.getClass().getSimpleName()  // modded block entity, assuming the class name is not obfuscated
        );
    }

    public static @NotNull MutableComponentCompat blockEntityCompat(@NotNull BlockEntity blockEntity) {
        return MutableComponentCompat.of(ComponentUtil.blockEntity(blockEntity));
    }

    public static @NotNull BaseComponent item(@NotNull Item item) {
        return ComponentUtil.tr(item.getDescriptionId());
    }

    public static @NotNull MutableComponentCompat itemCompat(@NotNull Item item) {
        return MutableComponentCompat.of(ComponentUtil.item(item));
    }

    public static BaseComponent color(@NotNull DyeColor color) {
        return ComponentUtil.translator.tr("color." + color.getName().toLowerCase());
    }

    public static MutableComponentCompat colorCompat(@NotNull DyeColor color) {
        return MutableComponentCompat.of(ComponentUtil.color(color));
    }

    /*
     * --------------------
     *    Text Modifiers
     * --------------------
     */

    public static @NotNull BaseComponent hover(@NotNull BaseComponent text, HoverEvent hoverEvent) {
        text.withStyle(style -> style.withHoverEvent(hoverEvent));
        return text;
    }

    public static @NotNull MutableComponentCompat hoverCompat(@NotNull MutableComponentCompat text, HoverEventCompat hoverEvent) {
        text.withStyleCompat(style -> style.withHoverEvent(hoverEvent));
        return text;
    }

    public static @NotNull BaseComponent hover(BaseComponent text, BaseComponent hoverText) {
        return ComponentUtil.hover(text, new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
    }

    public static @NotNull MutableComponentCompat hoverCompat(MutableComponentCompat text, MutableComponentCompat hoverText) {
        return ComponentUtil.hoverCompat(text, HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT, hoverText));
    }

    public static @NotNull BaseComponent click(@NotNull BaseComponent text, ClickEvent clickEvent) {
        text.withStyle(style -> style.withClickEvent(clickEvent));
        return text;
    }

    public static @NotNull MutableComponentCompat clickCompat(@NotNull MutableComponentCompat text, ClickEventCompat clickEvent) {
        text.withStyleCompat(style -> style.withClickEvent(clickEvent));
        return text;
    }

    public static @NotNull BaseComponent formatting(@NotNull BaseComponent text, ChatFormatting... formattings) {
        text.withStyle(formattings);
        return text;
    }

    public static @NotNull MutableComponentCompat formattingCompat(@NotNull MutableComponentCompat text, ChatFormatting... formattings) {
        text.withStyle(formattings);
        return text;
    }

    public static @NotNull BaseComponent style(@NotNull BaseComponent text, Style style) {
        text.setStyle(style);
        return text;
    }

    public static @NotNull MutableComponentCompat styleCompat(@NotNull MutableComponentCompat text, StyleCompat style) {
        text.setStyle(style);
        return text;
    }

    public static @NotNull MutableComponentCompat copy(@NotNull MutableComponentCompat text) {
        return MutableComponentCompat.of(ComponentUtil.copy(text.get()));
    }

    public static @NotNull BaseComponent copy(@NotNull BaseComponent text) {
        BaseComponent copied;

        //#if MC > 11802
        //$$ copied = text.copy();
        //#elseif MC > 11502
        copied = (BaseComponent) text.copy();
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
                if (args[i] instanceof BaseComponent) {
                    args[i] = ComponentUtil.copy((BaseComponent) args[i]);
                }
            }

            ((TranslatableComponentAccessor) translatableText).magiclib$setArgs(args);
        }
        //#endif

        return copied;
    }
}
