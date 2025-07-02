package erms.backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class AuthService {

    private static final String API_BASE_URL = "http://localhost/erms-api";
    private final HttpClient client = HttpClient.newHttpClient();

    public boolean login(String username, String password) throws ApiException {
        // Create a JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("username", username);
        jsonBody.put("password", password);

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/student/login.php"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        
        try {
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the HTTP status code from the server
            if (response.statusCode() == 200) {
                return true;
            } else {
                // Any other status code is a failure.
                // We parse the error message from the JSON response and throw our custom exception.
                JSONObject responseJson = new JSONObject(response.body());
                String errorMessage = responseJson.optString("message", "An unknown server error occurred.");
                throw new ApiException(errorMessage);
            }
            // Catch specific network/IO exceptions FIRST. These are true connection errors.
            } catch (java.io.IOException | InterruptedException ex) { 
                // Wrap these genuine connection errors in our generic message.
                throw new ApiException("Could not connect to the server. Please check your network and ensure XAMPP/Laragon is running.", ex);
            // Catch our own specific exception and re-throw it as is.
            } catch (ApiException ex) {
                throw ex; // Let the original, clean error message pass through.
            // Catch any other unexpected exceptions.
            } catch (Exception ex) {
                // This is for other unexpected problems, like a malformed JSON response.
                throw new ApiException("An unexpected error occurred: " .concat(ex.getMessage()), ex);
            }
    }
}