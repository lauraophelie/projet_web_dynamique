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
import etu1885.JSon;
import javax.servlet.RequestDispatcher;
import etu1885.framework.Mapping;
import etu1885.framework.ModelView;
import etu1885.FileUpload;
import etu1885.Parametre;
import etu1885.Scope;
import etu1885.Auth;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;

@WebServlet("/upload")
@MultipartConfig
public class FrontServlet extends HttpServlet {

    private HashMap<String, Mapping> mappingUrls;
    private HashMap<String, Object> singletons;
    private String sessionName;
    private String profil;

/// scan packages 
    public void init() throws ServletException {
        
        mappingUrls = new HashMap<String, Mapping>();
        singletons = new HashMap<String, Object>();
        
        ServletContext context = this.getServletContext();
        String contextPath = context != null ? context.getRealPath("") : null;
        if (contextPath == null) {
            contextPath = new File("").getAbsolutePath();
        }
        File webInfDirectory = new File(contextPath, "WEB-INF");
        List<File> files = Utilitaire.getFiles(webInfDirectory.getAbsolutePath());

        sessionName = this.getInitParameter("sessionName");
        profil = this.getInitParameter("profileName");
        
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


                    if(c.isAnnotationPresent(Scope.class)) { // test : annotation @scope
                        System.out.println("Singleton found : " + c.getName());
                        Object obj = null;
                        String className = c.getName();
                        singletons.put(className, obj); // raha singleton 
                    }

                    Method [] m = c.getDeclaredMethods();
                    for (int k = 0; k < m.length; k++) {
                        if (m[k].isAnnotationPresent(URLs.class)) { // test : méthode annotée @URLs 
                            String url = m[k].getAnnotation(URLs.class).url();
                            String className = c.getName();
                            String methodName = m[k].getName();
                            Mapping map = new Mapping(className, methodName);
                            mappingUrls.put(url, map); // raha annotée @URLs
                        }
                    }
                } catch (ClassNotFoundException e) {
                   
                }
            }
        } 
    }

/// vérification hoe singleton sa tsia
    public boolean isSingleton(String className) {
        for(String key : singletons.keySet()) {
            if(key.equals(className)) return true;
        }
        return false;
    }

    public Object getInstance(String className) {
        return singletons.get(className);
    }

    public void setInstance(String className, Object instance) {
        singletons.put(className, instance);
    }

