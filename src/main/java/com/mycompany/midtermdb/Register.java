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

        setVisible(true);
    }
}
