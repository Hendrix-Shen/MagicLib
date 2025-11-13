package top.hendrixshen.magiclib.buildLogic.config;

import org.gradle.api.Project;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.jetbrains.annotations.NotNull;

import net.fabricmc.loom.api.LoomGradleExtensionAPI;
import net.fabricmc.loom.util.ModPlatform;
import top.hendrixshen.magiclib.buildLogic.Util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessResourcesMCConfig extends ProcessResourcesConfig {
    private static final Map<String, List<ModPlatform>> FILE_PATTERNS = Util.make(() -> {
        Map<String, List<ModPlatform>> res = new HashMap<>();
        List<ModPlatform> list = new ArrayList<>();
        list.add(ModPlatform.FABRIC);
        res.put("fabric.mod.json", list);
        list = new ArrayList<>();
        list.add(ModPlatform.FORGE);
        list.add(ModPlatform.NEOFORGE);
        res.put("META-INF", list);
        res.put("META-INF/mods.toml", list);
        list = new ArrayList<>();
        list.add(ModPlatform.NEOFORGE);
        res.put("META-INF/neoforge.mods.toml", list);
        return res;
    });

    private ModPlatform modPlatform;

    @Override
    public void apply(@NotNull Project project) {
        // We need to wait for the configuration to evaluate before we can release magic
        // in order to inject subproject's own input configuration.
        project.afterEvaluate(super::apply);
        this.modPlatform = project.getExtensions().getByType(LoomGradleExtensionAPI.class).getPlatform().get();
    }

    @Override
    public void addInputProperty(Project project, ProcessResources task) {
        super.addInputProperty(project, task);
        Map<String, Object> map = new HashMap<>();
        map.put("minecraft_dependency", project.property("dependencies.minecraft_dependency"));
        task.getInputs().properties(map);
    }

    @Override
    public void filesMatching(Project project, ProcessResources task) {
        ProcessResourcesMCConfig.FILE_PATTERNS.forEach((pattern, platforms) -> {
            if (platforms.stream().anyMatch(platform -> platform == this.modPlatform)) {
                this.filesMatching(project, task, pattern);
            } else {
                task.exclude(pattern);
            }
        });
    }

    @Override
    public void addExtraFile(Project project, ProcessResources task) {
        Path rootDir = project.getRootDir().toPath();
        Project parent = project.getParent();
        assert parent != null;
        task.from(rootDir.resolve("LICENSE"));
        task.from(rootDir.resolve("icon.png"), spec -> {
            if (this.modPlatform == ModPlatform.FABRIC) {
                spec.into("assets/" + parent.property("mod.id"));
            }
        });
    }
}
