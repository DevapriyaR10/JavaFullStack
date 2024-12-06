package com.mycompany.billingsystem;

import javax.swing.*;
import java.awt.*;

public class HomePage extends javax.swing.JFrame {

    public HomePage() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        // Create and initialize components
        JLabel jLabel3 = new javax.swing.JLabel();
        JButton jButton1 = new javax.swing.JButton();
        JButton jButton2 = new javax.swing.JButton();
        JButton jButton3 = new javax.swing.JButton();
        JButton jButton4 = new javax.swing.JButton();

        // Set default close operation
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Retail Management - Admin Home");
        setSize(400, 400); // Set the size of the HomePage

        // Set background color
        getContentPane().setBackground(new java.awt.Color(34, 45, 50));

        // Header label
        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 36)); // Set font size
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Admin Menu");
        jLabel3.setForeground(Color.WHITE);

        // Button to generate invoice
        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jButton1.setForeground(Color.WHITE);
        jButton1.setText("Generate Customer Invoice");
        jButton1.setPreferredSize(new Dimension(250, 50));
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        // Button to update product
        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jButton2.setForeground(Color.WHITE);
        jButton2.setText("Update Product");
        jButton2.setPreferredSize(new Dimension(250, 50));
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        // Button to view product details
        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jButton3.setForeground(Color.WHITE);
        jButton3.setText("View Product Details");
        jButton3.setPreferredSize(new Dimension(250, 50));
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt));

        // Button to track product selling
        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jButton4.setForeground(Color.WHITE);
        jButton4.setText("Track Product Selling");
        jButton4.setPreferredSize(new Dimension(250, 50));
        jButton4.addActionListener(evt -> jButton4ActionPerformed(evt));

        // Layout setup using GroupLayout for better positioning
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(87, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap(109, Short.MAX_VALUE)
        );

        // Packing and setting visibility of the form
        pack();
    }

    // Action when clicking "Generate Customer Invoice"
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        GenerateInvoiceForm invoiceForm = new GenerateInvoiceForm();
        invoiceForm.setVisible(true);
        this.setVisible(false);  // Hide current HomePage
    }                                         

    // Action when clicking "Update Product"
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        UpdateProductForm updateForm = new UpdateProductForm();
        updateForm.setVisible(true);
        this.setVisible(false);  // Hide current HomePage
    }                                         

    // Action when clicking "View Product Details"
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        ViewProductDetailsForm viewForm = new ViewProductDetailsForm();
        viewForm.setVisible(true);
        this.setVisible(false);  // Hide current HomePage
    }                                         

    // Action when clicking "Track Product Selling"
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        SalesTrackingForm salesForm = new SalesTrackingForm();
        salesForm.setVisible(true);
        this.setVisible(false);  // Hide current HomePage
    }                                         

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }
}
