<?php

require "connection.php";
require "constants.php";
$email = $_POST["email"];
 
$myArray = array();
if ($result = $connect->query("SELECT a.ID_ANUNT as id, a.data_introducerii as dataIntroducerii, a.tip as tipAnunt, u.username, p.denumire, s.tip as tipStatus, p.imagine from anunturi a, statusuri s, produse p, users u where a.ID_USER = u.ID_USER and a.ID_PRODUS = p.ID_PRODUS and a.ID_STATUS = s.ID_STATUS and u.email = '".$email. "' and a.tip = 'oferta';")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
    echo json_encode($myArray);
}

$result->close();
$connect->close(); 
?>