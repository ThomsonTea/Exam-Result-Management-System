package erms.backend;

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
		System.out.print("data:" + data.toString(2)); //pretty json format
		
	    URL url = new URL(API_BASE + "/Student/export-to-sheets.php"); 
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setDoOutput(true);

	    try (OutputStream os = conn.getOutputStream()) {
	        os.write(data.toString().getBytes("UTF-8"));
	        os.flush();
	    }

	    int responseCode = conn.getResponseCode();
	    System.out.print("\nerror code:" + responseCode);
	    return responseCode == 200;
	}
<<<<<<< HEAD
=======

	
	// fetch enrolled subjects for "View Marks/Grades" dropdown.
	public static JSONArray fetchSubjects(JSONObject data) throws Exception {
	    URL url = new URL(API_BASE + "/Student/fetch-enrolled-subjects-marks.php");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setDoOutput(true);

	    // Send JSON body
	    try (OutputStream os = conn.getOutputStream()) {
	        os.write(data.toString().getBytes("UTF-8"));
	        os.flush();
	    }

	    int responseCode = conn.getResponseCode();
	    if (responseCode != 200) {
	        throw new RuntimeException("Failed: HTTP error code : " + responseCode);
	    }

	    // Read response
	    StringBuilder response = new StringBuilder();
	    try (Scanner scanner = new Scanner(conn.getInputStream())) {
	        while (scanner.hasNextLine()) {
	            response.append(scanner.nextLine());
	        }
	    }

	    conn.disconnect();
	    return new JSONArray(response.toString());
	}


    
     // Fetches the specific mark for a student in a given subject.
    public static JSONArray fetchMarks(JSONObject data) throws Exception {
    	URL url = new URL(API_BASE + "/Student/fetch-subject-mark.php");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setDoOutput(true);

	    // Send JSON body
	    try (OutputStream os = conn.getOutputStream()) {
	        os.write(data.toString().getBytes("UTF-8"));
	        os.flush();
	    }

	    int responseCode = conn.getResponseCode();
	    if (responseCode != 200) {
	        throw new RuntimeException("Failed: HTTP error code : " + responseCode);
	    }

	    // Read response
	    StringBuilder response = new StringBuilder();
	    try (Scanner scanner = new Scanner(conn.getInputStream())) {
	        while (scanner.hasNextLine()) {
	            response.append(scanner.nextLine());
	        }
	    }

	    conn.disconnect();
	    return new JSONArray(response.toString());
    }
    
    // Fetch all subjects that are enrolled by current student
    public static JSONArray fetchEnrolledSubjects(JSONObject data) throws Exception {
    	URL url = new URL(API_BASE + "/Student/fetch-enrolled-subjects.php");
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Content-Type", "application/json");
	    conn.setDoOutput(true);

	    // Send JSON body
	    try (OutputStream os = conn.getOutputStream()) {
	        os.write(data.toString().getBytes("UTF-8"));
	        os.flush();
	    }

	    int responseCode = conn.getResponseCode();
	    if (responseCode != 200) {
	        throw new RuntimeException("Failed: HTTP error code : " + responseCode);
	    }

	    // Read response
	    StringBuilder response = new StringBuilder();
	    try (Scanner scanner = new Scanner(conn.getInputStream())) {
	        while (scanner.hasNextLine()) {
	            response.append(scanner.nextLine());
	        }
	    }

	    conn.disconnect();
	    return new JSONArray(response.toString());
    }
>>>>>>> student-ui-logic
}