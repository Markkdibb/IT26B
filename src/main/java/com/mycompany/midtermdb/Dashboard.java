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

