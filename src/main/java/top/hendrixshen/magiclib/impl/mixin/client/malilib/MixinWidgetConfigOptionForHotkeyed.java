package top.hendrixshen.magiclib.impl.mixin.client.malilib;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOptionBase;
import fi.dy.masa.malilib.gui.widgets.WidgetKeybindSettings;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.config.TranslatableConfigHotkey;
import top.hendrixshen.magiclib.language.I18n;

@Mixin(value = WidgetConfigOption.class, remap = false)
public abstract class MixinWidgetConfigOptionForHotkeyed extends WidgetConfigOptionBase<GuiConfigsBase.ConfigOptionWrapper> {
    public MixinWidgetConfigOptionForHotkeyed(int x, int y, int width, int height, WidgetListConfigOptionsBase<?, ?> parent, GuiConfigsBase.ConfigOptionWrapper entry, int listIndex) {
        super(x, y, width, height, parent, entry, listIndex);
    }

    @Shadow
    @Final
    protected IKeybindConfigGui host;

    @Shadow
    protected abstract void addKeybindResetButton(int x, int y, IKeybind keybind, ConfigButtonKeybind buttonHotkey);

    @Inject(
            method = "addConfigOption",
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11701
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addHotkeyConfigElements(IIILjava/lang/String;Lfi/dy/masa/malilib/hotkeys/IHotkey;)V"
                    //#else
                    //$$ target = "Lfi/dy/masa/malilib/gui/button/ConfigButtonKeybind;<init>(IIIILfi/dy/masa/malilib/hotkeys/IKeybind;Lfi/dy/masa/malilib/gui/interfaces/IKeybindConfigGui;)V"
                    //#endif
            ),
            cancellable = true
    )
    private void addConfigHotkeyedElements(int x, int y, float zLevel, int labelWidth, int configWidth, IConfigBase config, CallbackInfo ci) {
        if (config instanceof TranslatableConfigHotkey) {
            this.magiclib$addConfigHotkeyedElements(x, y, configWidth, (IHotkey)config);
            ci.cancel();
        }
    }

    private void magiclib$addConfigHotkeyedElements(int x, int y, int configWidth, @NotNull IHotkey hotkey) {
        IKeybind keybind = hotkey.getKeybind();
        //#if MC > 11701
        int triggerBtnWidth = (configWidth - 24) / 2;
        //#else
        //$$ int triggerBtnWidth = configWidth / 2 + 1;
        //#endif
        ButtonGeneric triggerButton = new ButtonGeneric(
                x, y, triggerBtnWidth, 20,
                I18n.get("magiclib.gui.button.widget.trigger.text"),
                I18n.get("magiclib.gui.button.widget.trigger.hover", hotkey.getName()));
        this.addButton(triggerButton, ((buttonBase, i) -> {
            IHotkeyCallback callback = ((KeybindMultiAccessor)keybind).getCallback();
            KeyAction activateOn = keybind.getSettings().getActivateOn();
            if (activateOn == KeyAction.BOTH || activateOn == KeyAction.PRESS) {
                callback.onKeyAction(KeyAction.PRESS, keybind);
            }
            if (activateOn == KeyAction.BOTH || activateOn == KeyAction.RELEASE) {
                callback.onKeyAction(KeyAction.RELEASE, keybind);
            }
        }));
        x += triggerBtnWidth + 2;
        //#if MC > 11701
        configWidth -= triggerBtnWidth + 24;
        //#else
        //$$ configWidth -= triggerBtnWidth;
        //#endif
        ConfigButtonKeybind keybindButton = new ConfigButtonKeybind(x, y, configWidth, 20, keybind, this.host);
        x += configWidth + 2;
        this.addWidget(new WidgetKeybindSettings(x, y, 20, 20, keybind, hotkey.getName(), this.parent, this.host.getDialogHandler()));

        //#if MC > 11701
        x += 22;
        //#else
        //$$ x += 25;
        //#endif

        this.addButton(keybindButton, this.host.getButtonPressListener());
        this.addKeybindResetButton(x, y, keybind, keybindButton);
    }
}
