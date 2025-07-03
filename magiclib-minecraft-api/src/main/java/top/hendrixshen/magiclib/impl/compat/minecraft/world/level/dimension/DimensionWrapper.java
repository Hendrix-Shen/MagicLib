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

package top.hendrixshen.magiclib.impl.compat.minecraft.world.level.dimension;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11502
import net.minecraft.resources.ResourceKey;
//#else
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif
// CHECKSTYLE.ON: ImportOrder

import java.util.Objects;

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/65afc48e8c748ee6b7caa3c972b4cb1f7090b9ea/src/main/java/carpettisaddition/utils/compat/DimensionWrapper.java">Carpet-TIS-Addition</a>.
 *
 * <p>
 * A wrapper class to deal with dimension type class differences between minecraft version:
 * <li>{@code DimensionType} in 1.15- </li>
 * <li>{@code Registry<Level>} in 1.16+ </li>
 * </p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DimensionWrapper {
    public static final DimensionWrapper OVERWORLD = DimensionWrapper.of(
            // CHECKSTYLE.OFF: Indentation
            //#if MC > 11502
            Level.OVERWORLD
            //#else
            //$$ DimensionType.OVERWORLD
            //#endif
            // CHECKSTYLE.ON: Indentation
    );
    public static final DimensionWrapper NETHER = DimensionWrapper.of(
            // CHECKSTYLE.OFF: Indentation
            //#if MC > 11502
            Level.NETHER
            //#else
            //$$ DimensionType.NETHER
            //#endif
            // CHECKSTYLE.ON: Indentation
    );
    public static final DimensionWrapper THE_END = DimensionWrapper.of(
            // CHECKSTYLE.OFF: Indentation
            //#if MC > 11502
            Level.END
            //#else
            //$$ DimensionType.THE_END
            //#endif
            // CHECKSTYLE.ON: Indentation
    );

    //#if MC > 11502
    private final ResourceKey<Level> dimensionType;
    //#else
    //$$ private final DimensionType dimensionType;
    //#endif

    /**
     * Warning: mc version dependent.
     */
    public static @NotNull DimensionWrapper of(
            //#if MC > 11502
            ResourceKey<Level> dimensionType
            //#else
            //$$ DimensionType dimensionType
            //#endif
    ) {
        return new DimensionWrapper(dimensionType);
    }

    public static @NotNull DimensionWrapper of(@NotNull Level level) {
        return new DimensionWrapper(
                //#if MC > 11502
                level.dimension()
                //#else
                //$$ level.getDimension().getType()
                //#endif
        );
    }

    public static @NotNull DimensionWrapper of(@NotNull Entity entity) {
        return DimensionWrapper.of(
                //#if MC >= 12106
                //$$ entity.level()
                //#else
                entity.getCommandSenderWorld()
                //#endif
        );
    }

    /**
     * Warning: mc version dependent.
     */
    // CHECKSTYLE.OFF: Indentation
    public
    //#if MC > 11502
    ResourceKey<Level>
    //#else
    //$$ DimensionType
    //#endif
    getValue() {
        // CHECKSTYLE.ON: Indentation
        return this.dimensionType;
    }

    public ResourceLocation getResourceLocation() {
        //#if MC > 11502
        return this.dimensionType.location();
        //#else
        //$$ return DimensionType.getName(this.dimensionType);
        //#endif
    }

    public String getResourceLocationString() {
        //#if MC > 11502
        return this.getResourceLocation().toString();
        //#else
        //$$ return this.dimensionType.toString();
        //#endif
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DimensionWrapper that = (DimensionWrapper) o;
        return Objects.equals(dimensionType, that.dimensionType);
    }

    @Override
    public int hashCode() {
        return this.dimensionType.hashCode();
    }

    @ApiStatus.Obsolete
    @Override
    public String toString() {
        return this.getResourceLocationString();
    }
}
