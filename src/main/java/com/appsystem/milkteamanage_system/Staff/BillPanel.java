/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.appsystem.milkteamanage_system.Staff;

import com.appsystem.milkteamanage_system.Utils.DBConnection;
import com.appsystem.milkteamanage_system.Utils.FormatCurrency;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class BillPanel extends javax.swing.JPanel {

    private double discountAmount;
    private String discountCodeName;
    private int orderID;

    /**
     * Creates new form BillPanel
     */
    public BillPanel() {
        initComponents();
    }

    public BillPanel(int orderId, double discountAmount, String discountCodeName) {
        this();
        this.orderID = orderId;
        this.discountAmount = discountAmount;
        this.discountCodeName = discountCodeName;
        loadData(orderId);
    }

    private void loadData(int orderId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Header
            String hSql = "SELECT o.TableNumber, o.TotalAmount, o.OrderID, s.FullName, o.OrderDate, o.DiscountID FROM Orders o JOIN Staffs s ON s.StaffID = o.StaffID WHERE o.OrderID = ?";
            PreparedStatement h = conn.prepareStatement(hSql);
            h.setInt(1, orderId);
            ResultSet rh = h.executeQuery();
            if (rh.next()) {
                OrderIDLabel.setText(String.valueOf(rh.getInt("OrderID")));
                tableNumberLabel.setText(rh.getInt("TableNumber") == 0 ? "—" : String.valueOf(rh.getInt("TableNumber")));
                createdTImeOrderLabel.setText(rh.getTimestamp("OrderDate").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy")));
                StaffNameLabel.setText(rh.getString("FullName"));
                TotalPriceLabel.setText(FormatCurrency.formatCurrency(rh.getDouble("TotalAmount")));

                // Handle discount display
                if (discountCodeName != null && discountAmount > 0) {
                    DiscountLabel.setText(discountCodeName + ": -" + FormatCurrency.formatCurrency(discountAmount) + " đ");
                } else {
                    DiscountLabel.setText("Không có");
                }

                double finalTotal = rh.getDouble("TotalAmount") - discountAmount;
                FinalTotalPriceLabel.setText(FormatCurrency.formatCurrency(finalTotal) + " đ");
            }

            // Detail
            DefaultTableModel model = new DefaultTableModel(new Object[]{"STT", "Tên Món", "SL", "Đơn Giá", "Thành Tiền"}, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            String dSql = "SELECT p.Name, od.Quantity, od.UnitPrice, od.SubTotal FROM OrderDetails od JOIN Products p ON p.ProductID = od.ProductID WHERE od.OrderID = ?";
            PreparedStatement d = conn.prepareStatement(dSql);
            d.setInt(1, orderId);
            ResultSet rd = d.executeQuery();
            int stt = 1;
            while (rd.next()) {
                String name = rd.getString(1);
                int qty = rd.getInt(2);
                double unit = rd.getDouble(3);
                double tot = rd.getDouble(4);
                model.addRow(new Object[]{stt++, name, qty, FormatCurrency.formatCurrency(unit), FormatCurrency.formatCurrency(tot)});
            }
            OrderDetailTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi load bill: " + ex.getMessage());
        }
    }

    private void exportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Hóa Đơn PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        fileChooser.setSelectedFile(new File("HoaDon_" + OrderIDLabel.getText() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) {
            filePath += ".pdf";
        }

        try (Connection conn = DBConnection.getConnection()) {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Load font DejaVu Sans
            PdfFont font;
            try {
                font = PdfFontFactory.createFont("src/main/Resources/fonts/DejaVuSans.ttf", "Identity-H", PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy font DejaVuSans.ttf: " + e.getMessage());
                font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            }

            // Tiêu đề
            document.add(new Paragraph("PHIẾU THANH TOÁN")
                    .setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16)
                    .setBold());
            document.add(new Paragraph("Mã HĐ: " + OrderIDLabel.getText())
                    .setFont(font)
                    .setFontSize(12));
            document.add(new Paragraph("Số Bàn: " + tableNumberLabel.getText())
                    .setFont(font)
                    .setFontSize(12));
            document.add(new Paragraph("Thời gian: " + createdTImeOrderLabel.getText())
                    .setFont(font)
                    .setFontSize(12));
            document.add(new Paragraph("Thu ngân: " + StaffNameLabel.getText())
                    .setFont(font)
                    .setFontSize(12));
            document.add(new Paragraph("\n"));

            // Bảng chi tiết hóa đơn
            float[] columnWidths = {50, 200, 50, 100, 100};
            Table table = new Table(columnWidths);
            table.addHeaderCell(new Paragraph("STT").setFont(font));
            table.addHeaderCell(new Paragraph("Tên Món").setFont(font));
            table.addHeaderCell(new Paragraph("SL").setFont(font));
            table.addHeaderCell(new Paragraph("Đơn Giá").setFont(font));
            table.addHeaderCell(new Paragraph("Thành Tiền").setFont(font));

            String dSql = "SELECT p.Name, od.Quantity, od.UnitPrice, od.SubTotal FROM OrderDetails od JOIN Products p ON p.ProductID = od.ProductID WHERE od.OrderID = ?";
            PreparedStatement d = conn.prepareStatement(dSql);
            d.setInt(1, orderID);
            ResultSet rd = d.executeQuery();
            int stt = 1;
            while (rd.next()) {
                table.addCell(new Paragraph(String.valueOf(stt++)).setFont(font));
                table.addCell(new Paragraph(rd.getString("Name")).setFont(font));
                table.addCell(new Paragraph(String.valueOf(rd.getInt("Quantity"))).setFont(font));
                table.addCell(new Paragraph(FormatCurrency.formatCurrency(rd.getDouble("UnitPrice"))).setFont(font));
                table.addCell(new Paragraph(FormatCurrency.formatCurrency(rd.getDouble("SubTotal"))).setFont(font));
            }
            document.add(table);

            // Thông tin tổng kết
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Thành tiền: " + TotalPriceLabel.getText())
                    .setFont(font)
                    .setFontSize(12));
            document.add(new Paragraph("Khuyến mãi: " + DiscountLabel.getText())
                    .setFont(font)
                    .setFontSize(12));
            document.add(new Paragraph("Tổng tiền: " + FinalTotalPriceLabel.getText())
                    .setFont(font)
                    .setFontSize(12)
                    .setBold());
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Cảm ơn Quý Khách!")
                    .setFont(font)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12));

            document.close();
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được xuất thành công tại: " + filePath);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        OrderIDLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        StaffNameLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        createdTImeOrderLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tableNumberLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        OrderDetailTable = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        TotalPriceLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        FinalTotalPriceLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        DiscountLabel = new javax.swing.JLabel();
        printIntoPDFbtn = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("PHIẾU THANH TOÁN");

        jLabel2.setText("Mã HD :");

        OrderIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        OrderIDLabel.setText("XXXXX");

        jLabel3.setText("Thu ngân :");

        StaffNameLabel.setText("TRAN VAN A");

        jLabel4.setText("Thời gian :");

        createdTImeOrderLabel.setText("15:30:00 28/12/2005");

        jLabel6.setText("Số Bàn :");

        tableNumberLabel.setText("1");

        OrderDetailTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "Tên Món", "SL", "Đơn Giá", "Thành Tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(OrderDetailTable);

        jLabel8.setText("Thành tiền: ");

        TotalPriceLabel.setText("100,000 đ");

        jLabel10.setText("Tổng tiền: ");

        FinalTotalPriceLabel.setText("100,000 đ");

        jLabel12.setText("Cảm ơn Quý Khách !");

        jLabel5.setText("Khuyến mãi:");

        DiscountLabel.setText("XXXX");

        printIntoPDFbtn.setText("Xuất pdf");
        printIntoPDFbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printIntoPDFbtnActionPerformed(evt);
            }
        });

        btnClose.setText("Đóng");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(40, 40, 40)
                                    .addComponent(jLabel3))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(52, 52, 52)
                                    .addComponent(jLabel2))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(39, 39, 39)
                                    .addComponent(jLabel6))))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(OrderIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(createdTImeOrderLabel)
                            .addComponent(jLabel1)
                            .addComponent(tableNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(StaffNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 12, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel10)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(DiscountLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TotalPriceLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(FinalTotalPriceLabel, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addGap(64, 64, 64))
            .addGroup(layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(printIntoPDFbtn)
                .addGap(40, 40, 40)
                .addComponent(btnClose)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(OrderIDLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tableNumberLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(createdTImeOrderLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(StaffNameLabel))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TotalPriceLabel)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(DiscountLabel))
                .addGap(8, 8, 8)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(FinalTotalPriceLabel))
                .addGap(23, 23, 23)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printIntoPDFbtn)
                    .addComponent(btnClose))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void printIntoPDFbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printIntoPDFbtnActionPerformed
        // TODO add your handling code here:
        exportToPDF();
    }//GEN-LAST:event_printIntoPDFbtnActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnCloseActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DiscountLabel;
    private javax.swing.JLabel FinalTotalPriceLabel;
    private javax.swing.JTable OrderDetailTable;
    private javax.swing.JLabel OrderIDLabel;
    private javax.swing.JLabel StaffNameLabel;
    private javax.swing.JLabel TotalPriceLabel;
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel createdTImeOrderLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton printIntoPDFbtn;
    private javax.swing.JLabel tableNumberLabel;
    // End of variables declaration//GEN-END:variables
}
