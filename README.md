<h1> Framework </h1>

<h2> Pré - requis </h2>

<ul>
    <li> JDK version 8 </li>
    <li> Tomcat version 8 </li>
</ul>

<h2> web.xml </h2>

<ul>
    <li> servlet-name : FrontServlet </li>
    <li> url-pattern : / </li>
</ul>

<p>
    <servlet>
        <servlet-name> FrontServlet </servlet-name>
        <servlet-class> etu1885.framework.servlet.FrontServlet </servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name> FrontServlet </servlet-name>
        <url-pattern> / </url-pattern>
    </servlet-mapping>
</p>

<h2> Annotations </h2>

<p> 
    chaque fonction est de type ModelView et est annotée @URLs,
    dans chaque fonction de type ModelView on utiliser la fonction setView() ( de type String) pour définir la vue pour le RequestDispatcher
    et c'est aussi dans la fonction de type ModelView qu'on utilise la fonction addItem() pour définir l'attribut de la requête et la valeur de l'attribut
</p>
<p>
    Exemple : 
        exemple : 
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

<h2> Fonction save() </h2> 

<p> la fonction save() prend en paramètre un Objet </p>

<p>
    exemple : dans la classe Emp -> la fonction save prend en paramètre un Objet de type Emp 
            @URLs(url="emp-save")
            public ModelView save(Emp e) {

                ModelView mv = new ModelView();
                mv.setView("/saved-emp.jsp");
                
                int id = e.getId();
                String nom = e.getNom();
                double salaire = e.getSalaire();

                Emp emp = new Emp(id, nom, salaire);
                mv.addItem("Emp", emp);

                return mv;
            }
</p>
<p>
    pour la fonction save, les types disponibles pour le cast() sont : 
        int, long, double, float, boolean, String, LocalDate, LocalTime, 
        LocalDateTime, java.sql.Date, java.sql.Time
</p>
<p>
    la fonction de cast() est disponible dans la class Utilitaire -> convertParameterToType()
</p>