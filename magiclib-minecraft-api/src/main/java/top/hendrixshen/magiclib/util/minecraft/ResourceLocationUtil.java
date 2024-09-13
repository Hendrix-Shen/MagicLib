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

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/blob/2733a1dfa4978374e7422376486b5c291ebb1bbc/src/main/java/carpettisaddition/utils/IdentifierUtil.java">Carpet-TIS-Addition</a>
 */
public class ResourceLocationUtil {
    public static @NotNull ResourceLocation id(Block block) {
        //#if MC > 11902
        //$$ return BuiltInRegistries.BLOCK.getKey(block);
        //#else
        return Registry.BLOCK.getKey(block);
        //#endif
    }

    public static @NotNull ResourceLocation id(Fluid fluid) {
        //#if MC > 11902
        //$$ return BuiltInRegistries.FLUID.getKey(fluid);
        //#else
        return Registry.FLUID.getKey(fluid);
        //#endif
    }

    public static @NotNull ResourceLocation id(EntityType<?> entityType) {
        //#if MC > 11902
        //$$ return BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        //#else
        return Registry.ENTITY_TYPE.getKey(entityType);
        //#endif
    }

    public static ResourceLocation id(BlockEntityType<?> blockEntityType) {
        //#if MC > 11902
        //$$ return BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
        //#else
        return Registry.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
        //#endif
    }

    public static ResourceLocation id(PoiType poiType) {
        //#if MC > 11902
        //$$ return BuiltInRegistries.POINT_OF_INTEREST_TYPE.getKey(poiType);
        //#else
        return Registry.POINT_OF_INTEREST_TYPE.getKey(poiType);
        //#endif
    }
}
