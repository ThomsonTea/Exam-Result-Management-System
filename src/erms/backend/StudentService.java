package erms.backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class StudentService {

	private static final String API_BASE = "http://localhost/Exam-Result-Management-System/erms-api/";
	
	// export data to sheets
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


	
	// fetch enrolled subjects for "View Marks/Grades" dropdown.
	public static JSONArray fetchSubjects(String currentToken) throws Exception {
        // Get the raw string response from the server.
        String responseString = executePostRequest("/Student/fetch-enrolled-subjects-marks.php", "{}", currentToken);
        // Parse the string into a JSONArray and return it.
        return new JSONArray(responseString);
    }


    
     // Fetches the specific mark for a student in a given subject.
	public static JSONObject fetchMark(String subjectID, String currentToken) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("subjectID", subjectID);
        
        String responseString = executePostRequest("/Student/fetch-subject-mark.php", requestBody.toString(), currentToken);
        return new JSONObject(responseString);
    }
    
    // Fetch all subjects that are enrolled by current student
	public static JSONArray fetchEnrolledSubjects(String currentToken) throws Exception {
        // Get the raw string response.
        String responseString = executePostRequest("/Student/fetch-enrolled-subjects.php", "{}", currentToken);
        // Parse it into a JSONArray.
        return new JSONArray(responseString);
    }
	
	private static String executePostRequest(String endpoint, String jsonBody, String token) throws Exception {
        URL url = new URL(API_BASE + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = conn.getResponseCode();

        InputStreamReader reader = new InputStreamReader(responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream());
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(reader)) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        
        conn.disconnect();

        if (responseCode != 200) {
            JSONObject errorJson = new JSONObject(response.toString());
            String errorMessage = errorJson.optString("message", "An unknown error occurred.");
            throw new RuntimeException("Failed: HTTP " + responseCode + " - " + errorMessage);
        }

        return response.toString();
    }
}