<?php

include 'conexion.php';

$correo = isset($_POST['Correo']) ? $_POST['Correo'] : null;
$contraseña = isset($_POST['Contraseña']) ? $_POST['Contraseña'] : null;

// Imprimir la consulta SQL para depurar
echo "Consulta SQL: SELECT nombreapellido, correo, edad FROM usuarios WHERE correo = '$correo' AND contraseña = '$contraseña'";

$sentencia = $conexion->prepare("SELECT nombreapellido, correo, edad FROM usuarios WHERE correo = ? AND contraseña = ?");
$sentencia->bind_param('ss', $correo, $contraseña);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
    echo json_encode($fila, JSON_UNESCAPED_UNICODE);
}

$sentencia->close();
$conexion->close();

?>