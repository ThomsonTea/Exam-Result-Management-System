<?php
require_once __DIR__ . '/../database.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    $data = json_decode(file_get_contents("php://input"));

    if (!isset($data->studentID) || empty($data->studentID) || !isset($data->subjectID) || empty($data->subjectID)) {
        http_response_code(400);
        echo json_encode(["message" => "Both studentID and subjectID are required"]);
        exit();
    }

    $studentID = trim(htmlspecialchars(strip_tags($data->studentID)));
    $subjectID = trim(htmlspecialchars(strip_tags($data->subjectID)));

    $stmt = $db_conn->prepare("SELECT mark.score, mark.grade FROM mark JOIN subject ON mark.subjectID = subject.subjectID WHERE mark.studentID = :studentID AND subject.subjectID = :subjectID;");
    $stmt->bindParam(":studentID", $studentID);
    $stmt->bindParam(":subjectID", $subjectID);
    $stmt->execute();

    $subjects = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($subjects);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => $e->getMessage()]);
}
?>
