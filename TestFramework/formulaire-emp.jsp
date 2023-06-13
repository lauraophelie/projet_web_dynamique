<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> Formulaire </title>
</head>
<body>
    <h1> Formulaire d'insertion </h1>
    <form method="post" action="emp-save">
        <p> Nom : <input type="text" name="nom"/> </p>
        <p> Salaire : <input type="text" name="salaire"/> </p>
        <p> <input type="submit" value="Valider"/> </p>
    </form>
</body>
</html>