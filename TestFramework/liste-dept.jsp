<%@page import="test.Dept"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <h1> Liste des départements </h1>
    <table>
        <tr>
            <th> ID </th>
            <th> Détails </th>
        </tr>
        <%
            List<Dept> liste = (List<Dept>) request.getAttribute("ListeDepartement");
            for(int i = 0; i < liste.size(); i++) {
        %>
            <tr>
                <td>
                    <% out.println(liste.get(i).getDeptId()); %>
                </td>
                <td>

                </td>
            </tr>
        <% } %>
    </table>
</body>
</html>