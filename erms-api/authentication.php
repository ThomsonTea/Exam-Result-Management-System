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
    $role = strtolower($request_data->role);

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

    $query = "SELECT $idColumn, $nameColumn, password FROM $table WHERE $idColumn = :userid";
    $statement = $db_conn->prepare($query);
    $statement->bindParam(":userid", $userid);
    $statement->execute();

    if ($statement->rowCount() > 0) {
        $row = $statement->fetch(PDO::FETCH_ASSOC);
        $storedHashedPassword = $row['password'];
		
        // If the password is correct, proceed to get the token
        if (password_verify($submittedPassword, $storedHashedPassword)) {

            $current_time = time();
            $update_query = "UPDATE $table SET token_valid_from = :current_time WHERE $idColumn = :userid";
            $update_statement = $db_conn->prepare($update_query);
            $update_statement->bindParam(':current_time', $current_time);
            $update_statement->bindParam(':userid', $userid);
            $update_statement->execute();

            // 1. Prepare the data to send to the token generator
            $token_request_data = json_encode([
                "userid" => $row[$idColumn],
                "password" => $submittedPassword,
                "role" => $role
            ]);

            // 2. Initialize and configure the cURL request
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, 'http://localhost/Exam-Result-Management-System/erms-api/jwt/generate_token.php');
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $token_request_data);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true); // This is crucial - it returns the response as a string
            curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));

            // 3. Execute the request and get information
            $token_response_string = curl_exec($ch);
            $http_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);

            // 4. Check for errors from every possible source
            if (curl_errno($ch)) {
                // This handles network errors (e.g., cannot connect)
                http_response_code(500);
                echo json_encode(["message" => "Network error contacting token service: " . curl_error($ch)]);
                curl_close($ch);
                exit();
            }

            curl_close($ch); // Close the connection

            if ($http_code == 200) {
                // The server responded OK, now check if the response body is valid
                $token_data = json_decode($token_response_string, true);

                if (is_null($token_data) || !isset($token_data['token'])) {
                    // This handles cases where the server sent 200 OK but the body was empty or not valid JSON
                    http_response_code(500);
                    echo json_encode([
                        "message" => "Invalid response from token service.",
                        "actual_response" => $token_response_string // Shows you what went wrong
                    ]);
                    exit();
                }
                
                // 5. SUCCESS: If we get here, everything worked.
                http_response_code(200);
                echo json_encode([
                    "message" => "Login successful.",
                    "id" => $row[$idColumn],
                    "name" => $row[$nameColumn],
                    "token" => $token_data['token'] // Extract the token from the decoded JSON
                ]);

            } else {
                // This handles cases where generate_token.php returned an error (e.g., 400 or 500)
                http_response_code(500);
                echo json_encode([
                    "message" => "Token service failed with HTTP status code.",
                    "statusCode" => $http_code,
                    "response" => $token_response_string
                ]);
            }
            // --- End of cURL block ---

        } else {
            http_response_code(401);
            echo json_encode(array("message" => "Invalid ID or password."));
        }
    } else {
        http_response_code(401);
        echo json_encode(array("message" => "Invalid ID or password."));
    }

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(array("message" => "Unexpected error.", "error" => $e->getMessage()));
}
?>