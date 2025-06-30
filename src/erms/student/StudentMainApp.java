package erms.student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StudentMainApp {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Set a modern Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            createAndShowGui();
        });
    }

    private static void createAndShowGui() {
        // 1. Create the main frame. It's the only JFrame in the app.
        JFrame frame = new JFrame("Exam Result Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Application closing.");
            }
        });

        // 2. Create the CardLayout and a panel to hold the "cards".
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // 3. Create instances of our view panels.
        LoginPanel loginPanel = new LoginPanel(mainPanel, cardLayout);
        MainMenuPanel mainMenuPanel = new MainMenuPanel();

        // 4. Add the panels to the main "deck", giving each a unique name.
        mainPanel.add(loginPanel, "LOGIN_CARD");
        mainPanel.add(mainMenuPanel, "MAIN_MENU_CARD");

        // 5. Add the main panel to the frame.
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        // 6. Show the first card (the login panel).
        cardLayout.show(mainPanel, "LOGIN_CARD");

        // 7. Pack the frame and display it.
        frame.pack(); // Automatically sizes the window
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}