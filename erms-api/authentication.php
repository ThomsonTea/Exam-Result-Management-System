<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    require_once __DIR__ . '/database.php';

    if (!isset($db_conn) || !is_object($db_conn)) {
        http_response_code(500);
        echo json_encode(array("message" => "Database connection failed."));
        exit();
    }

    $request_data = json_decode(file_get_contents("php://input"));

    if (
        empty($request_data->userid) ||
        empty($request_data->password) ||
        empty($request_data->role)
    ) {
        http_response_code(400);
        echo json_encode(array("message" => "User ID, password, and role are required."));
        exit();
    }

    $userid = htmlspecialchars(strip_tags($request_data->userid));
    $submittedPassword = $request_data->password;
    $role = strtolower($request_data->role); // Normalize case

    // Choose the correct table and ID column based on role
	// Determine table and columns
	if ($role === "student") {
	    $table = "student";
	    $idColumn = "studentID";
	    $nameColumn = "studentName";
	} elseif ($role === "teacher") {
	    $table = "teacher";
	    $idColumn = "teacherID";
	    $nameColumn = "teacherName";
	} else {
	    http_response_code(400);
	    echo json_encode(array("message" => "Invalid role specified."));
	    exit();
	}

	// Fetch ID, Name, and password
	$query = "SELECT $idColumn, $nameColumn, password FROM $table WHERE $idColumn = :userid";
    $statement = $db_conn->prepare($query);
    $statement->bindParam(":userid", $userid);
    $statement->execute();

    if ($statement->rowCount() > 0) {
        $row = $statement->fetch(PDO::FETCH_ASSOC);
        $storedHashedPassword = $row['password'];
		
        if (password_verify($submittedPassword, $storedHashedPassword)) {
			http_response_code(200);
            echo json_encode(array(
                "message" => "Login successful.",
                "id" => $row[$idColumn],
                "name" => $row[$nameColumn]
            ));
        } else {
            http_response_code(401);
            echo json_encode(array("message" => "Invalid ID or password2."));
        }
    } else {
        http_response_code(401);
        echo json_encode(array("message" => "Invalid ID or password."));
    }

} catch (PDOException $e) {
    http_response_code(503);
    echo json_encode(array("message" => "Database error.", "error" => $e->getMessage()));
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(array("message" => "Unexpected error.", "error" => $e->getMessage()));
}
?>
