<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="test.Dept"%>
<%@page import="etu1885.FileUpload"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> Département inséré </title>
</head>
<body>
    <h1> Département inséré </h1>
    <%
        Dept d = (Dept) request.getAttribute("DeptSaved");
    %>
    <p> ID : <% out.println(d.getDeptId()); %> </p>
</body>
</html>