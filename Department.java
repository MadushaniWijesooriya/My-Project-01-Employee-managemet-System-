package com.mycompany.employeesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Department extends JFrame {

    JTextField departmentIdField, departmentNameField, locationField;
    JTable departmentTable;
    DefaultTableModel tableModel;
    Connection conn = DBConnection.getConnection();

    public Department() {
        setTitle("Department");
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // Background Image
        ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\dep.jpeg");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 700, 600);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(700, 600));
        setContentPane(layeredPane);
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBounds(50, 20, 600, 120);

        JLabel deptIdLabel = new JLabel("Department ID:");
        JLabel deptNameLabel = new JLabel("Department Name:");
        JLabel locationLabel = new JLabel("Location:");

        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Color labelColor = Color.BLACK;
        for (JLabel lbl : new JLabel[]{deptIdLabel, deptNameLabel, locationLabel}) {
            lbl.setFont(labelFont);
            lbl.setForeground(labelColor);
        }

        departmentIdField = new JTextField(15);
        departmentNameField = new JTextField(15);
        locationField = new JTextField(15);

        formPanel.add(deptIdLabel); formPanel.add(departmentIdField);
        formPanel.add(deptNameLabel); formPanel.add(departmentNameField);
        formPanel.add(locationLabel); formPanel.add(locationField);

        layeredPane.add(formPanel, Integer.valueOf(1));

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(50, 150, 600, 50);

        JButton backBtn = createButton("Back", new Color(255, 165, 0));
        JButton addBtn = createButton("Add Department", new Color(70, 130, 180));
        JButton deleteBtn = createButton("Delete Department", new Color(178, 34, 34));
        JButton clearBtn = createButton("Clear", new Color(100, 100, 100));
        JButton nextBtn = createButton("Next", new Color(40, 167, 69));

        buttonPanel.add(backBtn); buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn); buttonPanel.add(clearBtn); buttonPanel.add(nextBtn);

        layeredPane.add(buttonPanel, Integer.valueOf(1));

        // Table Panel
        tableModel = new DefaultTableModel(new String[]{"Department ID", "Department Name", "Location"}, 0);
        departmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.setBounds(30, 230, 640, 300);
        layeredPane.add(scrollPane, Integer.valueOf(1));

        // Load data initially
        loadDepartments();

        // Button Actions
        addBtn.addActionListener(e -> {
            addDepartment();
            loadDepartments();
        });

        deleteBtn.addActionListener(e -> {
            deleteDepartment();
            loadDepartments();
        });

        clearBtn.addActionListener(e -> clearForm());

        backBtn.addActionListener(e -> {
            dispose();
            new Employee().setVisible(true);  
        });

        nextBtn.addActionListener(e -> {
            dispose();
            new Project().setVisible(true);  
        });
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void addDepartment() {
        try {
            if (departmentIdField.getText().isEmpty() || departmentNameField.getText().isEmpty() || locationField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }
            String sql = "INSERT INTO department (departmentid, departmentname, location) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, departmentIdField.getText());
            pst.setString(2, departmentNameField.getText());
            pst.setString(3, locationField.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Department added successfully!");
            clearForm();
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Department ID already exists!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding department: " + ex.getMessage());
        }
    }

    private void deleteDepartment() {
        try {
            if (departmentIdField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Department ID to delete");
                return;
            }
            String sql = "DELETE FROM department WHERE departmentid = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, departmentIdField.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Department deleted successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Department ID not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting department: " + ex.getMessage());
        }
    }

    private void clearForm() {
        departmentIdField.setText("");
        departmentNameField.setText("");
        locationField.setText("");
    }

    private void loadDepartments() {
        try {
            tableModel.setRowCount(0); 
            String sql = "SELECT * FROM department";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("departmentid"),
                    rs.getString("departmentname"),
                    rs.getString("location")
                };
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading departments: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Department().setVisible(true));
    }
}