package com.mycompany.employeesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class Attendence extends JFrame {

    JTextField attendanceIdField, empIdField;
    JSpinner dateSpinner, checkInSpinner, checkOutSpinner;
    JComboBox<String> statusBox;
    JTable attendanceTable;
    DefaultTableModel tableModel;

    Connection conn = DBConnection.getConnection();

    public Attendence() {
        setTitle("Attendance");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Background image
        ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\att.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel idLabel = new JLabel("Attendance ID:");
        JLabel empIdLabel = new JLabel("Employee ID:");
        JLabel dateLabel = new JLabel("Date:");
        JLabel checkInLabel = new JLabel("Check-In Time:");
        JLabel checkOutLabel = new JLabel("Check-Out Time:");
        JLabel statusLabel = new JLabel("Status:");

        Color labelColor = Color.BLACK;
        Font labelFont = new Font("Arial", Font.BOLD, 13);
        for (JLabel label : new JLabel[]{idLabel, empIdLabel, dateLabel, checkInLabel, checkOutLabel, statusLabel}) {
            label.setForeground(labelColor);
            label.setFont(labelFont);
        }

        attendanceIdField = new JTextField(15);
        empIdField = new JTextField(15);

        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        checkInSpinner = new JSpinner(new SpinnerDateModel());
        checkInSpinner.setEditor(new JSpinner.DateEditor(checkInSpinner, "HH:mm"));

        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        checkOutSpinner.setEditor(new JSpinner.DateEditor(checkOutSpinner, "HH:mm"));

        statusBox = new JComboBox<>(new String[]{"Present", "Absent"});

        formPanel.add(idLabel);         formPanel.add(attendanceIdField);
        formPanel.add(empIdLabel);      formPanel.add(empIdField);
        formPanel.add(dateLabel);       formPanel.add(dateSpinner);
        formPanel.add(checkInLabel);    formPanel.add(checkInSpinner);
        formPanel.add(checkOutLabel);   formPanel.add(checkOutSpinner);
        formPanel.add(statusLabel);     formPanel.add(statusBox);

        // Button Panel 
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton backBtn = new JButton("Back");
        JButton addBtn = new JButton("Add Attendance");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        JButton nextBtn = new JButton("Next");

        backBtn.setBackground(new Color(255, 165, 0));     backBtn.setForeground(Color.WHITE);
        addBtn.setBackground(new Color(100, 149, 237));    addBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 53, 69));   deleteBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(new Color(128, 128, 128));  clearBtn.setForeground(Color.WHITE);
        nextBtn.setBackground(new Color(40, 167, 69));     nextBtn.setForeground(Color.WHITE);

        buttonPanel.add(backBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(nextBtn);

        // Table Panel
        tableModel = new DefaultTableModel();
        attendanceTable = new JTable(tableModel);
        tableModel.setColumnIdentifiers(new String[]{"Attendance ID", "Employee ID", "Date", "Check-In", "Check-Out", "Status"});

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setPreferredSize(new Dimension(750, 200));

        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.add(scrollPane);

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER); 
        centerPanel.add(tablePanel, BorderLayout.SOUTH);    

        backgroundLabel.add(centerPanel, BorderLayout.CENTER);

        // Action Listeners
        addBtn.addActionListener(e -> addAttendance());
        deleteBtn.addActionListener(e -> deleteAttendance());
        clearBtn.addActionListener(e -> clearForm());

        backBtn.addActionListener(e -> {
            dispose();
            new Project().setVisible(true);
        });

        nextBtn.addActionListener(e -> {
            dispose();
            new Leave().setVisible(true);
        });

        loadAttendanceData(); 
    }

    private void addAttendance() {
        try {
            String sql = "INSERT INTO attendance (attendanceid, employeeid, date, checkintime, checkouttime, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

            pst.setString(1, attendanceIdField.getText());
            pst.setString(2, empIdField.getText());
            pst.setString(3, sdfDate.format(dateSpinner.getValue()));
            pst.setString(4, sdfTime.format(checkInSpinner.getValue()));
            pst.setString(5, sdfTime.format(checkOutSpinner.getValue()));
            pst.setString(6, (String) statusBox.getSelectedItem());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance added successfully!");
            clearForm();
            loadAttendanceData(); 
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding attendance: " + ex.getMessage());
        }
    }

    private void updateAttendance() {
        try {
            String sql = "UPDATE attendance SET employeeid=?, date=?, checkintime=?, checkouttime=?, status=? WHERE attendanceid=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

            pst.setString(1, empIdField.getText());
            pst.setString(2, sdfDate.format(dateSpinner.getValue()));
            pst.setString(3, sdfTime.format(checkInSpinner.getValue()));
            pst.setString(4, sdfTime.format(checkOutSpinner.getValue()));
            pst.setString(5, (String) statusBox.getSelectedItem());
            pst.setString(6, attendanceIdField.getText());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Attendance updated successfully!");
                clearForm();
                loadAttendanceData();
            } else {
                JOptionPane.showMessageDialog(this, "Attendance ID not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating attendance: " + ex.getMessage());
        }
    }

    private void deleteAttendance() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this attendance record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM attendance WHERE attendanceid = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, attendanceIdField.getText());

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Attendance deleted successfully!");
                    clearForm();
                    loadAttendanceData();
                } else {
                    JOptionPane.showMessageDialog(this, "Attendance ID not found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting attendance: " + ex.getMessage());
            }
        }
    }

    private void clearForm() {
        attendanceIdField.setText("");
        empIdField.setText("");
        dateSpinner.setValue(new java.util.Date());
        checkInSpinner.setValue(new java.util.Date());
        checkOutSpinner.setValue(new java.util.Date());
        statusBox.setSelectedIndex(0);
    }

    private void loadAttendanceData() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM attendance";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("attendanceid"));
                row.add(rs.getString("employeeid"));
                row.add(rs.getString("date"));
                row.add(rs.getString("checkintime"));
                row.add(rs.getString("checkouttime"));
                row.add(rs.getString("status"));
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading attendance data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Attendence().setVisible(true));
    }
}