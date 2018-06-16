<?php
require "connection.php";
require "constants.php";

$sql = "SELECT (SELECT count(*) from istoric i, anunturi a, categorii c, produse p where i.ID_STATUS=7 and a.ID_PRODUS = p.ID_PRODUS and p.ID_CATEGORIE = c.ID_CATEGORIE and c.denumire='alimente' and i.ID_ANUNT = a.ID_ANUNT) as alimente, (SELECT count(*) from istoric i, anunturi a, categorii c, produse p where i.ID_STATUS=7 and a.ID_PRODUS = p.ID_PRODUS and p.ID_CATEGORIE = c.ID_CATEGORIE and c.denumire='haine' and i.ID_ANUNT = a.ID_ANUNT) as haine, (SELECT count(*) from istoric i, anunturi a, categorii c, produse p where i.ID_STATUS=7 and a.ID_PRODUS = p.ID_PRODUS and p.ID_CATEGORIE = c.ID_CATEGORIE and c.denumire='altele' and i.ID_ANUNT = a.ID_ANUNT) as altele
";
 $raport = $connect->query($sql);

 
if($raport->num_rows > 0) 
{
	while($item = $raport->fetch_array(MYSQLI_ASSOC)) {
            $vector[] = $item;
    }
          echo json_encode($vector);

}
else {
	echo "fail";
}
?>