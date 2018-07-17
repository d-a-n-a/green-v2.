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
		 $avgNota = 0;
$array = array("review" => $avgNota,"foto" => $foto);
	for($ii = 1; $ii <= 5; $ii++){ 
		 
		$queryR = $connect->prepare("SELECT count(*) from users u, reviews r where  r.ID_USER = u.ID_USER
		and r.nota = ? and u.username like ? ;");  
		$queryR->bind_param('is',$ii, $username);
		$queryR->execute();
		$queryR->store_result();
		$queryR->bind_result($nri);
		$queryR->fetch();

		array_push($array, $nri);
} 
echo json_encode($array);
  
?>