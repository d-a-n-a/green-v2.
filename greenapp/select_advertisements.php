<?php

require "connection.php";
require "constants.php";
 
$username = $_POST["username"];
$myArray = array();
if ($result = $connect->query("SELECT l.strada, a.ID_ANUNT as id, a.data_introducerii as dataIntroducerii, a.tip as tipAnunt,  u.username, p.denumire, s.tip as tipStatus, p.detalii as descriereProdus, p.valabilitate, a.descriere as detaliiAnunt,c.denumire as categorie, p.imagine, u.email from anunturi a, statusuri s, produse p, users u, categorii c, locatii l where a.ID_USER = u.ID_USER and a.ID_PRODUS = p.ID_PRODUS and a.ID_STATUS = s.ID_STATUS and c.ID_CATEGORIE = p.ID_CATEGORIE and u.ID_LOCATIE = l.ID_LOCATIE and s.tip not like 'indisponibil' and u.username not like '".$username."'
;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}

$result->close();
$connect->close(); 
?>