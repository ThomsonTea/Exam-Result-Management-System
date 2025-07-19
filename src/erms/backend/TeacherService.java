package erms.backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TeacherService {
    private static final String API_BASE = "http://localhost/Exam-Result-Management-System/erms-api/";

    public static JSONArray fetchStudents(String currentToken) throws Exception {
        String responseString = executeRequest("GET", "/Teacher/fetch-students.php", null, currentToken);
        return new JSONArray(responseString);
    }

    public static JSONArray fetchSubjects(String currentToken) throws Exception {
        String responseString = executeRequest("GET", "/Teacher/fetch-subjects.php", null, currentToken);
        return new JSONArray(responseString);
    }

    public static void submitMark(JSONObject data, String currentToken) throws Exception {
        // This method creates a resource. It succeeds or throws an exception. It doesn't need to return anything.
        executeRequest("POST", "/Teacher/submit-mark.php", data.toString(), currentToken);
    }
    
    public static JSONArray fetchMarks(String currentToken) throws Exception {
        String responseString = executeRequest("GET", "/Teacher/fetch-marks.php", null, currentToken);
        return new JSONArray(responseString);
    }
    
    public static String exportToSheets(JSONObject data) {
        try {
        	String appScript = "https://script.google.com/macros/s/AKfycbzFxLauWg_r8wDN3WV9LbT2UUW6sdfe5-NZ9TJTHk4_4a5edYS5j37qWUXk071RX6le/exec";        	
        	URL url = new URL(appScript);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.toString().getBytes("UTF-8"));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response body: " + response);
                    
                    JSONObject responseJson = new JSONObject(response.toString());
                    return responseJson.optString("url", null); // Return the URL if present
                }
            } else {
                System.err.println("Export failed: HTTP " + responseCode);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    private static String executeRequest(String method, String endpoint, String jsonBody, String token) throws Exception {
        URL url = new URL(API_BASE + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        // --- THE CRITICAL SECURITY STEP ---
        // Add the Authorization header to every single API request.
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");

        if (("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) && jsonBody != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes("UTF-8"));
                os.flush();
            }
        }

        int responseCode = conn.getResponseCode();
        
        // Read the response body. Use getErrorStream() if the request failed.
        InputStreamReader reader = new InputStreamReader(responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream());
        
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(reader)) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        conn.disconnect();

        // If the request failed, throw an exception with the server's error message.
        if (responseCode >= 400) {
            JSONObject errorJson = new JSONObject(response.toString());
            String errorMessage = errorJson.optString("message", "An unknown server error occurred.");
            throw new RuntimeException("Failed: HTTP " + responseCode + " - " + errorMessage);
        }
        
        // On success, return the raw response string for the public methods to parse.
        return response.toString();
    }

}
