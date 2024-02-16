<?php

    include 'conexion.php';

    $idtareas= isset($_POST['idtareas']) ? $_POST['idtareas'] : null;
    //$correo="luna@gmail.com";

    $sentencia = $conexion->prepare("DELETE FROM tareas WHERE idtareas = ?");
    $sentencia->bind_param('s', $idtareas);
    

    if ($sentencia->execute()) {
        echo json_encode(['mensaje' => 'todo correcto'], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode(['mensaje' => 'error'], JSON_UNESCAPED_UNICODE);
    }

    $sentencia->close();
    $conexion->close();




?>