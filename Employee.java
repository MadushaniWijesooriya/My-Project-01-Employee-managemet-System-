package com.mycompany.employeesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

public class Employee extends JFrame {

    JTextField txtEmployeeId, txtFname, txtLname, txtAddress, txtNIC, txtContact, txtSalary, txtDeptId;
    JSpinner dobSpinner;
    JComboBox<String> educationBox, positionBox, genderBox;
    JTable employeeTable;
    DefaultTableModel tableModel;
    Connection conn = DBConnection.getConnection();

    public Employee() {
        setTitle("Employee");
        setSize(900, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        ImageIcon icon = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\12.jpeg");
        Image img = icon.getImage().getScaledInstance(800, 700, Image.SCALE_SMOOTH); 
        ImageIcon scaledIcon = new ImageIcon(img);

        JLabel background = new JLabel(scaledIcon);
        background.setBounds(0, 0, 800, 700);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 700));
        setContentPane(layeredPane);
        layeredPane.add(background, Integer.valueOf(0));

        JPanel formPanel = new JPanel(new GridLayout(13, 2, 10, 5));
        formPanel.setOpaque(false);
        formPanel.setBounds(50, 20, 700, 400);

        Color labelColor = Color.WHITE;
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);

        txtEmployeeId = new JTextField();
        txtFname = new JTextField();
        txtLname = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        txtAddress = new JTextField();
        txtNIC = new JTextField();
        txtContact = new JTextField();
        dobSpinner = new JSpinner(new SpinnerDateModel());
        dobSpinner.setEditor(new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd"));
        educationBox = new JComboBox<>(new String[]{"O/L", "A/L", "Diploma", "Degree", "Masters"});
        txtSalary = new JTextField();
        positionBox = new JComboBox<>(new String[]{"Assistant", "Clerk", "Manager", "Executive"});
        txtDeptId = new JTextField();

        String[] labels = {
            "Employee Id:", "First Name:", "Last Name:", "Gender:", "Address:",
            "NIC:", "Contact Number:", "Date Of Birth:", "Educated Level:",
            "Salary(Rs.):", "Position:", "Department Id:"
        };

        Component[] fields = {
            txtEmployeeId, txtFname, txtLname, genderBox, txtAddress,
            txtNIC, txtContact, dobSpinner, educationBox, txtSalary,
            positionBox, txtDeptId
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setForeground(labelColor);
            label.setFont(labelFont);
            formPanel.add(label);
            formPanel.add(fields[i]);
        }

        layeredPane.add(formPanel, Integer.valueOf(1));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(100, 430, 600, 50);

        JButton btnBack = createStyledButton("Back", new Color(255, 140, 0));
        JButton btnAdd = createStyledButton("Add", new Color(0, 120, 215));
        JButton btnDelete = createStyledButton("Delete", new Color(220, 53, 69));
        JButton btnClear = createStyledButton("Clear", new Color(128, 128, 128));
        JButton btnNext = createStyledButton("Next", new Color(40, 167, 69));

        buttonPanel.add(btnBack);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnNext);
        layeredPane.add(buttonPanel, Integer.valueOf(1));

        // Table setup
        tableModel = new DefaultTableModel(new String[]{
            "ID", "First Name", "Last Name", "Gender", "Address",
            "NIC", "Contact", "DOB", "Education", "Salary", "Position", "Dept ID"
        }, 0);

        employeeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(employeeTable);
        tableScrollPane.setBounds(30, 500, 750, 150);
        layeredPane.add(tableScrollPane, Integer.valueOf(1));

        // Load data initially
        loadEmployeeData();

        btnAdd.addActionListener(e -> {
            addEmployee();
            loadEmployeeData();
        });

        btnDelete.addActionListener(e -> {
            deleteEmployee();
            loadEmployeeData();
        });

        btnClear.addActionListener(e -> clearForm());
        btnBack.addActionListener(e -> {
            dispose();
            new UserCreation().setVisible(true);
        });

        btnNext.addActionListener(e -> {
            dispose();
            new Department().setVisible(true);
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void addEmployee() {
        try {
            String sql = "INSERT INTO employee (employeeid, firstname, lastname, gender, address, nic, contactNumber, dateofbirth, educatedlevel, salary, position, departmentid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtEmployeeId.getText());
            pst.setString(2, txtFname.getText());
            pst.setString(3, txtLname.getText());
            pst.setString(4, genderBox.getSelectedItem().toString());
            pst.setString(5, txtAddress.getText());
            pst.setString(6, txtNIC.getText());
            pst.setString(7, txtContact.getText());
            pst.setDate(8, new java.sql.Date(((Date) dobSpinner.getValue()).getTime()));
            pst.setString(9, educationBox.getSelectedItem().toString());
            pst.setDouble(10, Double.parseDouble(txtSalary.getText()));
            pst.setString(11, positionBox.getSelectedItem().toString());
            pst.setString(12, txtDeptId.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Added Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Adding Employee: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            String sql = "DELETE FROM employee WHERE employeeid = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtEmployeeId.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Employee Deleted Successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Employee ID Not Found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error Deleting Employee: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txtEmployeeId.setText("");
        txtFname.setText("");
        txtLname.setText("");
        genderBox.setSelectedIndex(0);
        txtAddress.setText("");
        txtNIC.setText("");
        txtContact.setText("");
        dobSpinner.setValue(new Date());
        educationBox.setSelectedIndex(0);
        txtSalary.setText("");
        positionBox.setSelectedIndex(0);
        txtDeptId.setText("");
    }

    private void loadEmployeeData() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM employee";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                    rs.getString("employeeid"),
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getString("gender"),
                    rs.getString("address"),
                    rs.getString("nic"),
                    rs.getString("contactNumber"),
                    rs.getDate("dateofbirth"),
                    rs.getString("educatedlevel"),
                    rs.getDouble("salary"),
                    rs.getString("position"),
                    rs.getString("departmentid")
                };
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Employee().setVisible(true));
    }
}