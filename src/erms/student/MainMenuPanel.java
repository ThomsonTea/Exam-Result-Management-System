package erms.student;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainMenuPanel() {
        setLayout(new BorderLayout(0, 0)); 
        setPreferredSize(new Dimension(600, 400));

        JLabel lblWelcome = new JLabel("Login Successful! Welcome to the Main Menu.");
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(lblWelcome, BorderLayout.CENTER);

        // add buttons here to navigate to other panels
        // like a "Subject Marking" panel or a "View Results" panel.
    }
}