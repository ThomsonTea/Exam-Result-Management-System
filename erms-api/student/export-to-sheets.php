<?php
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST");

try {
    $rawPayload = file_get_contents("php://input");
    $decoded = json_decode($rawPayload, true);

    if (
        empty($decoded["data"]) ||
        !is_array($decoded["data"])
    ) {
        http_response_code(400);
        echo json_encode(["message" => "Invalid or missing 'data' array."]);
        exit();
    }

    // No conversion — keep object structure as is
    $sheetPayload = json_encode(["data" => $decoded["data"]]);

    $googleScriptUrl = "https://script.google.com/macros/s/AKfycbyhViVcFbW759uY1HFItbCPpccVsbVxt0LrgvQh2YLGbOmxK8hA5-ENkjcIesprS6wHiA/exec";

    $ch = curl_init($googleScriptUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $sheetPayload);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json; charset=UTF-8']);

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($httpCode !== 200) {
        http_response_code(502);
        echo json_encode([
            "message" => "Failed to export. The Google Script returned a non-200 status.",
            "googleResponseCode" => $httpCode,
            "googleResponseBody" => $response
        ]);
    } else {
        http_response_code(200);
        echo json_encode(["message" => "Data exported successfully to Google Sheets."]);
    }

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["error" => "An internal server error occurred: " . $e->getMessage()]);
}