<?php

    include 'conexion.php';



    $nombre = isset($_POST['nombreapellido']) ? $_POST['nombreapellido'] : null;
    $edad = isset($_POST['edad']) ? $_POST['edad'] : null;
    $correo = isset($_POST['correo']) ? $_POST['correo'] : null;
    $contraseña = isset($_POST['contraseña']) ? $_POST['contraseña'] : null;

    $sentencia=$conexion->prepare("INSERT INTO usuarios (nombreapellido,edad,correo,contraseña) VALUES (?,?,?,?)");
    $sentencia->bind_param('ssss', $nombre,$edad,$correo, $contraseña);
    $sentencia->execute();

    $sentencia = $conexion->prepare("SELECT nombreapellido, correo, edad FROM usuarios WHERE correo LIKE ? AND contraseña Like ?");
    $sentencia->bind_param('ss', $correo, $contraseña);
    $sentencia->execute();

    $resultado = $sentencia->get_result();
    if ($fila = $resultado->fetch_assoc()) {
        
        echo json_encode($fila, JSON_UNESCAPED_UNICODE);
    }else{

        echo json_encode(['nombreapellido' => 'vacio'], JSON_UNESCAPED_UNICODE);}

    $sentencia->close();
    $conexion->close();



?>