<?php

try {
    require_once ("connect.php");

    if(isset($_GET['token'])) { // get pastes
        $token = $_GET['token'];

        $stmt = $pdo->prepare("SELECT * FROM tokens WHERE token=?");
        $stmt->execute(array($token));

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        if($result) { // token exists
            $stmt = $pdo->prepare("SELECT pastes FROM tokens WHERE token=?");
            $stmt->execute(array($token));

            $result = $stmt->fetch();
            echo $result["pastes"]; // print pastes
            exit();
        }
        else {
            die("Token does not exist.");
        }
    }
    else {
        die("Wrong request.");
    }
} catch (PDOException $e) {
    die("DB Error.");
}