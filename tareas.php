<?php

    include 'conexion.php';

    $correo= isset($_POST['correo']) ? $_POST['correo'] : null;
    $estado= isset($_POST['estado']) ? $_POST['estado'] : null;

    if($estado=="Finalizado"){
    //$correo="luna@gmail.com";

        $sentencia = $conexion->prepare("SELECT * FROM tareas WHERE correo LIKE ? AND estado NOT LIKE ?");
        $sentencia->bind_param('ss', $correo,$estado);
        $sentencia->execute();
        $resultado = $sentencia->get_result();
    }else{
        $sentencia = $conexion->prepare("SELECT * FROM tareas WHERE correo LIKE ? AND estado LIKE 'Finalizado' ");
        $sentencia->bind_param('s', $correo);
        $sentencia->execute();
        $resultado = $sentencia->get_result();
    }
    $temas = array();

    while ($fila = $resultado->fetch_assoc()) {
        $temas[] = $fila;
    }

    echo json_encode($temas);

    $sentencia->close();
    $conexion->close();




?>