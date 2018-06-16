<?php

require "connection.php";
require "constants.php";
  
$myArray = array();
if ($result = $connect->query("SELECT * from locatii;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}

$result->close();
$connect->close(); 
?>