package top.hendrixshen.magiclib.impl.malilib;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.mixin.MixinPredicate;
import top.hendrixshen.magiclib.libs.org.semver4j.Semver;

public class MixinPredicates {
    // Workaround for SemVer4j bug!
    public static class Malilib_0_11_4 implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            //#if FABRIC_LIKE
            String malilibVer = MagicLib.getInstance().getPlatformManage()
                    .getCurrentPlatform().getModVersion("malilib");

            if ("?".equals(malilibVer)) {
                return false;
            }

            String[] split = malilibVer.split("-");
            Semver semver = Semver.coerce(split[0]);

            if (semver == null) {
                return false;
            }

            return semver.satisfies("<0.11.4");
            //#else
            //$$ return false;
            //#endif
        }
    }
}
