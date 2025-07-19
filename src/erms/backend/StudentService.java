package erms.backend;

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
	public static boolean exportDataToSheets(JSONArray data) throws Exception {
	    // Wrap data array inside a parent object with key "data"
	    JSONObject payload = new JSONObject();
	    payload.put("data", data);

	    System.out.print("Sending JSON payload:\n" + payload.toString(2));

	    URL url = new URL(API_BASE + "/Student/export-to-sheets.php");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	    conn.setDoOutput(true);

	    try (OutputStream os = conn.getOutputStream()) {
	        os.write(payload.toString().getBytes("UTF-8")); // Send wrapped object
	        os.flush();
	    }

	    int responseCode = conn.getResponseCode();
	    System.out.println("Export response code: " + responseCode);

	    return responseCode == 200;
	}


	
	// fetch enrolled subjects for "View Marks/Grades" dropdown.
	public static JSONArray fetchSubjects(String currentToken) throws Exception {
        // --- THE FIX ---
        // 1. Get the raw string response from the server.
        String responseString = executePostRequest("/Student/fetch-enrolled-subjects-marks.php", "{}", currentToken);
        // 2. Parse the string into a JSONArray and return it.
        return new JSONArray(responseString);
    }


    
     // Fetches the specific mark for a student in a given subject.
	public static JSONObject fetchMark(String subjectID, String currentToken) throws Exception {
        JSONObject requestBody = new JSONObject();
        requestBody.put("subjectID", subjectID);
        // This method was already correct in the previous step.
        String responseString = executePostRequest("/Student/fetch-subject-mark.php", requestBody.toString(), currentToken);
        return new JSONObject(responseString);
    }
    
    // Fetch all subjects that are enrolled by current student
	public static JSONArray fetchEnrolledSubjects(String currentToken) throws Exception {
        // --- THE FIX ---
        // 1. Get the raw string response.
        String responseString = executePostRequest("/Student/fetch-enrolled-subjects.php", "{}", currentToken);
        // 2. Parse it into a JSONArray.
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