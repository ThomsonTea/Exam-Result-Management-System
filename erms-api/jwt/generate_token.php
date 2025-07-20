<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

require_once '../vendor/autoload.php';

use Firebase\JWT\JWT;

// Get the posted data
$data = json_decode(file_get_contents("php://input"));

if (empty($data->userid) || empty($data->password) || empty($data->role)) {
    http_response_code(400);
    echo json_encode(["message" => "Incomplete data. 'userid', password and 'role' are required."]);
    exit();
}

$secret_key = "SSSQ3qGLqvhy]F5k^!QsSFq>fw]L#/";
$issuer_claim = "erms-api";
$audience_claim = "erms-client";
$issuedat_claim = time();
$notbefore_claim = $issuedat_claim;
$expire_claim = $issuedat_claim + 86400; // Token valid for 1 day

$token = [
    "iat" => $issuedat_claim,
    "exp" => $expire_claim,
    "data" => [
        "userid" => $data->userid,
        "role" => $data->role
    ]
];


http_response_code(200);

$jwt = JWT::encode($token, $secret_key, 'HS256');

echo json_encode(
    [
        "message" => "Token generated successfully.",
        "token" => $jwt
    ]
);
?>