package ar.edu.itba.pod.client;

import java.nio.file.Path;
import java.util.Objects;

public class Utils {
    public static String getFolderPath(String path){
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return Path.of(Objects.requireNonNull(classloader.getResource(path+"dummy")).getPath()).getParent().toString();
    }
}
