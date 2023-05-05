package etu1885.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
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

import java.util.List;
import java.util.Map;

public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> mappingUrls;

    public void init() throws ServletException {
        
        mappingUrls = new HashMap<String, Mapping>();
        
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
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            Utilitaire utilitaire = new Utilitaire();
            String url = request.getRequestURL().toString();
            String value = utilitaire.getUrlValues(url);

            Mapping m = new Mapping();

            for(Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
                String key = entry.getKey();
                String valeur = entry.getValue().getClassName();
            }
                
            if (mappingUrls.containsKey(value)) m = mappingUrls.get(value);
            else out.println("URL inconnue");

            Class c = Class.forName(m.getClassName());
            Object obj = null;

            if(m.getMethod().contains("save") == false) {
                Object o = c.newInstance();
                if(m.getMethod().contains("findById") == false) {
                    obj = c.getMethod(m.getMethod()).invoke(c.newInstance());
                } else {
                    Method meth = o.getClass().getMethod(m.getMethod(), int.class);
                    Parameter [] params = meth.getParameters();
                    for (Parameter parameter : params) {
                        String idParam = request.getParameter(parameter.getName());
                        
                        if(idParam.isEmpty() == false || idParam != null) {
                            int id = Integer.parseInt(idParam);
                            obj = meth.invoke(o, id);
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
