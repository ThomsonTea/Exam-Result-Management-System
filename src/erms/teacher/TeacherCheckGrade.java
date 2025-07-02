package erms.teacher;

import erms.backend.TeacherService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class TeacherCheckGrade extends JPanel {

    private JTable gradeTable;
    private JComboBox<String> subjectFilter;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private String teacherID;

    public TeacherCheckGrade(String teacherID) {
        this.teacherID = teacherID;
        setLayout(new BorderLayout());

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Check Student Grades", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter by Subject:"));

        subjectFilter = new JComboBox<>(new String[]{"All"});
        filterPanel.add(subjectFilter);
        topPanel.add(filterPanel);
        add(topPanel, BorderLayout.NORTH);

        // Table model
        tableModel = new DefaultTableModel(new String[]{"Student ID", "Subject ID", "Teacher ID", "Score", "Grade"}, 0);
        gradeTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        gradeTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(gradeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Center cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < gradeTable.getColumnCount(); i++) {
            gradeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Filter logic
        subjectFilter.addActionListener(e -> {
            Object selectedObj = subjectFilter.getSelectedItem();
            if (selectedObj == null) {
                sorter.setRowFilter(null);
                return;
            }

            String selected = selectedObj.toString();
            if ("All".equals(selected)) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter(Pattern.quote(selected), 1)); // column 1 = Subject ID
            }
        });

        // Initial load
        reload();
    }

    public void reload() {
        try {
            tableModel.setRowCount(0); // clear table
            subjectFilter.removeAllItems();
            subjectFilter.addItem("All");

            JSONArray marks = TeacherService.fetchMarks(teacherID);
            Set<String> subjects = new HashSet<>();

            for (int i = 0; i < marks.length(); i++) {
                JSONObject row = marks.getJSONObject(i);
                String studentID = row.getString("studentID");
                String subjectID = row.getString("subjectID");
                String tID = row.getString("teacherID");
                String score = row.get("score").toString();
                String grade = row.getString("grade");

                tableModel.addRow(new Object[]{studentID, subjectID, tID, score, grade});
                subjects.add(subjectID);
            }

            // Add unique subjects to filter dropdown
            for (String sub : subjects) {
                subjectFilter.addItem(sub);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to load marks: " + e.getMessage());
        }
    }
}
