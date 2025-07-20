package com.appsystem.milkteamanage_system.Staff;

import com.appsystem.milkteamanage_system.Utils.DBConnection;
import com.appsystem.milkteamanage_system.Utils.Utils;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscountPopup extends JDialog {
    private JTextField txtDiscountCode;
    private JButton btnApply;
    private JButton btnNoDiscount;
    private int orderId;
    private double totalAmount;
    private double finalTotalAmount;
    private Integer appliedDiscountId;
    private boolean paymentProcessed = false;
    private double discountAmount;
    private String discountCodeName;

    public DiscountPopup(JFrame parent, int orderId, double totalAmount) {
        super(parent, "Áp Dụng Mã Khuyến Mãi", true);
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.finalTotalAmount = totalAmount; 
        this.discountAmount = 0.0; 
        this.discountCodeName = null; 
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Create components
        JLabel lblPrompt = new JLabel("Nhập mã khuyến mãi (nếu có):", SwingConstants.CENTER);
        lblPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        txtDiscountCode = new JTextField();
        txtDiscountCode.setPreferredSize(new Dimension(200, 25));

        btnApply = new JButton("Áp Dụng");
        btnApply.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnApply.addActionListener(e -> applyDiscount());

        btnNoDiscount = new JButton("Không Nhập");
        btnNoDiscount.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNoDiscount.addActionListener(e -> processPaymentWithoutDiscount());

        // Layout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(txtDiscountCode);
        inputPanel.add(btnApply);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(btnNoDiscount);

        add(lblPrompt, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void applyDiscount() {
        String discountCode = txtDiscountCode.getText().trim();
        if (discountCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khuyến mãi!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DiscountID, Name, DiscountPercent FROM Discounts WHERE DiscountID = ? AND StartDate <= GETDATE() AND EndDate >= GETDATE()";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, discountCode);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                appliedDiscountId = rs.getInt("DiscountID");
                discountCodeName = rs.getString("Name");
                double discountPercent = rs.getDouble("DiscountPercent");
                discountAmount = totalAmount * (discountPercent / 100);
                finalTotalAmount = totalAmount - discountAmount;
                processPayment();
            } else {
                JOptionPane.showMessageDialog(this, "Mã khuyến mãi không hợp lệ hoặc đã hết hạn!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra mã khuyến mãi: " + e.getMessage());
        }
    }

    private void processPaymentWithoutDiscount() {
        appliedDiscountId = null;
        discountCodeName = null;
        discountAmount = 0.0;
        finalTotalAmount = totalAmount;
        processPayment();
    }

    private void processPayment() {
        if (JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán hóa đơn #" + orderId + " với số tiền: " + Utils.formatCurrency(finalTotalAmount) + "?", 
                "Xác Nhận Thanh Toán", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Check if order is empty
                try (PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM OrderDetails WHERE OrderID = ?")) {
                    checkStmt.setInt(1, orderId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        JOptionPane.showMessageDialog(this, "Đơn hàng trống!");
                        return;
                    }
                }

                // Update Orders table
                String updateSql = "UPDATE Orders SET Status = N'Đã thanh toán', IsActive = 0, TotalAmount = ?, DiscountID = ? WHERE OrderID = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setDouble(1, totalAmount);
                    if (appliedDiscountId != null) {
                        updateStmt.setInt(2, appliedDiscountId);
                    } else {
                        updateStmt.setNull(2, java.sql.Types.INTEGER);
                    }
                    updateStmt.setInt(3, orderId);
                    updateStmt.executeUpdate();
                }

                conn.commit();
                paymentProcessed = true;
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
                showBillPanel();
                dispose();
            } catch (SQLException ex) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Lỗi thanh toán: " + ex.getMessage());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void showBillPanel() {
        BillPanel billPanel = new BillPanel(orderId, discountAmount, discountCodeName);
        JDialog billDialog = new JDialog((JFrame) getOwner(), "Hóa Đơn #" + orderId, true);
        billDialog.getContentPane().add(billPanel);
        billDialog.pack();
        billDialog.setLocationRelativeTo(getOwner());
        billDialog.setVisible(true);
    }

    public boolean isPaymentProcessed() {
        return paymentProcessed;
    }

    public double getFinalTotalAmount() {
        return finalTotalAmount;
    }

    public Integer getAppliedDiscountId() {
        return appliedDiscountId;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountCodeName() {
        return discountCodeName;
    }

    public static void main(String[] args) {
        // For testing purposes
        JFrame parent = new JFrame();
        parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parent.setSize(400, 300);
        parent.setVisible(true);
        DiscountPopup popup = new DiscountPopup(parent, 1, 100000);
        popup.setVisible(true);
    }
}