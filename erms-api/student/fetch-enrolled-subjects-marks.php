<?php
require_once __DIR__ . '/../database.php'; // adjust if needed

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    $data = json_decode(file_get_contents("php://input"));

    if (!isset($data->studentID) || empty($data->studentID)) {
        http_response_code(400);
        echo json_encode(["message" => "studentID is required"]);
        exit();
    }

    $studentID = htmlspecialchars(strip_tags($data->studentID));

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
