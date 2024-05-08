package top.hendrixshen.magiclib.language.impl;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

//#if MC <= 11404
//$$ import net.minecraft.server.packs.Pack;
//#endif

public class MagicLanguageResourceManager implements ResourceManager {
    private static final Pattern languageResourcePattern = Pattern.compile("^assets/([\\w-]*)/lang/([a-z\\d-_]*)\\.json$");
    private final Set<String> namespaces = new HashSet<>();
    private final HashMap<ResourceLocation, Set<Resource>> resources = new HashMap<>();

    public MagicLanguageResourceManager() {
        try {
            for (URL resource : Collections.list(this.getClass().getClassLoader().getResources("assets"))) {
                if (resource.getProtocol().equalsIgnoreCase("jar")) {
                    this.addResources(MagicLanguageResourceManager.getJarResources(resource));
                } else if (resource.getProtocol().equalsIgnoreCase("file")) {
                    this.addResources(MagicLanguageResourceManager.getFileResources(resource));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull Map<ResourceLocation, Resource> getJarResources(@NotNull URL resourceUrl) throws IOException {
        JarURLConnection conn = (JarURLConnection) resourceUrl.openConnection();
        JarFile jar = conn.getJarFile();
        Map<ResourceLocation, Resource> ret = new HashMap<>();

        for (JarEntry entry : Collections.list(jar.entries())) {
            String entryName = entry.getName();
            Matcher matcher = languageResourcePattern.matcher(entryName);
            if (matcher.find()) {
                String namespace = matcher.group(1);
                String code = matcher.group(2);

                if (!entry.isDirectory()) {
                    String languagePath = String.format("lang/%s.json", code);
                    ResourceLocation resourceLocation = new ResourceLocation(namespace, languagePath);
                    Resource resource = new MagicLanguageResource(
                            //#if MC > 12001
                            //$$ new FilePackResources.FileResourcesSupplier(new File(resourceUrl.getPath()), true).openPrimary(namespace),
                            //#elseif MC > 11902
                            //$$ new FilePackResources(namespace, new File(resourceUrl.getPath()), true),
                            //#else
                            namespace,
                            //#endif
                            resourceLocation,
                            () -> {
                                try {
                                    return jar.getInputStream(entry);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }});
                    ret.put(resourceLocation, resource);
                }
            }
        }
        return ret;
    }

    private static @NotNull Map<ResourceLocation, Resource> getFileResources(@NotNull URL resourceUrl) throws IOException {
        Map<ResourceLocation, Resource> ret = new HashMap<>();

        try {
            Path assetsPath = Paths.get(resourceUrl.toURI());
            Files.walkFileTree(assetsPath, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String fileName = "assets/" + assetsPath.relativize(file).toString().replace("\\", "/");
                    Matcher matcher = languageResourcePattern.matcher(fileName);

                    if (matcher.find()) {
                        String namespace = matcher.group(1);
                        String code = matcher.group(2);
                        String languagePath = String.format("lang/%s.json", code);
                        ResourceLocation resourceLocation = new ResourceLocation(namespace, languagePath);

                        Resource resource = new MagicLanguageResource(
                                //#if MC > 12001
                                //$$ new FilePackResources.FileResourcesSupplier(file, true).openPrimary(namespace),
                                //#elseif MC > 11902
                                //$$ new FilePackResources(namespace, file.toFile(), true),
                                //#else
                                namespace,
                                //#endif
                                resourceLocation,
                                () -> {
                                    try {
                                        return Files.newInputStream(file);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                        ret.put(resourceLocation, resource);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return ret;
    }

    private void addResources(@NotNull Map<ResourceLocation, Resource> resources) {
        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation location = entry.getKey();
            namespaces.add(location.getNamespace());
            Set<Resource> resourceList = this.resources.computeIfAbsent(location, resourceLocation -> new HashSet<>());
            resourceList.add(entry.getValue());
        }
    }

    @Override
    public Set<String> getNamespaces() {
        return namespaces;
    }

    @Override
    public List<Resource> getResources(@NotNull ResourceLocation resourceLocation) {
        return new ArrayList<>(resources.getOrDefault(resourceLocation, new HashSet<>()));
    }

    //#if MC > 11802
    //$$ @Override
    //$$ public Map<ResourceLocation, Resource> listResources(@NotNull String string, @NotNull Predicate<ResourceLocation> predicate) {
    //$$     return null;
    //$$ }
    //$$
    //$$ @Override
    //$$ public Map<ResourceLocation, List<Resource>> listResourceStacks(@NotNull String string, @NotNull Predicate<ResourceLocation> predicate) {
    //$$     return null;
    //$$ }
    //#else
    @Override
    public Collection<ResourceLocation> listResources(@NotNull String string, @NotNull Predicate<String> predicate) {
        return null;
    }
    //#endif

    //#if MC > 11502
    @Override
    public Stream<PackResources> listPacks() {
        return null;
    }
    //#endif

    //#if MC <= 11404
    //$$ @Override
    //$$ public void add(Pack pack) {}
    //#endif

    //#if MC > 11802
    //$$ @Override
    //$$ public Optional<Resource> getResource(ResourceLocation resourceLocation) {
    //$$     return Optional.empty();
    //$$ }
    //#else
    @Override
    public Resource getResource(@NotNull ResourceLocation resourceLocation) {
        return null;
    }

    @Override
    public boolean hasResource(@NotNull ResourceLocation resourceLocation) {
        return false;
    }
    //#endif
}
