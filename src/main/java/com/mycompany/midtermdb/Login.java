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

    JLabel l1, l2;
    JTextField tf1;
    JPasswordField pf1;
    JButton b1, b2;

    Login() {
        setTitle("Login");
        setSize(300, 200);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        l1 = new JLabel("Username:");
        l1.setBounds(20, 30, 80, 25);
        add(l1);

        tf1 = new JTextField();
        tf1.setBounds(110, 30, 150, 25);
        add(tf1);

        l2 = new JLabel("Password:");
        l2.setBounds(20, 70, 80, 25);
        add(l2);

        pf1 = new JPasswordField();
        pf1.setBounds(110, 70, 150, 25);
        add(pf1);

        b1 = new JButton("Login");
        b1.setBounds(20, 120, 90, 25);
        b1.addActionListener(this);
        add(b1);

        b2 = new JButton("Register");
        b2.setBounds(170, 120, 90, 25);
        b2.addActionListener(this);
        add(b2);

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

        if (e.getSource() == b2) {
            new Register();
            dispose();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
