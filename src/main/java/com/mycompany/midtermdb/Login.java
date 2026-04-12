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

public class Login extends JFrame implements ActionListener {

    JLabel l1, l2, lNote;
    JTextField tf1;
    JPasswordField pf1;
    JButton b1;
    JLabel registerLink;

    Login() {
        setTitle("Login");
        setSize(450, 380);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
    JLabel title = new JLabel("Mikko Bayot", SwingConstants.CENTER);
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
        
    //Tuplokanan 
    
    b1 = new JButton("LOGIN");
        b1.setBounds(80, 250, 280, 40);
        b1.setFont(new Font("Arial", Font.BOLD, 14));
        b1.setBackground(new Color(70, 130, 180));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);
        b1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b1.addActionListener(this);
        add(b1);
        
    registerLink = new JLabel("Don't have an account yet? Click here to Register", SwingConstants.CENTER);
        registerLink.setBounds(0, 300, 450, 25);
        registerLink.setFont(new Font("Arial", Font.PLAIN, 12));
        registerLink.setForeground(new Color(70, 130, 180));
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Register();
                dispose();
            }
            
            public void mouseEntered(MouseEvent e) {
                registerLink.setText("<html><u>Don't have an account yet? Click here to Register</u></html>");
            }
            public void mouseExited(MouseEvent e) {
                registerLink.setText("Don't have an account yet? Click here to Register");
            }
        });
        add(registerLink);

        setVisible(true);
    }

public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            String user = tf1.getText();
            String pass = new String(pf1.getPassword());

            try {
                Connection con = connectionDB.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                ps.setString(1, user);
                ps.setString(2, pass);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    new Dashboard(user);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password!");
                }
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
        

