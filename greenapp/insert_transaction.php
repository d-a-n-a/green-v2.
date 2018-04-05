<?php

require "connection.php";
require "constants.php";

$expeditor = $_POST["expeditor"];
$destinatar = $_POST["destinatar"]; 
$locatie = $_POST["locatie"];
$data = $_POST["data"];
$ora = $_POST["ora"];
$idAnunt = $_POST["idAnunt"]; 


	$userIdByUsername = $connect->prepare("select ID_USER from users where username like ?;");
    $userIdByUsername->bind_param('s', $expeditor);
    $userIdByUsername->execute();
    $userIdByUsername->store_result();
	$userIdByUsername->bind_result($idExpeditor);
	$userIdByUsername->fetch(); 

	$userIdByUsername = $connect->prepare("select ID_USER from users where username like ?;");
    $userIdByUsername->bind_param('s', $destinatar);
    $userIdByUsername->execute();
    $userIdByUsername->store_result();
	$userIdByUsername->bind_result($idDestinatar);
	$userIdByUsername->fetch(); 

	    $insertTransaction = $connect->prepare("insert into tranzactii(locatie_predare, data_predare, ora_predare,
		    ID_EXPEDITOR, ID_DESTINATAR, ID_ANUNT, ID_STATUS) values (?,?,?,?,?,?,?);");
	    $idStatus = 5;
	    $insertTransaction->bind_param('sssdddd',$locatie, $data, $ora,$idExpeditor,$idDestinatar,$idAnunt,$idStatus);
	    $insertTransaction->execute();  
		$idTranzactie =(int) $insertTransaction->insert_id;
		echo $idTranzactie;

 
 ?>
 