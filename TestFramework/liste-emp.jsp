<%@page import="java.util.List"%>
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
    <h1> Liste des employés </h1>
    <table>
        <tr>
            <th> ID </th>
            <th> Nom </th>
            <th> Salaire </th>
            <th> </th>
        </tr>
    <%
        List<Emp> liste = (List<Emp>) request.getAttribute("Liste-Emp");
        for(int i = 0; i < liste.size(); i++) {
    %> 
        <tr> 
            <td> <% out.println(liste.get(i).getId()); %> </td>
            <td> <% out.println(liste.get(i).getNom()); %> </td>
            <td> <% out.println(liste.get(i).getSalaire()); %> </td>
            <td>
                <a href="emp-find-by-id?id=<% out.println(liste.get(i).getId()); %>"> Détails </a>
            </td>
        </tr>
    <% } %> 
    </table>
    <a href="./formulaire-emp.jsp"> Formulaire d'insertion </a>
</body>
</html>