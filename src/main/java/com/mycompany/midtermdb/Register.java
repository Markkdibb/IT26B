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
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register extends JFrame implements ActionListener{
    
    JLabel l1, l2;
    JTextField tf1;
    JPasswordField pf1;
    JButton b1;
    JLabel loginLink;
    
    Register() {
        setTitle("Register");
        setSize(450, 380);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JLabel title = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        title.setBounds(0, 30, 450, 35);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title);
        
        l1 = new JLabel("Username:");
        l1.setBounds(80, 100, 100, 25);
        l1.setFont(new Font("Arial", Font.PLAIN, 13));
        add(l1);

        tf1 = new JTextField();
        tf1.setBounds(80, 125, 280, 35);
        tf1.setFont(new Font("Arial", Font.PLAIN, 13));
        add(tf1);
        
        l2 = new JLabel("Password:");
        l2.setBounds(80, 170, 100, 25);
        l2.setFont(new Font("Arial", Font.PLAIN, 13));
        add(l2);

        pf1 = new JPasswordField();
        pf1.setBounds(80, 195, 280, 35);
        pf1.setFont(new Font("Arial", Font.PLAIN, 13));
        add(pf1);
        
        b1 = new JButton("REGISTER");
        b1.setBounds(80, 250, 280, 40);
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        b1.setBackground(new Color(63, 94, 251));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);
        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b1.addActionListener(this);
        add(b1);
        
        loginLink = new JLabel("Already have an account? Click here to Login", SwingConstants.CENTER);
        loginLink.setBounds(0, 315, 450, 25);
        loginLink.setFont(new Font("Arial", Font.PLAIN, 12));
        loginLink.setForeground(new Color(63, 94, 251));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Login();
                dispose();
            }
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Already have an account? Click here to Login</u></html>");
            }
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Already have an account? Click here to Login");
            }
    });
        add(loginLink);

        setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            String user = tf1.getText().trim();
            String pass = new String(pf1.getPassword()).trim();
            
            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
        }
            
            try {
                Connection con = connectionDB.getConnection();
                PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password) VALUES (?,?)");
                ps.setString(1, user);
                ps.setString(2, pass);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registered successfully!");
                new Login();
                dispose();
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }

    }
}
