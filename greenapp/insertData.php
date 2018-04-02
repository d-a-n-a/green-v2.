<?php

require "connection.php";
require "constants.php";
require "functions.php";

//$cod = $_POST["cod"];
//din android eu trimit toate datele mele si aici verific ce am primit... dar cum???
$cod = "0";
switch($cod)
{
	case "0":
	{
		//insert in tabela de produse
		//dar asta inseamna mai intai preluarea namevaluepair
		$email = $_POST["email"];
		$titlu = $_POST["titlu"];
		$data = $_POST["data_introducerii"];
		$durata = $_POST["durata"];
		$tip = $_POST["tip"];
		$denumire = $_POST["denumire_produs"]; 
		$valabilitate = $_POST["valabilitate_produs"];
		//$imagine = $_POST["imagine"];
		$categorie = $_POST["categorie"];
		//durata? parca in layout nu am oricum; dar s-ar putea sa am nevoie pe triggeri so baga
		//$durata = $_POST["durata"];//probabil in zile
		insertAd($email, $titlu, $data, $durata, $tip, $denumire, $valabilitate, $categorie);
		echo "a ajuns pe case";
		break;
	}
	default:
		echo "Codul introdus nu corespunde niciunei ramuri.";
}

?>