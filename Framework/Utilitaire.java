package etu1885.framework;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    public Object convertParameterToType(String parameter, Type type) {
        Object value = null;
    
        if (type == int.class || type == Integer.class) {
            value = Integer.parseInt(parameter);
        } else if (type == long.class || type == Long.class) {
            value = Long.parseLong(parameter);
        } else if (type == float.class || type == Float.class) {
            value = Float.parseFloat(parameter);
        } else if (type == double.class || type == Double.class) {
            value = Double.parseDouble(parameter);
        } else if (type == boolean.class || type == Boolean.class) {
            value = Boolean.parseBoolean(parameter);
        } else if (type == String.class) {
            value = parameter;
        } else if (type == LocalDate.class) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(parameter, formatter);
            value = date;
        } else if (type == LocalTime.class) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(parameter, formatter);
            value = time;
        } else if (type == LocalDateTime.class) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime datetime = LocalDateTime.parse(parameter, formatter);
            value = datetime;
        } else if(type == java.sql.Date.class) { 
            java.sql.Date date = java.sql.Date.valueOf(parameter);
            value = date;
        } else if(type == java.sql.Time.class) {
            java.sql.Time time = java.sql.Time.valueOf(parameter);
            value = time;
        }
    
        return value;
    }
    
    
}
