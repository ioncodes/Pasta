<?php

try {
    require_once ("connect.php");

    if(isset($_GET['token']) && isset($_POST['source'])) { // create paste
        $source = urldecode($_POST['source']);
        $token = $_GET['token'];

        $stmt = $pdo->prepare("SELECT * FROM tokens WHERE token=?");
        $stmt->execute(array($token));

        $result = $stmt->fetchAll(PDO::FETCH_ASSOC);

        if($result) {
            $id = uniqid(rand(), true) . FILE_EXTENSION;
            $path = './pastes/'.$id;

            if(file_put_contents($path, $source) != false) {
                $stmt = $pdo->prepare("SELECT pastes FROM tokens WHERE token=?");
                $stmt->execute(array($token));

                $result = $stmt->fetch();
                $pastes = $result["pastes"];

                if($pastes == "") {
                    $pastes = str_replace(FILE_EXTENSION, "", $id);
                } else {
                    $pastes = $pastes.",".str_replace(FILE_EXTENSION, "", $id);
                }

                $stmt = $pdo->prepare("UPDATE tokens SET pastes=? WHERE token=?");
                $stmt->execute(array($pastes, $token));

                $url = "http://www.ioncodes.com/Pasta/?paste=".str_replace(FILE_EXTENSION, "", $id);
                echo $url;
                exit();
            } else {
                die("Error creating file.");
            }
        }
        else {
            die("Token does not exist!");
        }
    }
    else {
        die("Wrong request.");
    }
} catch (PDOException $e) {
    die("DB Error.");
}