package top.hendrixshen.magiclib.tool.mixin;

import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.util.FabricUtil;
import top.hendrixshen.magiclib.util.MixinUtil;

/**
 * See {@link top.hendrixshen.magiclib.impl.mixin.audit.MixinAuditor}
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class MixinAuditExecutor {
    private static final String KEYWORD_PROPERTY = String.format("%s.mixin_audit", MagicLibReference.getModIdentifier());

    public static void execute() {
        if (MagicLib.getInstance().getPlatformManage().getCurrentPlatform().isDevelopmentEnvironment() &&
                "true".equals(System.getProperty(MixinAuditExecutor.KEYWORD_PROPERTY))) {
            MagicLibReference.getLogger().info("Triggered mixin audit.");
            MixinUtil.audit();
            System.exit(0);
        }
    }
}
