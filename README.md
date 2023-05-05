<h1> Framework </h1>

<h2> web.xml </h2>

<ul>
    <li> servlet-name : FrontServlet </li>
    <li> url-pattern : / </li>
</ul>

<servlet>
        <servlet-name> FrontServlet </servlet-name>
        <servlet-class> etu1885.framework.servlet.FrontServlet </servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name> FrontServlet </servlet-name>
    <url-pattern> / </url-pattern>
</servlet-mapping>      

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