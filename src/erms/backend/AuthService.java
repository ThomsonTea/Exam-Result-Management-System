package erms.backend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class AuthService {

	private final JwtService jwtService = new JwtService();
    private static final String API_BASE_URL = "http://localhost/Exam-Result-Management-System/erms-api";
    private final HttpClient client = HttpClient.newHttpClient();

    public JSONObject login(String userid, String password, String role) throws ApiException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userid", userid);
        jsonBody.put("password", password);
        jsonBody.put("role", role);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/authentication.php"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .build();
        
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Login response status: " + response.statusCode());
            System.out.println("Response body: " + response.body());
            
            JSONObject responseJson = new JSONObject(response.body());

            if (response.statusCode() == 200) {

            	return responseJson;
            	
            } else {
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
    
    public Boolean logout(String currentToken) throws ApiException {
    	
        if (currentToken == null || currentToken.isEmpty()) {
            throw new ApiException("No token provided for logout.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/logout.php"))
                .header("Authorization", "Bearer " + currentToken) 
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
                
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Logout response status: " + response.statusCode());
            System.out.println("Logout response body: " + response.body());

            if (response.statusCode() != 200) {
                JSONObject responseJson = new JSONObject(response.body());
                String errorMessage = responseJson.optString("message", "An unknown error occurred during logout.");
                throw new ApiException(errorMessage);
            }
            // If status is 200, logout was successful.
            return true;

        } catch (java.io.IOException | InterruptedException ex) {
            throw new ApiException("Could not connect to the server for logout.", ex);
        } catch (Exception ex) {
            throw new ApiException("An unexpected error occurred during logout: " + ex.getMessage(), ex);
        }
    }
}