<?php

require_once ("connect.php");

if(isset($_POST['id'])) { // create token
    $id = $_POST['id'];
    if($id != "") {
        $token = sha1(uniqid($id, true));

        try {
            $pdo = new PDO('mysql:host=' . $hostname . ';dbname=' . $dbname, $username, $password, array(PDO::ATTR_EMULATE_PREPARES => false, PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));
            $stmt = $pdo->prepare("INSERT INTO tokens (token) VALUES (?)");
            $result = $stmt->execute(array($token));

            if($result) {
                echo $token;
                exit();
            } else {
                die("Error creating token.");
            }
        } catch (PDOException $e) {
            die("DB Error.");
        }
    } else {
        die("ID empty.");
    }
} else {
    die("Wrong request.");
}