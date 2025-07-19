<?php
require_once __DIR__ . '/../auth_guard.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    if ($auth_user->role !== 'student') {
        http_response_code(403); // 403 Forbidden is the correct code for a valid user with wrong permissions.
        echo json_encode(["message" => "Access denied. This resource is for students only."]);
        exit();
    }

    $studentID = $auth_user->userid;

    $stmt = $db_conn->prepare("SELECT mark.subjectID, subject.subjectName FROM mark JOIN subject ON mark.subjectID = subject.subjectID WHERE mark.studentID = :studentID;");
    $stmt->bindParam(":studentID", $studentID);
    $stmt->execute();

    $subjects = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($subjects);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => $e->getMessage()]);
}
?>
