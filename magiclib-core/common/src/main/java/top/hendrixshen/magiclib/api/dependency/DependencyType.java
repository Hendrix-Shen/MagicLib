package top.hendrixshen.magiclib.api.dependency;

import top.hendrixshen.magiclib.api.platform.DistType;

public enum DependencyType {
    /**
     * Test if the current environment satisfies the condition.
     * (see {@link DistType}).
     */
    DIST,
    /**
     * Test if the mod satisfies the condition.
     */
    MOD_ID,
    /**
     * Test if the predicate satisfies the condition.
     * (see {@link top.hendrixshen.magiclib.util.collect.SimplePredicate SimplePredicate}).
     */
    PREDICATE,
    ;

    public boolean matches(DependencyType type) {
        return type == this;
    }
}
