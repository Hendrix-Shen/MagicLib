package top.hendrixshen.magiclib.impl.mixin.audit.minecraft;

import lombok.Getter;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.event.minecraft.DedicatedServerListener;
import top.hendrixshen.magiclib.api.event.minecraft.MinecraftListener;
import top.hendrixshen.magiclib.impl.mixin.audit.MixinAuditor;

public class MinecraftMixinAudit implements MinecraftListener, DedicatedServerListener {
    @Getter
    private static final MinecraftMixinAudit instance = new MinecraftMixinAudit();

    private MinecraftMixinAudit() {
        MagicLib.getInstance().getEventManager().register(MinecraftListener.class, this);
        MagicLib.getInstance().getEventManager().register(DedicatedServerListener.class, this);
    }

    public static void init() {
        // NO-OP
    }

    @Override
    public void postInit() {
        MixinAuditor.trigger("post_client_init");
    }

    @Override
    public void postServerInit() {
        MixinAuditor.trigger("post_server_init");
    }
}
