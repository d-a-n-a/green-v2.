<?php

require "connection.php";
require "constants.php";


$country = $_POST["country"];
$city = $_POST["city"];
$address = $_POST["address"];
$email = $_POST["email"]; 
$latitudine = $_POST["latitudine"];
$longitudine = $_POST["longitudine"];

$userLocation = $connect->prepare('select ID_LOCATIE from users where email like  ?;');
$userLocation->bind_param('s', $email);
$userLocation->execute(); 
$userLocation->store_result();
$userLocation->bind_result($idLocation);
$userLocation->fetch(); 

if($idLocation === NULL){
	 
	 $lat = (double)$latitudine;
	 $lng = (double)$longitudine;
	$insertQuery = $connect->prepare('insert into locatii(oras, strada, tara, latitudine, longitudine) values (?,?,?,?,?);');
	$insertQuery->bind_param('sssdd', $city, $address, $country, $lat, $lng);
	$insertQuery->execute();
	$locationId = $insertQuery->insert_id;
	
	if($insertQuery->execute()){  
			$updateUserLocation = $connect->prepare('update users set ID_LOCATIE = ? where email like ?; ');
			$updateUserLocation->bind_param('ss', $locationId, $email);
			$updateUserLocation->execute();
			
	}
		else
		{
			echo "fai!!!l";
		}
}
else{
	 $lat = (double)$latitudine;
	 $lng = (double)$longitudine;
	$updateQuery = $connect->prepare('update locatii set oras=?, strada=?, tara=?, latitudine = ?, longitudine = ? where ID_LOCATIE = ?;');
	$updateQuery->bind_param('sssddi', $city, $address, $country, $lat, $lng,$idLocation);
	$updateQuery->execute();

	if($updateQuery->execute())
		echo "success";
	else
		echo "fail";
}
 
$connect->close(); 
?>