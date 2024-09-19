package top.hendrixshen.magiclib.impl.malilib.config.gui;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.GuiUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iList;
import top.hendrixshen.magiclib.impl.malilib.config.gui.widget.WidgetListVec3iListEdit;
import top.hendrixshen.magiclib.impl.malilib.config.gui.widget.WidgetVec3iListEditEntry;

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

public class GuiVec3iListEdit extends GuiListBase<Vec3i, WidgetVec3iListEditEntry, WidgetListVec3iListEdit> {
    @Getter
    protected final ConfigVec3iList config;
    protected final MagicConfigGui configGui;
    protected int dialogWidth;
    protected int dialogHeight;
    protected int dialogLeft;
    protected int dialogTop;
    @Nullable
    protected final IDialogHandler dialogHandler;

    public GuiVec3iListEdit(ConfigVec3iList config, MagicConfigGui configGui, @Nullable IDialogHandler dialogHandler, Screen parent) {
        super(0, 0);
        this.config = config;
        this.configGui = configGui;
        this.dialogHandler = dialogHandler;
        this.title = I18n.tr("magiclib.config.gui.title.vec3i_list_edit", config.getName());

        if (this.dialogHandler == null) {
            this.setParent(parent);
        }
    }

    protected void setWidthAndHeight() {
        this.dialogWidth = 400;
        this.dialogHeight = GuiUtils.getScaledWindowHeight() - 90;
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

    //#if MC > 11605
    //$$ @Override
    //$$ public void initGui() {
    //$$     this.setWidthAndHeight();
    //$$     this.centerOnScreen();
    //$$     this.reCreateListWidget();
    //$$     super.initGui();
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
        this.reCreateListWidget();
        this.initGui();
    }
    //#endif

    protected int getBrowserWidth() {
        return this.dialogWidth - 14;
    }

    @Override
    protected int getBrowserHeight() {
        return this.dialogHeight - 30;
    }

    @Override
    protected WidgetListVec3iListEdit createListWidget(int x, int y) {
        return new WidgetListVec3iListEdit(this.dialogLeft + 10, this.dialogTop + 20, this.getBrowserWidth(), this.getBrowserHeight(), this.dialogWidth - 100, this);
    }

    @Override
    public void removed() {
        if (this.getListWidget().wereConfigsModified()) {
            this.getListWidget().applyPendingModifications();
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
