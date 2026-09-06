package top.hendrixshen.magiclib.impl.dependency;

/**
 * The implementation level result type, kept for backward compatibility.
 *
 * @deprecated Use {@link top.hendrixshen.magiclib.api.dependency.DependencyCheckResult} instead.
 */
@Deprecated
public class DependencyCheckResult extends top.hendrixshen.magiclib.api.dependency.DependencyCheckResult {
    public DependencyCheckResult(boolean success, String reason) {
        super(success, reason);
    }
}
