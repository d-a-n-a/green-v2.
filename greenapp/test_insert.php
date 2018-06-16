<?php
require "connection.php";
require "constants.php";


$sql = "insert into locatii(oras, strada, tara, latitudine, longitudine) values ('testxampp', 'teststradaxampp', 'Romaniaxampp', 454.05, 54.25);";
if ($connect->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conect->error;
} 
$connect->close(); 
?>