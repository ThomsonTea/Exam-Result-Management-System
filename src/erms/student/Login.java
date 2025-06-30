package erms.student;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import javax.swing.SwingConstants;

import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Login {

	private JFrame frame;
	private JTextField usernameField;
	private JPasswordField passwordField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("ERMS Login");
		frame.setBounds(100, 100, 450, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); 
		
		JLabel lblLoginTitle = new JLabel("Exam Result Management System");
		lblLoginTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JLabel lblUsername = new JLabel("Username:");
		
		usernameField = new JTextField();
		usernameField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		
		passwordField = new JPasswordField();
		
		JButton btnLogin = new JButton("Login");

		JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
		
		// this is the logic that toggles the password's visibility.
		showPasswordCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					// if checkbox is ticked, show the password.
					// setting echo char to 0 reveals the text.
					passwordField.setEchoChar((char) 0);
				} else {
					// if checkbox is unticked, hide the password.
					passwordField.setEchoChar('•');
				}
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		
		// HORIZONTAL LAYOUT DEFINITION
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
		
		// VERTICAL LAYOUT DEFINITION
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
					.addPreferredGap(ComponentPlacement.UNRELATED) // gap between password field and checkbox
					.addComponent(showPasswordCheckBox) // add checkbox to the vertical layout
					.addGap(18) // gap between checkbox and login button
					.addComponent(btnLogin)
					.addContainerGap(62, Short.MAX_VALUE))
		);
		
		frame.getContentPane().setLayout(groupLayout);
	}
}