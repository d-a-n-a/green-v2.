<?php

require "connection.php";
require "constants.php";
 
$username = $_POST["username"];
$myArray = array();

if ($result = $connect->query("SELECT  DISTINCT t.ID_TRANZACTIE, a.ID_ANUNT,t.data_predare, t.locatie_predare, t.ora_predare, p.denumire,s.tip as status,t.ID_EXPEDITOR as expeditor,(SELECT username from users where ID_USER = expeditor) as username_expeditor, t.ID_DESTINATAR as destinatar, (SELECT username from users where ID_USER = destinatar) as username_destinatar FROM tranzactii t, anunturi a, users u, produse p, statusuri s WHERE t.ID_ANUNT = a.ID_ANUNT and a.ID_PRODUS = p.ID_PRODUS and t.ID_STATUS = s.ID_STATUS  and u.ID_USER = a.ID_USER and (t.ID_EXPEDITOR = (SELECT  ID_USER from users where username = '".$username."') or t.ID_DESTINATAR = (SELECT  ID_USER from users where username = '".$username."'));"))
    {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
          echo json_encode($myArray);
}

 
$connect->close();
?>
