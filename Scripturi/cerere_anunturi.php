<?php
 require "connection.php";
 require "constants.php";


 if ($result = $connect->query("select (SELECT count(*) from anunturi a, produse p 
where p.ID_PRODUS = a.ID_PRODUS and p.ID_CATEGORIE = 1 and a.tip='cerere') as cerere_haine,
(SELECT count(*) from anunturi a, produse p 
where p.ID_PRODUS = a.ID_PRODUS and p.ID_CATEGORIE = 3 and a.tip='cerere') as cerere_alimente,
(SELECT count(*) from anunturi a, produse p 
where p.ID_PRODUS = a.ID_PRODUS and p.ID_CATEGORIE = 8 and a.tip='cerere') as cerere_altele,
(SELECT count(*) from anunturi a, produse p 
where p.ID_PRODUS = a.ID_PRODUS and p.ID_CATEGORIE = 1 and a.tip='oferta') as oferta_haine,
(SELECT count(*) from anunturi a, produse p 
where p.ID_PRODUS = a.ID_PRODUS and p.ID_CATEGORIE = 3 and a.tip='oferta') as oferta_alimente,
(SELECT count(*) from anunturi a, produse p 
where p.ID_PRODUS = a.ID_PRODUS and p.ID_CATEGORIE = 8 and a.tip='oferta') as oferta_altele")) {

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
            $myArray[] = $row;
    }
}
    echo json_encode($myArray);
?>