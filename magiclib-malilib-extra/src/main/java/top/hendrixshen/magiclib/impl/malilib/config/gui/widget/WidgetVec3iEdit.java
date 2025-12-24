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
import org.lwjgl.glfw.GLFW;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12111
//$$ import fi.dy.masa.malilib.render.GuiContext;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12109
//$$ import net.minecraft.client.input.CharacterEvent;
//$$ import net.minecraft.client.input.KeyEvent;
//$$ import net.minecraft.client.input.MouseButtonEvent;
//#endif

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.util.IntegerUtil;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class WidgetVec3iEdit extends WidgetContainer {
    public static TextFieldMaker textFieldMaker = (x, y, width, height, initialValue, textFieldListenerFunction) -> {
        GuiTextFieldGeneric field = new GuiTextFieldInteger(x, y, width, height, Minecraft.getInstance().font);
        field.setMaxLength(11);
        field.setValue(String.valueOf(initialValue));
        ConfigOptionChangeListenerTextField textFieldListener = textFieldListenerFunction.apply(field);
        return new TextFieldWrapper<>(field, textFieldListener);
    };

    private final String xLabel;
    private final String yLabel;
    private final String zLabel;
    protected final Vec3i defaultValue;
    protected final Consumer<Vec3i> valueApplier;
    protected Vec3i initialValue;
    protected Vec3i lastAppliedValue;
    protected TextFieldWrapper<GuiTextFieldGeneric> xTextField;
    protected TextFieldWrapper<GuiTextFieldGeneric> yTextField;
    protected TextFieldWrapper<GuiTextFieldGeneric> zTextField;

    public WidgetVec3iEdit(int x, int y, int width, int height, Vec3i initialValue, Vec3i defaultValue,
                           Consumer<Vec3i> valueApplier) {
        this(x, y, width, height, initialValue, defaultValue, valueApplier, "x:", "y:", "z:");
    }

    public WidgetVec3iEdit(int x, int y, int width, int height, Vec3i initialValue, Vec3i defaultValue,
                           Consumer<Vec3i> valueApplier, String xLabel, String yLabel, String zLabel) {
        super(x, y, width, height);
        this.defaultValue = defaultValue;
        this.initialValue = initialValue;
        this.lastAppliedValue = initialValue;
        this.valueApplier = valueApplier;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.zLabel = zLabel;
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

        this.addLabel(x, y, 20, 20, 0xFFFFFFFF, this.xLabel);
        this.xTextField = WidgetVec3iEdit.textFieldMaker.make(x + 15, y, xyTextFieldWidth - 20, height, this.initialValue.getX(),
                textField -> new ChangeListenerTextField(textField, resetButton, String.valueOf(this.defaultValue.getX())));
        x += xyTextFieldWidth;

        this.addLabel(x, y, 20, 20, 0xFFFFFFFF, this.yLabel);
        this.yTextField = WidgetVec3iEdit.textFieldMaker.make(x + 15, y, xyTextFieldWidth - 20, height, this.initialValue.getY(),
                textField -> new ChangeListenerTextField(textField, resetButton, String.valueOf(this.defaultValue.getY())));
        x += xyTextFieldWidth;

        this.addLabel(x, y, 20, 20, 0xFFFFFFFF, this.zLabel);
        this.zTextField = WidgetVec3iEdit.textFieldMaker.make(x + 15, y, zTextFieldWidth - 20, height, this.initialValue.getZ(),
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
    //#if MC >= 12106
    //$$ public void render(
    //$$         //#if MC >= 12111
    //$$         //$$ GuiContext guiGraphics,
    //$$         //#else
    //$$         GuiGraphics guiGraphics,
    //$$         //#endif
    //$$         int mouseX,
    //$$         int mouseY,
    //$$         boolean selected
    //$$ ) {
    //$$     this.drawTextFields(mouseX, mouseY, guiGraphics);
    //$$     super.render(guiGraphics, mouseX, mouseY, selected);
    //$$ }
    //#else
    public void render(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            int mouseX,
            int mouseY,
            boolean selected
            //#if MC > 11904
            //$$ , GuiGraphics poseStackOrGuiGraphics
            //#elseif MC > 11502
            , PoseStack poseStackOrGuiGraphics
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        this.drawTextFields(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                mouseX,
                mouseY
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        );

        super.render(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                mouseX,
                mouseY,
                selected
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        );
    }
    //#endif

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
        boolean ret = false;

        Function<TextFieldWrapper<? extends GuiTextFieldGeneric>, Boolean> mouseClickImpl = wrapper -> {
            if (wrapper == null) {
                return false;
            }

            return wrapper.getTextField().mouseClicked(
                    //#if MC >= 12109
                    //$$ click,
                    //$$ doubleClick
                    //#else
                    mouseX,
                    mouseY,
                    mouseButton
                    //#endif
            );
        };

        ret |= mouseClickImpl.apply(this.xTextField);
        ret |= mouseClickImpl.apply(this.yTextField);
        ret |= mouseClickImpl.apply(this.zTextField);

        for (WidgetBase widget : this.subWidgets) {
            //#if MC >= 12109
            //$$ int mouseX = (int) click.x();
            //$$ int mouseY = (int) click.y();
            //#endif
            ret |= widget.isMouseOver(mouseX, mouseY) && widget.onMouseClicked(
                    //#if MC >= 12109
                    //$$ click,
                    //$$ doubleClick
                    //#else
                    mouseX,
                    mouseY,
                    mouseButton
                    //#endif
            );
        }

        return ret;
    }

    @Override
    public boolean onKeyTypedImpl(
            //#if MC >= 12109
            //$$ KeyEvent input
            //#else
            int keyCode,
            int scanCode,
            int modifiers
            //#endif
    ) {
        Function<TextFieldWrapper<? extends GuiTextFieldGeneric>, Boolean> keyTypedImpl = wrapper -> {
            if (wrapper == null || !wrapper.isFocused()) {
                return false;
            }

            //#if MC >= 12109
            //$$ int keyCode = input.key();
            //#endif

            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                this.applyNewValueToConfig();
                return true;
            }

            return wrapper.onKeyTyped(
                    //#if MC >= 12109
                    //$$ input
                    //#else
                    keyCode,
                    scanCode,
                    modifiers
                    //#endif
            );
        };

        return keyTypedImpl.apply(this.xTextField)
                || keyTypedImpl.apply(this.yTextField)
                || keyTypedImpl.apply(this.zTextField);
    }

    @Override
    protected boolean onCharTypedImpl(
            //#if MC >= 12109
            //$$ CharacterEvent input
            //#else
            char charIn,
            int modifiers
            //#endif
    ) {
        Function<TextFieldWrapper<? extends GuiTextFieldGeneric>, Boolean> charTypedImpl = wrapper -> {
            if (wrapper == null) {
                return false;
            }

            return wrapper.onCharTyped(
                    //#if MC >= 12109
                    //$$ input
                    //#else
                    charIn,
                    modifiers
                    //#endif
            );
        };

        return charTypedImpl.apply(this.xTextField) || charTypedImpl.apply(this.yTextField) || charTypedImpl.apply(this.zTextField);
    }

    protected void drawTextFields(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            int mouseX,
            int mouseY
            //#if MC > 11904
            //$$ , GuiGraphics poseStackOrGuiGraphics
            //#elseif MC > 11502
            , PoseStack poseStackOrGuiGraphics
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        if (this.xTextField == null || this.yTextField == null || this.zTextField == null) {
            return;
        }

        Consumer<TextFieldWrapper<? extends GuiTextFieldGeneric>> drawTextField = wrapper -> wrapper.getTextField()
                .render(
                        // CHECKSTYLE.OFF: NoWhitespaceBefore
                        // CHECKSTYLE.OFF: SeparatorWrap
                        //#if MC > 11502
                        poseStackOrGuiGraphics,
                        //#endif
                        mouseX,
                        mouseY,
                        0.0F
                        // CHECKSTYLE.ON: SeparatorWrap
                        // CHECKSTYLE.ON: NoWhitespaceBefore
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
            BiConsumer<Function<WidgetVec3iEdit, TextFieldWrapper<GuiTextFieldGeneric>>, Function<Vec3i, Integer>> transformer =
                    (wrapperTransformer, vec3iTransformer) ->
                            wrapperTransformer.apply(this.parent).getTextField()
                                    .setValue(String.valueOf(vec3iTransformer.apply(this.parent.defaultValue)));

            transformer.accept(w -> w.xTextField, Vec3i::getX);
            transformer.accept(w -> w.yTextField, Vec3i::getY);
            transformer.accept(w -> w.zTextField, Vec3i::getZ);
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
