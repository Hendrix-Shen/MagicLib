package top.hendrixshen.magiclib.api.platform;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

@Getter
@AllArgsConstructor
public enum PlatformType {
    ANY("any"),
    FABRIC("fabric"),
    FABRIC_LIKE("fabric_like"),
    FORGE("forge"),
    FORGE_LIKE("forge_like"),
    NEOFORGE("neoforge"),
    QUILT("quilt"),
    UNKNOWN("unknown")
    ;

    private final String name;

    // This method should be static.
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public boolean isForgeLike(PlatformType type) {
        return type == PlatformType.FORGE_LIKE || type == PlatformType.FORGE || type == PlatformType.NEOFORGE;
    }

    // This method should be static.
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public boolean isFabricLike(PlatformType type) {
        return type == PlatformType.FABRIC_LIKE || type == PlatformType.FABRIC || type == PlatformType.QUILT;
    }

    public boolean isForgeLike() {
        return this.isForgeLike(this);
    }

    public boolean isFabricLike() {
        return this.isFabricLike(this);
    }

    public boolean matches(PlatformType type) {
        if (this == PlatformType.UNKNOWN || type == PlatformType.UNKNOWN) {
            return false;
        }

        if (this == PlatformType.ANY || type == PlatformType.ANY) {
            return true;
        }

        if (type == PlatformType.FABRIC_LIKE && this.isFabricLike() ||
                this == PlatformType.FABRIC_LIKE && type.isFabricLike()) {
            return true;
        }

        if (type == PlatformType.FORGE_LIKE && this.isForgeLike() ||
                this == PlatformType.FORGE_LIKE && type.isForgeLike()) {
            return true;
        }

        return type == this;
    }
}
