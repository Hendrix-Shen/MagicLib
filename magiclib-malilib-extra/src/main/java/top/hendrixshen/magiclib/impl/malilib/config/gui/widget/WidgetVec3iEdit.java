package top.hendrixshen.magiclib.impl.malilib.config.gui.widget;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.gui.ConfigOptionChangeListenerTextField;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.GuiTextFieldInteger;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetContainer;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import org.lwjgl.glfw.GLFW;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.util.IntegerUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

public class WidgetVec3iEdit extends WidgetContainer {
    public static TextFieldMaker textFieldMaker = (x, y, width, height, initialValue, textFieldListenerFunction) -> {
        GuiTextFieldGeneric field = new GuiTextFieldInteger(x, y, width, height, Minecraft.getInstance().font);
        field.setMaxLength(11);
        field.setValue(String.valueOf(initialValue));
        ConfigOptionChangeListenerTextField textFieldListener = textFieldListenerFunction.apply(field);
        return new TextFieldWrapper<>(field, textFieldListener);
    };
    protected final Vec3i defaultValue;
    protected final Consumer<Vec3i> valueApplier;
    protected Vec3i initialValue;
    protected Vec3i lastAppliedValue;
    protected TextFieldWrapper<GuiTextFieldGeneric> xTextField;
    protected TextFieldWrapper<GuiTextFieldGeneric> yTextField;
    protected TextFieldWrapper<GuiTextFieldGeneric> zTextField;

    public WidgetVec3iEdit(int x, int y, int width, int height, Vec3i initialValue, Vec3i defaultValue, Consumer<Vec3i> valueApplier) {
        super(x, y, width, height);
        this.defaultValue = defaultValue;
        this.initialValue = initialValue;
        this.lastAppliedValue = initialValue;
        this.valueApplier = valueApplier;
        this.init(x, y, width, height);
    }

    protected void init(int x, int y, int width, int height) {
        ButtonGeneric resetButton = this.createResetButton(x + width - 34, y);
        this.createTextFields(x, y + 1, width - 36, 17, resetButton);
        this.updateResetButtonState(resetButton);
    }

    protected ButtonGeneric createResetButton(int x, int y) {
        ButtonGeneric resetButton = new ButtonGeneric(x, y, -1, 20, I18n.tr("malilib.gui.button.reset.caps"));
        WidgetVec3iEdit.ListenerResetConfig listenerReset = new WidgetVec3iEdit.ListenerResetConfig(resetButton, this);
        resetButton.setActionListener(listenerReset);
        this.addButton(resetButton, listenerReset);
        return resetButton;
    }

    protected void updateResetButtonState(ButtonGeneric resetButton) {
        resetButton.setEnabled(!this.getVec3iValue().equals(this.defaultValue));
    }

    protected void createTextFields(int x, int y, int width, int height, ButtonGeneric resetButton) {
        int xyTextFieldWidth = width / 3;
        int zTextFieldWidth = width - (xyTextFieldWidth * 2);

        this.addLabel(x, y, 20, 20, 0xFFFFFFFF, "X:");
        this.xTextField = WidgetVec3iEdit.textFieldMaker.make(x + 10, y, xyTextFieldWidth - 20, height, this.initialValue.getX(),
                textField -> new ChangeListenerTextField(textField, resetButton, String.valueOf(this.defaultValue.getX())));
        x += xyTextFieldWidth;

        this.addLabel(x, y, 20, 20, 0xFFFFFFFF, "Y:");
        this.yTextField = WidgetVec3iEdit.textFieldMaker.make(x + 10, y, xyTextFieldWidth - 20, height, this.initialValue.getY(),
                textField -> new ChangeListenerTextField(textField, resetButton, String.valueOf(this.defaultValue.getY())));
        x += xyTextFieldWidth;

        this.addLabel(x, y, 20, 20, 0xFFFFFFFF, "Z:");
        this.zTextField = WidgetVec3iEdit.textFieldMaker.make(x + 10, y, zTextFieldWidth - 20, height, this.initialValue.getZ(),
                textField -> new ChangeListenerTextField(textField, resetButton, String.valueOf(this.defaultValue.getZ())));
    }

    public boolean wasConfigModified() {
        Vec3i newValue = this.getVec3iValue();
        return !newValue.equals(this.initialValue);
    }

    public void applyNewValueToConfig() {
        Vec3i newValue = this.getVec3iValue();

        if (this.valueApplier != null) {
            this.valueApplier.accept(newValue);
        }

        this.lastAppliedValue = newValue;
    }

    public Vec3i getVec3iValue() {
        int x = IntegerUtil.parseIntegerWithBound(this.xTextField.getTextField().getValue());
        int y = IntegerUtil.parseIntegerWithBound(this.yTextField.getTextField().getValue());
        int z = IntegerUtil.parseIntegerWithBound(this.zTextField.getTextField().getValue());
        return new Vec3i(x, y, z);
    }

