package top.hendrixshen.magiclib.util.minecraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

//#if MC > 11902
//$$ import net.minecraft.core.registries.BuiltInRegistries;
//#else
import net.minecraft.core.Registry;
//#endif

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
