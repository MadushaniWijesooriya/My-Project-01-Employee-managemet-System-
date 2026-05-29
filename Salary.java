package com.mycompany.employeesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Salary extends JFrame {

    Connection conn = DBConnection.getConnection();
    private Image backgroundImage;
    private JTable salaryTable;
    private DefaultTableModel tableModel;

    public Salary() {
        setTitle("Salary");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        backgroundImage = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\sala.jpeg").getImage();

        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JTextField salaryIdField = new JTextField();
        JTextField empIdField = new JTextField();
        JTextField basicSalaryField = new JTextField();
        JTextField workingDaysField = new JTextField("26");
        JTextField presentDaysField = new JTextField();
        JTextField leaveDaysField = new JTextField();
        JTextField otHoursField = new JTextField();
        JTextField otRateField = new JTextField("0");
        JTextField bonusField = new JTextField();
        JTextField netSalaryField = new JTextField();
        netSalaryField.setEditable(false);
        netSalaryField.setForeground(new Color(0, 102, 0));
        netSalaryField.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Salary ID:")); formPanel.add(salaryIdField);
        formPanel.add(new JLabel("Employee ID:")); formPanel.add(empIdField);
        formPanel.add(new JLabel("Basic Salary (Rs):")); formPanel.add(basicSalaryField);
        formPanel.add(new JLabel("Working Days:")); formPanel.add(workingDaysField);
        formPanel.add(new JLabel("Present Days:")); formPanel.add(presentDaysField);
        formPanel.add(new JLabel("Leave Days:")); formPanel.add(leaveDaysField);
        formPanel.add(new JLabel("OT Hours:")); formPanel.add(otHoursField);
        formPanel.add(new JLabel("OT Rate (per hr):")); formPanel.add(otRateField);
        formPanel.add(new JLabel("Bonus (Rs):")); formPanel.add(bonusField);
        formPanel.add(new JLabel("Net Salary:")); formPanel.add(netSalaryField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton backBtn = new JButton("Back");
        JButton calculateBtn = new JButton("Calculate");
        JButton saveBtn = new JButton("Save");
        JButton clearBtn = new JButton("Clear");
        JButton deleteBtn = new JButton("Delete");
        JButton logoutBtn = new JButton("Logout");

        styleButton(backBtn, new Color(255, 215, 0));
        styleButton(calculateBtn, new Color(100, 149, 237));
        styleButton(saveBtn, new Color(60, 179, 113));
        styleButton(clearBtn, Color.GRAY);
        styleButton(deleteBtn, new Color(220, 20, 60));
        styleButton(logoutBtn, new Color(255, 99, 71));

        buttonPanel.add(backBtn);
        buttonPanel.add(calculateBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(logoutBtn);

        // Table setup
        String[] columns = {"Salary ID", "Emp ID", "Basic", "Working", "Present", "Leave", "OT Hrs", "OT Rate", "Bonus", "Net Salary"};
        tableModel = new DefaultTableModel(columns, 0);
        salaryTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(salaryTable);

        // Combine formPanel and buttonPanel at top
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(backgroundPanel);
        loadSalaryData();

        calculateBtn.addActionListener(e -> {
            try {
                double basic = Double.parseDouble(basicSalaryField.getText());
                int working = Integer.parseInt(workingDaysField.getText());
                int present = Integer.parseInt(presentDaysField.getText());
                int otHours = Integer.parseInt(otHoursField.getText());
                double otRate = Double.parseDouble(otRateField.getText());
                double bonus = Double.parseDouble(bonusField.getText());

                double perDay = basic / working;
                double salary = (perDay * present) + (otHours * otRate) + bonus;

                netSalaryField.setText(String.format("%.2f", salary));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values!");
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                String sql = "INSERT INTO salary (salaryid, employeeid, basicsalary, workingdays, presentdays, leavedays, othours, otrate, bonus, netsalary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, salaryIdField.getText());
                pst.setString(2, empIdField.getText());
                pst.setDouble(3, Double.parseDouble(basicSalaryField.getText()));
                pst.setInt(4, Integer.parseInt(workingDaysField.getText()));
                pst.setInt(5, Integer.parseInt(presentDaysField.getText()));
                pst.setInt(6, Integer.parseInt(leaveDaysField.getText()));
                pst.setInt(7, Integer.parseInt(otHoursField.getText()));
                pst.setDouble(8, Double.parseDouble(otRateField.getText()));
                pst.setDouble(9, Double.parseDouble(bonusField.getText()));
                pst.setDouble(10, Double.parseDouble(netSalaryField.getText()));

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Salary saved successfully!");
                    loadSalaryData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to save salary.");
                }

                pst.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error while saving: " + ex.getMessage());
            }
        });

        deleteBtn.addActionListener(e -> {
            String salaryId = salaryIdField.getText();
            if (salaryId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Salary ID to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this salary record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM salary WHERE salaryid = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, salaryId);
                    int rows = pst.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Salary record deleted.");
                        clearBtn.doClick();
                        loadSalaryData();
                    } else {
                        JOptionPane.showMessageDialog(this, "No record found.");
                    }
                    pst.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error while deleting: " + ex.getMessage());
                }
            }
        });

        clearBtn.addActionListener(e -> {
            salaryIdField.setText("");
            empIdField.setText("");
            basicSalaryField.setText("");
            workingDaysField.setText("26");
            presentDaysField.setText("");
            leaveDaysField.setText("");
            otHoursField.setText("");
            otRateField.setText("0");
            bonusField.setText("");
            netSalaryField.setText("");
        });

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new MainMenu().setVisible(true);
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new Leave().setVisible(true);
        });
    }

    private void loadSalaryData() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM salary";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("salaryid"),
                    rs.getString("employeeid"),
                    rs.getDouble("basicsalary"),
                    rs.getInt("workingdays"),
                    rs.getInt("presentdays"),
                    rs.getInt("leavedays"),
                    rs.getInt("othours"),
                    rs.getDouble("otrate"),
                    rs.getDouble("bonus"),
                    rs.getDouble("netsalary")
                };
                tableModel.addRow(row);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Salary().setVisible(true));
    }
}