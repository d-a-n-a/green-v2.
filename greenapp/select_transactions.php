<?php

require "connection.php";
require "constants.php";
 
$username = $_REQUEST["username"]??'';

$myArray = array();

if ($result = $connect->query("SELECT tt.ID_TRANZACTIE, tt.locatie_predare, tt.data_predare, tt.ora_predare, tt.ID_ANUNT, (SELECT distinct s.tip from statusuri s, tranzactii t where t.ID_STATUS = s.ID_STATUS and tt.ID_STATUS = s.ID_STATUS) as status, (SELECT distinct u.username from users u where u.ID_USER = ID_EXPEDITOR) as expeditor, (SELECT distinct u.username from users u where u.ID_USER = ID_DESTINATAR) as destinatar, (SELECT distinct p.denumire  from produse p, tranzactii t, anunturi a WHERE t.ID_ANUNT = a.ID_ANUNT AND a.ID_PRODUS = p.ID_PRODUS) as denumire FROM tranzactii tt
        WHERE (SELECT DISTINCT u.username  from users u where u.ID_USER = ID_DESTINATAR) like '".$username."' OR
        (SELECT DISTINCT u.username  from users u where u.ID_USER = ID_EXPEDITOR) like '".$username."';")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}

$result->close();
$connect->close();
?>
