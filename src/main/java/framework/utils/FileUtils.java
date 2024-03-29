package framework.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class FileUtils {

    public static String getAbsolutePathToResourceFile(String fileName){
        return getResourceFile(fileName).getAbsolutePath();
    }

    private static File getResourceFile(String fileName){
        try {
            URL res = Paths.get("target/test-classes", fileName).toUri().toURL();
            return Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
