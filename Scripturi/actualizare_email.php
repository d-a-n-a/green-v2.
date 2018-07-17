<?php
 require "connection.php";
 require "constants.php";

 $oldEmail = $_POST["oldEmail"];
 $newEmail = $_POST["newEmail"];

  $query = $connect->prepare("update users set email = ? where email like ?;");
  $query->bind_param('ss', $newEmail, $oldEmail);
   
  if($query->execute())
		echo "success";
	else
		echo "fail";
 ?>