/// get mapping ao @ mappingURls
    public Mapping getMapping(String value) {
        if(mappingUrls.containsKey(value)) {
            return mappingUrls.get(value);
        }
        return null;
    }

    public boolean isJson(Mapping m) throws Exception {
        Class<?> clazz = Class.forName(m.getClassName());
        String mappingMethod = m.getMethod();
        for(Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(mappingMethod)) {
                if(method.isAnnotationPresent(JSon.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isConnected(ModelView modelView) {
        if(modelView.getSession().containsKey(sessionName)) {
            return true;
        }
        return false;
    }

    public Method getTargetMethod(Mapping mapping) throws Exception {
        Method targetMethod = null;
        Class<?> targetClass = Class.forName(mapping.getClassName());
        for (Method method : targetClass.getDeclaredMethods()) {
            if (method.getName().equals(mapping.getMethod())) {
                targetMethod = method;
                break;
            }
        }
        return targetMethod;
    }

/// ilay objet ho avadika ModelView
    protected Object modelView(Mapping m, HttpServletRequest request) throws Exception {
        Object obj = null;

        if (m == null) {
            throw new Exception("URL inconnue"); // L'URL n'est pas dans le hashmap
        }

        String className = m.getClassName();
        Class<?> c = Class.forName(className);

        if (isSingleton(className)) {
            obj = getInstance(className); // Obtenir l'instance à partir du HashMap des singletons
            if (obj == null) {
                System.out.println("Instanciation d'un nouveau singleton : " + className);
                obj = c.newInstance();
                this.setInstance(className, obj); // Mettre à jour l'instance dans le HashMap si elle est null
            } else {
                System.out.println("Utilisation de l'instance existante du singleton : " + className);
            }
        } else {
            System.out.println("Instanciation d'un nouvel objet : " + className);
            obj = c.newInstance();
        }
        Utilitaire.resetAttributes(obj);

        Object objet = c.newInstance();
        Object [] argArray = null;

        String mappingMethod = m.getMethod();
        Method targetMethod = this.getTargetMethod(m);

        if (targetMethod != null) {
            Parameter[] params = targetMethod.getParameters();

            if (params.length > 0) { // raha misy paramètre ilay méthode 
                argArray = new Object[params.length];
                int i = 0;

                for (Parameter parameter : params) {
                    if (parameter.isAnnotationPresent(Parametre.class)) {
                        Parametre parametre = parameter.getAnnotation(Parametre.class);
                        String param = parametre.param();
                        String valueParam = request.getParameter(param);

                        if (valueParam != null && !valueParam.isEmpty()) {
                            Object valueObject = Utilitaire.convertParameterToType(valueParam, parameter.getType());
                            argArray[i] = valueObject;
                            i++;
                        }
                    }
                }
                obj = targetMethod.invoke(objet, argArray); // appel fonction miaraka @ tableau de paramètre 
            } else { // raha tsy misy paramètre ilay méthode 
                HashMap<String, Type> attributs = Utilitaire.getAttributs(c);

                for (Map.Entry<String, Type> entry : attributs.entrySet()) {
                    String name = entry.getKey();
                    Type type = entry.getValue();
                    String req = request.getParameter(name);

                    if (req != null && !req.isEmpty()) { // set 
                        Object val = Utilitaire.convertParameterToType(req, type);
                        Field f = objet.getClass().getDeclaredField(name);
                        f.setAccessible(true);
                        f.set(objet, val);
                    }
                    if ("file".equals(name)) { // raha file ny name @ attributs 
                        try {
                            Part filePart = request.getPart("file");
                            FileUpload upload = getFileUploaded(filePart); // upload file 

                            Field f = objet.getClass().getDeclaredField(name); // set 
                            f.setAccessible(true);
                            f.set(objet, upload);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    obj = targetMethod.invoke(objet); // invoke @ methode 
                }
            }
        } else {
            throw new Exception("Méthode non trouvée : " + mappingMethod); // La méthode spécifique n'a pas été trouvée
        }

        return obj;
    }


/// upload de fichier
    protected FileUpload getFileUploaded(Part filePart) throws IOException {
        String fileName = Utilitaire.getFileName(filePart); // nom de fichier 
        byte [] fileContent = Utilitaire.fileToBytes(filePart); // tableau de bytes 

        filePart.getInputStream().close();

        FileUpload upload = new FileUpload();
        upload.setName(fileName);
        upload.setBytes(fileContent);

        return upload;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            System.out.println("Session : " + sessionName);
            System.out.println("Profil : " + profil);

            String url = request.getRequestURL().toString();
            String value = Utilitaire.getUrlValues(url);
            Mapping mapping = this.getMapping(value);
            HttpSession session = request.getSession();

            Object obj = this.modelView(mapping, request);

            String className = mapping.getClassName();

            if(this.isJson(mapping) == true) {
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                String json = Utilitaire.objectToJson(obj);
                writer.println(json);
            } else {
                ModelView mv = (ModelView) obj;
                HashMap<String, Object> sessions = mv.getSession();

                if(sessions != null || sessions.isEmpty() == false) {
                    for(Map.Entry<String, Object> entry : sessions.entrySet()) {
                        String key = entry.getKey();
                        Object val = entry.getValue();
                        System.out.println("key : " + key + " val : " + val);
                        session.setAttribute(key, val);
                    }
                }

                String mappingMethod = mapping.getMethod();

                HashMap<String, Object> data = mv.getData();

                if(data.isEmpty() == false || data != null) {
                    for(Map.Entry<String, Object> entry : data.entrySet()) {
                        String key = entry.getKey();
                        Object val = entry.getValue();
                        request.setAttribute(key, val);
                    }
                } 
                if(mv.isJson() == false) {
                    RequestDispatcher dispat = request.getRequestDispatcher(mv.getView());
                    dispat.forward(request, response);
                } else {
                    response.setContentType("application/json");
                    PrintWriter writer = response.getWriter();
                    String json = Utilitaire.objectToJson(data);
                    writer.println(json);
                }
            }
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
