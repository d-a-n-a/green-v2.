
		<?php
        require "connection.php";
        require "constants.php";
        $username = $_POST["username"];
        $query = $connect->prepare("SELECT data_inregistrarii, biografie, 
				(SELECT DISTINCT l.strada from locatii l, users u
				where l.ID_LOCATIE = u.ID_LOCATIE and u.username like ?)
				from users where username like ?;");
        $query->bind_param('ss', $username,$username);
        $query->execute();
        $query->store_result();
        $query->bind_result($data, $biografie, $locatie);
        $query->fetch();
        $array = array("data" => $data,"bio" => $biografie, "locatie" => $locatie);
        echo json_encode($array);
        ?>
 