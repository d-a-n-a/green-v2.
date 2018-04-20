<?php

require "connection.php";
require "constants.php";

$autor = $_POST["autor"];
$user = $_POST["user"];
$tranzactie = $_POST["tranzactie"];

$idTranzactie = (int)$tranzactie;
 
$reviewExists = $connect->prepare('select * from reviews where ID_TRANZACTIE = ? and (SELECT ID_USER from users where username  = ?) = ID_AUTOR and (SELECT ID_USER from users where username  = ?) = ID_USER ;');
$reviewExists -> bind_param('iss', $idTranzactie, $autor, $user);
$reviewExists -> execute();
$reviewExists -> store_result(); 

if($reviewExists->num_rows === 0){
	echo SUCCESS;  
}
else
	echo "fail";

?>