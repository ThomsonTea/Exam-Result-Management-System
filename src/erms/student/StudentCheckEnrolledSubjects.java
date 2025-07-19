package erms.student;

import erms.backend.StudentService;
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

public class StudentCheckEnrolledSubjects extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable subjectTable;
    private JButton exportBtn;
    private JComboBox<String> subjectFilter;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private String studentID;

    public StudentCheckEnrolledSubjects(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Check Enrolled Subjects Marks", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(title, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new BorderLayout());
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.add(new JLabel("Filter by Subject:"));
        subjectFilter = new JComboBox<>(new String[]{"All"});
        filterPanel.add(subjectFilter);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        exportBtn = new JButton("View in Google Sheets");
        exportBtn.setBorderPainted(false);
        exportBtn.setOpaque(true);
        exportBtn.setBackground(new Color(21, 115, 71));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFocusPainted(false);
        exportBtn.setFont(new Font("Arial", Font.BOLD, 12));
		exportBtn.setPreferredSize(new Dimension(190, 30));
        buttonPanel.add(exportBtn);

        controlsPanel.add(filterPanel, BorderLayout.WEST);
        controlsPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(titlePanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(controlsPanel);
        
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Subject ID", "Subject Name", "Score", "Grade", "Teacher ID"}, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        subjectTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(subjectTable);
        add(scrollPane, BorderLayout.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            subjectTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
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
                sorter.setRowFilter(RowFilter.regexFilter(Pattern.quote(selected), 0));
            }
        });
        

        // View the marks in a newly created google sheets
        exportBtn.addActionListener(e -> {
            exportBtn.setEnabled(false); // Disable while creating
            exportBtn.setText("Generating...");

            new Thread(() -> {
                try {
                	// Convert tableModel to JSON
                	JSONArray tableArray = new JSONArray();

                	// ---- Add header row first
                	JSONArray headerRow = new JSONArray();
                	for (int col = 0; col < tableModel.getColumnCount(); col++) {
                	    headerRow.put(tableModel.getColumnName(col));
                	}
                	tableArray.put(headerRow);

                	// ---- Add visible (filtered/sorted) rows
                	for (int viewRow = 0; viewRow < subjectTable.getRowCount(); viewRow++) {
                	    int modelRow = subjectTable.convertRowIndexToModel(viewRow);
                	    JSONArray rowArray = new JSONArray();
                	    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                	        Object cell = tableModel.getValueAt(modelRow, col);
                	        rowArray.put(cell != null ? cell.toString() : "");
                	    }
                	    tableArray.put(rowArray);
                	}

                	// --- Assign the data to JSONObject
                    JSONObject root = new JSONObject();
                    root.put("table", tableArray);

                    // Call export and get the sheet URL
                    String sheetUrl = StudentService.exportToSheets(root);

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

        // Initial data load
        reload();
    }

    public void reload() {
        try {
        	exportBtn.setEnabled(false); // Disable before load
            tableModel.setRowCount(0); // clear table
            subjectFilter.removeAllItems();
            subjectFilter.addItem("All");
            
            JSONObject requestData = new JSONObject();
            requestData.put("studentID", this.studentID);
            
            JSONArray subjectsDataArray = StudentService.fetchEnrolledSubjects(requestData);

            Set<String> subjects = new HashSet<>();

            for (int i = 0; i < subjectsDataArray.length(); i++) {
                JSONObject row = subjectsDataArray.getJSONObject(i);
                String subjectIDValue = row.getString("subjectID");
                String subjectName = row.getString("subjectName");

                Object score = row.has("score") ? row.getInt("score") : "N/A";
                String grade = row.optString("grade", "N/A");
                String tID = row.getString("teacherID");

                tableModel.addRow(new Object[]{subjectIDValue, subjectName, score, grade, tID});
                subjects.add(subjectIDValue);
            }

            subjects.stream().sorted().forEach(subjectFilter::addItem);

            // Enable export button only if table has rows
            if (tableModel.getRowCount() > 0) {
                exportBtn.setEnabled(true);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load marks: " + e.getMessage());
        }
    }
}