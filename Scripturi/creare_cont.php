<?php

require "connection.php";
require "constants.php";


$last_name = $_POST["lastname"];
$first_name = $_POST["firstname"];
$username = $_POST["username"];
$password = $_POST["password"];
$phone = $_POST["phone"];
$email = $_POST["email"];
$date = $_POST["date"];

$email_exists=$connect->prepare('select * from users where email like ?;');
$email_exists->bind_param('s',$email);
$email_exists->execute();
$email_exists->store_result();
if($email_exists->num_rows > 0)
{
	echo EMAIL_EXISTENT;
}

else{
	$username_exists = $connect->prepare('select * from users where username like ?');
	$username_exists->bind_param('s', $username);
	$username_exists->execute();
	$username_exists->store_result();
	if($username_exists->num_rows > 0)
	{
		echo USERNAME_INVALID;
	}
	else
	{
		$hash_pass = md5($password);
		$insert_row = $connect->prepare('insert into users(nume, prenume, email, username, parola, telefon, data_inregistrarii) values(?,?,?,?,?,?,?)');
		$insert_row->bind_param('sssssss', $last_name, $first_name,$email,$username,$hash_pass,$phone,$date);
		$insert_row->execute(); 
	}
	
}

?>