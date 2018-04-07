<?php

        require "connection.php";
        require "constants.php";

        $username = $_POST["username"];

        $query = $connect->prepare("select fotografie from users where username like ?;");
        $query->bind_param('s', $username);
        $query->execute();
        $query->store_result();
        $query->bind_result($foto);
        $query->fetch(); 
        echo $foto;

        ?>