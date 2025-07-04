<?php
// export-to-sheets.php

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

try {
    $rawPayload = file_get_contents("php://input");
    $dataArray = json_decode($rawPayload, true);

    if (empty($dataArray) || !is_array($dataArray)) {
        http_response_code(400);
        echo json_encode(["message" => "Invalid or empty data received."]);
        exit();
    }
    
    $sheetPayload = json_encode(["data" => $dataArray]);
    echo "Payload to be sent to Google Sheets: " . $sheetPayload . "\n"; // Debugging line
    
    $googleScriptUrl = "https://script.google.com/macros/s/AKfycbz-XXN58u7kvHmmA0rH9axl9C0QShmoyX73Le0Lz0N1knvy98JlsMO2I_GebkF0ChRQ/exec";
    
    $ch = curl_init($googleScriptUrl); 
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    
    // --- THE FIX IS HERE ---
    // Tell cURL to automatically follow any redirects sent by the Google server.
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true); 
    
    curl_setopt($ch, CURLOPT_POSTFIELDS, $sheetPayload); 
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json; charset=UTF-8']);
    
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    // After following the redirect, the final httpCode should be 200
    if ($httpCode !== 200) { // Check for a strict 200 OK
        http_response_code(502); // Bad Gateway
        echo json_encode([
            "message" => "Failed to export. The Google Script returned a non-200 status.",
            "googleResponseCode" => $httpCode,
            "googleResponseBody" => $response
        ]);
    } else {
        http_response_code(200); // OK
        echo json_encode(["message" => "Data exported successfully to Google Sheets."]);
    }

} catch (Exception $e) {
    http_response_code(500); // Internal Server Error
    echo json_encode(["error" => "An internal server error occurred: " . $e->getMessage()]);
}
?>