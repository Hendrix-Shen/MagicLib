package top.hendrixshen.magiclib.buildLogic.config;

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import groovy.lang.Closure;
import groovy.lang.GString;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.api.artifacts.VersionCatalog;
import org.gradle.api.artifacts.VersionCatalogsExtension;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.plugins.BasePluginExtension;
import org.gradle.api.plugins.JavaLibraryPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.provider.Provider;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskCollection;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.javadoc.Javadoc;
import org.gradle.external.javadoc.CoreJavadocOptions;
import org.gradle.jvm.tasks.Jar;
import org.gradle.plugins.signing.Sign;
import org.gradle.plugins.signing.SigningExtension;
import top.hendrixshen.replacetoken.ReplaceTokenExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoreProjectBuildLogic implements Plugin<Project> {
    private Project project;

    @Override
    public void apply(Project project) {
        this.project = project;
        this.applyPlugins();
        this.configurations();
        this.commonRepositories();
        this.commonDependencies();
        this.extensionConfigure();
        this.taskConfigure();
    }

    private void applyPlugins() {
        this.project.getPluginManager();
        PluginContainer plugins = this.project.getPlugins();
        plugins.apply("checkstyle");
        plugins.apply("java-library");
        plugins.apply("maven-publish");
        plugins.apply("signing");
    }

    private void configurations() {
        ConfigurationContainer configurations = this.project.getConfigurations();
        NamedDomainObjectProvider<Configuration> shade = configurations.register("shade");
        configurations.register("shadeOnly");
        configurations.getByName("compileOnly").extendsFrom(shade.get());
    }

    private void commonRepositories() {
        RepositoryHandler repositories = this.project.getRepositories();
        repositories.add(repositories.mavenLocal());
        repositories.maven(repo -> {
            repo.setName("NyanMaven Mirror");
            repo.setUrl("https://maven.hendrixshen.moe/mirror");
        });
    }

    private void commonDependencies() {
        VersionCatalogsExtension catalogsExtension = project.getExtensions().getByType(VersionCatalogsExtension.class);
        VersionCatalog libs = catalogsExtension.named("libs");
        DependencyHandler dependencies = project.getDependencies();

        // ASM
        dependencies.add("compileOnly", libs.findBundle("asm").orElseThrow(RuntimeException::new));

        // Lombok
        Provider<MinimalExternalModuleDependency> lombok = libs.findLibrary("lombok").orElseThrow(RuntimeException::new);
        dependencies.add("annotationProcessor", lombok);
        dependencies.add("compileOnly", lombok);

        // Mixin
        dependencies.add("compileOnly", libs.findLibrary("spongepowered.mixin").orElseThrow(RuntimeException::new));

        // Mixin Extras
        MinimalExternalModuleDependency mixinExtras = libs.findLibrary("mixinextras-common").orElseThrow(RuntimeException::new).get();
        Closure<Void> disableTransit = new Closure<Void>(this) {
            public void doCall(ExternalModuleDependency dependency) {
                dependency.setTransitive(false);
            }
        };
        dependencies.add("annotationProcessor", mixinExtras, disableTransit);
        dependencies.add("shade", mixinExtras, disableTransit);

        // Other
        dependencies.add("compileOnly", libs.findLibrary("commons.lang3").orElseThrow(RuntimeException::new));
        dependencies.add("compileOnly", libs.findLibrary("guava").orElseThrow(RuntimeException::new));
        dependencies.add("compileOnly", libs.findLibrary("gson").orElseThrow(RuntimeException::new));
        dependencies.add("compileOnly", libs.findLibrary("jetbrains.annotations").orElseThrow(RuntimeException::new));
        dependencies.add("compileOnly", libs.findLibrary("log4j.api").orElseThrow(RuntimeException::new));

        if (!"common".equals(this.project.getName())) {
            dependencies.add("shade", this.project.project(":magiclib-core:common"));
        }
    }

    private void extensionConfigure() {
        this.baseConfigure();
        this.checkstyleConfigure();
        this.javaConfigure();
        this.replaceTokenConfigure();
        this.signingConfigure();
        this.publishingConfigure();
    }

    private void baseConfigure() {
        assert this.project.getParent() != null;
        Map<String, ?> parentProperties = this.project.getParent().getProperties();
        BasePluginExtension baseExtension = this.project.getExtensions().getByType(BasePluginExtension.class);
        baseExtension.getArchivesName().set(parentProperties.get("mod.archives_base_name") + "-" + this.project.getName());
        this.project.setGroup(parentProperties.get("mod.maven_group"));
        @SuppressWarnings("unchecked")
        Closure<GString> getModVersion = (Closure<GString>) this.project.getRootProject().getExtensions().getExtraProperties().get("getModVersion");
        assert getModVersion != null;
        this.project.setVersion(getModVersion.call(this.project.getParent()));
    }

    private void checkstyleConfigure() {
        this.project.getExtensions().configure(CheckstyleExtension.class, checkstyleExtension -> {
            VersionCatalogsExtension catalogsExtension = project.getExtensions().getByType(VersionCatalogsExtension.class);
            VersionCatalog libs = catalogsExtension.named("libs");
            checkstyleExtension.setConfigFile(this.project.getRootDir().toPath().resolve("checkstyle.xml").toFile());
            checkstyleExtension.setToolVersion(libs.findVersion("checkstyle").orElseThrow(RuntimeException::new).getRequiredVersion());
        });
    }

    private void javaConfigure() {
        this.project.getExtensions().configure(JavaPluginExtension.class, (javaExtension -> {
            javaExtension.withSourcesJar();
            javaExtension.withJavadocJar();
        }));
    }

    private void replaceTokenConfigure() {
        this.project.getExtensions().configure(ReplaceTokenExtension.class, replaceTokenExtension -> {
            assert this.project.getParent() != null;
            List<SourceSet> sourceSets = new ArrayList<>();
            sourceSets.add(this.project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets().getByName("main"));
            replaceTokenExtension.getTargetSourceSets().set(sourceSets);
            Map<String, ?> parentProperties = this.project.getParent().getProperties();
            Map<String, ?> rootProperties = this.project.getRootProject().getProperties();
            @SuppressWarnings("unchecked")
            Closure<GString> getVersionWithCommitHash = (Closure<GString>) this.project.getRootProject().getExtensions().getExtraProperties().getProperties().get("getVersionWithCommitHash");
            replaceTokenExtension.replace("@MOD_IDENTIFIER@", parentProperties.get("mod.id").toString().replace("-", "_"));
            replaceTokenExtension.replace("@MOD_NAME@", parentProperties.get("mod.name"));
            replaceTokenExtension.replace("@MOD_VERSION@", getVersionWithCommitHash.call(this.project.getParent()));
            replaceTokenExtension.replace("@ROOT_MOD_IDENTIFIER@", rootProperties.get("mod.id").toString().replace("-", "_"));
            replaceTokenExtension.replace("@ROOT_MOD_NAME@", rootProperties.get("mod.name"));
            replaceTokenExtension.replace("@ROOT_MOD_VERSION@", getVersionWithCommitHash.call(this.project.getRootProject()));
            replaceTokenExtension.replaceIn("top/hendrixshen/magiclib/SharedConstants");
            replaceTokenExtension.replaceIn("top/hendrixshen/magiclib/entrypoint/core/MagicLibForge");
            replaceTokenExtension.replaceIn("top/hendrixshen/magiclib/entrypoint/core/MagicLibNeoForge");
        });
    }

    private void signingConfigure() {
        this.project.getExtensions().configure(SigningExtension.class, signingExtension -> {
            Map<String, Object> extraProperties = this.project.getRootProject().getExtensions().getExtraProperties().getProperties();
            @SuppressWarnings("unchecked")
            Closure<GString> getOrDefault = (Closure<GString>) extraProperties.get("getOrDefault");
            @SuppressWarnings("unchecked")
            Closure<Map<String, String>> getEnv = (Closure<Map<String, String>>) extraProperties.get("getEnv");
            String signingKey = String.valueOf(getOrDefault.call("secrets.gpg.signingKey", getEnv.call().get("SIGNING_PGP_KEY")));
            String signingPassword = String.valueOf(getOrDefault.call("secrets.gpg.signingPassword", getEnv.call().get("SIGNING_PGP_PASSWORD")));
            signingExtension.setRequired(signingKey != null);
            signingExtension.useInMemoryPgpKeys(signingKey, signingPassword);
        });
    }

    private void publishingConfigure() {
        this.project.getExtensions().configure(PublishingExtension.class, publishingExtension -> {
            Map<String, Object> extraProperties = this.project.getRootProject().getExtensions().getExtraProperties().getProperties();

            publishingExtension.getPublications().register("release", MavenPublication.class, mavenPublication -> {
                assert this.project.getParent() != null;
                mavenPublication.setArtifactId(this.project.getParent().getProperties().get("mod.artifact_name") + "-" + this.project.getName());
                mavenPublication.artifact(this.project.getTasks().getByName("jar"));
                mavenPublication.artifact(this.project.getTasks().getByName("javadocJar"));
                mavenPublication.artifact(this.project.getTasks().getByName("sourcesJar"));
                mavenPublication.artifact(this.project.getTasks().getByName("shadowJar"));
                @SuppressWarnings("unchecked")
                Closure<GString> getMavenArtifactVersion = (Closure<GString>) extraProperties.get("getMavenArtifactVersion");
                assert getMavenArtifactVersion != null;
                mavenPublication.setVersion(String.valueOf(getMavenArtifactVersion.call(this.project.getParent())));
                mavenPublication.setGroupId(String.valueOf(this.project.getGroup()));
                @SuppressWarnings("unchecked")
                Closure<Void> addPomMetadataInformation = (Closure<Void>) extraProperties.get("addPomMetadataInformation");
                assert addPomMetadataInformation != null;
                addPomMetadataInformation.call(this.project, mavenPublication);
            });

            publishingExtension.repositories(repositoryHandler -> {
                repositoryHandler.add(repositoryHandler.mavenLocal());
                repositoryHandler.maven(maven -> {
                    maven.setName("projectLocalRelease");
                    maven.setUrl(this.project.getRootDir().toPath().resolve("publish/release"));
                });
                repositoryHandler.maven(maven -> {
                    maven.setName("nyanMavenRelease");
                    maven.setUrl("https://maven.hendrixshen.moe/releases");
                    @SuppressWarnings("unchecked")
                    Closure<Void> credentialsNyanMaven = (Closure<Void>) extraProperties.get("credentialsNyanMaven");
                    assert credentialsNyanMaven != null;
                    credentialsNyanMaven.call(maven);
                });
            });
        });
    }

    private void taskConfigure() {
        this.signingTasksConfigure();
        this.publishToMavenRepositoryTasksConfigure();
        this.javadocTasksConfigure();
        this.javaCompileTasksConfigure();
        this.jarConfigure();
        this.shadowJarConfigure();
        this.sourcesJarConfigure();
    }

    private void signingTasksConfigure() {
        // Solutions from: https://youtrack.jetbrains.com/issue/KT-46466
        TaskCollection<Sign> signingTasks = this.project.getTasks().withType(Sign.class);
        this.project.getTasks().withType(AbstractPublishToMaven.class).configureEach(task ->
                task.dependsOn(signingTasks)
        );
    }

    private void publishToMavenRepositoryTasksConfigure() {
        this.project.getTasks().withType(PublishToMavenRepository.class).configureEach(task -> task.onlyIf(t -> {
            PublishingExtension publishingExtension = this.project.getExtensions().getByType(PublishingExtension.class);
            RepositoryHandler repositories = publishingExtension.getRepositories();
            PublicationContainer publications = publishingExtension.getPublications();
            @SuppressWarnings("unchecked")
            Closure<Boolean> isNyanMavenCredentialsExist = (Closure<Boolean>) this.project.getRootProject().getExtensions().getExtraProperties().getProperties().get("isNyanMavenCredentialsExist");
            return "MavenLocal".equals(task.getRepository().getName())
                    || (task.getRepository() == repositories.named("projectLocalRelease").get() && task.getPublication() == publications.named("release").get())
                    || (task.getRepository() == repositories.named("nyanMavenRelease").get() && task.getPublication() == publications.named("release").get() && isNyanMavenCredentialsExist.call());
        }));
    }

    private void javadocTasksConfigure() {
        this.project.getTasks().withType(Javadoc.class).configureEach(task -> {
            task.include("**/api/**");
            task.getOptions().setEncoding("UTF-8");
            ((CoreJavadocOptions) task.getOptions()).addStringOption("Xdoclint:none", "-quiet");

            if (!"common".equals(this.project.getName())) {
                task.getClasspath().plus(this.project.project(":magiclib-core:common").getConfigurations().getByName("compileClasspath"));
                task.source(this.project.project(":magiclib-core:common").getExtensions().getByType(JavaPluginExtension.class).getSourceSets().getByName("main").getAllSource());
            }
        });
    }

    private void javaCompileTasksConfigure() {
        this.project.getTasks().withType(JavaCompile.class).configureEach(task ->
                task.getOptions().setEncoding("UTF-8")
        );
    }

    private void jarConfigure() {
        this.project.getTasks().named("jar", Jar.class).configure(task -> {
            task.dependsOn(this.project.getTasks().getByName("shadowJar"));
            task.getArchiveClassifier().set("slim");
        });
    }

    private void shadowJarConfigure() {
        this.project.getTasks().named("shadowJar", ShadowJar.class).configure(task -> {
            task.getArchiveClassifier().set((String) null);
            List<Configuration> configurations = new ArrayList<>();
            configurations.add(this.project.getConfigurations().getByName("shade"));
            configurations.add(this.project.getConfigurations().getByName("shadeOnly"));
            task.getConfigurations().set(configurations);
            task.relocate("com.llamalad7", this.project.getRootProject().getExtensions().getExtraProperties().getProperties().get("mod.maven_group") + ".libs.com.llamalad7");
            task.exclude("META-INF/maven/**/*");
            task.mergeServiceFiles();
        });
    }

    private void sourcesJarConfigure() {
        if ("common".equals(this.project.getName())) {
            return;
        }

        this.project.getTasks().named("sourcesJar", Jar.class).configure(task -> {
            Project project = this.project.project(":magiclib-core:common");
            project.getPlugins().withType(JavaLibraryPlugin.class, plugin -> {
                SourceSet sourceSet = project.getExtensions().getByType(JavaPluginExtension.class).getSourceSets().getByName("main");
                task.from(sourceSet.getAllSource());
            });
        });
    }
}
