<?php

require "connection.php";

$cod = $_POST["cod"];
$email = $_POST["email"]; 
$data = $_POST["data"];
$durata = (int)$_POST["durata"];
$tip = $_POST["tip"];
$denumire = $_POST["denumire"];
$valabilitate = $_POST["valabilitate"];
$categorie = $_POST["categorie"]; 
$detalii = $_POST["detalii"];
$descriere = $_POST["descriere"];

	    $insertProdus = $connect->prepare("insert into produse(denumire, valabilitate, ID_CATEGORIE,detalii) values (?,?,?,?);");
	    $idCategorie = 4;
	    $insertProdus->bind_param('sdds', $denumire, $valabilitate, $idCategorie,$detalii);
	    $insertProdus->execute();  
		$idProdus =(int) $insertProdus->insert_id;


	$userIdByEmail = $connect->prepare("select ID_USER from users where email like ?;");
    $userIdByEmail->bind_param('s', $email);
    $userIdByEmail->execute();
    $userIdByEmail->store_result();
	$userIdByEmail->bind_result($idUser);
	$userIdByEmail->fetch(); 
 
 	$insertAd = $connect->prepare("insert into anunturi( data_introducerii, durata_in_zile, tip, ID_USER, ID_PRODUS, ID_STATUS) values (?,?,?,?,?,?);");
	$idStatus = 1; 
	$durata = 5;
	$insertAd->bind_param('sdsddd', $data, $durata, $tip, $idUser, $idProdus, $idStatus);
	$insertAd->execute();  
 ?>