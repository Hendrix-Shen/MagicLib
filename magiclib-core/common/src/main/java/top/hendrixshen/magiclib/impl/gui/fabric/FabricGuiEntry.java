/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.hendrixshen.magiclib.impl.gui.fabric;

import top.hendrixshen.magiclib.impl.gui.fabric.FabricStatusTree.FabricBasicButtonType;
import top.hendrixshen.magiclib.impl.gui.fabric.FabricStatusTree.FabricStatusTab;
import top.hendrixshen.magiclib.impl.gui.fabric.FabricStatusTree.FabricTreeWarningLevel;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.util.fabric.LoaderUtil;
import top.hendrixshen.magiclib.util.fabric.UrlUtil;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Reference to <a href="https://github.com/FabricMC/fabric-loader/blob/1a833267b54beea5eb635222df4af149f8a6a1d6/src/main/java/net/fabricmc/loader/impl/gui/FabricGuiEntry.java">FabricLoader<a/>
 * <p>
 *
 * The main entry point for all fabric-based stuff.
 */
public final class FabricGuiEntry {
    /**
     * Opens the given {@link FabricStatusTree} in a new swing window.
     *
     * @throws Exception if something went wrong while opening the window.
     */
    public static void open(FabricStatusTree tree) throws Exception {
        if (!LoaderUtil.hasMacOs()) {
            FabricMainWindow.open(tree, true);
        } else {
            FabricGuiEntry.openForked(tree);
        }
    }

    private static void openForked(FabricStatusTree tree) throws IOException, InterruptedException {
        Path javaBinDir = LoaderUtil.normalizePath(Paths.get(System.getProperty("java.home"), "bin"));
        String[] executables = {"javaw.exe", "java.exe", "java"};
        Path javaPath = null;

        for (String executable : executables) {
            Path path = javaBinDir.resolve(executable);

            if (Files.isRegularFile(path)) {
                javaPath = path;
                break;
            }
        }

        if (javaPath == null) {
            throw new RuntimeException("can't find java executable in " + javaBinDir);
        }

        Process process = new ProcessBuilder(javaPath.toString(), "-Xmx100M", "-cp",
                UrlUtil.LOADER_CODE_SOURCE.toString(), FabricGuiEntry.class.getName())
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();

        final Thread shutdownHook = new Thread(process::destroy);
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        try (DataOutputStream os = new DataOutputStream(process.getOutputStream())) {
            tree.writeTo(os);
        }

        int rVal = process.waitFor();
        Runtime.getRuntime().removeShutdownHook(shutdownHook);

        if (rVal != 0) {
            throw new IOException("subprocess exited with code " + rVal);
        }
    }

    public static void main(String[] args) throws Exception {
        FabricStatusTree tree = new FabricStatusTree(new DataInputStream(System.in));
        FabricMainWindow.open(tree, true);
        System.exit(0);
    }

    /**
     * @param exitAfter If true then this will call {@link System#exit(int)} after showing the gui, otherwise this will
     *                  return normally.
     */
    public static void displayCriticalError(Throwable exception, boolean exitAfter) {
        MagicLib.getLogger().error("A critical error occurred", exception);
        FabricGuiEntry.displayError(I18n.tr("fabric.gui.error.header"), exception, exitAfter);
    }

    public static void displayError(String mainText, Throwable exception, boolean exitAfter) {
        FabricGuiEntry.displayError(mainText, exception, tree -> {
            StringWriter error = new StringWriter();
            error.append(mainText);

            if (exception != null) {
                error.append(System.lineSeparator());
                exception.printStackTrace(new PrintWriter(error));
            }

            tree.addButton(I18n.tr("fabric.gui.button.copyError"),
                    FabricBasicButtonType.CLICK_MANY).withClipboard(error.toString());
        }, exitAfter);
    }

    public static void displayError(String mainText, Throwable exception, Consumer<FabricStatusTree> treeCustomiser, boolean exitAfter) {
        if (!GraphicsEnvironment.isHeadless()) {
            String title = "MagicLib";
            FabricStatusTree tree = new FabricStatusTree(title, mainText);
            FabricStatusTab crashTab = tree.addTab(I18n.tr("fabric.gui.tab.crash"));

            if (exception != null) {
                crashTab.node.addCleanedException(exception);
            } else {
                crashTab.node.addMessage(I18n.tr("fabric.gui.error.missingException"), FabricTreeWarningLevel.NONE);
            }

            // Maybe add an "open mods folder" button?
            // or should that be part of the main tree's right-click menu?
            tree.addButton(I18n.tr("fabric.gui.button.exit"), FabricBasicButtonType.CLICK_ONCE).makeClose();
            treeCustomiser.accept(tree);

            try {
                FabricGuiEntry.open(tree);
            } catch (Exception e) {
                if (exitAfter) {
                    MagicLib.getLogger().error("Failed to open the error gui!", e);
                } else {
                    throw new RuntimeException("Failed to open the error gui!", e);
                }
            }
        }

        if (exitAfter) {
            System.exit(1);
        }
    }
}
