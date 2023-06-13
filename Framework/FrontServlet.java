package etu1885.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.text.Annotation;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import etu1885.framework.Utilitaire;
import etu1885.URLs;
import javax.servlet.RequestDispatcher;
import etu1885.framework.Mapping;
import etu1885.framework.ModelView;
import etu1885.FileUpload;
import etu1885.Parametre;
import etu1885.Scope;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/upload")
@MultipartConfig
public class FrontServlet extends HttpServlet {

    private HashMap<String, Mapping> mappingUrls;
    private HashMap<String, Object> singletons;

    public void init() throws ServletException {
        
        mappingUrls = new HashMap<String, Mapping>();
        singletons = new HashMap<String, Object>();
        
        ServletContext context = this.getServletContext();
        String contextPath = context != null ? context.getRealPath("") : null;
        if (contextPath == null) {
            contextPath = new File("").getAbsolutePath();
        }
        File webInfDirectory = new File(contextPath, "WEB-INF");
        List<File> files = new Utilitaire().getFiles(webInfDirectory.getAbsolutePath());

        
        for(File file : files) {
            String f = "";
            File [] liste = file.listFiles();
            for (int i = 0; i < liste.length; i++) {
                if(liste[i].isDirectory() == false) {
                    String fl = liste[i].getName();
                    String [] chain = fl.split(".class");
                    for (int k = 0; k < chain.length; k++) {
                        f += chain[k] + "//";
                    }
                }
            }
            String [] attributs = f.split("//");
            for (int j = 0; j < attributs.length; j++) {
                String name = file.getName() + "." + attributs[j];
                try {
                    Class c = Class.forName(name);

                    if(c.isAnnotationPresent(Scope.class)) {
                        System.out.println("Singleton found : " + c.getName());
                        Object obj = null;
                        String className = c.getName();
                        singletons.put(className, obj);
                    }

                    Method[] m = c.getDeclaredMethods();
                    for (int k = 0; k < m.length; k++) {
                        if (m[k].isAnnotationPresent(URLs.class)) {
                            String url = m[k].getAnnotation(URLs.class).url();
                            String className = c.getName();
                            String methodName = m[k].getName();
                            Mapping map = new Mapping(className, methodName);
                            mappingUrls.put(url, map);
                        }
                    }
                } catch (ClassNotFoundException e) {
                   
                }
            }
        } 
    }

    public boolean isSingleton(String className) {
        for(String key : singletons.keySet()) {
            if(key.equals(className)) return true;
        }
        return false;
    }

    public Mapping getMapping(String value) {
        if(mappingUrls.containsKey(value)) {
            return mappingUrls.get(value);
        }
        return null;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            Utilitaire utilitaire = new Utilitaire();
            String url = request.getRequestURL().toString();
            String value = utilitaire.getUrlValues(url);

            Mapping m = this.getMapping(value);

            if(m == null) {
                throw new Exception("URL inconnue");
            }
            Class c = Class.forName(m.getClassName());
            Object obj = null;

            if(m.getMethod().contains("save") == false) {
                Object o = c.newInstance();
                Object [] argArray = null;
                for(Method method : c.getDeclaredMethods()) {
                    if(method.getName().equals(m.getMethod())) {
                        Parameter [] params = method.getParameters();
                        if(params.length > 0) {
                            argArray = new Object[params.length];
                            int i = 0;
                            for(Parameter parameter : params) {
                                if(parameter.isAnnotationPresent(Parametre.class)) {
                                    Parametre parametre = parameter.getAnnotation(Parametre.class);
                                    String valueParam = request.getParameter(parametre.param());
                                    
                                    if(valueParam != null || valueParam.isEmpty() == false) {
                                        Object valueObject = utilitaire.convertParameterToType(valueParam, parameter.getType());
                                        argArray[i] = valueObject;
                                        i++;
                                    }
                                }
                            }
                            obj = method.invoke(o, argArray);
                        } else if(params.length == 0) {
                            obj = c.getMethod(m.getMethod()).invoke(c.newInstance());
                        }
                    }
                }
            } else{
                HashMap<String, Type> attributs = utilitaire.getAttributs(c);
                Object objet = c.newInstance();

                for (Map.Entry<String, Type> entry : attributs.entrySet()) {

                    String name = entry.getKey();
                    Type type = entry.getValue();
                    String req = request.getParameter(name);
                    
                    if (req != null && !req.isEmpty()) {
                        Object val = utilitaire.convertParameterToType(req, type);
                        Field f = objet.getClass().getDeclaredField(name);
                        f.setAccessible(true);
                        f.set(objet, val);
                    }
                    if(name == "file") {
                        try {
                            Part filePart = request.getPart("file");
                            String fileName = utilitaire.getFileName(filePart);

                            byte [] fileContent = utilitaire.fileToBytes(filePart);
                            filePart.getInputStream().close();

                            FileUpload upload = new FileUpload();

                            upload.setName(fileName);
                            upload.setBytes(fileContent);

                            Field f = objet.getClass().getDeclaredField(name);
                            f.setAccessible(true);
                            f.set(objet, upload);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }                
                Method saveMethod = c.getMethod(m.getMethod(), objet.getClass());
                obj = saveMethod.invoke(c.newInstance(), objet);
            }
            ModelView mv = (ModelView) obj;

            HashMap<String, Object> data = mv.getData();

            if(data.isEmpty() == false || data != null) {
                for(Map.Entry<String, Object> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object val = entry.getValue();
                    request.setAttribute(key, val);
                }
            } 
            RequestDispatcher dispat = request.getRequestDispatcher(mv.getView());
            dispat.forward(request, response);
            
        } catch(Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }  

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
