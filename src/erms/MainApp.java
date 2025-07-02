package erms;
import erms.backend.ApiException;

import erms.backend.AuthService;
import erms.teacher.TeacherMainMenu;

import javax.swing.*;

import org.json.JSONObject;

import java.awt.*;
import java.util.Objects;

public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainApp().showLoginUI();
        });
    }

    public void showLoginUI() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(350, 250);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("User ID:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JLabel roleLabel = new JLabel("Role:");
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Teacher", "Student"});

        JButton loginBtn = new JButton("Login");

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(roleLabel, gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginBtn, gbc);

        loginFrame.setContentPane(panel);
        loginFrame.setVisible(true);

        loginBtn.addActionListener(e -> {
            String userid = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String role = Objects.requireNonNull(roleCombo.getSelectedItem()).toString();

            if (userid.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please enter both user ID and password.",
                        "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AuthService authService = new AuthService();

            try {
                JSONObject response = authService.login(userid, password, role);

                // Extract id and name from response
                String id = response.getString("id");
                String name = response.getString("name");

                System.out.println("Logged in as: " + name + " (ID: " + id + ")");

                loginFrame.dispose();

                if (role.equals("Teacher")) {
                    openTeacherView(id, name);
                } else {
                    openStudentView(id, name);
                }
            } catch (ApiException ex) {
                JOptionPane.showMessageDialog(loginFrame, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void openTeacherView(String id, String name) {
        JFrame frame = new JFrame("Teacher Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new TeacherMainMenu(id, name));
        frame.setVisible(true);
    }

    // replace the actual student UI inside this function below 
    private void openStudentView(String id, String name) {
        JFrame frame = new JFrame("Student Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel studentPanel = new JPanel();
        studentPanel.add(new JLabel("Welcome to the Student View (Placeholder)"));
        frame.setContentPane(studentPanel);

        frame.setVisible(true);
    }
}
