<?php

require "connection.php";
require "constants.php";
 
$username = $_POST["username"];
$myArray = array();
if ($result = $connect->query("SELECT  n.ID_NOTIFICARE as id, n.descriere, n.data, nu.citit from notificari n, users u, notificari_useri nu 
							   WHERE n.ID_NOTIFICARE = nu.ID_NOTIFICARE 
							   		 and nu.ID_USER = u.ID_USER 
							   		 AND nu.citit = 0 
							   		 AND u.username  like '".$username."';")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}

$result->close();
$connect->close(); 
?>