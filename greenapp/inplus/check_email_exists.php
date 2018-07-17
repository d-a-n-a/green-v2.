<?php
 require "connection.php";
 require "constants.php";

 $email = $_POST["email"];

  $query = $connect->prepare("select count(*) from users where email like ?;");
        $query->bind_param('s', $email);
        $query->execute();
        $query->store_result();
        $query->bind_result($existent);
        $query->fetch(); 
        echo $existent;
 ?>