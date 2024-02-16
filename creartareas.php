<?php

    include 'conexion.php';
    
    $nombre = isset($_POST['nombre']) ? $_POST['nombre'] : null;
    $descripcion = isset($_POST['descripcion']) ? $_POST['descripcion'] : null;
    $fecha = isset($_POST['fecha']) ? $_POST['fecha'] : null;
    $prioridad = isset($_POST['prioridad']) ? $_POST['prioridad'] : null;
    $estado = isset($_POST['estado']) ? $_POST['estado'] : null;
    $correo = isset($_POST['correo']) ? $_POST['correo'] : null;
/*    
    $nombre = "adawd";
    $descripcion = "awdawd";
    $fecha = "2000-12-12";
    $prioridad = "prio";
    $estado = "estado";
    $correo = "luna@gmail.com";
*/
    $sentencia=$conexion->prepare("INSERT INTO tareas (nombre,descripcion,fecha,prioridad,estado,correo) VALUES (?,?,?,?,?,?)");
    $sentencia->bind_param('ssssss', $nombre,$descripcion,$fecha, $prioridad,$estado,$correo);
    
    if ($sentencia->execute()) {
        echo json_encode(['mensaje' => 'todo correcto'], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(['mensaje' => 'error'], JSON_UNESCAPED_UNICODE);
    }
    

    $sentencia->close();
    $conexion->close();

?>