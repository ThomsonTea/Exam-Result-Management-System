package erms.teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MarkEntryForm extends JPanel {

    private JComboBox<String> studentDropdown, subjectDropdown, teacherDropdown;

    public MarkEntryForm() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 248, 255));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel header = new JLabel("Enter Student Marks");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(33, 33, 33));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(header, gbc);
        gbc.gridwidth = 1;

        int row = 1;
        formPanel.add(label("Student ID:"), constraints(0, row));
        studentDropdown = new JComboBox<>(new String[] { "S001", "S002", "S003" });
        formPanel.add(studentDropdown, constraints(1, row++));

        formPanel.add(label("Subject ID:"), constraints(0, row));
        subjectDropdown = new JComboBox<>(new String[] { "SUB001", "SUB002", "SUB003" });
        formPanel.add(subjectDropdown, constraints(1, row++));

        formPanel.add(label("Teacher ID:"), constraints(0, row));
        teacherDropdown = new JComboBox<>(new String[] { "T001", "T002", "T003" });
        formPanel.add(teacherDropdown, constraints(1, row++));

        formPanel.add(label("Score:"), constraints(0, row));
        JTextField scoreField = new JTextField(10);
        formPanel.add(scoreField, constraints(1, row++));

        formPanel.add(label("Grade (Auto):"), constraints(0, row));
        JTextField gradeField = new JTextField(10);
        gradeField.setEditable(false);
        gradeField.setBackground(new Color(240, 240, 240));
        formPanel.add(gradeField, constraints(1, row++));

        JButton submitBtn = button("Submit Marks", new Color(30, 144, 255));
        JButton exportBtn = button("Export to Google Sheets", new Color(34, 139, 34));
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(submitBtn, gbc);
        gbc.gridx = 1;
        formPanel.add(exportBtn, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(getBackground());

        add(scrollPane, BorderLayout.CENTER);

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
                if (score >= 90) grade = "A";
                else if (score >= 80) grade = "B";
                else if (score >= 70) grade = "C";
                else if (score >= 60) grade = "D";
                else grade = "F";
                gradeField.setText(grade);
            }
        });

        // Submit mock
        submitBtn.addActionListener(e -> {
            String studentID = (String) studentDropdown.getSelectedItem();
            String subjectID = (String) subjectDropdown.getSelectedItem();
            String teacherID = (String) teacherDropdown.getSelectedItem();
            String score = scoreField.getText().trim();
            String grade = gradeField.getText();

            if (score.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid score.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String msg = String.format(
                    "\u2705 Mark Submitted (Mock):\nStudent: %s\nSubject: %s\nTeacher: %s\nScore: %s\nGrade: %s",
                    studentID, subjectID, teacherID, score, grade
            );
            JOptionPane.showMessageDialog(this, msg, "Submitted", JOptionPane.INFORMATION_MESSAGE);
        });

        // Export mock
        exportBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "\u2705 Exported to Google Sheets (Mock)", "Export", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return lbl;
    }

    private GridBagConstraints constraints(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);
        return gbc;
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
