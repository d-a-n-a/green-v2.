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


	$queryF = $connect->prepare("SELECT fotografie from   users where username like ?;");
	$queryF->bind_param('s', $username);
	$queryF->execute();
	$queryF->store_result();
	$queryF->bind_result($foto);
	$queryF->fetch();

	if(is_null($avgNota ))
		 $avgNota = -1;
	$array = array("review" => $avgNota,"foto" => $foto);
    echo json_encode($array);
	



?>