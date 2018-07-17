<?php
 require "connection.php";
 require "constants.php";

 $lat = $_POST["latitudine"];
 $lng = $_POST["longitudine"]; 
 $distanta = $_POST["distanta"];
 $username = $_POST["username"];
 $myArray = array();  
//echo $distanta;
 if ($result = $connect->query("SELECT l.strada, l.latitudine, l.longitudine, u.username, u.email,  u.fotografie,
 (6371 * 2 * ASIN(SQRT(POWER(SIN(RADIANS($lat - ABS(l.latitudine))), 2) + COS(RADIANS($lat)) * COS(RADIANS(ABS(l.latitudine))) * POWER(SIN(RADIANS($lng - l.longitudine)), 2)))) AS distanta 
				from   users u,   locatii l
                where u.ID_LOCATIE = l.ID_LOCATIE  and u.username not like '$username'
                HAVING distanta < $distanta
				ORDER BY distanta;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
}
    echo json_encode($myArray);

?>  
