<?php

require "connection.php";

$cod = $_POST["cod"];
$email = $_POST["email"]; 
$data = $_POST["data_introducerii"];
$durata = (int)$_POST["durata"];
$tip = $_POST["tip"];
$denumire = $_POST["denumire"];
$valabilitate = $_POST["valabilitate"];
$categorie = $_POST["categorie"]; 
$detalii = $_POST["detalii"];
$descriere = $_POST["descriere"]; 

if(strcmp($cod, "insert_add")==0){ 
	if(strcmp($categorie, "alimente")==0)
	$id_categorie = 3;
	else
		if(strcmp($categorie, "haine")==0)
			$id_categorie = 1;
			else
				if(strcmp($categorie, "altele")==0)
					$id_categorie = 8;

	$sql ="insert into produse(denumire, valabilitate, ID_CATEGORIE,detalii) values ('$denumire','$valabilitate',$id_categorie,'$detalii');";
	if ($connect->query($sql) === TRUE) {
    	$idProdus = $connect->insert_id;
	} 
	else {
    	echo "Eroare: ".$connect->error ;
	}
	
	$userIdByEmail = $connect->prepare("select ID_USER from users where email like ?;");
    $userIdByEmail->bind_param('s', $email);
    $userIdByEmail->execute();
    $userIdByEmail->store_result();
	$userIdByEmail->bind_result($idUser);
	$userIdByEmail->fetch();  
 	$insertAd = $connect->prepare("insert into anunturi( data_introducerii, durata_in_zile, tip, descriere, ID_USER, ID_PRODUS, ID_STATUS) values (?,?,?,?,?,?,?);");
	$idStatus = 1;  
	$insertAd->bind_param('sdssddd', $data, $durata, $tip, $descriere, $idUser, $idProdus, $idStatus);
	$insertAd->execute();   
}
else
	if(strcmp($cod,"demand")==0){
	$userIdByEmail = $connect->prepare("select ID_USER from users where email like ?;");
    $userIdByEmail->bind_param('s', $email);
    $userIdByEmail->execute();
    $userIdByEmail->store_result();
	$userIdByEmail->bind_result($idUser);
	$userIdByEmail->fetch();  
    if(strcmp($categorie, "alimente")==0)
	$id_categorie = 3;
	else
		if(strcmp($categorie, "haine")==0)
			$id_categorie = 1;
			else
				if(strcmp($categorie, "altele")==0)
					$id_categorie = 8;  
	$img = "/greenapp/images/wanted.png";
    $sql ="insert into produse(denumire, valabilitate, ID_CATEGORIE,detalii,imagine) values ('$denumire','$valabilitate',$id_categorie,'$detalii', '$img');";
	if ($connect->query($sql) === TRUE) { 
    	$idProdus = $connect->insert_id;
	} 
	else {
    	echo "Eroare: ".$connect->error ;
	}

 	$insertAd = $connect->prepare("insert into anunturi( data_introducerii, durata_in_zile, tip, descriere, ID_USER, ID_PRODUS, ID_STATUS) 									  values (?,?,?,?,?,?,?);");
	$idStatus = 1;  
	$insertAd->bind_param('sdssdsd', $data, $durata, $tip, $descriere, $idUser, $idProdus, $idStatus);
	$insertAd->execute();   
	}
	     
$connect->close(); 
?>

