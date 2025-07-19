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

    public static JSONArray fetchStudents() throws Exception {
        URL url = new URL(API_BASE + "/Teacher/fetch-students.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        Scanner scanner = new Scanner(conn.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        return new JSONArray(response);
    }

    public static JSONArray fetchSubjects(String teacherID) throws Exception {
        URL url = new URL(API_BASE + "/Teacher/fetch-subjects.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JSONObject payload = new JSONObject();
        payload.put("teacherID", teacherID);

        OutputStream os = conn.getOutputStream();
        os.write(payload.toString().getBytes());
        os.flush();
        os.close();

        Scanner scanner = new Scanner(conn.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        return new JSONArray(response);
    }

    public static boolean submitMark(JSONObject data) throws Exception {
        URL url = new URL(API_BASE + "/Teacher/submit-mark.php");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        os.write(data.toString().getBytes());
        os.flush();
        os.close();

        return conn.getResponseCode() == 200;
    }
    
    public static String exportToSheets(JSONObject data) {
        try {
        	String appScript = "https://script.google.com/macros/s/AKfycbxe39cgzDIR0MPI7_Ky4KpKTN9Ksx8O-bd-RT1zwJuGb8cO_NgtZ10YiZiBEXalb83k/exec";
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

    
    public static JSONArray fetchMarks(String teacherID) throws Exception {
        URL url = new URL(API_BASE + "/Teacher/fetch-marks.php?teacherID=" + teacherID);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");

        Scanner scanner = new Scanner(conn.getInputStream());
        String response = scanner.useDelimiter("\\A").next();
        scanner.close();

        return new JSONArray(response);
    }

}
