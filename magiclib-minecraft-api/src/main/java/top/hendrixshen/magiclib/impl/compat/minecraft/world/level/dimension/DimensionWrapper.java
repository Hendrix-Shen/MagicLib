package top.hendrixshen.magiclib.impl.compat.minecraft.world.level.dimension;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//#if MC > 11502
import net.minecraft.resources.ResourceKey;
//#else
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/">Carpet-TIS-Addition</a>
 * <p>
 * A wrapper class to deal with dimension type class differences between minecraft version:
 * <li>DimensionType in 1.15-</li>
 * <li>Registry<Level> in 1.16+</li>
 */
public class DimensionWrapper {
    public static final DimensionWrapper OVERWORLD = DimensionWrapper.of(
            //#if MC > 11502
            Level.OVERWORLD
            //#else
            //$$ DimensionType.OVERWORLD
            //#endif
    );
    public static final DimensionWrapper NETHER = DimensionWrapper.of(
            //#if MC > 11502
            Level.NETHER
            //#else
            //$$ DimensionType.NETHER
            //#endif
    );
    public static final DimensionWrapper THE_END = DimensionWrapper.of(
            //#if MC > 11502
            Level.END
            //#else
            //$$ DimensionType.THE_END
            //#endif
    );

    private final
    //#if MC > 11502
    ResourceKey<Level>
            //#else
            //$$ DimensionType
            //#endif
            dimensionType;

    private DimensionWrapper(
            //#if MC > 11502
            ResourceKey<Level> dimensionType
            //#else
            //$$ DimensionType dimensionType
            //#endif
    ) {
        this.dimensionType = dimensionType;
    }

    /**
     * Warning: mc version dependent
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
        return DimensionWrapper.of(entity.getCommandSenderWorld());
    }

    /**
     * Warning: mc version dependent
     */
    public
    //#if MC > 11502
    ResourceKey<Level>
    //#else
    //$$ DimensionType
    //#endif
    getValue() {
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
