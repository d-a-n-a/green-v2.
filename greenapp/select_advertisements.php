<?php

require "connection.php";
require "constants.php";
 
$username = $_POST["username"];
$myArray = array();
if ($result = $connect->query("SELECT l.strada, a.ID_ANUNT as id, a.data_introducerii as dataIntroducerii, a.tip as tipAnunt, u.username, p.denumire, s.tip as tipStatus, p.detalii as descriereProdus, p.valabilitate, a.descriere as detaliiAnunt,c.denumire as categorie from anunturi a, statusuri s, produse p, users u, categorii c, locatii l where a.ID_USER = u.ID_USER and a.ID_PRODUS = p.ID_PRODUS and a.ID_STATUS = s.ID_STATUS and c.ID_CATEGORIE = p.ID_CATEGORIE and u.username not like '".$username."'
and u.ID_LOCATIE = l.ID_LOCATIE;")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}

$result->close();
$connect->close();
/*$sth = mysqli_query("SELECT a.ID_ANUNT, a.data_introducerii, a.tip, u.username, p.denumire, s.ID_STATUS from anunturi a, statusuri s, produse p, users u where a.ID_USER = u.ID_USER and a.ID_PRODUS = p.ID_PRODUS and a.ID_STATUS = s.ID_STATUS;");
$rows = array();
while($r = mysqli_fetch_assoc($sth)) {
    $rows[] = $r;
}
echo json_encode($rows);*/
?>