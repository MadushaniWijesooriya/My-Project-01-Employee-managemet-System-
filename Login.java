package com.mycompany.employeesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Login extends JFrame {

    JTextField txtUsername;
    JPasswordField txtPassword;

    public Login() {
        setTitle("Login");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        Color bgColor = new Color(230, 240, 255);
        Color lblColor = new Color(0, 0, 102);
        Color btnColor = new Color(100, 149, 237);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel();
        formPanel.setBackground(bgColor);
        
        JLabel lblUser = new JLabel("User Name:");
        lblUser.setForeground(lblColor);
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(lblColor);

        txtUsername = new JTextField();
        txtUsername.setBackground(new Color(236,240,241));
        txtPassword = new JPasswordField();
        txtPassword.setBackground(new Color(236,240,241));

        JButton btnLogin = new JButton("Login");
        btnLogin.setBackground(new Color(41,128,185));
        btnLogin.setForeground(Color.WHITE);

        panel.add(lblUser);
        panel.add(txtUsername);
        panel.add(lblPass);
        panel.add(txtPassword);
        panel.add(new JLabel());  // empty label for spacing
        panel.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());

            // For demo, simple check hardcoded username/password
            if (user.equals("admin") && pass.equals("admin123")) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new Home().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}