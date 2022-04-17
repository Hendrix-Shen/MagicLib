package top.hendrixshen.magiclib.util;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MagicStreamHandler extends URLStreamHandler {
    private static final URLStreamHandler handler = new MagicStreamHandler();
    private static final Map<URL, byte[]> contents = new ConcurrentHashMap<>();

    private static final String MAGIC_PROTOCOL = "magic";

    public static void addFile(String fileName, byte[] data) {
        try {
            contents.put(new URL(MAGIC_PROTOCOL, "", -1, fileName, handler), data);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addClass(ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        addFile(String.format("/%s.class", classNode.name), cw.toByteArray());
        if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
            File f = new File(".mixin.out/" + classNode.name.replace("/", ".") + ".remap.class");
            try {
                OutputStream fOut = Files.newOutputStream(f.toPath());
                fOut.write(cw.toByteArray());
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static URL getMemoryClassLoaderUrl() {
        try {
            return new URL(MAGIC_PROTOCOL, null, -1, "/", handler);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        if (!u.getProtocol().equals(MAGIC_PROTOCOL)) {
            throw new IOException("Cannot handle protocol: " + u.getProtocol());
        }
        return new URLConnection(u) {
            private byte[] data = null;

            @Override
            public void connect() throws IOException {
                initDataIfNeeded();
                checkDataAvailability();
                // Protected field from superclass
                connected = true;
            }

            @Override
            public long getContentLengthLong() {
                initDataIfNeeded();
                if (data == null)
                    return 0;
                return data.length;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                initDataIfNeeded();
                checkDataAvailability();
                return new ByteArrayInputStream(data);
            }

            private void initDataIfNeeded() {
                if (data == null)
                    data = contents.get(u);
            }

            private void checkDataAvailability() throws IOException {
                if (data == null)
                    throw new IOException("In-memory data cannot be found for: " + u.getPath());
            }
        };
    }
}
