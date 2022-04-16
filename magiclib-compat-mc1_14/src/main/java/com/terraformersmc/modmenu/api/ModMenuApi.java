package com.terraformersmc.modmenu.api;

import net.minecraft.client.gui.screens.Screen;

import java.util.function.Function;

public interface ModMenuApi extends io.github.prospector.modmenu.api.ModMenuApi {
    ConfigScreenFactory<?> getModConfigScreenFactory();

    @Override
    default Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return screen -> getModConfigScreenFactory().create(screen);
    }
}
