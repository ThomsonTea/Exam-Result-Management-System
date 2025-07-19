package erms.backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class JwtService {

    private static final String API_BASE_URL = "http://localhost/Exam-Result-Management-System/erms-api/jwt";
    private final HttpClient client = HttpClient.newHttpClient();

    public String generateToken(String userid, String password, String role) throws ApiException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("userid", userid);
        requestBody.put("password", password);
        requestBody.put("role", role);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/generate_token.php"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject responseJson = new JSONObject(response.body());
                // Assuming the PHP script returns a JSON object with a "token" key.
                return responseJson.getString("token");
            } else {
                throw new ApiException("Failed to generate token. Server responded with status: " + response.statusCode());
            }

        } catch (Exception e) {
            throw new ApiException("Error while calling token generation endpoint: " + e.getMessage(), e);
        }
    }

    public JSONObject validateToken(String token) throws ApiException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/validate_token.php"))
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.noBody()) // Or .GET() if you change the PHP script
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseJson = new JSONObject(response.body());

            if (response.statusCode() == 200) {
                // Return the "data" object from the PHP response.
                return responseJson.getJSONObject("data");
            } else {
                String errorMessage = responseJson.optString("message", "Token validation failed.");
                throw new ApiException(errorMessage);
            }

        } catch (Exception e) {
            throw new ApiException("Error while calling token validation endpoint: " + e.getMessage(), e);
        }
    }
}