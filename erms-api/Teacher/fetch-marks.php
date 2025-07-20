<?php
require_once __DIR__ . '/../auth_guard.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    if ($auth_user->role !== 'teacher') {
        http_response_code(403);
        echo json_encode(["message" => "Access denied. Only teachers can access this resource."]);
        exit();
    }

    $teacherID = trim($auth_user->userid);

    $stmt = $db_conn->prepare("SELECT studentID, subjectID, teacherID, score, grade FROM mark WHERE teacherID = :teacherID");
    $stmt->bindParam(':teacherID', $teacherID);
    $stmt->execute();
    $marks = $stmt->fetchAll(PDO::FETCH_ASSOC);

    http_response_code(200);
    echo json_encode($marks);

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => "Database error: " . $e->getMessage()]);
}
