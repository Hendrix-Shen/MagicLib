package top.hendrixshen.magiclib.impl.dependency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DependencyCheckResult {
    private final boolean success;
    private final String reason;
}
