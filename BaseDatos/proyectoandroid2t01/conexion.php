<?php

$servername = "localhost";
$username = "root"; 
$password = ""; 
$database = "proyectoandroid2t01"; 

// Crear conexión
$conexion = new mysqli($servername, $username, $password, $database);

// Verificar la conexión
if ($conexion->connect_error) {
    die("Error de conexión: " . $conexion->connect_error);
}

?>