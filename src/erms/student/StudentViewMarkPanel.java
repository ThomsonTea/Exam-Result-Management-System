package erms.student;

import org.json.JSONArray;
import org.json.JSONObject;

import erms.backend.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentViewMarkPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private JComboBox<String> subjectDropdown;
    private JTextField scoreField, gradeField;
	private String studentID;
	private String[] studentName;
    private final AuthService authService;
    private final String currentToken;

    public StudentViewMarkPanel(String studentID, String studentName, AuthService service, String token) {
    	this.studentID = studentID;
    	this.studentName = studentName.split(" ");
        this.authService = service;
        this.currentToken = token;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Welcome, " + this.studentName[0] + "!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        add(createViewMarkForm(), BorderLayout.CENTER);
        fetchAndDisplayMark(); // initial data fetch
    }

    private JPanel createViewMarkForm() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(new Color(245, 248, 255));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 30, 20, 30)
        ));

        // Fetch dropdown data
        subjectDropdown = new JComboBox<>();
        fetchSubjectsMarks();

        scoreField = new JTextField(10);
        scoreField.setEditable(false);
        scoreField.setBackground(new Color(240, 240, 240));
        
        gradeField = new JTextField(10);
        gradeField.setEditable(false);
        gradeField.setBackground(new Color(240, 240, 240));

        formPanel.add(createRow("Select Subject:", subjectDropdown));
        formPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Add some space
        formPanel.add(createRow("Your Score:", scoreField));
        formPanel.add(createRow("Your Grade:", gradeField));
        
        subjectDropdown.addActionListener(e -> fetchAndDisplayMark());

        formContainer.add(formPanel, BorderLayout.CENTER);

        return formContainer;
    }

     // Fetches the subjects the student is enrolled in and populates the dropdown.
    private void fetchSubjectsMarks() {
        try {
            JSONArray subjects = StudentService.fetchSubjects(this.currentToken);

            for (int i = 0; i < subjects.length(); i++) {
                JSONObject subject = subjects.getJSONObject(i);
                String display = subject.getString("subjectID") + " - " + subject.getString("subjectName");
                subjectDropdown.addItem(display);
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(this, "Failed to fetch subject list: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     // Called when a subject is selected. Fetches the corresponding mark and updates the text fields.
    private void fetchAndDisplayMark() {
        String selectedSubject = (String) subjectDropdown.getSelectedItem();
        if (selectedSubject == null || selectedSubject.isEmpty()) {
            scoreField.setText("");
            gradeField.setText("");
            return;
        }

        String subjectID = selectedSubject.split(" - ")[0].trim();

        try {
            JSONObject markObject = StudentService.fetchMark(subjectID, this.currentToken);

            if (markObject != null) {
                int score = markObject.getInt("score");
                String grade = markObject.getString("grade");

                scoreField.setText(String.valueOf(score));
                gradeField.setText(grade);
            } else {
                scoreField.setText("Not Marked Yet");
                gradeField.setText("N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Could not fetch mark: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            scoreField.setText("Not Marked Yet");
            gradeField.setText("N/A");
        }
    }

    private JPanel createRow(String labelText, JComponent input) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lbl.setPreferredSize(new Dimension(120, 25));
        row.add(lbl);
        row.add(input);
        return row;
    }

    private JButton button(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }
}
