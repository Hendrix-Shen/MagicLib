package top.hendrixshen.magiclib.api.platform;

import lombok.Getter;

@Getter
public enum PlatformType {
    FABRIC("fabric"),
    FABRIC_LIKE("fabric_like"),
    FORGE("forge"),
    NEOFORGE("neoforge"),
    FORGE_LIKE("forge_like"),
    QUILT("quilt");

    private final String name;

    PlatformType(String name) {
        this.name = name;
    }

    public boolean isForgeLike(PlatformType type) {
        return this == PlatformType.FORGE_LIKE || type == PlatformType.FORGE_LIKE || type == this;
    }

    public boolean isFabricLike(PlatformType type) {
        return this == PlatformType.FABRIC_LIKE || type == PlatformType.FABRIC_LIKE || type == this;
    }

    public boolean matches(PlatformType type) {
        return type == this || this.isFabricLike(type) || this.isForgeLike(type);
    }
}
