/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.midtermdb;

/**
 *
 * @author LENOVO
 */
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Dashboard extends JFrame implements ActionListener {

    String currentUser;
    JPanel mainPanel, navBar, contentPanel;
    JButton btnHome, btnAbout, btnProfile;
    JLabel contentLabel;
    
Dashboard(String user) {
        currentUser = user;
        setTitle("Dashboard");
        setSize(700, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        
        navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(70, 130, 180));
        navBar.setPreferredSize(new Dimension(700, 50));

       
        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftNav.setOpaque(false);

        btnHome = new JButton("🏠 Home");
        styleNavButton(btnHome);
        btnHome.addActionListener(this);
        leftNav.add(btnHome);

        btnAbout = new JButton("ℹ About");
        styleNavButton(btnAbout);
        btnAbout.addActionListener(this);
        leftNav.add(btnAbout);

        
        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightNav.setOpaque(false);

        btnProfile = new JButton("👤 " + currentUser);
        styleNavButton(btnProfile);
        btnProfile.addActionListener(this);
        rightNav.add(btnProfile);

        navBar.add(leftNav, BorderLayout.WEST);
        navBar.add(rightNav, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);

        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        showHome();
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    void styleNavButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 130, 180));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    void showHome() {
        contentPanel.removeAll();

        JLabel lbl = new JLabel("Welcome, " + currentUser + "!", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(new Color(70, 130, 180));
        contentPanel.add(lbl, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    void showAbout() {
        contentPanel.removeAll();

        JPanel aboutPanel = new JPanel(new GridBagLayout());
        aboutPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("About This App", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(70, 130, 180));
        JLabel desc = new JLabel("<html><div style='text-align:center;'>"
                + "This is a simple Java Desktop Application<br>"
                + "built using JFrame and MySQL database.<br><br>"
                + "Features: Register, Login, and CRUD operations."
                + "</div></html>", SwingConstants.CENTER);
        desc.setFont(new Font("Arial", Font.PLAIN, 13));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10);
        aboutPanel.add(title, gbc);
        gbc.gridy = 1;
        aboutPanel.add(desc, gbc);
        contentPanel.add(aboutPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    void showProfile() {
    contentPanel.removeAll();

    JPanel profilePanel = new JPanel(null);
    profilePanel.setBackground(Color.WHITE);

    JLabel title = new JLabel("My Account", SwingConstants.CENTER);
    title.setBounds(0, 20, 700, 30);
    title.setFont(new Font("Arial", Font.BOLD, 18));
    title.setForeground(new Color(70, 130, 180));
    profilePanel.add(title);
    
    final int[] selectedId = {-1};
    final String[] createdAt = {""};
    try {
        Connection con = connectionDB.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT id, created_at FROM users WHERE username=?");
        ps.setString(1, currentUser);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            selectedId[0] = rs.getInt(1);
            createdAt[0] = rs.getString(2);
        }
        con.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage());
    }
    
    JLabel lblUsername = new JLabel("Username:  " + currentUser);
    lblUsername.setBounds(200, 80, 300, 25);
    lblUsername.setFont(new Font("Arial", Font.PLAIN, 13));
    profilePanel.add(lblUsername);

    JLabel lblCreated = new JLabel("Member since:  " + createdAt[0]);
    lblCreated.setBounds(200, 110, 300, 25);
    lblCreated.setFont(new Font("Arial", Font.PLAIN, 13));
    profilePanel.add(lblCreated);
    
    JSeparator sep = new JSeparator();
    sep.setBounds(30, 150, 630, 10);
    profilePanel.add(sep);

    JLabel l1 = new JLabel("New Username:");
    l1.setBounds(150, 170, 110, 25);
    l1.setFont(new Font("Arial", Font.PLAIN, 12));
    profilePanel.add(l1);

    JTextField tf1 = new JTextField();
    tf1.setBounds(270, 170, 200, 28);
    profilePanel.add(tf1);

    JLabel l2 = new JLabel("New Password:");
    l2.setBounds(150, 210, 110, 25);
    l2.setFont(new Font("Arial", Font.PLAIN, 12));
    profilePanel.add(l2);

    JPasswordField pf1 = new JPasswordField();
    pf1.setBounds(270, 210, 200, 28);
    profilePanel.add(pf1);

    JButton btnUpdate = new JButton("Update");
    btnUpdate.setBounds(150, 265, 120, 35);
    btnUpdate.setBackground(new Color(70, 130, 180));
    btnUpdate.setForeground(Color.WHITE);
    btnUpdate.setFocusPainted(false);
    btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
    profilePanel.add(btnUpdate);

    JButton btnDelete = new JButton("Delete Account");
    btnDelete.setBounds(285, 265, 150, 35);
    btnDelete.setBackground(new Color(200, 60, 60));
    btnDelete.setForeground(Color.WHITE);
    btnDelete.setFocusPainted(false);
    btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
    profilePanel.add(btnDelete);

    JButton btnLogout = new JButton("Logout");
    btnLogout.setBounds(150, 320, 285, 35);
    btnLogout.setBackground(new Color(100, 100, 100));
    btnLogout.setForeground(Color.WHITE);
    btnLogout.setFocusPainted(false);
    btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
    profilePanel.add(btnLogout);


    
}





