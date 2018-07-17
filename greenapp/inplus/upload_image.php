<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
 $image = $_POST['image'];
 $email = $_POST['email'];
 require_once('connection.php');
 
 $sql = "INSERT INTO users (fotografie) VALUES (?) where email like ?;";
 
 $stmt = mysqli_prepare($connect,$sql);
 
 mysqli_stmt_bind_param($stmt,"ss",$image,$email);
 mysqli_stmt_execute($stmt);
 
 $check = mysqli_stmt_affected_rows($stmt);
 
 if($check == 1){
 echo "Image Uploaded Successfully";
 }else{
 echo "Error Uploading Image";
 }
 mysqli_close($connect);
 }else{
 echo "Error";
 }