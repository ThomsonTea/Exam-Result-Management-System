<?php
header("Content-Type: application/json; charset=UTF-8");

require_once __DIR__ . '/vendor/autoload.php';
require_once __DIR__ . '/database.php';

use Firebase\JWT\JWT;
use Firebase\JWT\Key;

// GET THE TOKEN FROM THE HEADER
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? null;
$jwt = null;

if ($authHeader && preg_match('/Bearer\s(\S+)/', $authHeader, $matches)) {
    $jwt = $matches[1];
}

// If no token is found, block access immediately.
if (!$jwt) {
    http_response_code(401);
    echo json_encode(["message" => "Access denied. No token provided."]);
    exit();
}

$secret_key = "SSSQ3qGLqvhy]F5k^!QsSFq>fw]L#/";

try {
    // DECODE AND VERIFY THE TOKEN'S SIGNATURE AND EXPIRATION
    $decoded = JWT::decode($jwt, new Key($secret_key, 'HS256'));

    // CHECK AGAINST THE DATABASE TO SEE IF THE TOKEN WAS INVALIDATED
    $user_id = $decoded->data->userid;
    $role = $decoded->data->role;
    $token_issued_at = $decoded->iat;

    $table = ($role === 'student') ? 'student' : 'teacher';
    $idColumn = ($role === 'student') ? 'studentID' : 'teacherID';

    $query = "SELECT token_valid_from FROM $table WHERE $idColumn = :userid";
    $statement = $db_conn->prepare($query);
    $statement->bindParam(':userid', $user_id);
    $statement->execute();

    $user_row = $statement->fetch(PDO::FETCH_ASSOC);

    if (!$user_row || is_null($user_row['token_valid_from'])) {
        throw new Exception("Could not verify token validity. User may not exist.");
    }

    if ($token_issued_at < $user_row['token_valid_from']) {
        // This token was issued before the last login/logout event. It's old. Block it.
        http_response_code(401);
        echo json_encode(["message" => "Access denied. Token has been invalidated. Please log in again."]);
        exit();
    }
    
    // 4. SUCCESS
    $auth_user = $decoded->data;

} catch (Exception $e) {
    http_response_code(401);
    echo json_encode([
        "message" => "Access denied. Invalid or expired token.",
        "error" => $e->getMessage()
    ]);
    exit();
}
?>