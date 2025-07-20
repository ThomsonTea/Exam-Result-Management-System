<?php
require_once __DIR__ . '/../auth_guard.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

if ($auth_user->role !== 'teacher') {
        // The user is logged in, but they are not a teacher. Deny access.
        http_response_code(403);
        echo json_encode(["message" => "Access denied. You do not have permission to view all students."]);
        exit();
}

try {
    $stmt = $db_conn->prepare("SELECT studentID, studentName FROM student");
    $stmt->execute();
    $rows = $stmt->fetchAll(PDO::FETCH_ASSOC);

    http_response_code(200);
    echo json_encode($rows);

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => "Database error: " . $e->getMessage()]);
}
?>
