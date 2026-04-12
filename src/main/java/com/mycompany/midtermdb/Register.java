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

public class Register extends JFrame {

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
        
        JButton b1 = new JButton("REGISTER");
        b1.setBounds(80, 250, 280, 40);
        b1.setBackground(new Color(70, 130, 180));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);
        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(b1);
        
        loginLink = new JLabel("Already have an account? Click here to Login", SwingConstants.CENTER);
        loginLink.setBounds(0, 315, 450, 25);
        loginLink.setFont(new Font("Arial", Font.PLAIN, 12));
        loginLink.setForeground(new Color(70, 130, 180));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Login();
                dispose();
                }
    });
    add(loginLink);

        setVisible(true);
    }
}
