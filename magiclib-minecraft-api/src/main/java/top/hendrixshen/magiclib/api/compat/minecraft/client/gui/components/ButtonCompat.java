package top.hendrixshen.magiclib.api.compat.minecraft.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.util.collect.Provider;

import java.util.function.Supplier;

//#if MC > 11902
//$$ import net.minecraft.client.gui.components.Tooltip;
//#endif

@Environment(EnvType.CLIENT)
public interface ButtonCompat extends Provider<Button> {
    static ButtonCompat.@NotNull Builder builder(@NotNull Component message, @NotNull ButtonCompat.OnPress onPress) {
        return new ButtonCompat.Builder(message, onPress);
    }

    @Environment(EnvType.CLIENT)
    class Builder {
        //#if MC > 11902
        //$$ private final Button.Builder builder;
        //#else
        private final Component message;
        private final ButtonCompat.OnPress onPress;
        private int x;
        private int y;
        private int height = 20;
        //#endif
        private int width = 150;

        public Builder(@NotNull Component message, @NotNull ButtonCompat.OnPress onPress) {
            //#if MC > 11902
            //$$ this.builder = new Button.Builder(message, onPress::onPress);
            //#else
            this.message = message;
            this.onPress = onPress;
            //#endif
        }

        public ButtonCompat.Builder pos(int x, int y) {
            //#if MC > 11902
            //$$ this.builder.pos(x, y);
            //#else
            this.x = x;
            this.y = y;
            //#endif
            return this;
        }

        public ButtonCompat.Builder width(int width) {
            this.width = width;
            //#if MC > 11902
            //$$ this.builder.width(width);
            //#endif
            return this;
        }

        public ButtonCompat.Builder height(int height) {
            return this.size(this.width, height);
        }

        public ButtonCompat.Builder size(int width, int height) {
            //#if MC > 11902
            //$$ this.builder.size(width, height);
            //#else
            this.width = width;
            this.height = height;
            //#endif
            return this;
        }

        public ButtonCompat.Builder bounds(int x, int y, int width, int height) {
            //#if MC > 11902
            //$$ this.builder.bounds(x, y, width, height);
            //#else
            this.pos(x, y).size(width, height);
            //#endif
            return this;
        }


        public ButtonCompat.Builder tooltip(@Nullable
                                            //#if MC > 11902
                                            //$$ Tooltip tooltip
                                            //#else
                                            Object tooltip
                                            //#endif
        ) {
            //#if MC > 11902
            //$$ this.builder.tooltip(tooltip);
            //#endif
            return this;
        }

        public ButtonCompat.Builder createNarration(
                //#if MC > 11902
                //$$ Button.CreateNarration narration
                //#else
                Object narration
                //#endif
        ) {
            //#if MC > 11902
            //$$ this.builder.createNarration(narration);
            //#endif
            return this;
        }

        public Button build() {
            //#if MC > 11902
            //$$ return this.builder.build();
            //#else
            return new Button(
                    this.x,
                    this.y,
                    this.width,
                    this.height,
                    //#if MC > 11502
                    this.message,
                    //#else
                    //$$ this.message.getString(),
                    //#endif
                    this.onPress::onPress
            );
            //#endif
        }
    }


    @Environment(EnvType.CLIENT)
    interface CreateNarration {
        MutableComponentCompat createNarrationMessage(Supplier<MutableComponentCompat> narration);
    }

    @Environment(EnvType.CLIENT)
    interface OnPress {
        void onPress(Button button);
    }
}
