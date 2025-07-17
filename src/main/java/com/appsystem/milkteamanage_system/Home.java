/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.appsystem.milkteamanage_system;

import com.appsystem.milkteamanage_system.DiscountManage.DiscountManage;
import com.appsystem.milkteamanage_system.OrderManage.OrderManage;
import com.appsystem.milkteamanage_system.ProductManager.productmanager;
import com.appsystem.milkteamanage_system.Statistic.StatisticManager;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Home extends javax.swing.JFrame {

    private JPanel mainContentPanel;
    private JButton activeButton;
    private JButton dashboardButton;
    private JButton usersButton;
    private JButton productsButton;
    private JButton ordersButton;
    private JButton discountButton;
    private JButton statsButton;
    private JButton testButton;
    private JButton optionsButton;
    private JButton logoutButton;

    public Home() throws IOException {
        setTitle("Hệ thống quản lí bán trà sữa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        JPanel rootPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setPaint(new Color(240, 242, 245));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(rootPanel);

        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 180, 219), 0, getHeight(), new Color(0, 131, 176));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel logoContainer = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new Color(255, 255, 255, 25));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        logoContainer.setOpaque(false);
        logoContainer.setBorder(new EmptyBorder(5, 10, 5, 10));
        logoContainer.setPreferredSize(new Dimension(280, 200));
        logoContainer.setMaximumSize(new Dimension(280, 200));
        JLabel logoImage = new JLabel();
        logoImage.setIcon(loadAndResizeIcon("src/main/Resources/images/milk-tea.png", 30, 30));
        logoImage.setHorizontalAlignment(SwingConstants.CENTER);
        logoImage.setOpaque(false);
        JLabel logoLabel = new JLabel("Chào bạn, Admin");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        logoContainer.add(logoImage, BorderLayout.WEST);
        logoContainer.add(logoLabel, BorderLayout.CENTER);

        JPanel mainMenu = new JPanel();
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        mainMenu.setOpaque(false);
        mainMenu.setBorder(new EmptyBorder(20, 0, 20, 0));
        JLabel mainMenuHeader = new JLabel("MAIN MENU");
        mainMenuHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainMenuHeader.setForeground(new Color(255, 255, 255, 179));
        mainMenu.add(mainMenuHeader);
        mainMenu.add(Box.createVerticalStrut(10));

        dashboardButton = createNavButton("Dashboard", "🏠", e -> {
            showDashboard();
            setActiveButton(dashboardButton);
        });
        mainMenu.add(dashboardButton);
        mainMenu.add(createSeparator());
        usersButton = createNavButton("Quản Lí Nhân Viên", "user-icon.png", e -> {
            showUsersManage();
            setActiveButton(usersButton);
        });
        mainMenu.add(usersButton);
        mainMenu.add(createSeparator());
        productsButton = createNavButton("Quản Lí Hàng Hoá", "mug-icon.png", e -> {
            showProductsManage();
            setActiveButton(productsButton);
        });
        mainMenu.add(productsButton);
        mainMenu.add(createSeparator());
        ordersButton = createNavButton("Quản Lí Đơn Hàng", "receipt-icon.png", e -> {
            showOrdersManage();
            setActiveButton(ordersButton);
        });
        mainMenu.add(ordersButton);
        mainMenu.add(createSeparator());
        statsButton = createNavButton("Thống Kê", "chart-icon.png", e -> {
            showStatisticManage();
            setActiveButton(statsButton);
        });
        mainMenu.add(statsButton);

        mainMenu.add(createSeparator());
        discountButton = createNavButton("Quản Lí Khuyến Mãi", "receipt-icon.png", e -> {
            showDiscountManage();
            setActiveButton(discountButton);
        });
        mainMenu.add(discountButton);

        JPanel additionalMenu = new JPanel();
        additionalMenu.setLayout(new BoxLayout(additionalMenu, BoxLayout.Y_AXIS));
        additionalMenu.setOpaque(false);
        additionalMenu.setBorder(new EmptyBorder(20, 10, 20, 10));
        JLabel additionalMenuHeader = new JLabel("MY DATA");
        additionalMenuHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        additionalMenuHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        additionalMenuHeader.setForeground(new Color(255, 255, 255, 179));
        additionalMenuHeader.setBorder(new EmptyBorder(10, 15, 5, 15));
        additionalMenu.add(additionalMenuHeader);
        additionalMenu.add(Box.createVerticalStrut(5));
        testButton = createNavButton("TEST", "plus-icon.png", e -> setActiveButton(testButton));
        additionalMenu.add(testButton);
        additionalMenu.add(createSeparator());
        optionsButton = createNavButton("Tuỳ Chọn Khác...", "ellipsis-icon.png", e -> setActiveButton(optionsButton));
        additionalMenu.add(optionsButton);
        additionalMenu.add(createSeparator());
        logoutButton = createNavButton("Đăng Xuất", "logout-icon.png", e -> {
            setActiveButton(logoutButton);
            this.dispose();
        });
        additionalMenu.add(logoutButton);

        sidebar.add(logoContainer);
        sidebar.add(mainMenu);
        sidebar.add(additionalMenu);

        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(240, 242, 245));

        rootPanel.add(sidebar, BorderLayout.WEST);
        rootPanel.add(mainContentPanel, BorderLayout.CENTER);

        showDashboard();
        setActiveButton(dashboardButton);
    }

    private ImageIcon loadAndResizeIcon(String iconPath, int width, int height) throws IOException {
        if (iconPath.length() <= 2 && Character.isHighSurrogate(iconPath.charAt(0))) {
            return createEmojiIcon(iconPath, width, height);
        }
        File iconFile = new File(iconPath);
        if (!iconFile.exists()) {
            System.out.println("Icon file not found: " + iconPath);
            return createDefaultIcon(width, height);
        }
        BufferedImage originalImage = ImageIO.read(iconFile);
        if (originalImage == null) {
            System.out.println("Could not read image: " + iconPath);
            return createDefaultIcon(width, height);
        }
        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private ImageIcon createEmojiIcon(String emoji, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, width - 8));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(emoji)) / 2;
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(emoji, x, y);
        g2d.dispose();
        return new ImageIcon(image);
    }

    private ImageIcon createDefaultIcon(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillRoundRect(2, 2, width - 4, height - 4, 4, 4);
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.drawRoundRect(2, 2, width - 4, height - 4, 4, 4);
        g2d.dispose();
        return new ImageIcon(image);
    }

    private JButton createNavButton(String text, String iconPath, java.awt.event.ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 38));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 51));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
                if (this == activeButton) {
                    g2d.setColor(new Color(255, 255, 255, 64));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.setColor(new Color(255, 210, 0));
                    g2d.fillRect(0, 0, 4, getHeight());
                }
            }
        };
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(280, 40));
        button.setPreferredSize(new Dimension(280, 40));
        button.setIcon(new ImageIcon(iconPath));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
        if (action != null) {
            button.addActionListener(action);
        }
        return button;
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(224, 224, 224));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setOpaque(false);
        return separator;
    }

    private void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.repaint();
        }
        activeButton = button;
        button.repaint();
    }

    private void showDashboard() {
        mainContentPanel.removeAll();
        Dashboard dashboardPanel = new Dashboard();
        mainContentPanel.add(dashboardPanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showUsersManage() {
        mainContentPanel.removeAll();
        UserManage userManagePanel = new UserManage();
        mainContentPanel.add(userManagePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showProductsManage() {
        mainContentPanel.removeAll();
        productmanager productManagePanel = new productmanager();
        mainContentPanel.add(productManagePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showOrdersManage() {
        mainContentPanel.removeAll();
        OrderManage orderManagePanel = new OrderManage();
        mainContentPanel.add(orderManagePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showDiscountManage() {
        mainContentPanel.removeAll();
        DiscountManage discountManagePanel = new DiscountManage();
        mainContentPanel.add(discountManagePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showStatisticManage() {
        mainContentPanel.removeAll();
        StatisticManager statisticManagePanel = new StatisticManager();
        mainContentPanel.add(statisticManagePanel, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 641, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 348, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf"); // Optional: modern look
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            Home home = null;
            try {
                home = new Home();
            } catch (IOException ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }
            home.setVisible(true);
        });

        productmanager pr = new productmanager();
        pr.setVisible(true);

    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

              
