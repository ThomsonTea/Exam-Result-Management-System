<?php
require_once __DIR__ . '/../auth_guard.php';

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    if ($auth_user->role !== 'teacher') {
        http_response_code(403); // Forbidden
        echo json_encode(["message" => "Access denied. Only teachers can submit marks."]);
        exit();
    }

    $teacherID = trim($auth_user->userid);

    $data = json_decode(file_get_contents("php://input"));

    if (
        empty($data->studentID) ||
        empty($data->subjectID) ||
        !isset($data->score)   ||
        empty($data->grade)
    ) {
        http_response_code(400); // Bad Request
        echo json_encode(["message" => "Missing required fields: studentID, subjectID, score, and grade are all required."]);
        exit();
    }

    $studentID = trim(htmlspecialchars(strip_tags($data->studentID)));
    $subjectID = trim(htmlspecialchars(strip_tags($data->subjectID)));
    $score = filter_var($data->score, FILTER_VALIDATE_INT);
    $grade = trim(htmlspecialchars(strip_tags($data->grade)));
	
	// Check if mark already exists for student & subject
	$checkStmt = $db_conn->prepare("SELECT COUNT(*) FROM mark WHERE studentID = :studentID AND subjectID = :subjectID");
	$checkStmt->bindParam(":studentID", $studentID);
	$checkStmt->bindParam(":subjectID", $subjectID);
	$checkStmt->execute();
	$existingCount = $checkStmt->fetchColumn();

	if ($existingCount > 0) {
	    http_response_code(409); // Conflict
	    echo json_encode(["message" => "This student already has a mark for this subject."]);
	    exit();
	}

    $stmt = $db_conn->prepare("INSERT INTO mark (studentID, subjectID, teacherID, score, grade) VALUES (:studentID, :subjectID, :teacherID, :score, :grade)");
    $stmt->bindParam(":studentID", $studentID);
    $stmt->bindParam(":subjectID", $subjectID);
    $stmt->bindParam(":teacherID", $teacherID); // <-- Using the secure ID
    $stmt->bindParam(":score", $score);
    $stmt->bindParam(":grade", $grade);

    if ($stmt->execute()) {
        http_response_code(201); // 201 Created is the standard for a successful creation.
        echo json_encode(["message" => "Mark created successfully."]);
    } else {
        throw new Exception("Failed to execute statement.");
    }

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(["error" => "An unexpected error occurred: " . $e->getMessage()]);
}
?>
