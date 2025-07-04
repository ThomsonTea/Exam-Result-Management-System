package erms.backend;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

}