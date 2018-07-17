<?php

require "connection.php";
require "constants.php";

$idStatus = $_POST["idStatus"];
$idAd = $_POST["idAd"];

$update_status = $connect->prepare("update anunturi set ID_STATUS = ? where ID_ANUNT = ?");
$update_status -> bind_param('dd', $idStatus, $idAd);
if($update_status->execute())
	{
	echo SUCCESS;}
else
	{
	echo "fail";
	}

?>