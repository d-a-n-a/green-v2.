<?php

require "connection.php";
require "constants.php";
$username = $_POST["username"];
 
$myArray = array();
 

 
if($result = $connect->query("SELECT r.data_postarii as data, r.detalii, r.nota, u.username from users u, reviews r where u.ID_USER = r.ID_AUTOR AND r.ID_USER = (SELECT u.ID_USER from users u WHERE u.username='".$username."');"))

 {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}


$result->close();
$connect->close(); 

?>

 