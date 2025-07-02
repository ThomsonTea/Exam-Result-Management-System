package erms.teacher;

import erms.MainApp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TeacherMainMenu extends JPanel {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private TeacherCheckGrade checkGradePanel; // <-- Add this at class level

    private final String teacherID;
    private final String teacherName;

    public TeacherMainMenu(String id, String name) {
        this.teacherID = (id != null) ? id : "";
        this.teacherName = (name != null) ? name : "";

        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setBackground(new Color(70, 130, 180));

        JLabel userLabel = new JLabel("Logged in as: " + teacherID + " - " + teacherName);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFocusPainted(false);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Navigation Buttons Panel
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(160, getHeight())); // Fixed width for nav
        navPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        JButton btnMarkEntry = new JButton("Enter Student Marks");
        JButton btnCheckGrades = new JButton("Check Grades");

        btnMarkEntry.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCheckGrades.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMarkEntry.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Smaller font
        btnCheckGrades.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Smaller font
        btnMarkEntry.setMaximumSize(new Dimension(180, 40));
        btnCheckGrades.setMaximumSize(new Dimension(180, 40));

        navPanel.add(btnMarkEntry);
        navPanel.add(Box.createVerticalStrut(20)); // spacing
        navPanel.add(btnCheckGrades);
        add(navPanel, BorderLayout.WEST);

        // Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        TeacherMarkEntryPanel markEntryPanel = new TeacherMarkEntryPanel(teacherID);
        TeacherCheckGrade checkGradePanel = new TeacherCheckGrade(teacherID);

        contentPanel.add(markEntryPanel, "MarkEntry");
        contentPanel.add(checkGradePanel, "CheckGrade");

        add(contentPanel, BorderLayout.CENTER); // takes the remaining ~65-75%
        
        // Button Actions
        btnMarkEntry.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "MarkEntry"));
        btnCheckGrades.addActionListener((ActionEvent e) -> {
            checkGradePanel.reload(); // refresh the table
            cardLayout.show(contentPanel, "CheckGrade");
        });


        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "You have logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);

            SwingUtilities.getWindowAncestor(this).dispose();
            MainApp.main(null);
        });
    }

    // Optional for testing standalone
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Teacher Menu");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new TeacherMainMenu("T001", "Example Teacher"));
            frame.setVisible(true);
        });
    }
}
