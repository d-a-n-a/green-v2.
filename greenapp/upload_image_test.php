<?php
include 'connection.php';

 $DefaultId = 0;
 
 $ImageData = $_POST['image']; 
 
    $result = $connect -> query("SELECT max(ID_PRODUS) FROM produse");
    $row = mysqli_fetch_row($result);
    $highest_id = $row[0]; 

 $name = "product-upload".$highest_id;

 $ImagePath = "images/$name.png";
 $ServerURL = "/greenapp/$ImagePath"; 

 $InsertSQL = "update  produse set imagine = ?  where ID_PRODUS = ?;";
 $insertPhoto = $connect->prepare($InsertSQL); 
 $insertPhoto->bind_param('sd', $ServerURL,$highest_id);
 $insertPhoto->execute();   
  file_put_contents($ImagePath,base64_decode($ImageData));
?>
