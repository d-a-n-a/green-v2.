<?php
 require "connection.php";
 require "constants.php";

 $lat = $_POST["latitudine"];
 $lng = $_POST["longitudine"];
 $cerere = $_POST["cerere"];
 $oferta = $_POST["oferta"];
 $alimente = $_POST["alimente"];
 $haine = $_POST["haine"];
 $altele = $_POST["altele"];
 $distanta = $_POST["distanta"];
 $myArray = array();  

 //echo $lat.$lng.$cerere.$oferta.$alimente.$haine.$altele.$distanta;
if ($result = $connect->query("SELECT  u.fotografie, u.username, l.strada, (SELECT avg(r.nota) from  reviews r, users uu
where r.ID_USER = uu.ID_USER and u.ID_USER = uu.ID_USER) as review,( 6371 * acos( cos( radians($lat) ) * cos( radians( l.latitudine) ) 
							* cos( radians( l.longitudine ) - radians($lng) ) + sin( radians($lat) ) * sin(radians(l.latitudine)) ) ) AS distanta 
							FROM users u, locatii l, anunturi a, categorii c, produse p
							WHERE  u.ID_LOCATIE = l.ID_LOCATIE and a.ID_USER = u.ID_USER and (a.tip = '$cerere'
							 or a.tip = '$oferta' )
							  and p.ID_PRODUS = a.ID_PRODUS and 
							c.ID_CATEGORIE = p.ID_CATEGORIE and (c.denumire = '$haine' or c.denumire = '$alimente' or c.denumire = '$altele') 
							HAVING distanta < $distanta
							ORDER BY distanta;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}
?>

