package top.hendrixshen.magiclib.impl.malilib.config.gui.button;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.StringUtils;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iList;
import top.hendrixshen.magiclib.impl.malilib.config.gui.GuiVec3iListEdit;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;

import java.util.stream.Collectors;

public class ConfigButtonVec3iList extends ButtonGeneric {
    private final ConfigVec3iList config;
    private final MagicConfigGui configGui;
    @Nullable
    private final IDialogHandler dialogHandler;

    public ConfigButtonVec3iList(int x, int y, int width, int height, ConfigVec3iList config, MagicConfigGui configGui, @Nullable IDialogHandler dialogHandler) {
        super(x, y, width, height, "");
        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;
        this.updateDisplayString();
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClickedImpl(mouseX, mouseY, mouseButton);

        if (this.dialogHandler != null) {
            this.dialogHandler.openDialog(new GuiVec3iListEdit(this.config, this.configGui, this.dialogHandler, null));
        } else {
            GuiBase.openGui(new GuiVec3iListEdit(this.config, this.configGui, null, GuiUtils.getCurrentScreen()));
        }

        return true;
    }

    @Override
    public void updateDisplayString() {
        this.displayString = StringUtils.getClampedDisplayStringRenderlen(this.config.getVec3iList().stream()
                .map(vec3i -> "(" + vec3i.getX() + ", " + vec3i.getY() + ", " + vec3i.getZ() + ")")
                .collect(Collectors.toList()), this.width - 10, "[ ", " ]");
    }
}
