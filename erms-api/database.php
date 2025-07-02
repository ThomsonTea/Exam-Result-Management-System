<?php

$db_host = "localhost";
$db_name = "erms_db"; 
$db_user = "root";
$db_pass = "";

$db_conn = null;

// The try-catch block is now removed from here. 
// We will let the script that includes this file handle the exception.
// This makes this file purely for configuration and connection attempt.
$db_conn = new PDO("mysql:host=" . $db_host . ";dbname=" . $db_name, $db_user, $db_pass);
$db_conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db_conn->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
?>