    protected List<TextFieldWrapper<GuiTextFieldGeneric>> getTextFields() {
        return Lists.newArrayList(this.xTextField, this.yTextField, this.zTextField);
    }

    @Override
    public void render(
            int mouseX,
            int mouseY,
            boolean selected
            //#if MC > 11904
            //$$ , GuiGraphics poseStackOrGuiGraphics
            //#elseif MC > 11502
            , PoseStack poseStackOrGuiGraphics
            //#endif
    ) {
        this.drawTextFields(
                mouseX,
                mouseY
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
        );

        super.render(
                mouseX,
                mouseY,
                selected
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
        );
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        boolean ret = false;

        Function<TextFieldWrapper<? extends GuiTextFieldGeneric>, Boolean> mouseClickImpl = wrapper -> {
            if (wrapper == null) {
                return false;
            }

            return wrapper.getTextField().mouseClicked(mouseX, mouseY, mouseButton);
        };

        ret |= mouseClickImpl.apply(this.xTextField);
        ret |= mouseClickImpl.apply(this.yTextField);
        ret |= mouseClickImpl.apply(this.zTextField);

        for (WidgetBase widget : this.subWidgets) {
            ret |= widget.isMouseOver(mouseX, mouseY) && widget.onMouseClicked(mouseX, mouseY, mouseButton);
        }

        return ret;
    }

    @Override
    public boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        Function<TextFieldWrapper<? extends GuiTextFieldGeneric>, Boolean> keyTypedImpl = wrapper -> {
            if (wrapper == null || !wrapper.isFocused()) {
                return false;
            }

            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                this.applyNewValueToConfig();
                return true;
            }

            return wrapper.onKeyTyped(keyCode, scanCode, modifiers);
        };

        return keyTypedImpl.apply(this.xTextField) ||
                keyTypedImpl.apply(this.yTextField) ||
                keyTypedImpl.apply(this.zTextField);
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        Function<TextFieldWrapper<? extends GuiTextFieldGeneric>, Boolean> charTypedImpl = wrapper -> {
            if (wrapper == null) {
                return false;
            }

            return wrapper.onCharTyped(charIn, modifiers);
        };

        return charTypedImpl.apply(this.xTextField) || charTypedImpl.apply(this.yTextField) || charTypedImpl.apply(this.zTextField);
    }

    protected void drawTextFields(
            int mouseX,
            int mouseY
            //#if MC > 11904
            //$$ , GuiGraphics poseStackOrGuiGraphics
            //#elseif MC > 11502
            , PoseStack poseStackOrGuiGraphics
            //#endif
    ) {
        if (this.xTextField == null || this.yTextField == null || this.zTextField == null) {
            return;
        }

        Consumer<TextFieldWrapper<? extends GuiTextFieldGeneric>> drawTextField = wrapper -> wrapper.getTextField()
                .render(
                        //#if MC > 11502
                        poseStackOrGuiGraphics,
                        //#endif
                        mouseX,
                        mouseY,
                        0.0F
                );

        drawTextField.accept(this.xTextField);
        drawTextField.accept(this.yTextField);
        drawTextField.accept(this.zTextField);
    }

    @FunctionalInterface
    public interface TextFieldMaker {
        TextFieldWrapper<GuiTextFieldGeneric> make(int x, int y, int width, int height, int initialValue,
                                                   Function<GuiTextFieldGeneric, ConfigOptionChangeListenerTextField> textFieldListenerFunction);
    }

    @AllArgsConstructor
    private static class ListenerResetConfig implements IButtonActionListener {
        private final ButtonGeneric resetButton;
        private final WidgetVec3iEdit parent;

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            this.parent.xTextField.getTextField().setValue(String.valueOf(this.parent.defaultValue.getX()));
            this.parent.yTextField.getTextField().setValue(String.valueOf(this.parent.defaultValue.getY()));
            this.parent.zTextField.getTextField().setValue(String.valueOf(this.parent.defaultValue.getZ()));
            this.resetButton.setEnabled(!this.parent.getVec3iValue().equals(this.parent.defaultValue));
        }
    }

    public static class ChangeListenerTextField extends ConfigOptionChangeListenerTextField {
        protected final String defaultValue;

        public ChangeListenerTextField(GuiTextFieldGeneric textField, ButtonBase resetButton, String defaultValue) {
            super(null, textField, resetButton);
            this.defaultValue = defaultValue;
        }

        public boolean onTextChange(GuiTextFieldGeneric textField) {
            this.buttonReset.setEnabled(!this.textField.getValue().equals(this.defaultValue));
            return false;
        }
    }
}
