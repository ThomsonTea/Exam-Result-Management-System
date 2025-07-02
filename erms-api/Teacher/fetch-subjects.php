<?php
require_once __DIR__ . '/../database.php'; // adjust if needed

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    $data = json_decode(file_get_contents("php://input"));

    if (!isset($data->teacherID) || empty($data->teacherID)) {
        http_response_code(400);
        echo json_encode(["message" => "teacherID is required"]);
        exit();
    }

    $teacherID = htmlspecialchars(strip_tags($data->teacherID));

    $stmt = $db_conn->prepare("SELECT subjectID, subjectName FROM subject WHERE teacherID = :teacherID");
    $stmt->bindParam(":teacherID", $teacherID);
    $stmt->execute();

    $subjects = $stmt->fetchAll(PDO::FETCH_ASSOC); // returns subjectID + subjectName
    echo json_encode($subjects);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => $e->getMessage()]);
}
?>
