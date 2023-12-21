package util;

import java.net.URL;
import java.util.Objects;

public final class ResourcePath {
    private static ResourcePath INSTANCE;
    private final String dir;

    private ResourcePath() {
        dir = Objects.requireNonNull(getClass().getResource("../resources/")).getPath();
    }

    public static ResourcePath getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResourcePath();
        }
        return INSTANCE;
    }

    public static void genResourcePath() {
        INSTANCE = new ResourcePath();
    }

    public static String Path(String s) {
        return INSTANCE.dir + s;
    }
    private URL getInstanceURL(String p) {
        URL url = getClass().getResource(p);

        if (url == null) {
            System.out.println("Could not load resource: " + p);
        }
//        assert url != null;

        return url;
    }
    public static URL getURL(String p) {
        return INSTANCE.getInstanceURL("../resources/" + p);
    }
}
