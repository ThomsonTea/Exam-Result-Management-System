<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

require_once '../vendor/autoload.php';
require_once '../database.php';

use Firebase\JWT\JWT;
use Firebase\JWT\Key;

// Get the JWT from the Authorization header
$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? null;
$jwt = null;

if ($authHeader && preg_match('/Bearer\s(\S+)/', $authHeader, $matches)) {
    $jwt = $matches[1];
}

if (!$jwt) {
    http_response_code(401);
    echo json_encode(["message" => "Access denied. No token provided."]);
    exit();
}

$secret_key = "SSSQ3qGLqvhy]F5k^!QsSFq>fw]L#/";

try {
    $decoded = JWT::decode($jwt, new Key($secret_key, 'HS256'));

    // Get the user ID and token's creation time from the token data
    $token_user_id = $decoded->data->userid;
    $token_issued_at = $decoded->iat;
    $user_role = $decoded->data->role;

    // Determine the table to check
    $table_to_check = ($user_role === 'student') ? 'student' : 'teacher';
    $id_column_to_check = ($user_role === 'student') ? 'studentID' : 'teacherID';

    // Fetch the `token_valid_from` timestamp from the database
    $query = "SELECT token_valid_from FROM $table_to_check WHERE $id_column_to_check = :userid";
    $statement = $db_conn->prepare($query);
    $statement->bindParam(':userid', $token_user_id);
    $statement->execute();

    $user_row = $statement->fetch(PDO::FETCH_ASSOC);

    if (!$user_row || is_null($user_row['token_valid_from'])) {
        throw new Exception("Could not verify token validity timestamp.");
    }
    
    // The token must have been issued AFTER the last login/invalidation event.
    if ($token_issued_at < $user_row['token_valid_from']) {
        // If the token was created before the timestamp in the DB, it's an old token. Reject it.
        http_response_code(401);
        echo json_encode(["message" => "This token has been invalidated. Please log in again."]);
        exit();
    }

    http_response_code(200);
    echo json_encode([
        "message" => "Access granted.",
        "data" => $decoded->data
    ]);
} catch (Exception $e) {
    http_response_code(401);
    echo json_encode([
        "message" => "Access denied. Invalid token.",
        "error" => $e->getMessage()
    ]);
}
?>