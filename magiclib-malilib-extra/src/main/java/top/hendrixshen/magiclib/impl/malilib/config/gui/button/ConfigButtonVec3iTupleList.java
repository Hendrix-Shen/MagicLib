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

import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iTupleList;
import top.hendrixshen.magiclib.impl.malilib.config.gui.GuiVec3iTupleListEdit;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;

import java.util.stream.Collectors;

public class ConfigButtonVec3iTupleList extends ButtonGeneric {
    private final ConfigVec3iTupleList config;
    private final MagicConfigGui configGui;
    @Nullable
    private final IDialogHandler dialogHandler;

    public ConfigButtonVec3iTupleList(int x, int y, int width, int height, ConfigVec3iTupleList config, MagicConfigGui configGui, @Nullable IDialogHandler dialogHandler) {
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
            this.dialogHandler.openDialog(new GuiVec3iTupleListEdit(this.config, this.configGui, this.dialogHandler, null));
        } else {
            GuiBase.openGui(new GuiVec3iTupleListEdit(this.config, this.configGui, null, GuiUtils.getCurrentScreen()));
        }

        return true;
    }

    @Override
    public void updateDisplayString() {
        this.displayString = StringUtils.getClampedDisplayStringRenderlen(this.config.getVec3iTupleList().stream()
                .map(tuple -> "(<"
                        + tuple.getFirstVec3i().getX() + ", " + tuple.getFirstVec3i().getY() + ", " + tuple.getFirstVec3i().getZ()
                        + ">, "
                        + "<"
                        + tuple.getSecondVec3i().getX() + ", " + tuple.getSecondVec3i().getY() + ", " + tuple.getSecondVec3i().getZ()
                        + ">)")
                .collect(Collectors.toList()), this.width - 10, "[ ", " ]");
    }
}
