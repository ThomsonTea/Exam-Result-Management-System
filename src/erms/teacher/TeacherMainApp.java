package erms.teacher;

import javax.swing.*;

public class TeacherMainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Teacher Dashboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new TeacherMainPanel());
            frame.setVisible(true);
        });
    }
}
