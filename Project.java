package com.mycompany.employeesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Project extends JFrame {

    JTextField projectIdField, projectNameField, descriptionField;
    JSpinner startdateSpinner, enddateSpinner;
    JComboBox<String> statusBox;
    JTable projectTable;
    DefaultTableModel tableModel;

    Connection conn = DBConnection.getConnection();

    public Project() {
        setTitle("Project");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Background Image
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\11.jpeg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(900, 600, Image.SCALE_SMOOTH); 
        ImageIcon backgroundIcon = new ImageIcon(scaledImage);

        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel projIdLabel = new JLabel("Project ID:");
        JLabel nameLabel = new JLabel("Project Name:");
        JLabel startDateLabel = new JLabel("Start Date:");
        JLabel endDateLabel = new JLabel("End Date:");
        JLabel statusLabel = new JLabel("Status:");
        JLabel descLabel = new JLabel("Description:");

        Color labelColor = Color.WHITE;
        Font labelFont = new Font("Arial", Font.BOLD, 13);
        for (JLabel label : new JLabel[]{projIdLabel, nameLabel, startDateLabel, endDateLabel, statusLabel, descLabel}) {
            label.setForeground(labelColor);
            label.setFont(labelFont);
        }

        projectIdField = new JTextField(15);
        projectNameField = new JTextField(15);
        descriptionField = new JTextField(15);

        startdateSpinner = new JSpinner(new SpinnerDateModel());
        startdateSpinner.setEditor(new JSpinner.DateEditor(startdateSpinner, "yyyy-MM-dd"));

        enddateSpinner = new JSpinner(new SpinnerDateModel());
        enddateSpinner.setEditor(new JSpinner.DateEditor(enddateSpinner, "yyyy-MM-dd"));

        statusBox = new JComboBox<>(new String[]{"Planned", "Ongoing", "Completed"});

        formPanel.add(projIdLabel);      formPanel.add(projectIdField);
        formPanel.add(nameLabel);        formPanel.add(projectNameField);
        formPanel.add(startDateLabel);   formPanel.add(startdateSpinner);
        formPanel.add(endDateLabel);     formPanel.add(enddateSpinner);
        formPanel.add(statusLabel);      formPanel.add(statusBox);
        formPanel.add(descLabel);        formPanel.add(descriptionField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton backBtn = new JButton("Back");
        JButton addBtn = new JButton("Add Project");
        JButton deleteBtn = new JButton("Delete Project");
        JButton nextBtn = new JButton("Next");
        JButton clearBtn = new JButton("Clear");

        Color white = Color.WHITE;
        backBtn.setBackground(new Color(255, 140, 0)); backBtn.setForeground(white);
        addBtn.setBackground(new Color(65, 105, 225)); addBtn.setForeground(white);
        deleteBtn.setBackground(new Color(220, 20, 60)); deleteBtn.setForeground(white);
        nextBtn.setBackground(new Color(40, 167, 69)); nextBtn.setForeground(white);
        clearBtn.setBackground(new Color(100, 100, 100)); clearBtn.setForeground(white);

        buttonPanel.add(backBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(nextBtn);

        // Table Panel
        tableModel = new DefaultTableModel();
        projectTable = new JTable(tableModel);
        tableModel.setColumnIdentifiers(new String[]{"Project ID", "Project Name", "Start Date", "End Date", "Status", "Description"});

        JScrollPane scrollPane = new JScrollPane(projectTable);
        scrollPane.setPreferredSize(new Dimension(850, 200));

        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.add(scrollPane);

        // Action Listeners
        addBtn.addActionListener(e -> addProject());
        deleteBtn.addActionListener(e -> deleteProject());
        clearBtn.addActionListener(e -> clearForm());

        backBtn.addActionListener(e -> {
            dispose();
            new Department().setVisible(true);
        });

        nextBtn.addActionListener(e -> {
            dispose();
            new Attendence().setVisible(true);
        });

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER); 
        centerPanel.add(tablePanel, BorderLayout.SOUTH);   

        backgroundLabel.add(centerPanel, BorderLayout.CENTER);

        loadProjectData(); 
    }

    private void addProject() {
        try {
            String sql = "INSERT INTO project (projectid, projectname, startdate, enddate, status, description) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            pst.setString(1, projectIdField.getText());
            pst.setString(2, projectNameField.getText());
            pst.setString(3, sdf.format(startdateSpinner.getValue()));
            pst.setString(4, sdf.format(enddateSpinner.getValue()));
            pst.setString(5, (String) statusBox.getSelectedItem());
            pst.setString(6, descriptionField.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Project added successfully!");
            clearForm();
            loadProjectData(); // Refresh table after insert
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding project: " + ex.getMessage());
        }
    }

    private void deleteProject() {
        try {
            String sql = "DELETE FROM project WHERE projectid = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, projectIdField.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Project deleted successfully!");
                clearForm();
                loadProjectData();
            } else {
                JOptionPane.showMessageDialog(this, "Project ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting project: " + ex.getMessage());
        }
    }

    private void clearForm() {
        projectIdField.setText("");
        projectNameField.setText("");
        descriptionField.setText("");
        startdateSpinner.setValue(new java.util.Date());
        enddateSpinner.setValue(new java.util.Date());
        statusBox.setSelectedIndex(0);
    }

    private void loadProjectData() {
        try {
            tableModel.setRowCount(0); 
            String sql = "SELECT * FROM project";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("projectid"));
                row.add(rs.getString("projectname"));
                row.add(rs.getString("startdate"));
                row.add(rs.getString("enddate"));
                row.add(rs.getString("status"));
                row.add(rs.getString("description"));
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading project data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Project().setVisible(true));
    }
}