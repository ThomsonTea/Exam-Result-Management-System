package erms.student;

import erms.backend.ApiException;
import erms.backend.AuthService;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;

    // The AuthService instance that will be use to make API calls
    private final AuthService authService;

    public LoginPanel(JPanel mainPanel, CardLayout cardLayout) {
        this.authService = new AuthService();
        initialize(mainPanel, cardLayout);
    }

    private void initialize(JPanel mainPanel, CardLayout cardLayout) {

        JLabel lblLoginTitle = new JLabel("Exam Result Management System");
        lblLoginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblLoginTitle.setFont(new Font("Tahoma", Font.BOLD, 16));

        JLabel lblUsername = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setColumns(10);

        JLabel lblPassword = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                attemptLogin(mainPanel, cardLayout);
            }
        });

        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addItemListener(e -> {
            passwordField.setEchoChar(e.getStateChange() == ItemEvent.SELECTED ? (char) 0 : '•');
        });

        // HORIZONTAL LAYOUT
        GroupLayout groupLayout = new GroupLayout(this);
        this.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(Alignment.CENTER)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap(50, Short.MAX_VALUE)
                    .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addComponent(lblLoginTitle, Alignment.CENTER, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                        .addGroup(Alignment.CENTER, groupLayout.createSequentialGroup()
                            .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                .addComponent(lblPassword)
                                .addComponent(lblUsername))
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(showPasswordCheckBox)
                                .addComponent(passwordField)
                                .addComponent(usernameField, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
                        .addComponent(btnLogin, Alignment.CENTER, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(50, Short.MAX_VALUE))
        );
        
        // VERTICAL LAYOUT
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(Alignment.CENTER)
                .addGroup(groupLayout.createSequentialGroup()
                    .addContainerGap(40, Short.MAX_VALUE)
                    .addComponent(lblLoginTitle)
                    .addGap(30)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblUsername)
                        .addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblPassword)
                        .addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addComponent(showPasswordCheckBox)
                    .addGap(18)
                    .addComponent(btnLogin)
                    .addContainerGap(62, Short.MAX_VALUE))
        );
    }
    
    private void attemptLogin(JPanel mainPanel, CardLayout cardLayout) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Call the service to perform the login.
            // All the complex HTTP/JSON logic is hidden inside authService.login()
            if (authService.login(username, password)) {
                // If login() returns true, it was a success.
                cardLayout.show(mainPanel, "MAIN_MENU_CARD");
            }
        } catch (ApiException ex) {
            // If login() throws an exception, it was a failure.
            // We can display the specific error message from the exception.
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Login Failed: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}