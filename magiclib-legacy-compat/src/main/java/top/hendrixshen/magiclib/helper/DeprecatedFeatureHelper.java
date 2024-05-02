package top.hendrixshen.magiclib.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLibReference;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class DeprecatedFeatureHelper {
    private static final LinkedBlockingQueue<String> deprecatedFeatureCache = Queues.newLinkedBlockingQueue();
    // Exclusion of records
    private static final List<Pattern> MAGICLIB_PACKAGE_PATTERN = Lists.newArrayList(
            // Fabric Loader
            Pattern.compile("^net\\.fabricmc\\.loader\\S+"),
            // Minecraft impl
            Pattern.compile("^net\\.minecraft\\S+"),
            // Java library
            Pattern.compile("^java\\S+"),
            // Quilt Loader
            Pattern.compile("^org\\.quiltmc.loader\\S+"),
            // Magiclib impl
            Pattern.compile("^top\\.hendrixshen\\.magiclib\\S+")
    );

    public static void warn(String inVersion) {
        DeprecatedFeatureHelper.warn(inVersion, null);
    }

    public static void warn(String inVersion, String identifier) {
        // Caller also treat as identifier.
        if (identifier == null) {
            identifier = "(Unknown Source)";
            StackTraceElement[] elements = (new Throwable()).getStackTrace();
            Arrays.stream(elements).forEach(System.out::println);

            for (StackTraceElement element : elements) {
                if (DeprecatedFeatureHelper.MAGICLIB_PACKAGE_PATTERN.stream()
                        .noneMatch(pattern -> pattern.matcher(element.getClassName()).matches())) {
                    identifier = element.toString();
                    break;
                }
            }
        }

        if (!DeprecatedFeatureHelper.deprecatedFeatureCache.contains(identifier)) {
            DeprecatedFeatureHelper.deprecatedFeatureCache.add(identifier);
            MagicLibReference.getLogger().warn("Deprecated MagicLib features were used in {}, making it incompatible with MagicLib {}.", identifier, inVersion);
        }
    }
}
