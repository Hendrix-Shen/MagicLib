package top.hendrixshen.magiclib.impl.malilib.config.gui.button;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import org.jetbrains.annotations.Nullable;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12109
//$$ import net.minecraft.client.input.MouseButtonEvent;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3i;
import top.hendrixshen.magiclib.impl.malilib.config.gui.GuiVec3iEdit;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;

import java.util.Collections;

public class ConfigButtonVec3i extends ButtonGeneric {
    private final ConfigVec3i config;
    private final MagicConfigGui configGui;
    @Nullable
    private final IDialogHandler dialogHandler;

    public ConfigButtonVec3i(int x, int y, int width, int height, ConfigVec3i config, MagicConfigGui configGui, @Nullable IDialogHandler dialogHandler) {
        super(x, y, width, height, "");
        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;
        this.updateDisplayString();
    }

    @Override
    protected boolean onMouseClickedImpl(
            //#if MC >= 12109
            //$$ MouseButtonEvent click,
            //$$ boolean doubleClick
            //#else
            int mouseX,
            int mouseY,
            int mouseButton
            //#endif
    ) {
        super.onMouseClickedImpl(
                //#if MC >= 12109
                //$$ click,
                //$$ doubleClick
                //#else
                mouseX,
                mouseY,
                mouseButton
                //#endif
        );

        if (this.dialogHandler != null) {
            this.dialogHandler.openDialog(new GuiVec3iEdit(this.config, this.configGui, this.dialogHandler, null));
        } else {
            GuiBase.openGui(new GuiVec3iEdit(this.config, this.configGui, null, GuiUtils.getCurrentScreen()));
        }

        return true;
    }

    @Override
    public void updateDisplayString() {
        this.displayString = StringUtils.getClampedDisplayStringRenderlen(
                Collections.singletonList(this.config.getX() + ", " + this.config.getY() + ", " + this.config.getZ()),
                this.width - 10, "(", ")");
    }
}
