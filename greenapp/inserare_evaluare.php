<?php

require "connection.php";
require "constants.php";

$autor = $_POST["autor"];
$user = $_POST["user"]; 
$detalii = $_POST["detalii"];
$nota = $_POST["nota"]; 
$data = $_POST["data"];
$idTranzactie = $_POST["tranzactie"];

$fNota = (float)$nota;
$iTranzactie = (int)$idTranzactie;

	$userIdByUsername = $connect->prepare("select ID_USER from users where username like ?;");
    $userIdByUsername->bind_param('s', $autor);
    $userIdByUsername->execute();
    $userIdByUsername->store_result();
	$userIdByUsername->bind_result($idAutor);
	$userIdByUsername->fetch(); 

	$userIdByUsername = $connect->prepare("select ID_USER from users where username like ?;");
    $userIdByUsername->bind_param('s', $user);
    $userIdByUsername->execute();
    $userIdByUsername->store_result();
	$userIdByUsername->bind_result($idUser);
	$userIdByUsername->fetch(); 


	    $insertReview = $connect->prepare("insert into reviews(ID_AUTOR, ID_USER, data_postarii, detalii, nota, ID_TRANZACTIE) values (?,?,?,?,?,?);");
 	    $insertReview->bind_param('iissdi',$idAutor, $idUser, $data, $detalii, $fNota, $iTranzactie);
	    //$insertReview->execute();  
		 if($insertReview->execute())
		 	echo SUCCESS;
		 else
		 	echo "fail ".mysqli_stmt_error($insertReview);

?>

 