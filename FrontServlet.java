package etu1885.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import etu1885.framework.Utilitaire;
import etu1885.URLs;
import etu1885.framework.Mapping;

public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> mappingUrls;

    public void init(String path) {
        mappingUrls = new HashMap<String, Mapping>();
        Utilitaire u = new Utilitaire();
        File[] files = new File("./").listFiles();
        for (int i = 0; i < files.length; i++) {
            String f = "";
            if (files[i].isDirectory()) {
                File[] liste = files[i].listFiles();
                for (int j = 0; j < liste.length; j++) {
                    String file = liste[j].getName();
                    String[] chain = file.split(".class");
                    for (int k = 0; k < chain.length; k++) {
                        f += chain[k] + "//";
                    }
                }
                String[] attributs = f.split("//");
                for (int j = 0; j < attributs.length; j++) {
                    String name = files[i].getName() + "." + attributs[j];
                    try {
                        Class c = Class.forName(name);
                        Method[] m = c.getDeclaredMethods();
                        for (int k = 0; k < m.length; k++) {
                            if (m[k].isAnnotationPresent(URLs.class)) {
                                String url = m[k].getAnnotation(URLs.class).url();
                                if (url.equals(path)) {
                                    String className = c.getName();
                                    String methodName = m[k].getName();
                                    Mapping map = new Mapping(className, methodName);
                                    mappingUrls.put(url, map);
                                }
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Utilitaire utilitaire = new Utilitaire();
            String url = request.getRequestURL().toString();
            String value = utilitaire.getUrlValues(url);
            init(value);
            out.println(value);
            /*for (String key : mappingUrls.keySet()) {
                out.println(key + " = " + mappingUrls.get(key));
            }*/
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }  

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
