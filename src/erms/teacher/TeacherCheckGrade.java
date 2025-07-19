package erms.teacher;

import erms.backend.TeacherService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class TeacherCheckGrade extends JPanel {

    private JTable gradeTable;
    private JButton exportBtn;
    private JComboBox<String> subjectFilter;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private String teacherID;
    private JSONArray marks;

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
        JPanel filterPanel = new JPanel(new BorderLayout());

		//--- Left side: Label + ComboBox
		JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftPanel.add(new JLabel("Filter by Subject:"));
		subjectFilter = new JComboBox<>(new String[]{"All"});
		leftPanel.add(subjectFilter);
		
		//--- Right side: Export Button
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		exportBtn = new JButton("View in Google Sheets");
		exportBtn.setEnabled(false); // Disable it initially
		exportBtn.setOpaque(true);
		exportBtn.setBackground(new Color(30, 144, 255));
		exportBtn.setForeground(Color.WHITE);
		exportBtn.setFocusPainted(false);
		exportBtn.setPreferredSize(new Dimension(190, 30));
		rightPanel.add(exportBtn);
		
		//--- Add both to filterPanel, then topPanel
		filterPanel.add(leftPanel, BorderLayout.WEST);
		filterPanel.add(rightPanel, BorderLayout.EAST);
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
                
        // Export button (is disabled first before tableModel is populated)
        exportBtn.addActionListener(e -> {
            exportBtn.setEnabled(false); // Disable while creating
            exportBtn.setText("Generating...");

            new Thread(() -> {
                try {
                    // Convert tableModel to JSON
                    JSONArray tableArray = new JSONArray();
                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        JSONArray rowArray = new JSONArray();
                        for (int col = 0; col < tableModel.getColumnCount(); col++) {
                            Object cell = tableModel.getValueAt(row, col);
                            rowArray.put(cell != null ? cell.toString() : "");
                        }
                        tableArray.put(rowArray);
                    }

                    JSONObject root = new JSONObject();
                    root.put("table", tableArray);

                    // Call export and get the sheet URL
                    String sheetUrl = TeacherService.exportToSheets(root);

                    SwingUtilities.invokeLater(() -> {
                        exportBtn.setEnabled(true);
                        exportBtn.setText("View in Google Sheets");

                        if (sheetUrl != null) {
                            JOptionPane.showMessageDialog(null, "✅ Sheet created!", "Success", JOptionPane.INFORMATION_MESSAGE);

                            try {
                                Desktop.getDesktop().browse(new URI(sheetUrl));
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Couldn't open sheet in browser: " + ex.getMessage());
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "❌ Failed to export data to Google Sheets.");
                        }
                    });

                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        exportBtn.setEnabled(true);
                        exportBtn.setText("View in Google Sheets");
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        });
      
           
        // Initial load
        reload();
    }

    public void reload() {
        try {
        	exportBtn.setEnabled(false); // Disable before load
            tableModel.setRowCount(0); // clear table
            subjectFilter.removeAllItems();
            subjectFilter.addItem("All");

            marks = TeacherService.fetchMarks(teacherID);
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
            
            // ✅ Enable export button only if table has rows
            if (tableModel.getRowCount() > 0) {
                exportBtn.setEnabled(true);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to load marks: " + e.getMessage());
        }
    }
}
