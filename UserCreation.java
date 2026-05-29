package com.mycompany.employeesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class UserCreation extends JFrame {

    private JTextField userIdField;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserCreation() {
        setTitle("User Creation");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Load and Resize Background Image
        ImageIcon rawIcon = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\us.jpeg");
        Image img = rawIcon.getImage().getScaledInstance(800, 550, Image.SCALE_SMOOTH);
        ImageIcon backgroundIcon = new ImageIcon(img);
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout());

        // Transparent Main Panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        JLabel userIdLabel = new JLabel("User ID:");
        JLabel userNameLabel = new JLabel("User Name:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");

        JLabel[] labels = {userIdLabel, userNameLabel, passwordLabel, roleLabel};
        Color labelColor = new Color(255, 255, 255);
        Font labelFont = new Font("Arial", Font.BOLD, 13);
        for (JLabel label : labels) {
            label.setForeground(labelColor);
            label.setFont(labelFont);
        }

        userIdField = new JTextField(15);
        userNameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        roleBox = new JComboBox<>(new String[]{"Admin", "Manager", "User"});

        formPanel.add(userIdLabel);
        formPanel.add(userIdField);
        formPanel.add(userNameLabel);
        formPanel.add(userNameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(roleLabel);
        formPanel.add(roleBox);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JButton backBtn = new JButton("Back");
        JButton addBtn = new JButton("Add User");
        JButton deleteBtn = new JButton("Delete User");
        JButton clearBtn = new JButton("Clear");
        JButton nextBtn = new JButton("Next");

        backBtn.setBackground(new Color(255, 165, 0));
        backBtn.setForeground(Color.WHITE);
        addBtn.setBackground(new Color(65, 105, 225));
        addBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 20, 60));
        deleteBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(new Color(128, 128, 128));
        clearBtn.setForeground(Color.WHITE);
        nextBtn.setBackground(new Color(46, 139, 87));
        nextBtn.setForeground(Color.WHITE);

        buttonPanel.add(backBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(nextBtn);

        // Table Panel
        tableModel = new DefaultTableModel();
        userTable = new JTable(tableModel);
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Username", "Password", "Role"});

        
        userTable.getColumnModel().getColumn(2).setCellRenderer(new PasswordCellRenderer());

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setPreferredSize(new Dimension(700, 200));
        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        tablePanel.add(scrollPane);

        // Action Listeners
        addBtn.addActionListener(e -> addUser());
        deleteBtn.addActionListener(e -> deleteUser());
        clearBtn.addActionListener(e -> clearFields());

        backBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to go back to the Main Menu?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new MainMenu().setVisible(true);
            }
        });

        nextBtn.addActionListener(e -> {
            dispose();
            new Employee().setVisible(true);
        });

        // Add to contentPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        backgroundLabel.add(contentPanel);

        setContentPane(backgroundLabel);

        loadUserData();
    }

    
    private static class PasswordCellRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            if (value != null) {
                String password = value.toString();

                String masked = "*".repeat(password.length());
                super.setValue(masked);
            } else {
                super.setValue(value);
            }
        }
    }

    private void clearFields() {
        userIdField.setText("");
        userNameField.setText("");
        passwordField.setText("");
        roleBox.setSelectedIndex(0);
    }

    private void addUser() {
        String userid = userIdField.getText().trim();
        String username = userNameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        if (userid.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            String query = "INSERT INTO user (userid, username, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, userid);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setString(4, role);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "User added successfully.");
                clearFields();
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add user.");
            }

            con.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "User ID already exists!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteUser() {
        String userid = userIdField.getText().trim();

        if (userid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter User ID to delete.");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            String query = "DELETE FROM user WHERE userid=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, userid);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                clearFields();
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(this, "User ID not found.");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void loadUserData() {
        try {
            tableModel.setRowCount(0);
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT userid, username, password, role FROM user");

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("userid"));
                row.add(rs.getString("username"));
                row.add(rs.getString("password")); 
                row.add(rs.getString("role"));
                tableModel.addRow(row);
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserCreation().setVisible(true));
    }
}