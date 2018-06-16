<?php
require "connection.php";
require "constants.php";

$idTransaction = $_POST["idTransaction"];
$idStatusTransaction = $_POST["idStatusTransaction"];
$idAd = $_POST["idAd"];
$idStatusAd = $_POST["idStatusAd"];

$update_status_ad = $connect->prepare("update anunturi set ID_STATUS = ? where ID_ANUNT = ?");
$update_status_ad -> bind_param('ii', $idStatusAd, $idAd);
$update_status_ad->execute();

$update_status_transaction = $connect->prepare("update tranzactii set ID_STATUS = ? where ID_TRANZACTIE = ?");
$update_status_transaction -> bind_param('ii', $idStatusTransaction, $idTransaction);

 $update_status_transaction->execute();
if($update_status_transaction->execute())
	{
	echo SUCCESS;}
else
	{
	echo "fail";
	}
	 
?>
 