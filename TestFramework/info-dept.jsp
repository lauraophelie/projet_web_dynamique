<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title> Upload de fichier </title>
</head>
<body>
    <h1> Upload de fichier </h1>
    <form action="dept-save" method="post" enctype="multipart/form-data">
        <p>
            ID : <input type="text" name="deptId">
        </p>
        <p>
            Image : <input type="file" name="file">
        </p>
        <p>
            <input type="submit" value="Upload" name="submit">
        </p>
    </form>
</body>
</html>