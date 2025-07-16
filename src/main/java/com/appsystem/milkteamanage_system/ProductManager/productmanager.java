/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.appsystem.milkteamanage_system.ProductManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.appsystem.milkteamanage_system.Utils.DBConnection;
import java.sql.Timestamp;
import javax.swing.JDialog;
import com.appsystem.milkteamanage_system.ProductManager.AddProductPanel;
import java.awt.Dialog;
import javax.swing.SwingUtilities;

public class productmanager extends javax.swing.JPanel {

    public productmanager() {
        initComponents();
        loadDataFromDatabase();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnXoa = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btnload = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableproduct = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(900, 700));

        btnXoa.setBackground(new java.awt.Color(255, 51, 0));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(102, 255, 0));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(51, 0, 51));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnedit.setBackground(new java.awt.Color(255, 255, 0));
        btnedit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnedit.setText("Sửa");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });

        btnload.setText("Load Data");
        btnload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnloadActionPerformed(evt);
            }
        });

        tableproduct.setBackground(new java.awt.Color(70, 130, 180));
        tableproduct.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tableproduct.setForeground(new java.awt.Color(51, 51, 51));
        tableproduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Description", "Price", "Status", "CreatedDate", "ImgPath"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableproduct.setToolTipText("");
        tableproduct.setPreferredSize(new java.awt.Dimension(900, 140));
        jScrollPane1.setViewportView(tableproduct);

        jScrollPane2.setViewportView(jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnload, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(130, 130, 130)
                        .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(135, 135, 135)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(431, 431, 431))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(btnload)
                .addGap(44, 44, 44))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Thêm sản phẩm", Dialog.ModalityType.APPLICATION_MODAL);
        AddProductPanel addPanel = new AddProductPanel();

        // Khi đóng panel, reload lại table
        dialog.setContentPane(addPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        // Sau khi thêm xong, load lại dữ liệu
        loadDataFromDatabase();

    }//GEN-LAST:event_btnAddActionPerformed


    private void btnloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnloadActionPerformed
        loadDataFromDatabase();
    }//GEN-LAST:event_btnloadActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int selectedRow = tableproduct.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Chọn dòng cần xóa!");
            return;
        }
        int id = (int) tableproduct.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM Products WHERE ProductID = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadDataFromDatabase();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
        int row = tableproduct.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để sửa");
            return;
        }
        // Lấy ID
        Object idObj = tableproduct.getValueAt(row, 0);
        int id = idObj instanceof Number
                ? ((Number) idObj).intValue()
                : Integer.parseInt(idObj.toString());

        // Lấy Name, Description an toàn
        String name = tableproduct.getValueAt(row, 1).toString();
        String desc = tableproduct.getValueAt(row, 2).toString();

        // Lấy Price an toàn
        Object priceObj = tableproduct.getValueAt(row, 3);
        double price;

        if (priceObj instanceof Number) {
            price = ((Number) priceObj).doubleValue();
        } else {
            price = Double.parseDouble(priceObj.toString());
        }

        // Lấy Status, ImgPath
        String status = tableproduct.getValueAt(row, 4).toString();
        Timestamp createdDate = (Timestamp) tableproduct.getValueAt(row, 5);
        String img = tableproduct.getValueAt(row, 6).toString();

        // Gọi popup sửa
        EditProductPanel.showEditDialog(
                this,
                id,
                name,
                desc,
                price,
                status,
                createdDate,
                img,
                () -> loadDataFromDatabase()
        );
    }//GEN-LAST:event_btneditActionPerformed
    private void loadDataFromDatabase() {
        String sql = "SELECT ProductID, Name, Description, Price, Status, CreatedDate, ImgPath FROM Products";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) tableproduct.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ProductID"),
                    rs.getString("Name"),
                    rs.getString("Description"),
                    rs.getDouble("Price"),
                    rs.getString("Status"),
                    rs.getTimestamp("CreatedDate"),
                    rs.getString("ImgPath")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnload;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tableproduct;
    // End of variables declaration//GEN-END:variables
}
