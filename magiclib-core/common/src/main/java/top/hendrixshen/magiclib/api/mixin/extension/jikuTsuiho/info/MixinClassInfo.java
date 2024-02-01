package top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public interface MixinClassInfo {
    /**
     * Get the MixinInfo
     *
     * @return {@link IMixinInfo} object.
     */
    IMixinInfo getMixinInfo();

    /**
     * Get the class version (see {@link org.objectweb.asm.Opcodes versions} versions section).
     *
     * @return The class version.
     */
    int getVersion();

    /**
     * Get the class's access flags (see {@link org.objectweb.asm.Opcodes Opcodes} access flags section).
     * This field also indicates if the class is deprecated.
     *
     * @return The access flags of the class.
     */
    int getAccess();

    /**
     * Get the internal name of the class (see {@link org.objectweb.asm.Type#getInternalName() getInternalName}).
     *
     * @return Internal class name.
     */
    String getName();
}
