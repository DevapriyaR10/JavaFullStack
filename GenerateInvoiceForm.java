package com.mycompany.billingsystem;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class GenerateInvoiceForm extends javax.swing.JFrame {

    // Constructor
    public GenerateInvoiceForm() {
        initComponents();
        
       homeButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Close the current window
        dispose();
        
        // Open the HomePage form (assuming HomePage is the correct class for the home page)
        new HomePage().setVisible(true);
    }
});
        
        // Set action for "Generate Invoice" button
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                generateInvoiceActionPerformed(evt);
            }
        });

        // Set action for "Add Product" button
        jButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addCustomerActionPerformed(evt);
            }
        });
    }
    // Action when the "Generate Invoice" button is clicked
    private void generateInvoiceActionPerformed(ActionEvent evt) {
        // Get customer ID from the text field
        String customerId = jTextField1.getText();
        
        if (customerId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Customer ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Fetch the customer details and product details
        fetchCustomerDetails(customerId);
    }

    // Method to fetch customer details and product data from the database
    private void fetchCustomerDetails(String customerId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Connect to the database
            connection = DatabaseConnection.connect();
            
            // SQL query to get customer details
            String customerQuery = "SELECT * FROM customers WHERE customer_id = ?";
            stmt = connection.prepareStatement(customerQuery);
            stmt.setString(1, customerId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Fill customer details into the form fields
                jTextField1.setText(rs.getString("customer_id"));
                jTextField2.setText(rs.getString("customer_name"));
                jTextField3.setText(rs.getString("customer_address"));
                
                // Fetch product details and show invoice in a popup
                fetchProductDetails(customerId);
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    // Method to fetch product details based on customer ID and display invoice
    private void fetchProductDetails(String customerId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuilder invoiceDetails = new StringBuilder();
        double totalAmount = 0;
        
        try {
            connection = DatabaseConnection.connect();
            
            // Query to get product details for the given customer ID (e.g., order details)
            String productQuery = "SELECT * FROM products WHERE customer_id = ?";
            stmt = connection.prepareStatement(productQuery);
            stmt.setString(1, customerId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double total = quantity * price;
                totalAmount += total;
                
                // Add product details to the invoice
                invoiceDetails.append("Product: ").append(productName)
                    .append("\nQuantity: ").append(quantity)
                    .append("\nPrice: ").append(price)
                    .append("\nTotal: ").append(total)
                    .append("\n\n");
            }

            // Show invoice popup with customer and product details
            String invoice = "Customer Name: " + jTextField2.getText() + "\n"
                    + "Customer Address: " + jTextField3.getText() + "\n\n"
                    + "Invoice Details:\n" + invoiceDetails.toString()
                    + "Total Amount: " + totalAmount;

            JOptionPane.showMessageDialog(this, invoice, "Invoice", JOptionPane.INFORMATION_MESSAGE);

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

    // Action when the "Add Product" button is clicked
  private void addCustomerActionPerformed(ActionEvent evt) {
    String customerId = jTextField1.getText();
    String customerName = jTextField2.getText();
    String customerAddress = jTextField3.getText();
    
    // Ask for Product ID
    String productId = JOptionPane.showInputDialog(this, "Enter Product ID");
    String productName = JOptionPane.showInputDialog(this, "Enter Product Name");
    int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Quantity"));
    double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Price"));

    if (productId.isEmpty() || productName.isEmpty() || customerId.isEmpty() || customerName.isEmpty() || customerAddress.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    addCustomerToDatabase(customerId, customerName, customerAddress, productId, productName, quantity, price);
}

private void addCustomerToDatabase(String customerId, String customerName, String customerAddress, 
                                   String productId, String productName, int quantity, double price) {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        connection = DatabaseConnection.connect();

        // Check if customer exists
        String checkCustomerQuery = "SELECT * FROM customers WHERE customer_id = ?";
        stmt = connection.prepareStatement(checkCustomerQuery);
        stmt.setString(1, customerId);
        rs = stmt.executeQuery();

        if (!rs.next()) {
            // Customer doesn't exist, insert into customers table
            String insertCustomerQuery = "INSERT INTO customers (customer_id, customer_name, customer_address) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(insertCustomerQuery);
            stmt.setString(1, customerId);
            stmt.setString(2, customerName);
            stmt.setString(3, customerAddress);
            int customerInsertResult = stmt.executeUpdate();
            
            if (customerInsertResult > 0) {
                JOptionPane.showMessageDialog(this, "Customer added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // After checking (or inserting) customer, proceed to add the product
        String checkProductQuery = "SELECT * FROM products WHERE customer_id = ? AND product_id = ?";
        stmt = connection.prepareStatement(checkProductQuery);
        stmt.setString(1, customerId);
        stmt.setString(2, productId);  // We now check by Product ID
        rs = stmt.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Product already exists for this customer", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Insert product into products table
            String insertProductQuery = "INSERT INTO products (customer_id, product_id, product_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertProductQuery);
            stmt.setString(1, customerId);
            stmt.setString(2, productId);
            stmt.setString(3, productName);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, price);

            int productInsertResult = stmt.executeUpdate();
            if (productInsertResult > 0) {
                JOptionPane.showMessageDialog(this, "Product added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


private void addCustomerToDatabase(String customerId, String customerName, String customerAddress, 
                                   String productName, int quantity, double price) {
    Connection connection = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        connection = DatabaseConnection.connect();

        // Check if customer exists
        String checkCustomerQuery = "SELECT * FROM customers WHERE customer_id = ?";
        stmt = connection.prepareStatement(checkCustomerQuery);
        stmt.setString(1, customerId);
        rs = stmt.executeQuery();

        if (!rs.next()) {
            // Customer doesn't exist, insert into customers table
            String insertCustomerQuery = "INSERT INTO customers (customer_id, customer_name, customer_address) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(insertCustomerQuery);
            stmt.setString(1, customerId);
            stmt.setString(2, customerName);
            stmt.setString(3, customerAddress);
            int customerInsertResult = stmt.executeUpdate();
            
            if (customerInsertResult > 0) {
                JOptionPane.showMessageDialog(this, "Customer added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add customer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // After checking (or inserting) customer, proceed to add the product
        String checkProductQuery = "SELECT * FROM products WHERE customer_id = ? AND product_name = ?";
        stmt = connection.prepareStatement(checkProductQuery);
        stmt.setString(1, customerId);
        stmt.setString(2, productName);
        rs = stmt.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Product already exists for this customer", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Insert product into products table
            String insertProductQuery = "INSERT INTO products (customer_id, product_name, quantity, price) VALUES (?, ?, ?, ?)";
            stmt = connection.prepareStatement(insertProductQuery);
            stmt.setString(1, customerId);
            stmt.setString(2, productName);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);

        }
    } catch (HeadlessException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
        }
    }
}


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                           
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        new javax.swing.JTextField();
        homeButton = new javax.swing.JButton();



        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 102));
        
        homeButton.setBackground(new java.awt.Color(0, 102, 102));
        homeButton.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        homeButton.setForeground(new java.awt.Color(255, 255, 255));
        homeButton.setText("Home"); 

        jLabel3.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Generate Customer Invoice");

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Generate Invoice");

        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Add Customer");

        jLabel1.setText("Customer ID");

        jLabel2.setText("Customer Name");

        jLabel4.setText("Customer Address");

        jLabel5.setText("Product Name");

        jLabel6.setText("Quantity");

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
                    .addComponent(jLabel2)
                    .addComponent(jTextField2)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(homeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)) // Added backToHomeButton here
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
);
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
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabel4)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jButton4)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jButton3)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) // Space between buttons
        .addComponent(homeButton) // Placed below Generate Invoice button
        .addContainerGap(40, Short.MAX_VALUE))
);

        pack();
    }// </editor-fold>



    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new GenerateInvoiceForm().setVisible(true);
        });
    }

    // Variables declaration
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JButton homeButton;

    // End of variables declaration
}