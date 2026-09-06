package top.hendrixshen.magiclib.api.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

/**
 * The result of a dependency check.
 *
 * <p>
 * A result is successful when the checked dependencies are satisfied. When the check fails, the reason is a
 * human-readable description of the unsatisfied dependencies, and it is {@code null} on success.
 * </p>
 */
@AllArgsConstructor
@Getter
@ToString
public class DependencyCheckResult {
    private final boolean success;
    @Nullable
    private final String reason;
}
