<?php
require_once __DIR__ . '/../auth_guard.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    if ($auth_user->role !== 'student') {
        http_response_code(403);
        echo json_encode(["message" => "Access denied. Only students can view enrolled subjects."]);
        exit();
    }
    
    $studentID = $auth_user->userid;

    $data = json_decode(file_get_contents("php://input"));

    if (isset($data->subjectID) && !empty($data->subjectID)) {
        // --- CASE 1: A specific subjectID was provided ---
        $subjectID = trim(htmlspecialchars(strip_tags($data->subjectID)));
        
        $stmt = $db_conn->prepare("SELECT mark.subjectID, subject.subjectName, mark.score, mark.grade, mark.teacherID 
            FROM mark 
            JOIN subject ON mark.subjectID = subject.subjectID 
            WHERE mark.studentID = :studentID AND mark.subjectID = :subjectID");
        $stmt->bindParam(":studentID", $studentID);
        $stmt->bindParam(":subjectID", $subjectID);

    } else {
        // --- CASE 2: No subjectID was provided, get all subjects ---
        $stmt = $db_conn->prepare("SELECT mark.subjectID, subject.subjectName, mark.score, mark.grade, mark.teacherID 
            FROM mark 
            JOIN subject ON mark.subjectID = subject.subjectID 
            WHERE mark.studentID = :studentID");
        $stmt->bindParam(":studentID", $studentID);
    }

    $stmt->execute();
    $subjects = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($subjects);
    
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => $e->getMessage()]);
}
?>
