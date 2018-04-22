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

// echo $lat.$lng.$cerere.$oferta.$alimente.$haine.$altele.$distanta;
if ($result = $connect->query("SELECT l.strada, a.data_introducerii as dataIntroducerii, a.tip as tipAnunt,  u.username, p.denumire, s.tip as tipStatus, p.detalii as descriereProdus, p.valabilitate, a.descriere as detaliiAnunt,c.denumire as categorie, p.imagine, u.email,  
( 6371 * acos( cos( radians($lat) ) * cos( radians( l.latitudine) ) * cos( radians( l.longitudine ) - radians($lng)) ) + sin( radians($lat) ) * sin(radians(l.latitudine))  ) AS distanta
				from anunturi a, statusuri s, produse p, users u, categorii c, locatii l
                where a.ID_USER = u.ID_USER and a.ID_PRODUS = p.ID_PRODUS and a.ID_STATUS = s.ID_STATUS and c.ID_CATEGORIE = p.ID_CATEGORIE and  u.ID_LOCATIE = l.ID_LOCATIE and s.tip not like 'indisponibil' and (a.tip = 'ofer' or a.tip = 'doresc')
                            
                          (c.denumire = 'haine' or c.denumire = 'alimente' or c.denumire = 'lactate')
                            HAVING distanta < $distanta
							ORDER BY distanta;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }

    echo json_encode($myArray);
}
else
{
	echo "nu intra pe if";
}
?>
 