package etu1885.framework;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import java.lang.reflect.Type;
import javax.servlet.http.Part;

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

    public String getFileName(Part part) {
        String contentDisposition = part.getHeader("Content-Disposition");
        System.out.println("Content-Disposition: " + contentDisposition);
        String [] elements = contentDisposition.split(";");
    
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                String fileName = element.substring(element.indexOf('=') + 1).trim();
                if (fileName.startsWith("\"") && fileName.endsWith("\"")) {
                    fileName = fileName.substring(1, fileName.length() - 1);
                }
                System.out.println("File name: " + fileName);
                return fileName;
            }
        }
        return null;
    }

    public byte [] fileToBytes(Part filePart) throws IOException {
        byte [] bytes = null;
        String fileName = this.getFileName(filePart);
    
        InputStream fileContent = filePart.getInputStream();
        ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();
    
        try {
            bytes = new byte[4096];
            int bytesRead;
    
            while ((bytesRead = fileContent.read(bytes)) != -1) {
                fileOutput.write(bytes, 0, bytesRead);
            }
    
            byte[] fileBytes = fileOutput.toByteArray();
            return fileBytes;
        } finally {
            fileContent.close();
            fileOutput.close();
        }
    }    

    public static void resetAttributes(Object obj) {
        Class<?> objClass = obj.getClass();
        Field [] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Class<?> fieldType = field.getType();
                Object defaultValue = getDefaultValue(fieldType);
                field.set(obj, defaultValue);
            } catch(IllegalAccessException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static Object getDefaultValue(Class<?> fieldType) {
        if (fieldType.isPrimitive()) {
            if (fieldType == boolean.class) {
                return false;
            } else if (fieldType == byte.class) {
                return (byte) 0;
            } else if (fieldType == short.class) {
                return (short) 0;
            } else if (fieldType == int.class) {
                return 0;
            } else if (fieldType == long.class) {
                return 0L;
            } else if (fieldType == float.class) {
                return 0.0f;
            } else if (fieldType == double.class) {
                return 0.0;
            } else if (fieldType == char.class) {
                return '\u0000';
            }
        }
        return null;
    }
}
