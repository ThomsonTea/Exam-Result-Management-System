package erms.student;

import erms.backend.AuthService;
import erms.MainApp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StudentMainMenu extends JPanel {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
    private JPanel contentPanel;
    
    private final AuthService authService;
    private final String currentToken;

    private final String studentID;
    private final String studentName;

    public StudentMainMenu(String id, String name, AuthService service, String token) {
        this.studentID = (id != null) ? id : "";
        this.studentName = (name != null) ? name : "";
        this.authService = service;
        this.currentToken = token;

        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setBackground(new Color(70, 130, 180));

        JLabel userLabel = new JLabel("Logged in as: " + studentID + " - " + studentName);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBorderPainted(false);
        logoutButton.setOpaque(true);
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);

        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setPreferredSize(new Dimension(160, getHeight())); // Fixed width for nav
        navPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        JButton btnViewMark = new JButton("View Marks/Grades");
        JButton btnCheckSubjects = new JButton("Check Subjects");

        btnViewMark.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCheckSubjects.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnViewMark.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Smaller font
        btnCheckSubjects.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Smaller font
        btnViewMark.setMaximumSize(new Dimension(180, 40));
        btnCheckSubjects.setMaximumSize(new Dimension(180, 40));

        navPanel.add(btnViewMark);
        navPanel.add(Box.createVerticalStrut(20)); // spacing
        navPanel.add(btnCheckSubjects);
        add(navPanel, BorderLayout.WEST);

        // Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        StudentViewMarkPanel viewMarkPanel = new StudentViewMarkPanel(studentID, studentName, authService, currentToken);
        StudentCheckEnrolledSubjects checkSubjectPanel = new StudentCheckEnrolledSubjects(studentID, authService, currentToken);

        contentPanel.add(viewMarkPanel, "Mark/GradeView");
        contentPanel.add(checkSubjectPanel, "CheckEnrolledSubjects");

        add(contentPanel, BorderLayout.CENTER); // takes the remaining ~65-75%
        
        // Button Actions
        btnViewMark.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "Mark/GradeView"));
        btnCheckSubjects.addActionListener((ActionEvent e) -> {
//        	checkSubjectPanel.reload(); // refresh the table
            cardLayout.show(contentPanel, "CheckEnrolledSubjects");
        });


        logoutButton.addActionListener(e -> {
        	handleLogout();
        });
    }

//    // Optional for testing standalone
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Student Menu");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(900, 500);
//            frame.setLocationRelativeTo(null);
//            frame.setContentPane(new StudentMainMenu("B032310000", "Example Student"));
//            frame.setVisible(true);
//        });
//    }
    
    private void handleLogout() {
        try {
            if(authService.logout(currentToken)) {
                SwingUtilities.getWindowAncestor(this).dispose();
                MainApp.main(null);
                
                JOptionPane.showMessageDialog(
                    this, 
                    "You have been successfully logged out.", 
                    "Logout", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception ex) {
            System.err.println("Logout failed on server: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                this, 
                "Could not contact the server, but you will be logged out locally.", 
                "Logout Warning", 
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}
