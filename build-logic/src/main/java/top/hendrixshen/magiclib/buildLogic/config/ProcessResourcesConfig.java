package top.hendrixshen.magiclib.buildLogic.config;

import groovy.lang.Closure;
import groovy.lang.GString;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.language.jvm.tasks.ProcessResources;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ProcessResourcesConfig implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project project) {
        project.afterEvaluate(p -> {
            p.getTasks().withType(ProcessResources.class).configureEach(task -> {
                if (!"processResources".equals(task.getName())) {
                    return;
                }

                this.addInputProperty(p, task);
                this.filesMatching(p, task);
                this.addExtraFile(p, task);
            });
        });
    }

    public void addInputProperty(Project project, ProcessResources task) {
        Project parent = project.getParent();
        assert parent != null;
        Project root = project.getRootProject();
        Map<String, Object> map = new HashMap<>();
        map.put("mod_alias", parent.property("mod.id"));
        map.put("mod_description", parent.property("mod.description"));
        map.put("mod_homepage", parent.property("mod.homepage"));
        map.put("mod_id", parent.property("mod.id").toString().replace("-", "_"));
        map.put("mod_license", parent.property("mod.license"));
        map.put("mod_name", parent.property("mod.name"));
        map.put("mod_sources", parent.property("mod.sources"));
        @SuppressWarnings("unchecked")
        Closure<GString> getVersionWithCommitHash = (Closure<GString>) project.getRootProject().getExtensions().getExtraProperties().getProperties().get("getVersionWithCommitHash");
        map.put("mod_version", getVersionWithCommitHash.call(project.getParent()));
        map.put("root_mod_id", root.property("mod.id"));
        map.put("root_mod_name", root.property("mod.name"));
        map.put("root_mod_description", root.property("mod.description"));
        task.getInputs().properties(map);
    }

    public void filesMatching(Project project, ProcessResources task) {
        switch (project.getName()) {
            case "fabric":
                this.filesMatching(project, task, "fabric.mod.json");
                break;
            case "forge":
                this.filesMatching(project, task, "META-INF/mods.toml");
                break;
            case "neoforge":
                this.filesMatching(project, task, "META-INF/neoforge.mods.toml");
                break;
        }
    }

    public void filesMatching(Project project, ProcessResources task, String pattern) {
        task.filesMatching(pattern, details -> details.expand(task.getInputs().getProperties()));
    }

    public void addExtraFile(Project project, ProcessResources task) {
        Path rootDir = project.getRootDir().toPath();
        Project parent = project.getParent();
        assert parent != null;
        task.from(rootDir.resolve("LICENSE"));
        task.from(rootDir.resolve("icon.png"), spec  -> {
            if ("fabric".equals(project.getName())) {
                spec.into("assets/" + parent.property("mod.id"));
            }
        });
    }
}
