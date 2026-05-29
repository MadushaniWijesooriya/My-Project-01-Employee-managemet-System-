package com.mycompany.employeesystem;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private Image backgroundImage;

    public MainMenu() {
        setTitle("Main Menu");
        setSize(400, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load background image
        backgroundImage = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\I3.jpeg").getImage();

        // Create custom panel with background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Transparent panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false); // make it transparent

        // Buttons
        JButton btnUser = new JButton("User Creation");
        JButton btnEmp = new JButton("Employee Details");
        JButton btnDept = new JButton("Department");
        JButton btnProject = new JButton("Project");
        JButton btnAttendance = new JButton("Attendance");
        JButton btnLeave = new JButton("Leave");
        JButton btnSalary = new JButton("Salary");

        // Style all buttons
        styleButton(btnUser, new Color(52, 152, 219));
        styleButton(btnEmp, new Color(46, 204, 113));
        styleButton(btnDept, new Color(241, 196, 15));
        styleButton(btnProject, new Color(155, 89, 182));
        styleButton(btnAttendance, new Color(230, 126, 34));
        styleButton(btnLeave, new Color(231, 76, 60));
        styleButton(btnSalary, new Color(26, 188, 156));

        JButton[] buttons = { btnUser, btnEmp, btnDept, btnProject, btnAttendance, btnLeave, btnSalary };
        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(250, 40));
            buttonPanel.add(btn);
            buttonPanel.add(Box.createVerticalStrut(10));
        }

        // Center panel
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);

        // Back button
        JButton btnBack = new JButton("Back to Home");
        styleButton(btnBack, new Color(255, 153, 51));
        btnBack.setPreferredSize(new Dimension(130, 30));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnBack);

        // Actions
        btnUser.addActionListener(e -> new UserCreation().setVisible(true));
        btnEmp.addActionListener(e -> new Employee().setVisible(true));
        btnDept.addActionListener(e -> new Department().setVisible(true));
        btnProject.addActionListener(e -> new Project().setVisible(true));
        btnAttendance.addActionListener(e -> new Attendence().setVisible(true));
        btnLeave.addActionListener(e -> new Leave().setVisible(true));
        btnSalary.addActionListener(e -> new Salary().setVisible(true));

        btnBack.addActionListener(e -> {
            dispose();
            new Home().setVisible(true);
        });

        // Add to frame
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    // Button styling method
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setOpaque(true); // needed when using image backgrounds
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}