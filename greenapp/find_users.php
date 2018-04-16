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
/*$result = $connect->prepare("SELECT a.ID_ANUNT, u.username, ( 6371 * acos( cos( radians(?) ) * cos( radians( l.latitudine) ) 
							* cos( radians( l.longitudine ) - radians(?) ) + sin( radians(?) ) * sin(radians(l.latitudine)) ) ) AS distanta 
							FROM users u, locatii l, anunturi a, categorii c, produse p
							WHERE  u.ID_LOCATIE = l.ID_LOCATIE and a.ID_USER = u.ID_USER and a.tip = 'cerere' and p.ID_PRODUS = a.ID_PRODUS and 
							c.ID_CATEGORIE = p.ID_CATEGORIE and c.denumire = 'lactate'
							HAVING distanta < 100
							ORDER BY distanta;");

$result->bind_param('ddd', $lat, $lng, $lat);
$result->execute();
 
  		while($row = $result->mysql_fetch_array(MYSQLI_ASSOC)) {
		            $myArray[] = $row;
		    }
		echo json_encode($myArray);
	 
*/
if ($result = $connect->query("SELECT  u.fotografie, u.username, l.strada, (SELECT avg(r.nota) from  reviews r, users uu
where r.ID_USER = uu.ID_USER and u.ID_USER = uu.ID_USER) as review,( 6371 * acos( cos( radians($lat) ) * cos( radians( l.latitudine) ) 
							* cos( radians( l.longitudine ) - radians($lng) ) + sin( radians($lat) ) * sin(radians(l.latitudine)) ) ) AS distanta 
							FROM users u, locatii l, anunturi a, categorii c, produse p
							WHERE  u.ID_LOCATIE = l.ID_LOCATIE and a.ID_USER = u.ID_USER and a.tip = 'cerere' and p.ID_PRODUS = a.ID_PRODUS and 
							c.ID_CATEGORIE = p.ID_CATEGORIE and c.denumire = 'lactate'
							HAVING distanta < 3000
							ORDER BY distanta;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}
?>

