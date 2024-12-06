package com.mycompany.billingsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JFrame {

    private JLabel jLabel1, jLabel2, jLabel3;
    private JTextField jTextField1;
    private JPasswordField jTextField2;
    private JButton jButton1;

    public LoginPage() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new JLabel("ADMIN LOGIN");
        jLabel2 = new JLabel("Username:");
        jLabel3 = new JLabel("Password:");
        jTextField1 = new JTextField();
        jTextField2 = new JPasswordField();
        jButton1 = new JButton("Login");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Login");
        setSize(400, 300);
        setLocationRelativeTo(null);

        jLabel1.setFont(new Font("Candara Light", Font.PLAIN, 36));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setForeground(new Color(51, 0, 51));

        jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        jTextField1.setPreferredSize(new Dimension(200, 30));
        jTextField2.setPreferredSize(new Dimension(200, 30));

        jButton1.setBackground(new Color(0, 102, 102));
        jButton1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        jButton1.setForeground(Color.WHITE);
        jButton1.setPreferredSize(new Dimension(150, 40));

        jButton1.addActionListener(evt -> {
            try {
                jButton1ActionPerformed(evt);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, GroupLayout.Alignment.CENTER))
                .addGap(50, 50, 50))
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .addGap(40, 40, 40)
            .addComponent(jLabel1)
            .addGap(20, 20, 20)
            .addComponent(jLabel2)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(20, 20, 20)
            .addComponent(jLabel3)
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addGap(30, 30, 30)
            .addComponent(jButton1)
            .addGap(50, 50, 50)
        );

        pack();
        setVisible(true);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws SQLException {
        String username = jTextField1.getText();
        String password = new String(jTextField2.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate the username and password with the database
        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful");
            this.dispose();  // Close the login page
            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean authenticateUser(String username, String password) throws SQLException {
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection connection = dbConnection.connect();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Database connection error", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            return rs.next(); // Return true if user found
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}
