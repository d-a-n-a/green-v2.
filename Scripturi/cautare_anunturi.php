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
 if ($result = $connect->query("SELECT l.strada, a.data_introducerii as dataIntroducerii, a.tip as tipAnunt,  u.username, p.denumire, s.tip                        as tipStatus, p.detalii as descriereProdus, p.valabilitate, a.descriere as detaliiAnunt,c.denumire as                        categorie, p.imagine, u.email,  (6371 * 2 * ASIN(SQRT(POWER(SIN(RADIANS($lat - ABS(l.latitudine))), 2) +                        COS(RADIANS ($lat)) * COS(RADIANS(ABS(l.latitudine))) * POWER(SIN(RADIANS($lng - l.longitudine)), 2)))) AS                        distanta, l.latitudine, l.longitudine
				         from anunturi a, statusuri s, produse p, users u, categorii c, locatii l
                         where a.ID_USER = u.ID_USER and a.ID_PRODUS = p.ID_PRODUS and a.ID_STATUS = s.ID_STATUS and c.ID_CATEGORIE = p.ID_CATEGORIE and  u.ID_LOCATIE = l.ID_LOCATIE and s.tip not like 'indisponibil' and (a.tip = '$oferta' 
                         or a.tip = '$cerere') and (c.denumire = '".$haine."' or c.denumire = '".$alimente."' or c.denumire = '".$altele."')
                          HAVING distanta < $distanta
						  ORDER BY distanta;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
}
    echo json_encode($myArray);
 
$connect->close(); 
?>  
