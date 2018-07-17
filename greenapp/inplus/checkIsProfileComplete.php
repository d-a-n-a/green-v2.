<?php

require "connection.php";
require "constants.php";


$token = $_POST["token"];
$password = $_POST["password"];

$mysql_query_userExist = $connect->prepare('select * from users where username like ? or email like ?');
$mysql_query_userExist -> bind_param('ss', $token, $token);
$mysql_query_userExist -> execute();
$mysql_query_userExist -> store_result();
if($mysql_query_userExist->num_rows == 0){
	echo INVALID;
}
else {

$mysql_query = $connect->prepare('select * from users where (username like ? and parola like ?) or (email like ? and parola like ?);');
$mysql_query->bind_param('ssss', $token, $password, $token, $password); 
$mysql_query->execute();
$mysql_query->store_result(); 

if($mysql_query->num_rows == 1)
	{
		echo RESULT_OK;
	}
else
	{
		echo INCORRECT_PASSWORD;
	}
}
?>