<?php

require "connection.php";
require "constants.php";

$idTranzactie = $_POST['id'];
$id = (int)$idTranzactie; 

$result = $connect->prepare("DELETE FROM tranzactii where ID_TRANZACTIE = ?;");
$result->bind_param('d', $id);
$result->execute();
if($result->execute())
	echo SUCCESS;
else
	echo "fail";
$connect->close();
?>