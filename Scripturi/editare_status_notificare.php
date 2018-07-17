<?php
require "connection.php";
require "constants.php";

$id = $_POST["id"];
$username = $_POST["username"];



$sql = "update notificari_useri nu, users u, notificari n
        set nu.citit = 1 
        where n.ID_NOTIFICARE = $id 
              and u.username like '".$username."'
              and u.ID_USER = nu.ID_USER
              and nu.ID_NOTIFICARE = n.ID_NOTIFICARE
              ;";

if ($connect->query($sql) === TRUE) {
    echo SUCCESS;
} else {
    echo FAIL;
} 

$connect->close(); 
?>