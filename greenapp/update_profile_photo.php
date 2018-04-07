<?php
include 'connection.php';

 $DefaultId = 0;
 
 $username = $_POST['username'];
 $ImageData = $_POST['image']; 
 
    $query = $connect->prepare("select ID_USER from users where username like ?;");
    $query->bind_param('s', $username);
    $query->execute();
    $query->store_result();
	$query->bind_result($iduser);
	$query->fetch(); 

 $name = "profile-photo".$iduser;

 $ImagePath = "images/$name.png";
 $ServerURL = "/greenapp/$ImagePath"; 

 $InsertSQL = "update  users set fotografie = ?  where ID_USER = ?;";
 $insertPhoto = $connect->prepare($InsertSQL); 
 $insertPhoto->bind_param('sd', $ServerURL,$iduser);
 $insertPhoto->execute();   
  file_put_contents($ImagePath,base64_decode($ImageData));
?>
