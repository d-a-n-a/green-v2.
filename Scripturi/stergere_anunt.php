<?php
require "connection.php";
require "constants.php";

$idAnunt = $_POST['id'];
$id = (int)$idAnunt; 

$result = $connect->prepare("DELETE FROM anunturi where ID_ANUNT = ?;");
$result->bind_param('d', $id);
$result->execute();
if($result->execute())
	echo SUCCESS;
else
	echo "fail";
$connect->close();

?>