<?php
require_once __DIR__ . '/../auth_guard.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST"); 
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    if ($auth_user->role !== 'student') {
        http_response_code(403);
        echo json_encode(["message" => "Access denied. Only students can perform this action."]);
        exit();
    }

    $studentID = $auth_user->userid;

    $data = json_decode(file_get_contents("php://input"));

    if (!isset($data->subjectID) || empty($data->subjectID)) {
        http_response_code(400); // Bad Request
        echo json_encode(["message" => "A subjectID is required in the request body."]);
        exit();
    }
    $subjectID = trim(htmlspecialchars(strip_tags($data->subjectID)));

    $stmt = $db_conn->prepare("SELECT mark.score, mark.grade FROM mark JOIN subject ON mark.subjectID = subject.subjectID WHERE mark.studentID = :studentID AND subject.subjectID = :subjectID;");
    $stmt->bindParam(":studentID", $studentID);
    $stmt->bindParam(":subjectID", $subjectID);
    $stmt->execute();

    $mark = $stmt->fetch(PDO::FETCH_ASSOC);
    
    if (!$mark) {
        http_response_code(404); // Not Found
        echo json_encode(["message" => "No mark found for the specified subject."]);
        exit();
    }
    
    http_response_code(200);
    echo json_encode($mark);

} catch (Exception $e) { // Catching generic Exception is safer
    http_response_code(500);
    echo json_encode(["error" => "An unexpected error occurred: " . $e->getMessage()]);
}
?>