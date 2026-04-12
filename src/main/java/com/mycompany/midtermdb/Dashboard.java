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

    
}





