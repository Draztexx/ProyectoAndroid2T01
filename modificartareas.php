<?php

    include 'conexion.php';

    $idtareas = isset($_POST['idtareas']) ? $_POST['idtareas'] : null;
    $nombre = isset($_POST['nombre']) ? $_POST['nombre'] : null;
    $descripcion = isset($_POST['descripcion']) ? $_POST['descripcion'] : null;
    $fecha = isset($_POST['fecha']) ? $_POST['fecha'] : null;
    $prioridad = isset($_POST['prioridad']) ? $_POST['prioridad'] : null;
    $estado = isset($_POST['estado']) ? $_POST['estado'] : null;
    $correo = isset($_POST['correo']) ? $_POST['correo'] : null;
    
   /*
    $idtareas = "4";
    $nombre = "4";
    $descripcion = "prueba";
    $fecha = "2000-12-12";
    $prioridad = "prio";
    $estado = "estado";
    $correo = "luna@gmail.com";
*/ 
    $sentencia=$conexion->prepare("UPDATE tareas SET nombre = ?, fecha = ?, prioridad = ?, estado = ?, correo = ?, descripcion = ? WHERE idtareas = ?; ");
    $sentencia->bind_param('sssssss', $nombre,$fecha,$prioridad,$estado,$correo,$descripcion,$idtareas);
    
    if ($sentencia->execute()) {
        echo json_encode(['mensaje' => 'todo correcto'], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(['mensaje' => 'error'], JSON_UNESCAPED_UNICODE);
    }
    

    $sentencia->close();
    $conexion->close();

?>