<%@page import="test.Emp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1> Fiche de l'employé </h1>
    <%
        Emp e = (Emp) request.getAttribute("FicheEmp");
    %>
    <p> Nom : <% out.println(e.getNom()); %> </p>
    <p> Salaire : <% out.println(e.getSalaire()); %> Ar </p>
</body>
</html>