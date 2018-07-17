<?php
require "connection.php";
require "constants.php";

 $top = $connect->query("select u.ID_USER as us, u.username, l.strada, l.oras,avg(r.nota) as nota, 
						(select count(*) from istoric where ID_EXPEDITOR = us and ID_STATUS = 7) as nr_trz, 
						(select count(*) from istoric where ID_DESTINATAR = us and ID_STATUS = 7) as nr_trz_d, 
						(select nr_trz_d + nr_trz from dual) totalfinalizate, 
						(select count(*) from istoric where ID_EXPEDITOR = us and ID_STATUS = 6) as nr_anulat, 
						(select count(*) from istoric where ID_DESTINATAR = us and ID_STATUS = 6) as nr_anulat_d, 
						(select nr_anulat + nr_anulat_d from DUAL) totalanulate
						from istoric i, reviews r, users u, locatii l
						where r.ID_USER = u.ID_USER and u.ID_LOCATIE = l.ID_LOCATIE
						GROUP by u.ID_USER ORDER by totalfinalizate DESC, totalanulate ASC
						LIMIT 3");

 
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

