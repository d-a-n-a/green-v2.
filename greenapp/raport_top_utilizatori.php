<?php
require "connection.php";
require "constants.php";

 $top = $connect->query("select u.ID_USER as us, u.username, l.strada, l.oras,avg(r.nota) as nota, (select count(*) from istoric where ID_EXPEDITOR = us and ID_STATUS = 7) as nr_trz from istoric i,reviews r, users u, locatii l where r.ID_USER = u.ID_USER and u.ID_LOCATIE = l.ID_LOCATIE and u.ID_USER = i.ID_EXPEDITOR GROUP by i.ID_EXPEDITOR ORDER by nr_trz DESC LIMIT 3");

 
if($top->num_rows > 0) 
{
	while($row = $top->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
          echo json_encode($myArray);

}
else {
	echo "fail";
}
?>