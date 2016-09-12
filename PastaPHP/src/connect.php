<?php
require_once "constants.php";

$hostname = "localhost";
$dbname = "pasta";
$username = "pastaapi";
$password = "";

$pdo = new PDO('mysql:host=' . $hostname . ';dbname=' . $dbname, $username, $password, array(PDO::ATTR_EMULATE_PREPARES => false,
    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION));