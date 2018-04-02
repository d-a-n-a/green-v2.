<?php

require "connection.php";
require "constants.php";


$token = $_POST["token"];
$password = $_POST["password"];   

 
$mysql_query_userExist = $connect->prepare('select * from users where username like ? or email like ?');
$mysql_query_userExist -> bind_param('ss', $token, $token);
$mysql_query_userExist -> execute();
$mysql_query_userExist -> store_result(); 
if($mysql_query_userExist->num_rows === 0){
	echo INVALID;
}
else {

$hash_pass = md5($password);   
 
$mysql_query = $connect->prepare('select ID_USER, nume, prenume, email, username, parola, fotografie, biografie, telefon, data_inregistrarii, ID_LOCATIE from users where (username like ? and parola like ?) or (email like ? and parola like ?);');
$mysql_query->bind_param('ssss', $token, $hash_pass, $token, $hash_pass); 
$mysql_query->execute();  
$mysql_query->store_result(); 
 

$mysql_query->bind_result($eid, $enume, $eprenume, $eemail, $eusername, $eparola, $efotografie, $ebiografie, $etelefon, $edata, $elocatie);

if($mysql_query->num_rows === 1)
	{ 
	$mysql_query->fetch();
		$userData  = array($enume, $eprenume, $eemail, $eusername, $eparola, $efotografie, $ebiografie, $etelefon, $edata, $elocatie);

		echo json_encode($userData);
 
	}

else
	{

		echo INCORRECT_PASSWORD;
	}
}
?>