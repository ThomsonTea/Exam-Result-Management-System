package erms.teacher;

import org.json.JSONArray;
import org.json.JSONObject;

import erms.backend.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class TeacherMarkEntryPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> studentDropdown, subjectDropdown;
    private String teacherID;

    public TeacherMarkEntryPanel(String teacherID) {
        this.teacherID = teacherID;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Teacher Mark Entry System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        add(createMarkEntryForm(), BorderLayout.CENTER);
    }

    private JPanel createMarkEntryForm() {
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
        studentDropdown = new JComboBox<>();
        subjectDropdown = new JComboBox<>();
        fetchDropdownData();

        JLabel teacherLabel = new JLabel(teacherID);

        JTextField scoreField = new JTextField(10);
        JTextField gradeField = new JTextField(10);
        gradeField.setEditable(false);
        gradeField.setBackground(new Color(240, 240, 240));

        formPanel.add(createRow("Student:", studentDropdown));
        formPanel.add(createRow("Subject:", subjectDropdown));
        formPanel.add(createRow("Teacher ID:", teacherLabel));
        formPanel.add(createRow("Score:", scoreField));
        formPanel.add(createRow("Grade (Auto):", gradeField));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitBtn = new JButton("Submit Mark");
        submitBtn.setOpaque(true);
        submitBtn.setBackground(new Color(30, 144, 255));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        
        Dimension buttonSize = new Dimension(140, 40);
        submitBtn.setPreferredSize(buttonSize);
        
        buttonPanel.add(submitBtn);
        buttonPanel.setOpaque(false);

        // Auto-grade logic
        scoreField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateGrade(); }
            public void removeUpdate(DocumentEvent e) { updateGrade(); }
            public void changedUpdate(DocumentEvent e) { updateGrade(); }

            private void updateGrade() {
                String scoreText = scoreField.getText().trim();
                if (!scoreText.matches("\\d{1,3}")) {
                    gradeField.setText("");
                    return;
                }
                int score = Integer.parseInt(scoreText);
                String grade;
                if (score >= 80) grade = "A";
                else if (score >= 70) grade = "B";
                else if (score >= 60) grade = "C";
                else if (score >= 50) grade = "D";
                else if (score >= 40) grade = "E";
                else grade = "F";
                gradeField.setText(grade);
            }
        });

        submitBtn.addActionListener(e -> {
        	String studentSelection = (String) studentDropdown.getSelectedItem();
            String subjectSelection = (String) subjectDropdown.getSelectedItem();

            // Extract only the ID (before " - ")
            String studentID = studentSelection.split(" - ")[0];
            String subjectID = subjectSelection.split(" - ")[0];
            String score = scoreField.getText().trim();
            String grade = gradeField.getText();

            if (score.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid score.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JSONObject data = new JSONObject();
            data.put("studentID", studentID);
            data.put("subjectID", subjectID);
            data.put("teacherID", teacherID);
            data.put("score", Integer.parseInt(score));
            data.put("grade", grade);
            
            System.out.print("data: " + data);

            try {
                boolean result = TeacherService.submitMark(data);
                if (result) {
                    JOptionPane.showMessageDialog(this, "✅ Mark submitted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Submission failed. The student might already have a mark for this subject.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formContainer.add(formPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        return formContainer;
    }

    private void fetchDropdownData() {
        try {
            JSONArray students = TeacherService.fetchStudents();
            for (int i = 0; i < students.length(); i++) {
                JSONObject student = students.getJSONObject(i);
                String display = student.getString("studentID") + " - " + student.getString("studentName");
                studentDropdown.addItem(display);
            }

            JSONArray subjects = TeacherService.fetchSubjects(teacherID);
            for (int i = 0; i < subjects.length(); i++) {
                JSONObject subject = subjects.getJSONObject(i);
                String display = subject.getString("subjectID") + " - " + subject.getString("subjectName");
                subjectDropdown.addItem(display);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Failed to fetch data: " + e.getMessage());
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
