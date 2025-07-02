package erms.teacher;

import javax.swing.*;
import java.awt.*;

public class TeacherMainPanel extends JPanel {
    public TeacherMainPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Teacher Mark Entry System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        MarkEntryForm form = new MarkEntryForm();
        add(form, BorderLayout.CENTER);
    }
}
