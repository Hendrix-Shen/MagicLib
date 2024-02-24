package top.hendrixshen.magiclib.api.platform.adapter;

public interface ModContainerAdapter {
    /**
     * Returns the metadata of this mod.
     */
    ModMetaDataAdapter getModMetaData();

    /**
     * Returns the entrypoint of this mod.
     */
    ModEntryPointAdapter getModEntryPoint();
}
