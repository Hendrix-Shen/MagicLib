package top.hendrixshen.magiclib.api.dependency;

public enum DistType {
    ANY,
    CLIENT,
    SERVER,
    ;

    public boolean matches(DistType type) {
        return this == DistType.ANY || type == DistType.ANY || type == this;
    }
}
