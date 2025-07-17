<?php
require_once __DIR__ . '/../database.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");

try {
    $data = json_decode(file_get_contents("php://input"));

    if (
        empty($data->studentID) ||
        empty($data->subjectID) ||
        empty($data->teacherID) ||
        !isset($data->score) ||
        empty($data->grade)
    ) {
        http_response_code(400);
        echo json_encode(["message" => "Missing required fields."]);
        exit();
    }

    $stmt = $db_conn->prepare("INSERT INTO mark (studentID, subjectID, teacherID, score, grade) VALUES (:studentID, :subjectID, :teacherID, :score, :grade)");
    $stmt->bindParam(":studentID", $data->studentID);
    $stmt->bindParam(":subjectID", $data->subjectID);
    $stmt->bindParam(":teacherID", $data->teacherID);
    $stmt->bindParam(":score", $data->score);
    $stmt->bindParam(":grade", $data->grade);
    $stmt->execute();

    // ---- Google Sheet API Export (optional) ----
	$sheetData = [
	    [
	        "studentID" => $data->studentID,
	        "subjectID" => $data->subjectID,
	        "teacherID" => $data->teacherID,
	        "score"     => $data->score,
	        "grade"     => $data->grade
	    ]
	];

    $sheetPayload = json_encode(["data" => $sheetData]);

    // Replace with your url get from Google sheet apps script
    $ch = curl_init("https://script.google.com/macros/s/AKfycbzvBR0r5W0ri6gGzdT8JEORCy1Lr_uIdLT2JXmi3XX_n1mLpYwrlZvDHrqTm2odw4hTzQ/exec"); 
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $sheetPayload);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    $response = curl_exec($ch);
    curl_close($ch);

    echo json_encode(["message" => "Mark inserted and exported."]);

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => $e->getMessage()]);
}
?>
