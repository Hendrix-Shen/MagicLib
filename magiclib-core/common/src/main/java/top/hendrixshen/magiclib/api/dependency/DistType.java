package top.hendrixshen.magiclib.api.dependency;

public enum DistType {
    /**
     * Match any distribution type.
     */
    ANY,
    /**
     * Match client distribution type.
     */
    CLIENT,
    /**
     * Match server distribution type.
     */
    SERVER,
    ;

    public boolean matches(DistType type) {
        return this == DistType.ANY || type == DistType.ANY || type == this;
    }
}
