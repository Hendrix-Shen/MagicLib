package top.hendrixshen.magiclib.api.platform.adapter;

import org.objectweb.asm.tree.ClassNode;

import java.util.Collection;

public interface ModEntryPointAdapter {
    Collection<ClassNode> getEntryPoints();

    Collection<ClassNode> getMagicEntryPoints();
}
