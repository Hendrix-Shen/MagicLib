/*
 * MIT License
 *
 * Copyright 2022 Steven Cao
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;
import top.hendrixshen.magiclib.impl.dev.threadtweak.ThreadTweaker;

/**
 * Reference to <a href="https://github.com/UltimateBoomer/mc-smoothboot/blob/9a519ade89af24aa8b337dfed7d8eb8c0b62ec81/src/main/java/io/github/ultimateboomer/smoothboot/mixin/client/MainMixin.java">SmoothBoot<a/>
 */
@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.TheadTweakPredicate.class
                )
        )
)
@Environment(EnvType.CLIENT)
@Mixin(Main.class)
public class ClientMainMixin {
    @Inject(
            method = "main",
            at = @At(
                    value = "HEAD"
            ),
            remap = false
    )
    private static void onMain(String[] strings, CallbackInfo ci) {
        Thread.currentThread().setPriority(ThreadTweaker.getGamePriority());
    }
}
