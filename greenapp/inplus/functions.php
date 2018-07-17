<?php

require "connection.php";
require "constants.php";

function userIdByEmail($email) {
    $query = $connect->prepare("select ID_USER from users where email like ?;");
    $query->bind_param('s', $email);
    $query->execute();
    $query->store_result();
	$query->bind_result($idUser);
	$query->fetch(); 

	if(id_nan($idUser))
	{
		echo INVALID;
	}
	else
	{
		return $idUser;
	}
}

function categoryId($categorie){
	$query = $connect->prepare("select ID_CATEGORIE from categorii where denumire like ?;");
	$query->bind_param('s', $categorie);
	$query->execute();
	$query->store_result();
	$query->bind_result($idCategorie);
	$query->fetch();
	return $idCategorie; 
}

function insertProduct($denumire, $valabilitate, $categorie){
	    $query = $connect->prepare("insert into produse(denumire, valabilitate, ID_CATEGORIE) values (?,?,?);");
	    $idCategorie = categoryId($categorie);
	    $query->bind_param('sdd', $denumire, $valabilitate, $idCategorie);
	    $query->execute();  
		$idProdus = $query->insert_id;
		return $idProdus;
}

function insertAd($email, $titlu, $data, $durata, $tip){
	$idProdus = insertProduct($denumire, $valabilitate, $categorie);
	$idUser = userIdByEmail($email);
	$query = $connect->prepare("insert into anunturi(titlu, data_intoducerii, durata_in_zile, tip, id_user, id_produs, id_status) values (?,?,?,?,?,?,?);");
	$query->bind_param('ssdsddd', $titlu, $data, (int)$durata, $tip, $idUser,(int)$idProdus, 1);
	$query->execute();  
	if($query->execute())
		echo "succes";
	else
		echo "fail";
}

?>