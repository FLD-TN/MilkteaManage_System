package com.appsystem.milkteamanage_system.Staff;

import com.appsystem.milkteamanage_system.Utils.DBConnection;
import com.appsystem.milkteamanage_system.Utils.Utils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class OrderFrame extends javax.swing.JFrame {

    private StaffHomePage parent;
    private int orderId;
    private int tableNumber;
    private int staffId;
    private String orderStatus;
    private JButton tableButton;
    private String orderType;
    private double totalAmount;
    private double finalTotalAmount;
    private Integer appliedDiscountId; // Store applied DiscountID

    public OrderFrame() {
        initComponents();
        OrderDetailPanel.setLayout(new BoxLayout(OrderDetailPanel, BoxLayout.Y_AXIS));
        ProductListPanel.setLayout(new java.awt.GridLayout(0, 2, 20, 20));
        ProductListPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 20));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        loadProducts();
        loadOrderDetails();
    }

    public OrderFrame(int orderId, int tableNumber, int staffId, String staffName, String orderType, String orderStatus, double totalAmount, JButton tableButton, StaffHomePage parent) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.staffId = staffId;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.finalTotalAmount = totalAmount;
        this.tableButton = tableButton;
        this.parent = parent;

        initComponents();
        OrderDetailPanel.setLayout(new BoxLayout(OrderDetailPanel, BoxLayout.Y_AXIS));
        ProductListPanel.setLayout(new java.awt.GridLayout(0, 2, 20, 20));
        ProductListPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 20));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        loadProducts();
        orderIDLabel.setText(String.valueOf(orderId));
        staffNameLabel.setText(staffName);
        if (orderType.equals("Mang đi")) {
            tableNameLabel.setText("—");
        } else {
            tableNameLabel.setText(String.valueOf(tableNumber));
        }
        orderTypeLabel.setText(orderType);
        OrderStatusLabel.setText(orderStatus);
        TotalAmountLabel.setText(Utils.formatCurrency(totalAmount));
        FinalTotalAmountLabel.setText(Utils.formatCurrency(totalAmount));
        loadOrderDetails();
    }

    private void initComponents() {
        jRadioButton1 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        orderIDLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        staffNameLabel = new javax.swing.JLabel();
        tableNameLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        orderTypeLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        OrderStatusLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        TotalAmountLabel = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        FinalTotalAmountLabel = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnThanhToan = new javax.swing.JButton();
        btnPrintOrder = new javax.swing.JButton();
        btnUpdateOrder = new javax.swing.JButton();
        btnCancelOrder = new javax.swing.JButton();
        btnCloseOrder = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        ProductListPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        OrderDetailPanel = new javax.swing.JPanel();

        jRadioButton1.setText("jRadioButton1");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jLabel1.setText("----------------------Thông Tin Hoá Đơn --------------------");

        jLabel2.setText("Mã HD: ");
        orderIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        orderIDLabel.setForeground(new java.awt.Color(255, 51, 51));
        orderIDLabel.setText("XXX");

        jLabel4.setText("Nhân Viên: ");
        jLabel5.setText("Bàn: ");
        staffNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        staffNameLabel.setForeground(new java.awt.Color(255, 51, 51));
        staffNameLabel.setText("NAME");

        tableNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        tableNameLabel.setForeground(new java.awt.Color(255, 51, 51));
        tableNameLabel.setText("X");

        jLabel8.setText("Loại Hoá Đơn: ");
        orderTypeLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        orderTypeLabel.setForeground(new java.awt.Color(255, 51, 51));
        orderTypeLabel.setText("XXXX");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jLabel10.setText("----------------------------- Chi Tiết ----------------------------");

        jLabel11.setText("Trạng Thái: ");
        OrderStatusLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        OrderStatusLabel.setForeground(new java.awt.Color(51, 255, 51));
        OrderStatusLabel.setText("Đã thanh toán ");

        jLabel13.setText("Tổng: ");
        TotalAmountLabel.setFont(new java.awt.Font("Segoe UI", 3, 12));
        TotalAmountLabel.setForeground(new java.awt.Color(0, 255, 0));
        TotalAmountLabel.setText("XXX VND");

        jLabel15.setText("Thành Tiền: ");
        FinalTotalAmountLabel.setFont(new java.awt.Font("Segoe UI", 3, 12));
        FinalTotalAmountLabel.setForeground(new java.awt.Color(0, 204, 0));
        FinalTotalAmountLabel.setText("XXXX VND");

        jLabel18.setText("--------------------------- Thao Tác ----------------------------");

        btnThanhToan.setBackground(new java.awt.Color(0, 153, 204));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnThanhToan.setText("Thanh Toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnPrintOrder.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnPrintOrder.setText("In Hoá Đơn");
        btnPrintOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintOrderActionPerformed(evt);
            }
        });

        btnUpdateOrder.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnUpdateOrder.setText("Cập Nhật");
        btnUpdateOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateOrderActionPerformed(evt);
            }
        });

        btnCancelOrder.setBackground(new java.awt.Color(255, 0, 51));
        btnCancelOrder.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnCancelOrder.setForeground(new java.awt.Color(0, 0, 0));
        btnCancelOrder.setText("Huỷ Hoá Đơn");
        btnCancelOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelOrderActionPerformed(evt);
            }
        });

        btnCloseOrder.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnCloseOrder.setText("Đóng");
        btnCloseOrder.setActionCommand("Đóng");
        btnCloseOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel4))
                                                .addGap(24, 24, 24)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(staffNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                                                        .addComponent(tableNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                                        .addComponent(btnUpdateOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnCancelOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnPrintOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(28, 28, 28))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel11)
                                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel15))
                                                .addGap(72, 72, 72)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(OrderStatusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(TotalAmountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(FinalTotalAmountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(104, 104, 104)
                                                .addComponent(btnCloseOrder)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(orderTypeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(orderIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(13, 13, 13))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(orderIDLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(staffNameLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(tableNameLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(orderTypeLabel))
                                .addGap(45, 45, 45)
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(OrderStatusLabel))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(TotalAmountLabel))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(FinalTotalAmountLabel))
                                .addGap(23, 23, 23)
                                .addComponent(jLabel18)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnThanhToan)
                                        .addComponent(btnPrintOrder))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnUpdateOrder)
                                        .addComponent(btnCancelOrder))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                                .addComponent(btnCloseOrder)
                                .addGap(27, 27, 27))
        );

        ProductListPanel.setBackground(new java.awt.Color(204, 204, 204));
        javax.swing.GroupLayout ProductListPanelLayout = new javax.swing.GroupLayout(ProductListPanel);
        ProductListPanel.setLayout(ProductListPanelLayout);
        ProductListPanelLayout.setHorizontalGroup(
                ProductListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 469, Short.MAX_VALUE)
        );
        ProductListPanelLayout.setVerticalGroup(
                ProductListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 621, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ProductListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(ProductListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel2);

        OrderDetailPanel.setBackground(new java.awt.Color(204, 255, 204));
        javax.swing.GroupLayout OrderDetailPanelLayout = new javax.swing.GroupLayout(OrderDetailPanel);
        OrderDetailPanel.setLayout(OrderDetailPanelLayout);
        OrderDetailPanelLayout.setHorizontalGroup(
                OrderDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 365, Short.MAX_VALUE)
        );
        OrderDetailPanelLayout.setVerticalGroup(
                OrderDetailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 621, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(OrderDetailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(OrderDetailPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jScrollPane2)
        );

        pack();
    }

    private void btnCloseOrderActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void btnCancelOrderActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn huỷ đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    String sqlDetail = "DELETE FROM OrderDetails WHERE OrderID = ?";
                    try (PreparedStatement pst1 = conn.prepareStatement(sqlDetail)) {
                        pst1.setInt(1, orderId);
                        pst1.executeUpdate();
                    }

                    String sqlOrder = "DELETE FROM Orders WHERE OrderID = ?";
                    try (PreparedStatement pst2 = conn.prepareStatement(sqlOrder)) {
                        pst2.setInt(1, orderId);
                        pst2.executeUpdate();
                    }

                    conn.commit();

                    if (parent != null) {
                        parent.resetTableButtonColor(tableNumber);
                    }
                    this.dispose();
                } catch (Exception ex) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Lỗi khi huỷ đơn: " + ex.getMessage());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage());
            }
        }
    }

    private void btnPrintOrderActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {
        DiscountPopup popup = new DiscountPopup(this, orderId, totalAmount);
        popup.setVisible(true);
        if (popup.isPaymentProcessed()) {
            this.finalTotalAmount = popup.getFinalTotalAmount();
            this.appliedDiscountId = popup.getAppliedDiscountId();
            TotalAmountLabel.setText(Utils.formatCurrency(totalAmount));
            FinalTotalAmountLabel.setText(Utils.formatCurrency(finalTotalAmount));
            if (parent != null) {
                parent.updateTableColor(tableNumber, false);
            }
            this.dispose();
        }
    }

    private void btnUpdateOrderActionPerformed(java.awt.event.ActionEvent evt) {
        // Implement if needed
    }

    private void loadProducts() {
        ProductListPanel.removeAll();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Products WHERE Status = N'Còn Bán'";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("ProductID");
                String name = rs.getString("Name");
                String desc = rs.getString("Description");
                double price = rs.getDouble("Price");
                String imgPath = rs.getString("ImgPath");

                JPanel card = createProductCard(productId, name, desc, price, imgPath);
                ProductListPanel.add(card);
            }

            ProductListPanel.revalidate();
            ProductListPanel.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải sản phẩm: " + e.getMessage());
        }
    }

    private JPanel createProductCard(int productId, String name, String desc, double price, String imgPath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        card.setPreferredSize(new Dimension(200, 200));

        JLabel lblName = new JLabel(name, JLabel.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setBorder(BorderFactory.createEmptyBorder(10, 0, 2, 0));

        DecimalFormat df = new DecimalFormat("#,###");
        String formattedPrice = df.format(price);
        JLabel lblPrice = new JLabel(formattedPrice + "đ / Ly", JLabel.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPrice.setForeground(Color.red);

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        try {
            if (imgPath != null && !imgPath.isEmpty()) {
                Image img = new ImageIcon(imgPath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            lblImage.setText("No Image");
        }

        JButton btnAdd = new JButton("Thêm");
        btnAdd.addActionListener(e -> {
            JDialog dialog = new JDialog(OrderFrame.this, "Thêm sản phẩm", true);
            dialog.setContentPane(new AddProductToOrder(orderId, productId, name, price, () -> loadOrderDetails()));
            dialog.pack();
            dialog.setLocationRelativeTo(OrderFrame.this);
            dialog.setVisible(true);
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(lblName);
        infoPanel.add(lblPrice);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(btnAdd);

        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblImage, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(btnAdd, BorderLayout.SOUTH);

        return card;
    }

    protected void loadOrderDetails() {
        OrderDetailPanel.removeAll();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT od.OrderDetailID, p.Name, p.ImgPath, od.Quantity, od.UnitPrice, od.SubTotal\n"
                    + "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID\n"
                    + "WHERE od.OrderID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int detailId = rs.getInt("OrderDetailID");
                String name = rs.getString("Name");
                String imgPath = rs.getString("ImgPath");
                int qty = rs.getInt("Quantity");
                double price = rs.getDouble("UnitPrice");
                double subtotal = rs.getDouble("SubTotal");

                JPanel item = createOrderItemPanel(detailId, name, qty, price, subtotal, imgPath);
                OrderDetailPanel.add(item);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi load OrderDetails: " + ex.getMessage());
        }
        OrderDetailPanel.revalidate();
        OrderDetailPanel.repaint();
        updateTotalAmountLabel();
    }

    private JPanel createOrderItemPanel(int detailId, String name, int qty, double unitPrice, double subtotal, String imgPath) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setBackground(new Color(230, 255, 230));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 230, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel pic = new JLabel();
        pic.setPreferredSize(new Dimension(70, 70));
        pic.setHorizontalAlignment(SwingConstants.CENTER);
        if (imgPath != null && !imgPath.isBlank()) {
            try {
                Image img = new ImageIcon(imgPath).getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                pic.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                pic.setText("No Img");
            }
        } else {
            pic.setText("No Img");
        }

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel lblName = new JLabel("\uD83C\uDF7A " + name);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel ctl = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        ctl.setOpaque(false);
        ctl.add(new JLabel("SL:"));
        SpinnerNumberModel model = new SpinnerNumberModel(qty, 1, 999, 1);
        JSpinner spQty = new JSpinner(model);
        spQty.setPreferredSize(new Dimension(55, 25));
        ctl.add(spQty);

        JButton btnOk = smallButton("✓");
        btnOk.addActionListener(e -> updateQuantity(detailId, (int) spQty.getValue()));
        ctl.add(btnOk);
        JButton btnDel = smallButton("✕");
        btnDel.setForeground(Color.RED);
        btnDel.addActionListener(e -> deleteItem(detailId));
        ctl.add(btnDel);

        JLabel lblPrice = new JLabel("Đơn giá: " + Utils.formatCurrency(unitPrice)
                + "   |   Tổng: " + Utils.formatCurrency(subtotal));
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblPrice.setForeground(new Color(70, 70, 70));

        content.add(lblName);
        content.add(Box.createVerticalStrut(4));
        content.add(ctl);
        content.add(lblPrice);

        wrapper.add(pic);
        wrapper.add(Box.createRigidArea(new Dimension(10, 0)));
        wrapper.add(content);
        return wrapper;
    }

    private JButton smallButton(String t) {
        JButton b = new JButton(t);
        b.setMargin(new java.awt.Insets(1, 6, 1, 6));
        b.setPreferredSize(new Dimension(38, 25));
        return b;
    }

    private void deleteItem(int detailId) {
        if (JOptionPane.showConfirmDialog(this, "Xoá sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement pst = conn.prepareStatement("DELETE FROM OrderDetails WHERE OrderDetailID = ?");
                pst.setInt(1, detailId);
                pst.executeUpdate();
                updateOrderTotal(conn);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
            loadOrderDetails();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xoá món: " + e.getMessage());
        }
    }

    private void updateQuantity(int detailId, int newQty) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                PreparedStatement pst = conn.prepareStatement("UPDATE OrderDetails SET Quantity = ? WHERE OrderDetailID = ?");
                pst.setInt(1, newQty);
                pst.setInt(2, detailId);
                pst.executeUpdate();
                updateOrderTotal(conn);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
            loadOrderDetails();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật SL: " + e.getMessage());
        }
    }

    private void updateOrderTotal(Connection conn) throws Exception {
        PreparedStatement pst = conn.prepareStatement("UPDATE Orders SET TotalAmount = (SELECT ISNULL(SUM(SubTotal),0) FROM OrderDetails WHERE OrderID = ?) WHERE OrderID = ?");
        pst.setInt(1, orderId);
        pst.setInt(2, orderId);
        pst.executeUpdate();

        // Update totalAmount and finalTotalAmount
        String sql = "SELECT TotalAmount FROM Orders WHERE OrderID = ?";
        try (PreparedStatement totalPst = conn.prepareStatement(sql)) {
            totalPst.setInt(1, orderId);
            ResultSet rs = totalPst.executeQuery();
            if (rs.next()) {
                totalAmount = rs.getDouble("TotalAmount");
                TotalAmountLabel.setText(Utils.formatCurrency(totalAmount));
                finalTotalAmount = totalAmount; // Update finalTotalAmount
                FinalTotalAmountLabel.setText(Utils.formatCurrency(finalTotalAmount));
            }
        }
    }

 

    private void updateTotalAmountLabel() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT SUM(SubTotal) AS total FROM OrderDetails WHERE OrderID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalAmount = rs.getDouble("total");
                TotalAmountLabel.setText(Utils.formatCurrency(totalAmount));
                finalTotalAmount = totalAmount;
                FinalTotalAmountLabel.setText(Utils.formatCurrency(finalTotalAmount));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tính tổng tiền: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderFrame().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JLabel FinalTotalAmountLabel;
    private javax.swing.JPanel OrderDetailPanel;
    private javax.swing.JLabel OrderStatusLabel;
    private javax.swing.JPanel ProductListPanel;
    private javax.swing.JLabel TotalAmountLabel;
    private javax.swing.JButton btnCancelOrder;
    private javax.swing.JButton btnCloseOrder;
    private javax.swing.JButton btnPrintOrder;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnUpdateOrder;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel orderIDLabel;
    private javax.swing.JLabel orderTypeLabel;
    private javax.swing.JLabel staffNameLabel;
    private javax.swing.JLabel tableNameLabel;
    // End of variables declaration
}
