<?php
 require "connection.php";
 require "constants.php";

 $username = $_POST["username"];

 	$query = $connect->prepare("SELECT AVG(r.nota) from reviews r, users u WHERE r.ID_USER=u.ID_USER and u.username like ?;");
	$query->bind_param('s', $username);
	$query->execute();
	$query->store_result();
	$query->bind_result($avgNota);
	$query->fetch();
	if(is_null($avgNota ))
		echo 'nu are reviews';
	else
		echo $avgNota; 



?>