package etu1885.framework;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import java.lang.reflect.Type;

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

    public HashMap<String, Type> getAttributs(Class<?> clazz) {
        HashMap<String, Type> attributs = new HashMap<>();
        Field [] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Type type = field.getType();
            attributs.put(name, type);
        }
        return attributs;
    }

    public List<Method> getListeSetters(Class<?> clazz) {
        List<Method> setters = new ArrayList<>();
        Method [] methods = clazz.getMethods();
        for (Method method : methods) {
            if(method.getName().contains("set")) {
                setters.add(method);
            }
        }
        return setters;
    }
}
