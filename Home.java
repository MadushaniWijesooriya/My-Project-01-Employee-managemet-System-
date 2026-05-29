package com.mycompany.employeesystem;

import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {

    private Image backgroundImage;

    public Home() {
        setTitle("Home");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load background image
        backgroundImage = new ImageIcon("C:\\Users\\User\\Desktop\\Group project\\Image\\I1.jpg").getImage();

        // Create main panel with background
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Welcome Label
        JLabel lblWelcome = new JLabel("Welcome to Employee Management System", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);  // White text for visibility
        lblWelcome.setOpaque(false);            // Transparent background

        panel.add(lblWelcome, BorderLayout.CENTER);

        // Button Panel (Transparent)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Buttons
        JButton btnMainMenu = new JButton("Go to Main Menu");
        JButton btnLogout = new JButton("Logout");

        styleButton(btnMainMenu, new Color(0, 123, 255)); // Blue
        styleButton(btnLogout, new Color(220, 53, 69));    // Red

        // Add actions
        btnMainMenu.addActionListener(e -> {
            dispose();
            new MainMenu().setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Login().setVisible(true);
            }
        });

        buttonPanel.add(btnMainMenu);
        buttonPanel.add(btnLogout);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(panel);
    }

    // Button Styling Method
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setOpaque(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home().setVisible(true));
    }
}