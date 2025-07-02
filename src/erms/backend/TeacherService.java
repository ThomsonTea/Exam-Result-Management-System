package erms.backend;

import org.json.JSONArray;
import org.json.JSONObject;

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
