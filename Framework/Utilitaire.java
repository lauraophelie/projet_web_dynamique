package etu1885.framework;
import java.util.ArrayList;
import java.io.File;
import java.net.URI;
import java.util.List;

public class Utilitaire {
    public String getUrlValues(String url) {
        
        URI uri = URI.create(url);
        String uriPath = uri.getPath();
        String [] values = uriPath.split("/");
        String value = "";
        
        for (int i = 2; i < values.length - 1; i++) {
            value += values[i] + "/";
        }
        value += values[values.length - 1];
        
        return value;
    }
    public List<File> getFiles(String path) {
        List<File> files = new ArrayList<File>();
        File [] f = new File(path).listFiles();
        for (int i = 0; i < f.length; i++) {
            if (f[i].isDirectory()) {
                files.add(f[i]);
                List<File> subFiles = getFiles(f[i].getPath());
                files.addAll(subFiles);
            }
        }
        return files;
    }
}
