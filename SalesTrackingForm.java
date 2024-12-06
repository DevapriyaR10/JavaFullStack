package com.mycompany.billingsystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class SalesTrackingForm extends javax.swing.JFrame {

    // Constructor
    public SalesTrackingForm() {
        initComponents();
        
        // Set action for "Track Sales" button
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                trackSalesActionPerformed(evt);
            }
        });

        // Set action for "Home" button
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });
    }

    // Action when the "Track Sales" button is clicked
    private void trackSalesActionPerformed(ActionEvent evt) {
        // Get product ID from the text field
        String productId = jTextField1.getText();
        
        if (productId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Product ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Fetch product details from the database
        fetchProductDetails(productId);
    }

    // Method to fetch product details from the database
    private void fetchProductDetails(String productId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.connect();
            
            // SQL query to get product details using product_id
            String query = "SELECT product_id, product_name, quantity, price FROM products WHERE product_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, productId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Retrieve product details from the result set
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double totalSale = quantity * price;
                
                // Prepare a string with all the sale details
                String salesDetails = "Sales Information:\n\n" +
                                      "Product ID: " + productId + "\n" +
                                      "Product Name: " + productName + "\n" +
                                      "Quantity Sold: " + quantity + "\n" +
                                      "Cost Per Product: " + price + "\n" +
                                      "Total Sale: " + totalSale;
                
                // Show all the details in a single pop-up message
                JOptionPane.showMessageDialog(this, salesDetails, "Sales Information", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Product sales not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching product details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Action when the "Home" button is clicked
    private void homeButtonActionPerformed(ActionEvent evt) {
        this.dispose(); // Close the current window
        new HomePage().setVisible(true); // Assuming HomePage is the home screen class
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        homeButton = new javax.swing.JButton(); // Home button
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Track Sales");

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Track Sales");

        homeButton.setBackground(new java.awt.Color(0, 102, 102)); // Set the background color for Home button
        homeButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // Set font for Home button
        homeButton.setForeground(new java.awt.Color(255, 255, 255)); // Set text color for Home button
        homeButton.setText("Home");

        jLabel1.setText("Product ID");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(homeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))));
       
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(homeButton) // Place Home button below "Track Sales"
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesTrackingForm().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton jButton3;
    private javax.swing.JButton homeButton; // Home button declaration
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration
}
