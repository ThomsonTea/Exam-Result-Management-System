<?php
require_once __DIR__ . '/../database.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

try {
    if (!isset($_GET['teacherID']) || empty($_GET['teacherID'])) {
        http_response_code(400);
        echo json_encode(["error" => "Missing teacherID"]);
        exit();
    }

    $teacherID = $_GET['teacherID'];

    $stmt = $db_conn->prepare("SELECT studentID, subjectID, teacherID, score, grade FROM mark WHERE teacherID = :teacherID");
    $stmt->bindParam(':teacherID', $teacherID);
    $stmt->execute();
    $marks = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($marks);

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => $e->getMessage()]);
}
