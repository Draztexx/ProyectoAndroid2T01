<?php

$servername = "localhost";
$username = "root"; 
$password = ""; 
$database = "proyectoandroid2t01"; 

/*
$servername="sql312.infinityfree.com";
$username="if0_35822449";
$database="if0_35822449_ProyectoAndroid2T01";
$password="SL6D5niIaj9YF";
*/

// Crear conexión
$conexion = new mysqli($servername, $username, $password, $database);

// Verificar la conexión
if ($conexion->connect_error) {
    die("Error de conexión: " . $conexion->connect_error);
}

?>