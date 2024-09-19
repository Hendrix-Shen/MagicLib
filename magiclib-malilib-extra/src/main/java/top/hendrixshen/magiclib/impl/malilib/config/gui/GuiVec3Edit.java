package top.hendrixshen.magiclib.impl.malilib.config.gui;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.render.RenderUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3i;
import top.hendrixshen.magiclib.impl.malilib.config.gui.widget.WidgetVec3iEdit;

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

public class GuiVec3Edit extends GuiBase {
    @Getter
    protected final ConfigVec3i config;
    protected final MagicConfigGui configGui;
    protected int dialogWidth;
    protected int dialogHeight;
    protected int dialogLeft;
    protected int dialogTop;
    @Nullable
    protected final IDialogHandler dialogHandler;
    protected WidgetVec3iEdit widget;

    public GuiVec3Edit(ConfigVec3i config, MagicConfigGui configGui, @Nullable IDialogHandler dialogHandler, Screen parent) {
        super();
        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;
        this.title = I18n.tr("magiclib.config.gui.title.vec3i_edit", config.getName());

        if (this.dialogHandler == null) {
            this.setParent(parent);
        }
    }

    protected void setWidthAndHeight() {
        this.dialogWidth = 300;
        this.dialogHeight = 50;
    }

    protected void centerOnScreen() {
        if (this.getParent() != null) {
            this.dialogLeft = this.getParent().width / 2 - this.dialogWidth / 2;
            this.dialogTop = this.getParent().height / 2 - this.dialogHeight / 2;
        } else {
            this.dialogLeft = 20;
            this.dialogTop = 20;
        }
    }

    protected void reCreateWidget() {
        this.widget = this.createListWidget();
        this.clearWidgets();
        this.addWidget(this.widget);
    }

    //#if MC > 11605
    //$$ @Override
    //$$ public void initGui() {
    //$$     this.setWidthAndHeight();
    //$$     this.centerOnScreen();
    //$$     super.initGui();
    //$$     this.reCreateWidget();
    //$$ }
    //#else
    @Override
    public void init(Minecraft mc, int width, int height) {
        if (this.getParent() != null) {
            this.getParent().init(mc, width, height);
        }

        super.init(mc, width, height);
        this.setWidthAndHeight();
        this.centerOnScreen();
        this.initGui();
        this.reCreateWidget();
    }
    //#endif

    protected WidgetVec3iEdit createListWidget() {
        return new WidgetVec3iEdit(this.dialogLeft + 10, this.dialogTop + 20,
                this.dialogWidth - 14, this.dialogHeight - 30,
                this.config.getVec3i(), this.config.getDefaultVec3iValue(), this.config::setVec3i);
    }

    @Override
    public void removed() {
        if (this.widget.wasConfigModified()) {
            this.widget.applyNewValueToConfig();
            ConfigManager.getInstance().onConfigsChanged(this.configGui.getModId());
        }

        super.removed();
    }

    @Override
    public void render(
            //#if MC > 11904
            //$$ GuiGraphics poseStackOrGuiGraphics,
            //#elseif MC > 11502
            PoseStack poseStackOrGuiGraphics,
            //#endif
            int mouseX,
            int mouseY,
            float partialTicks
    ) {
        if (this.getParent() != null) {
            this.getParent().render(
                    //#if MC > 11502
                    poseStackOrGuiGraphics,
                    //#endif
                    mouseX,
                    mouseY,
                    partialTicks
            );
        }

        super.render(
                //#if MC > 11502
                poseStackOrGuiGraphics,
                //#endif
                mouseX,
                mouseY,
                partialTicks
        );
    }

    @Override
    protected void drawScreenBackground(int mouseX, int mouseY) {
        RenderUtils.drawOutlinedBox(this.dialogLeft, this.dialogTop, this.dialogWidth, this.dialogHeight, 0xFF000000, 0xFF999999);
    }

    @Override
    protected void drawTitle(
            //#if MC > 11904
            //$$ GuiGraphics poseStackOrGuiGraphics,
            //#elseif MC > 11502
            PoseStack poseStackOrGuiGraphics,
            //#endif
            int mouseX,
            int mouseY,
            float partialTicks
    ) {
        this.drawStringWithShadow(
                //#if MC > 11502
                poseStackOrGuiGraphics,
                //#endif
                this.title,
                this.dialogLeft + 10,
                this.dialogTop + 6,
                -1
        );
    }

    @Override
    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.dialogHandler != null) {
            this.dialogHandler.closeDialog();
            return true;
        } else {
            return super.onKeyTyped(keyCode, scanCode, modifiers);
        }
    }
}
