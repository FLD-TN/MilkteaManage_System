/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.appsystem.milkteamanage_system.Statistic;

import com.appsystem.milkteamanage_system.Utils.DBConnection;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticManager extends javax.swing.JPanel {

    public StatisticManager() {
        setLayout(new BorderLayout());

        // Main panel with a light gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 248, 255), 0, getHeight(), new Color(200, 220, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setPreferredSize(new Dimension(900, 700));

        // Charts section
        JPanel chartsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        // Doanh thu theo tháng
        DefaultCategoryDataset revenueDataset = createRevenueDataset();
        JFreeChart revenueChart = ChartFactory.createBarChart(
                "Doanh Thu Theo Tháng", "Tháng", "Doanh Thu (VND)",
                revenueDataset, PlotOrientation.VERTICAL, true, true, false
        );
        ChartPanel revenueChartPanel = new ChartPanel(revenueChart);
        revenueChartPanel.setPreferredSize(new Dimension(300, 300));
        chartsPanel.add(revenueChartPanel);

        // Sản phẩm bán chạy
        DefaultCategoryDataset topProductsDataset = createTopProductsDataset();
        JFreeChart topProductsChart = ChartFactory.createBarChart(
                "Top 5 Sản Phẩm Bán Chạy", "Sản Phẩm", "Số Lượng",
                topProductsDataset, PlotOrientation.VERTICAL, true, true, false
        );
        ChartPanel topProductsChartPanel = new ChartPanel(topProductsChart);
        topProductsChartPanel.setPreferredSize(new Dimension(300, 300));
        chartsPanel.add(topProductsChartPanel);

        // Số lượng đơn hàng theo ngày
        DefaultCategoryDataset ordersDataset = createOrdersDataset();
        JFreeChart ordersChart = ChartFactory.createLineChart(
                "Đơn Hàng Theo Ngày", "Ngày", "Số Lượng",
                ordersDataset, PlotOrientation.VERTICAL, true, true, false
        );
        ChartPanel ordersChartPanel = new ChartPanel(ordersChart);
        ordersChartPanel.setPreferredSize(new Dimension(300, 300));
        chartsPanel.add(ordersChartPanel);

        mainPanel.add(chartsPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private DefaultCategoryDataset createRevenueDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT MONTH(OrderDate) AS Month, SUM(TotalAmount) AS Revenue "
                    + "FROM Orders WHERE YEAR(OrderDate) = YEAR(GETDATE()) GROUP BY MONTH(OrderDate)";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getDouble("Revenue"), "Doanh Thu", "Tháng " + rs.getInt("Month"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    private DefaultCategoryDataset createTopProductsDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT TOP 5 p.Name, SUM(od.Quantity) AS TotalSold "
                    + "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID "
                    + "GROUP BY p.Name ORDER BY TotalSold DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getInt("TotalSold"), "Số Lượng", rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    private DefaultCategoryDataset createOrdersDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DAY(OrderDate) AS Day, COUNT(*) AS OrderCount "
                    + "FROM Orders WHERE MONTH(OrderDate) = MONTH(GETDATE()) AND YEAR(OrderDate) = YEAR(GETDATE()) "
                    + "GROUP BY DAY(OrderDate)";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dataset.addValue(rs.getInt("OrderCount"), "Đơn Hàng", "Ngày " + rs.getInt("Day"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );
    }
}
