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
if ($result = $connect->query("SELECT  distinct u.username, u.fotografie,  l.strada, avg(r.nota) as review, ( 6371 * acos( cos( radians($lat) ) * cos( radians( l.latitudine) ) * cos( radians( l.longitudine ) - radians($lng) ) + sin( radians($lat) ) * sin(radians(l.latitudine)) ) ) AS distanta, p.ID_CATEGORIE, c.denumire
							FROM users u, locatii l, anunturi a,  produse p, categorii c, reviews r
							WHERE  u.ID_LOCATIE = l.ID_LOCATIE and a.ID_USER = u.ID_USER  and p.ID_PRODUS = a.ID_PRODUS and p.ID_CATEGORIE = c.ID_CATEGORIE and r.ID_USER = u.ID_USER and (a.tip = 'ofer' or a.tip = 'doresc')
                            and 
                            (c.denumire = 'haine' or c.denumire = 'alimente' or c.denumire = 'lactate') 
							HAVING distanta < $distanta
							ORDER BY distanta;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}
?>

<!-- /*SELECT  u.fotografie, u.username, l.strada,  avg(r.nota) as review, ( 6371 * acos( cos( radians(46.4028350000) ) * cos( radians( l.latitudine) ) * cos( radians( l.longitudine ) - radians(46.4028350000) ) + sin( radians(46.4028350000) ) * sin(radians(l.latitudine)) ) ) AS distanta 
							FROM users u, locatii l, anunturi a, categorii c, produse p, reviews r
							WHERE  u.ID_LOCATIE = l.ID_LOCATIE and a.ID_USER = u.ID_USER  and p.ID_PRODUS = a.ID_PRODUS and r.ID_USER = u.ID_USER and c.ID_CATEGORIE = p.ID_CATEGORIE and
                            (a.tip = 'cerere' or a.tip = 'oferta' )
							  and
							 (c.denumire = 'haine' or c.denumire = 'alimente' or c.denumire = 'altele')
                              HAVING distanta < 9999999990
							ORDER BY distanta*/ -->