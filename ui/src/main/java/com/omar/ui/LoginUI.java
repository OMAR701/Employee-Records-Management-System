package com.omar.ui;

import com.omar.api.ApiClient;
import com.omar.utils.AuthTokenHolder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        initComponents();
        this.toFront();
        this.requestFocus();
        this.repaint();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(0, 120, 160));

        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(0, 120, 160));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;

        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(0, 120, 160));
        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/logo.png"))
                .getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH));
        logoLabel.setIcon(logoIcon);
        logoPanel.add(logoLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(0, 120, 160));
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 10, 10, 10);
        formGbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formGbc.gridx = 0;
        formGbc.gridy = 0;
        formPanel.add(usernameLabel, formGbc);

        usernameField = new JTextField(20);
        formGbc.gridx = 1;
        formPanel.add(usernameField, formGbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formGbc.gridx = 0;
        formGbc.gridy = 1;
        formPanel.add(passwordLabel, formGbc);

        passwordField = new JPasswordField(20);
        formGbc.gridx = 1;
        formPanel.add(passwordField, formGbc);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 200, 100));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        formGbc.gridx = 1;
        formGbc.gridy = 2;
        formGbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, formGbc);

        // Add Action Listener for Login Button
        loginButton.addActionListener(this::handleLogin);

        // Add Logo and Form Side by Side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        centerPanel.add(logoPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        centerPanel.add(formPanel, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            String token = ApiClient.login(username, password);
            AuthTokenHolder.setToken(token);

            new DashboardUI().setVisible(true);
            dispose();
        } catch (Exception ex) {
            showNotification("Username or password incorrect", false);
        }
    }

    private void showNotification(String message, boolean success) {
        JDialog notification = new JDialog(this, "Notification", true);
        notification.setUndecorated(true);
        notification.setSize(350, 200);
        notification.setLocationRelativeTo(this);
        notification.setLayout(new BorderLayout());
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.WHITE);
        outerPanel.setBorder(BorderFactory.createLineBorder(success ? new Color(0, 200, 100) : new Color(200, 50, 50), 4));
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        if (success) {
            iconLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/icons/success.png"))
                    .getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        } else {
            iconLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/icons/error.png"))
                    .getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
        }
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(success ? new Color(0, 150, 80) : new Color(200, 50, 50));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton closeButton = new JButton("OK");
        closeButton.setBackground(success ? new Color(0, 200, 100) : new Color(200, 50, 50));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeButton.addActionListener(event -> notification.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);

        outerPanel.add(iconLabel, BorderLayout.NORTH);
        outerPanel.add(messageLabel, BorderLayout.CENTER);
        outerPanel.add(buttonPanel, BorderLayout.SOUTH);

        notification.add(outerPanel);

        notification.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->{
            LoginUI login = new LoginUI();
            login.setVisible(true);
            login.toFront();
            login.requestFocus();
            login.repaint();
            login.setExtendedState(JFrame.NORMAL);
        });
    }
}
