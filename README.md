<h1> Framework </h1>

<h2> Pré - requis </h2>

<ul>
    <li> JDK version 8 </li>
    <li> Tomcat version 8 </li>
</ul>

<h2> Mise en place </h2>

<p> Le fichier framework.jar doit être placé dans le dossier "lib/" du projet Tomcat <p>
<p> Le fichier gson.jar doit aussi être placé dans le dossier "lib/" du projet Tomcat <p>

<h2> web.xml </h2>

    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu1885.framework.servlet.FrontServlet</servlet-class>
        <init-param>
            <param-name> sessionName </param-name>
            <param-value> isConnected </param-value>
        </init-param>
        <init-param>
            <param-name> profileName </param-name>
            <param-value> profil </param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>


<h2> Annotations </h2>

<h3> @URLs </h3>

<p> 
    chaque fonction est de type ModelView et est annotée @URLs,
    dans chaque fonction de type ModelView on utiliser la fonction setView() ( de type String) pour définir la vue pour le RequestDispatcher
    et c'est aussi dans la fonction de type ModelView qu'on utilise la fonction addItem() pour définir l'attribut de la requête et la valeur de l'attribut
</p>
<p>
    Exemple : 
        
            @URLs(url="emp-find-all")
            public ModelView findAll() {

                ModelView mv = new ModelView();
                mv.setView("/liste-emp.jsp");   // le nom de la vue pour de RequestDispatcher

                Emp e1 = new Emp(1, "E1", 20000);
                Emp e2 = new Emp(2, "E2", 250000);

                List<Emp> e = new ArrayList<Emp>();
                mv.addItem("Liste-Emp", e);     // pour le request.setAttribute("Liste-Emp", e)

                return mv;
            }
</p>

<h3> @Parametre </h3>

<p>
    pour une fonction de type ModelView qui contient des paramètres, 
    chaque paramètre de la fonction doit être annotée @Parametre avec comme valeur le nom du paramètre, 
    c'est la valeur de cette annotation qui sera ensuite récupérée pour pouvoir appeler la fonction 
</p>

<p> 
    Exemple : 

        @URLs(url="emp-find-by-id")
        public ModelView findById(@Parametre(param="id") int id) {
            ModelView mv = new ModelView();
            mv.setView("/details-emp.jsp");

            Emp e = null;

            if(id == 1) e = new Emp(id, "E1", 20000);
            if(id == 2) e = new Emp(id, "E2", 250000);

            mv.addItem("FicheEmp", e);
            return mv;
        }
</p>

<h3> @Scope </h3>

<p> 
    chaque class annotée scope est considéré comme un singleton et est ajouté à un hashmap dans la méthode init() de la class FrontServlet, et ce singleton ne sera instancié qu'une seule fois
</p>

<p> Exemple 

        @scope()
        public class Emp {
            . . .
        }
</p>

<h3> @Auth </h3>

<p> 
    l'annotation est utilisée lorsqu'une fonction nécessite un login et 
    @auth prend également un paramètre au cas où la fonction nécessite un rôle particulier ( ex : admin)
</p>

<p> Exemple 

        @auth("admin")
        public ModelView save() {
            . . .
        }
</p>