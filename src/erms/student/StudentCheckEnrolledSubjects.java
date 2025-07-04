package erms.student;

import erms.backend.StudentService;

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

public class StudentCheckEnrolledSubjects extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable subjectTable;
    private JComboBox<String> subjectFilter;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private String studentID;

    public StudentCheckEnrolledSubjects(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Check Enrolled Subjects", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(title, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel(new BorderLayout());
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filterPanel.add(new JLabel("Filter by Subject:"));
        subjectFilter = new JComboBox<>(new String[]{"All"});
        filterPanel.add(subjectFilter);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton submitBtn = new JButton("Export to Google Sheets");
        submitBtn.setBorderPainted(false);
//        submitBtn.setOpaque(true);
        submitBtn.setBackground(new Color(21, 115, 71));
        submitBtn.setForeground(Color.WHITE);
//        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 12));
//        submitBtn.setPreferredSize(new Dimension(220, 35));
        buttonPanel.add(submitBtn);

        controlsPanel.add(filterPanel, BorderLayout.WEST);
        controlsPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(titlePanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(controlsPanel);
        
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"subjectID", "subjectName", "score", "grade", "teacherID"}, 0) {
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

        // export to google sheets
        submitBtn.addActionListener(e -> {
            submitBtn.setEnabled(false);
            submitBtn.setText("Exporting, please wait...");

            // using SwingWorker for multithreading.
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                private String errorMessage = null;

                @Override
                protected Void doInBackground() {
                    try {
                        String[] jsonKeys = {"subjectID", "subjectName", "score", "grade", "teacherID"};
                        JSONArray tableData = new JSONArray();

                        for (int i = 0; i < subjectTable.getRowCount(); i++) {
                            int modelRow = subjectTable.convertRowIndexToModel(i);
                            JSONObject rowObject = new JSONObject();
                            
                            for (int col = 0; col < jsonKeys.length; col++) {
                                rowObject.put(jsonKeys[col], tableModel.getValueAt(modelRow, col));
                            }
                            rowObject.put("studentID", studentID);
                            tableData.put(rowObject);
                        }

                        boolean success = StudentService.exportDataToSheets(tableData);
                        
                        if (!success) {
                            errorMessage = "Server returned an error during export.";
                        }

                    } catch (Exception ex) {
                        errorMessage = ex.getMessage();
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {

                    if (errorMessage == null) {
                        JOptionPane.showMessageDialog(
                            StudentCheckEnrolledSubjects.this,
                            "Data successfully exported to Google Sheets!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            StudentCheckEnrolledSubjects.this,
                            "Export failed: " + errorMessage,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                    
                    submitBtn.setText("Export to Google Sheets");
                    submitBtn.setEnabled(true);
                }
            };
            worker.execute();
        });

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

        // Initial data load
//        reload();
    }

//    public void reload() {
//        try {
//            tableModel.setRowCount(0);
//            subjectFilter.removeAllItems();
//            subjectFilter.addItem("All");
//            
//            JSONObject requestData = new JSONObject();
//            requestData.put("studentID", this.studentID);
//            
//            JSONArray subjectsDataArray = StudentService.fetchEnrolledSubjects(requestData);
//
//            Set<String> subjects = new HashSet<>();
//
//            for (int i = 0; i < subjectsDataArray.length(); i++) {
//                JSONObject row = subjectsDataArray.getJSONObject(i);
//                String subjectIDValue = row.getString("subjectID");
//                String subjectName = row.getString("subjectName");
//
//                Object score = row.has("score") ? row.getInt("score") : "N/A";
//                String grade = row.optString("grade", "N/A");
//                String tID = row.getString("teacherID");
//
//                tableModel.addRow(new Object[]{subjectIDValue, subjectName, score, grade, tID});
//                subjects.add(subjectIDValue);
//            }
//
//            subjects.stream().sorted().forEach(subjectFilter::addItem);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Failed to load marks: " + e.getMessage());
//        }
//    }
}