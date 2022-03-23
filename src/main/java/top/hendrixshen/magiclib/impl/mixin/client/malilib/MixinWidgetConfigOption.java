package top.hendrixshen.magiclib.impl.mixin.client.malilib;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.config.gui.ConfigOptionChangeListenerButton;
import fi.dy.masa.malilib.config.gui.ConfigOptionChangeListenerKeybind;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonBoolean;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOptionBase;
import fi.dy.masa.malilib.gui.widgets.WidgetKeybindSettings;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.config.TranslatableConfigBooleanHotkeyed;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.util.HotkeyedBooleanResetListener;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;


@Dependencies(and = @Dependency(value = "malilib", versionPredicate = "<0.11.4"))
@Mixin(value = WidgetConfigOption.class, remap = false)
public abstract class MixinWidgetConfigOption extends WidgetConfigOptionBase<GuiConfigsBase.ConfigOptionWrapper> {

    @Mutable
    @Final
    @Shadow
    protected KeybindSettings initialKeybindSettings;

    @Final
    @Shadow
    protected GuiConfigsBase.ConfigOptionWrapper wrapper;

    @Final
    @Shadow
    protected IKeybindConfigGui host;

    private boolean initialBoolean;

    public MixinWidgetConfigOption(int x, int y, int width, int height, WidgetListConfigOptionsBase<?, ?> parent, GuiConfigsBase.ConfigOptionWrapper entry, int listIndex) {
        super(x, y, width, height, parent, entry, listIndex);
    }

    @SuppressWarnings("unchecked")
    private static void addKeybindChangeListener(Object obj, ConfigOptionChangeListenerKeybind resetListener) {
        try {
            Field hotkeyChangeListenersField = GuiConfigsBase.class.getDeclaredField("hotkeyChangeListeners");
            hotkeyChangeListenersField.setAccessible(true);
            List<ConfigOptionChangeListenerKeybind> hotkeyChangeListeners = (List<ConfigOptionChangeListenerKeybind>) hotkeyChangeListenersField.get(obj);
            hotkeyChangeListeners.add(resetListener);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void initConfigBooleanHotkeyed(int x, int y, int width, int height, int labelWidth, int configWidth,
                                           GuiConfigsBase.ConfigOptionWrapper wrapper, int listIndex, IKeybindConfigGui host,
                                           WidgetListConfigOptionsBase<?, ?> parent, CallbackInfo ci) {
        if (wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG) {
            IConfigBase config = wrapper.getConfig();
            if (initialStringValue == null && config instanceof TranslatableConfigBooleanHotkeyed) {
                ConfigBooleanHotkeyed booleanHotkey = (ConfigBooleanHotkeyed) config;
                this.initialBoolean = booleanHotkey.getBooleanValue();
                this.initialStringValue = booleanHotkey.getKeybind().getStringValue();
                this.initialKeybindSettings = booleanHotkey.getKeybind().getSettings();
            }
        }
    }

    @Inject(method = "wasConfigModified", at = @At(value = "RETURN"), cancellable = true)
    private void wasConfigBooleanHotkeyedModified(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && this.wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG) {
            IConfigBase config = this.wrapper.getConfig();
            if (config instanceof TranslatableConfigBooleanHotkeyed) {
                ConfigBooleanHotkeyed booleanHotkey = (ConfigBooleanHotkeyed) config;
                IKeybind keybind = booleanHotkey.getKeybind();
                cir.setReturnValue(this.initialBoolean != booleanHotkey.getBooleanValue() || !Objects.equals(this.initialStringValue, keybind.getStringValue()) || !this.initialKeybindSettings.equals(keybind.getSettings()));
            }
        }
    }

    @Inject(method = "addConfigOption", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addConfigButtonEntry(IILfi/dy/masa/malilib/config/IConfigResettable;Lfi/dy/masa/malilib/gui/button/ButtonBase;)V", ordinal = 0), cancellable = true)
    private void addConfigBooleanHotkeyed(int x, int y, float zLevel, int labelWidth, int configWidth, IConfigBase config, CallbackInfo ci) {
        if (config instanceof TranslatableConfigBooleanHotkeyed) {
            ConfigBooleanHotkeyed hotkeyedBoolean = (ConfigBooleanHotkeyed) config;
            IKeybind keybind = hotkeyedBoolean.getKeybind();
            this.addBooleanAndHotkeyWidgets(x, y, configWidth, hotkeyedBoolean, hotkeyedBoolean, keybind);
            ci.cancel();
        }
    }

    protected void addBooleanAndHotkeyWidgets(int x, int y, int configWidth,
                                              ConfigBooleanHotkeyed resettableConfig,
                                              IConfigBoolean booleanConfig,
                                              IKeybind keybind) {
        int booleanBtnWidth = 60;
        ConfigButtonBoolean booleanButton = new ConfigButtonBoolean(x, y, booleanBtnWidth, 20, booleanConfig);
        x += booleanBtnWidth + 2;
        configWidth -= booleanBtnWidth + 2 + 22;

        ConfigButtonKeybind keybindButton = new ConfigButtonKeybind(x, y, configWidth, 20, keybind, this.host);
        x += configWidth + 2;

        this.addWidget(new WidgetKeybindSettings(x, y, 20, 20, keybind, booleanConfig.getName(), this.parent, this.host.getDialogHandler()));
        x += 22;

        ButtonGeneric resetButton = this.createResetButton(x, y, resettableConfig);

        ConfigOptionChangeListenerButton booleanChangeListener = new ConfigOptionChangeListenerButton(resettableConfig, resetButton, null);
        ConfigOptionChangeListenerKeybind resetListener = new HotkeyedBooleanResetListener(resettableConfig, booleanButton, keybindButton, resetButton, this.host);
        addKeybindChangeListener(this.host, resetListener);

        //this.host.addKeybindChangeListener(resetListener::updateButtons);

        this.addButton(booleanButton, booleanChangeListener);
        this.addButton(keybindButton, this.host.getButtonPressListener());
        this.addButton(resetButton, resetListener);
    }
}
