<?php

require "connection.php";
require "constants.php";

$email= $_POST["email"];
$new_value = $_POST["value"];
$code = $_POST["code"];

if($code === "0")//schimbare last name
{
	$update_ln = $connect->prepare("update users set nume = ? where email like ?");
	$update_ln -> bind_param('ss', $new_value, $email);
	$update_ln -> execute();
	$update_ln ->store_result();
	if($update_ln->num_rows>0)
	{
		echo "success";
	}
	else
	{
		echo "fail";
	}

}
else
	if($code === "1")//update first name
	{
		$update_fn = $connect->prepare("update users set prenume = ? where email like ?");
		$update_fn -> bind_param('ss', $new_value, $email);
		$update_fn -> execute();
		$update_fn ->store_result();
		if($update_fn->num_rows>0)
		{
			echo "success";
		}
		else
		{
			echo "fail";
		}
	}
	else
		if($code === "2")//update username
		{
			$username_exists = $connect->prepare('select * from users where username like ?');
			$username_exists->bind_param('s', $new_value);
			$username_exists->execute();
			$username_exists->store_result();
			if($username_exists->num_rows > 0)
			{
				echo USERNAME_INVALID;
			}
			else
			{
				$update_un = $connect->prepare("update users set username = ? where email like ?");
				$update_un -> bind_param('ss', $new_value, $email);
				$update_un -> execute();
				$update_un ->store_result();
				if($update_un->num_rows>0)
				{
					echo "success";
				}
				else
				{
					echo "fail";
				}
			}
			
		}
		else
			if($code === "3")//update email
			{
				$email_exists=$connect->prepare('select * from users where email like ?;');
				$email_exists->bind_param('s',$email);
				$email_exists->execute();
				$email_exists->store_result();
				if($email_exists->num_rows > 0)
				{
					echo EMAIL_EXISTENT;
				}
				else
				{
					$update_email = $connect->prepare("update users set email = ? where email like ?");
					$update_email -> bind_param('ss', $new_value, $email);
					$update_email -> execute();
					$update_email ->store_result();
					if($update_email->num_rows>0)
					{
						echo "success";
					}
					else
					{
						echo "fail";
					}
				}

			}
			else
				if($code === "4") //update nr telefon
			{
				
				
					$update_phone = $connect->prepare("update users set telefon = ? where email like ?");
					$update_phone -> bind_param('ss', $new_value, $email);
					$update_phone -> execute();
					$update_phone ->store_result();
					if($update_phone->num_rows>0)
					{
						echo "success";
					}
					else
					{
						echo "fail";
					}
			}
			else
				if($code === "5")
				{
					$update_about = $connect->prepare("update users set biografie = ? where email like ?");
					$update_about -> bind_param('ss', $new_value, $email);
					$update_about -> execute();
					$update_about ->store_result();
					if($update_about->num_rows>0)
					{
						echo "success";
					}
					else
					{
						echo "fail";
					}
				}
				else 
					if($code === "6")
					{
						//update fotografie
					}
					else
						if($code === "7")
						{
							//update parola
						}
						// !!!! switch !!!!
						
/*

$update_ln = mysql_query("update users set nume = ? where email like ?");
$no_rows = mysql_affected_rows();
if($no_rows > 0)
{
		echo "succes";
}
else
{
	echo "fail";
}*/
?>