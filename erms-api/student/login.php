<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

try {
    require_once __DIR__ . '/../database.php';

     // Check if $db_conn is a valid PDO object
    if (!isset($db_conn) || !is_object($db_conn)) {
        http_response_code(500);
        echo json_encode(array("message" => "Database connection failed."));
        exit();
    }
    
    $request_data = json_decode(file_get_contents("php://input"));

    if (empty($request_data->username) || empty($request_data->password)) {
        http_response_code(400);
        echo json_encode(array("message" => "Username and password are required."));
        exit();
    }

    $username = htmlspecialchars(strip_tags($request_data->username));
    $submittedPassword = $request_data->password; // No need to sanitize the password for verifying

    // Step 1: Fetch the stored HASHED password for the given user.
    $query = "SELECT password FROM student WHERE studentID = :username";
    $statement = $db_conn->prepare($query);
    $statement->bindParam(":username", $username);
    $statement->execute();

    if ($statement->rowCount() > 0) {
        // A user with that username was found. Fetch the row.
        $row = $statement->fetch(PDO::FETCH_ASSOC);
        $storedHashedPassword = $row['password'];

        // Step 2: Verify the submitted plain-text password against the stored hash.
        if (password_verify($submittedPassword, $storedHashedPassword)) {
            // Passwords match! Login is successful.
            http_response_code(200);
            echo json_encode(array("message" => "Login successful."));
        } else {
            // Passwords do not match.
            http_response_code(401);
            echo json_encode(array("message" => "Invalid username or password."));
        }
    } else {
        // No user with that username was found.
        http_response_code(401);
        echo json_encode(array("message" => "Invalid username or password."));
    }

} catch (PDOException $e) {
    http_response_code(503);
    echo json_encode(array("message" => "Database error.", "error" => $e->getMessage()));
} catch (Exception $e) {
    http_response_code(500);

    echo json_encode(array("message" => "An unexpected error occurred.", "error" => $e->getMessage()));
}
?>