<?php

require "connection.php";
require "constants.php";


$country = $_POST["country"];
$city = $_POST["city"];
$address = $_POST["address"];
$email = $_POST["email"]; 
 //echo $country.$city.$address.$email;
$userLocation = $connect->prepare('select ID_LOCATIE from users where email like  ?;');
$userLocation->bind_param('s', $email);
$userLocation->execute(); 
$userLocation->store_result();
$userLocation->bind_result($idLocation);
$userLocation->fetch(); 

if($idLocation === 0){
	 
	$insertQuery = $connect->prepare('insert into locatii(oras, strada, tara) values (?,?,?);');
	$insertQuery->bind_param('sss', $city, $address, $country);
	$insertQuery->execute();
	$locationId = $insertQuery->insert_id;
	
//	if($insertQuery->execute()){  
			$updateUserLocation = $connect->prepare('update users set ID_LOCATIE = ? where email like ?; ');
			$updateUserLocation->bind_param('ss', $locationId, $email);
			$updateUserLocation->execute();
			
//	}
//		else
//		{
//			echo "fail";
//		}
}
else{
	$updateQuery = $connect->prepare('update locatii set oras=?, strada=?, tara=? where ID_LOCATIE = ?;');
	$updateQuery->bind_param('sssd', $city, $address, $country, $idLocation);
	$updateQuery->execute();

	if($updateQuery->execute())
		echo "success";
	else
		echo "fail";
}

?>