<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-control-Allow-Headers, Authorization, X-Requested-With");

require_once __DIR__ . '/vendor/autoload.php';
require_once __DIR__ . '/database.php';

use Firebase\JWT\JWT;
use Firebase\JWT\Key;

$authHeader = $_SERVER['HTTP_AUTHORIZATION'] ?? null;
$jwt = null;

if ($authHeader && preg_match('/Bearer\s(\S+)/', $authHeader, $matches)) {
    $jwt = $matches[1];
}

if (!$jwt) {
    http_response_code(401);
    echo json_encode(["message" => "Authentication required to log out."]);
    exit();
}

$secret_key = "SSSQ3qGLqvhy]F5k^!QsSFq>fw]L#/";

try {
    $decoded = JWT::decode($jwt, new Key($secret_key, 'HS256'));
    $user_id = $decoded->data->userid;
    $role = $decoded->data->role;

    // Determine table and column based on role
    $table = ($role === 'student') ? 'student' : 'teacher';
    $idColumn = ($role === 'student') ? 'studentID' : 'teacherID';

    // Invalidate all tokens for this user by setting the valid-from timestamp to now.
    $current_time = time();
    $update_query = "UPDATE $table SET token_valid_from = :current_time WHERE $idColumn = :userid";
    $statement = $db_conn->prepare($update_query);
    $statement->bindParam(':current_time', $current_time);
    $statement->bindParam(':userid', $user_id);
    
    if ($statement->execute()) {
        http_response_code(200);
        echo json_encode(["message" => "Logout successful."]);
    } else {
        throw new Exception("Database update failed during logout.");
    }

} catch (Exception $e) {
    http_response_code(401);
    echo json_encode([
        "message" => "Logout failed. Invalid token or server error.",
        "error" => $e->getMessage()
    ]);
}
?>