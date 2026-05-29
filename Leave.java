package com.mycompany.employeesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Leave extends JFrame {

    JTextField txt1, txt2, txt3;
    JComboBox<String> leaveTypeBox, statusBox;
    JSpinner fromdateSpinner, todateSpinner;
    JTable table;
    DefaultTableModel tableModel;
    Connection conn = DBConnection.getConnection();

    public Leave() {
        setTitle("Leave Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Load Background Image
        ImageIcon bgIcon = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\pp.png");
        JLabel background = new JLabel(bgIcon);
        background.setLayout(new BorderLayout());
        setContentPane(background);

        Color lblColor = new Color(0, 0, 102);
        Color btnColor = new Color(100, 149, 237);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lbl1 = new JLabel("Leave ID:");
        lbl1.setForeground(lblColor);
        txt1 = new JTextField(15);

        JLabel lbl2 = new JLabel("Employee ID:");
        lbl2.setForeground(lblColor);
        txt2 = new JTextField(15);

        JLabel lbl3 = new JLabel("Leave Type:");
        lbl3.setForeground(lblColor);
        leaveTypeBox = new JComboBox<>(new String[]{
            "Casual Leave", "Medical Leave", "Annual Leave", "Maternity Leave"
        });

        JLabel lbl4 = new JLabel("From Date:");
        lbl4.setForeground(lblColor);
        fromdateSpinner = new JSpinner(new SpinnerDateModel());
        fromdateSpinner.setEditor(new JSpinner.DateEditor(fromdateSpinner, "yyyy-MM-dd"));

        JLabel lbl5 = new JLabel("To Date:");
        lbl5.setForeground(lblColor);
        todateSpinner = new JSpinner(new SpinnerDateModel());
        todateSpinner.setEditor(new JSpinner.DateEditor(todateSpinner, "yyyy-MM-dd"));

        JLabel lbl6 = new JLabel("Reason:");
        lbl6.setForeground(lblColor);
        txt3 = new JTextField(15);

        JLabel lbl7 = new JLabel("Status:");
        lbl7.setForeground(lblColor);
        statusBox = new JComboBox<>(new String[]{
            "Pending", "Approved", "Rejected"
        });

        // Add components to formPanel
        formPanel.add(lbl1); formPanel.add(txt1);
        formPanel.add(lbl2); formPanel.add(txt2);
        formPanel.add(lbl3); formPanel.add(leaveTypeBox);
        formPanel.add(lbl4); formPanel.add(fromdateSpinner);
        formPanel.add(lbl5); formPanel.add(todateSpinner);
        formPanel.add(lbl6); formPanel.add(txt3);
        formPanel.add(lbl7); formPanel.add(statusBox);

        // Table
        tableModel = new DefaultTableModel(new String[]{
            "Leave ID", "Employee ID", "Type", "From", "To", "Reason", "Status"
        }, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 200));

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton backBtn = new JButton("Back");
        JButton submitBtn = new JButton("Submit");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        JButton nextBtn = new JButton("Next");

        // Button styles
        JButton[] buttons = {backBtn, submitBtn, deleteBtn, clearBtn, nextBtn};
        Color[] colors = {
            new Color(255, 165, 0),
            btnColor,
            Color.RED,
            Color.GRAY,
            new Color(34, 139, 34)
        };

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(colors[i]);
            buttons[i].setForeground(Color.WHITE);
            buttonPanel.add(buttons[i]);
        }

        // Button actions
        submitBtn.addActionListener(e -> submitLeave());
        deleteBtn.addActionListener(e -> deleteLeave());
        clearBtn.addActionListener(e -> clearForm());

        backBtn.addActionListener(e -> {
            dispose(); new Attendence().setVisible(true);
        });

        nextBtn.addActionListener(e -> {
            dispose(); new Salary().setVisible(true);
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        background.add(topPanel, BorderLayout.NORTH);
        background.add(scrollPane, BorderLayout.CENTER);

        loadLeaveData();
    }

    private void submitLeave() {
        try {
            String sql = "INSERT INTO leaverequests (leaveid, employeeid, leavetype, fromdate, todate, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            pst.setString(1, txt1.getText());
            pst.setString(2, txt2.getText());
            pst.setString(3, (String) leaveTypeBox.getSelectedItem());
            pst.setString(4, sdf.format(fromdateSpinner.getValue()));
            pst.setString(5, sdf.format(todateSpinner.getValue()));
            pst.setString(6, txt3.getText());
            pst.setString(7, (String) statusBox.getSelectedItem());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Leave request submitted!");
            clearForm();
            loadLeaveData();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting leave: " + ex.getMessage());
        }
    }

    private void deleteLeave() {
        try {
            String leaveId = txt1.getText().trim();
            if (leaveId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Leave ID to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            String sql = "DELETE FROM leaverequests WHERE leaveid = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, leaveId);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Leave record deleted.");
                clearForm();
                loadLeaveData();
            } else {
                JOptionPane.showMessageDialog(this, "Leave ID not found.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting leave: " + ex.getMessage());
        }
    }

    private void clearForm() {
        txt1.setText("");
        txt2.setText("");
        leaveTypeBox.setSelectedIndex(0);
        fromdateSpinner.setValue(new java.util.Date());
        todateSpinner.setValue(new java.util.Date());
        txt3.setText("");
        statusBox.setSelectedIndex(0);
    }

    private void loadLeaveData() {
        try {
            tableModel.setRowCount(0);
            String sql = "SELECT * FROM leaverequests";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("leaveid"),
                    rs.getString("employeeid"),
                    rs.getString("leavetype"),
                    rs.getString("fromdate"),
                    rs.getString("todate"),
                    rs.getString("reason"),
                    rs.getString("status")
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Leave().setVisible(true));
    }
}