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
import java.time.*;
import java.time.temporal.ChronoUnit;

public class Dashboard extends JFrame implements ActionListener {

    String currentUser;
    int currentUserId;
    JPanel contentPanel;
    JButton btnHome, btnSessions, btnMembership, btnProfile;
    Timer sessionTimer;
    JLabel timerLabel;
    int activeSessionId = -1;

    Dashboard(String user) {
        currentUser = user;
        setTitle("Mikko's ComShop");
        setSize(750, 530);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        
        try {
            Connection con = connectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE username=?");
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) currentUserId = rs.getInt(1);
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JPanel navBar = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(63, 94, 251), getWidth(), 0, new Color(252, 70, 107));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        navBar.setPreferredSize(new Dimension(750, 50));

        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftNav.setOpaque(false);

        JLabel brand = new JLabel("Mikko's ComShop");
        brand.setFont(new Font("Arial", Font.BOLD, 15));
        brand.setForeground(Color.WHITE);
        leftNav.add(brand);

        btnHome = new JButton("Home");
        styleNav(btnHome);
        btnHome.addActionListener(this);
        leftNav.add(btnHome);

        btnSessions = new JButton("Sessions");
        styleNav(btnSessions);
        btnSessions.addActionListener(this);
        leftNav.add(btnSessions);

        btnMembership = new JButton("Membership");
        styleNav(btnMembership);
        btnMembership.addActionListener(this);
        leftNav.add(btnMembership);

        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightNav.setOpaque(false);

        btnProfile = new JButton(currentUser);
        styleNav(btnProfile);
        btnProfile.addActionListener(this);
        rightNav.add(btnProfile);

        navBar.add(leftNav, BorderLayout.WEST);
        navBar.add(rightNav, BorderLayout.EAST);
        add(navBar, BorderLayout.NORTH);
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 255));
        add(contentPanel, BorderLayout.CENTER);

        showHome();
        setVisible(true);
    }

        void styleNav(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    
    void showHome() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Dashboard", SwingConstants.CENTER);
        title.setBounds(0, 15, 750, 30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(63, 94, 251));
        panel.add(title);

        JLabel sessionInfo = new JLabel("", SwingConstants.CENTER);
        sessionInfo.setBounds(0, 55, 750, 25);
        sessionInfo.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(sessionInfo);

        timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setBounds(0, 80, 750, 25);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 13));
        timerLabel.setForeground(new Color(70, 130, 180));
        panel.add(timerLabel);

        try {
            Connection con = connectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT id, start_time FROM sessions WHERE user_id=? AND status='active'");
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                activeSessionId = rs.getInt(1);
                String start = rs.getString(2);
                sessionInfo.setText("Active session started at: " + start);
                startTimer();
            } else {
                sessionInfo.setText("No active session.");
                activeSessionId = -1;
            }
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JLabel pointsLabel = new JLabel("", SwingConstants.CENTER);
        pointsLabel.setBounds(0, 115, 750, 25);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        try {
            Connection con = connectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT points FROM users WHERE id=?");
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) pointsLabel.setText("Your Points: " + rs.getInt(1));
            con.close();
        } catch (Exception ex) { }
        panel.add(pointsLabel);

        JLabel lbTitle = new JLabel("Leaderboard - Top Members", SwingConstants.CENTER);
        lbTitle.setBounds(0, 155, 750, 25);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lbTitle);

        String[] cols = {"Rank", "Username", "Points"};
        DefaultTableModel lbModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable lbTable = new JTable(lbModel);
        lbTable.setRowHeight(25);
        lbTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane sp = new JScrollPane(lbTable);
        sp.setBounds(150, 185, 430, 220);
        panel.add(sp);

        try {
            Connection con = connectionDB.getConnection();
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT username, points FROM users ORDER BY points DESC LIMIT 10");
            int rank = 1;
            while (rs.next()) {
                String medal = rank == 1 ? "🥇" : rank == 2 ? "🥈" : rank == 3 ? "🥉" : String.valueOf(rank);
                lbModel.addRow(new Object[]{medal, rs.getString(1), rs.getInt(2) + " pts"});
                rank++;
            }
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    void startTimer() {
        if (sessionTimer != null) sessionTimer.stop();
        sessionTimer = new Timer(1000, ev -> {
            if (activeSessionId == -1) return;
            try {
                Connection con = connectionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "SELECT start_time FROM sessions WHERE id=?");
                ps.setInt(1, activeSessionId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Timestamp start = rs.getTimestamp(1);
                    long mins = ChronoUnit.MINUTES.between(start.toInstant(), Instant.now());
                    long hrs = mins / 60;
                    long rem = mins % 60;
                    timerLabel.setText("⏱ Elapsed: " + hrs + "h " + rem + "m");
                }
                con.close();
            } catch (Exception ex) { }
        });
        sessionTimer.start();
    }

    void showSessions() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("My Sessions", SwingConstants.CENTER);
        title.setBounds(0, 15, 750, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(63, 94, 251));
        panel.add(title);

       
        JButton btnStart = new JButton("▶ Start Session");
        btnStart.setBounds(150, 55, 180, 35);
        btnStart.setBackground(new Color(60, 170, 90));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnStart);

        JButton btnEnd = new JButton("⏹ End Session");
        btnEnd.setBounds(350, 55, 180, 35);
        btnEnd.setBackground(new Color(200, 60, 60));
        btnEnd.setForeground(Color.WHITE);
        btnEnd.setFocusPainted(false);
        btnEnd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnEnd);

        JLabel histTitle = new JLabel("Session History", SwingConstants.CENTER);
        histTitle.setBounds(0, 105, 750, 20);
        histTitle.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(histTitle);

        String[] cols = {"#", "Start Time", "End Time", "Duration", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 130, 680, 290);
        panel.add(sp);
        
        Runnable loadSessions = () -> {
            model.setRowCount(0);
            try {
                Connection con = connectionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "SELECT id, start_time, end_time, duration_minutes, amount_due, status " +
                    "FROM sessions WHERE user_id=? ORDER BY start_time DESC");
                ps.setInt(1, currentUserId);
                ResultSet rs = ps.executeQuery();
                int i = 1;
                while (rs.next()) {
                    model.addRow(new Object[]{
                        i++,
                        rs.getString(2),
                        rs.getString(3) == null ? "-" : rs.getString(3),
                        rs.getInt(4) + " mins",
                        "₱" + rs.getString(5),
                        rs.getString(6)
                    });
                }
                con.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        };
        loadSessions.run();

        btnStart.addActionListener(ev -> {
            try {
                Connection con = connectionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "SELECT id FROM sessions WHERE user_id=? AND status='active'");
                ps.setInt(1, currentUserId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "You already have an active session!");
                    con.close(); return;
                }
                ps = con.prepareStatement("INSERT INTO sessions (user_id) VALUES (?)");
                ps.setInt(1, currentUserId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Session started!");
                con.close();
                loadSessions.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnEnd.addActionListener(ev -> {
            try {
                Connection con = connectionDB.getConnection();
                PreparedStatement ps = con.prepareStatement(
                    "SELECT id, start_time FROM sessions WHERE user_id=? AND status='active'");
                ps.setInt(1, currentUserId);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "No active session to end.");
                    con.close(); return;
                }
                int sessId = rs.getInt(1);
                Timestamp start = rs.getTimestamp(2);
                long mins = ChronoUnit.MINUTES.between(start.toInstant(), Instant.now());
                if (mins < 1) mins = 1;
                long hrs = mins / 60;
                long points = hrs; // 1 point per hour

                
                double rate = 25.0; // default non-member
                PreparedStatement mp = con.prepareStatement(
                    "SELECT membership_type FROM membership WHERE user_id=? AND status='active'");
                mp.setInt(1, currentUserId);
                ResultSet mr = mp.executeQuery();
                if (mr.next()) {
                    rate = mr.getString(1).equals("premium") ? 15.0 : 20.0;
                }

                double amount = (mins / 60.0) * rate;

                ps = con.prepareStatement(
                    "UPDATE sessions SET end_time=NOW(), duration_minutes=?, amount_due=?, status='ended' WHERE id=?");
                ps.setLong(1, mins);
                ps.setDouble(2, amount);
                ps.setInt(3, sessId);
                ps.executeUpdate();

                if (points > 0) {
                    ps = con.prepareStatement("UPDATE users SET points = points + ? WHERE id=?");
                    ps.setLong(1, points);
                    ps.setInt(2, currentUserId);
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(this,
                    "Session ended!\nDuration: " + mins + " mins\n" +
                    "Amount Due: ₱" + String.format("%.2f", amount) + "\n" +
                    "Points Earned: " + points + " pt(s)");

                if (sessionTimer != null) sessionTimer.stop();
                activeSessionId = -1;
                con.close();
                loadSessions.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    void showMembership() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Membership", SwingConstants.CENTER);
        title.setBounds(0, 15, 750, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(63, 94, 251));
        panel.add(title);

        JLabel typeLabel = new JLabel("Type: None", SwingConstants.CENTER);
        typeLabel.setBounds(0, 65, 750, 25);
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(typeLabel);

        JLabel expiryLabel = new JLabel("", SwingConstants.CENTER);
        expiryLabel.setBounds(0, 95, 750, 25);
        expiryLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(expiryLabel);

        JLabel rateLabel = new JLabel("", SwingConstants.CENTER);
        rateLabel.setBounds(0, 125, 750, 25);
        rateLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        rateLabel.setForeground(new Color(70, 130, 180));
        panel.add(rateLabel);

        try {
            Connection con = connectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT membership_type, expiry_date, status FROM membership WHERE user_id=? ORDER BY id DESC LIMIT 1");
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
    String type = rs.getString(1);
    String expiry = rs.getString(2);
    String status = rs.getString(3);
    if (type != null) {
        typeLabel.setText("Membership: " + type.toUpperCase() + " (" + status + ")");
        expiryLabel.setText("Expires on: " + expiry);
        rateLabel.setText("Rate: ₱" + (type.equals("premium") ? "15.00" : "20.00") + " / hour");
    } else {
        typeLabel.setText("No active membership. Rate: ₱25.00 / hour");
    }
} else {
    typeLabel.setText("No active membership. Rate: ₱25.00 / hour");
}
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JSeparator sep = new JSeparator();
        sep.setBounds(100, 165, 540, 10);
        panel.add(sep);

        JLabel upgradeTitle = new JLabel("Subscribe / Upgrade Membership", SwingConstants.CENTER);
        upgradeTitle.setBounds(0, 175, 750, 25);
        upgradeTitle.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(upgradeTitle);

        JPanel basicCard = new JPanel(null);
        basicCard.setBounds(100, 210, 230, 150);
        basicCard.setBackground(new Color(240, 248, 255));
        basicCard.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180)));

        JLabel bTitle = new JLabel("BASIC", SwingConstants.CENTER);
        bTitle.setBounds(0, 15, 230, 25);
        bTitle.setFont(new Font("Arial", Font.BOLD, 15));
        basicCard.add(bTitle);

        JLabel bRate = new JLabel("₱20.00 / hour", SwingConstants.CENTER);
        bRate.setBounds(0, 45, 230, 20);
        bRate.setFont(new Font("Arial", Font.PLAIN, 12));
        basicCard.add(bRate);

        JLabel bDur = new JLabel("Valid: 30 days", SwingConstants.CENTER);
        bDur.setBounds(0, 70, 230, 20);
        bDur.setFont(new Font("Arial", Font.PLAIN, 12));
        basicCard.add(bDur);

        JButton btnBasic = new JButton("Subscribe");
        btnBasic.setBounds(55, 105, 120, 30);
        btnBasic.setBackground(new Color(70, 130, 180));
        btnBasic.setForeground(Color.WHITE);
        btnBasic.setFocusPainted(false);
        btnBasic.setCursor(new Cursor(Cursor.HAND_CURSOR));
        basicCard.add(btnBasic);
        panel.add(basicCard);

        JPanel premCard = new JPanel(null);
        premCard.setBounds(400, 210, 230, 150);
        premCard.setBackground(new Color(255, 250, 230));
        premCard.setBorder(BorderFactory.createLineBorder(new Color(200, 160, 0)));

        JLabel pTitle = new JLabel("PREMIUM", SwingConstants.CENTER);
        pTitle.setBounds(0, 15, 230, 25);
        pTitle.setFont(new Font("Arial", Font.BOLD, 15));
        pTitle.setForeground(new Color(180, 130, 0));
        premCard.add(pTitle);

        JLabel pRate = new JLabel("₱15.00 / hour", SwingConstants.CENTER);
        pRate.setBounds(0, 45, 230, 20);
        pRate.setFont(new Font("Arial", Font.PLAIN, 12));
        premCard.add(pRate);

        JLabel pDur = new JLabel("Valid: 30 days", SwingConstants.CENTER);
        pDur.setBounds(0, 70, 230, 20);
        pDur.setFont(new Font("Arial", Font.PLAIN, 12));
        premCard.add(pDur);

        JButton btnPrem = new JButton("Subscribe");
        btnPrem.setBounds(55, 105, 120, 30);
        btnPrem.setBackground(new Color(200, 160, 0));
        btnPrem.setForeground(Color.WHITE);
        btnPrem.setFocusPainted(false);
        btnPrem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        premCard.add(btnPrem);
        panel.add(premCard);

        btnBasic.addActionListener(ev -> subscribe("basic"));
        btnPrem.addActionListener(ev -> subscribe("premium"));

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    void subscribe(String type) {
        try {
            Connection con = connectionDB.getConnection();
          
            PreparedStatement ps = con.prepareStatement(
                "UPDATE membership SET status='expired' WHERE user_id=?");
            ps.setInt(1, currentUserId);
            ps.executeUpdate();
            
            ps = con.prepareStatement(
                "INSERT INTO membership (user_id, membership_type, expiry_date) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 30 DAY))");
            ps.setInt(1, currentUserId);
            ps.setString(2, type);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, type.toUpperCase() + " membership activated for 30 days!");
            con.close();
            showMembership();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    
    void showProfile() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("My Account", SwingConstants.CENTER);
        title.setBounds(0, 20, 750, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(63, 94, 251));
        panel.add(title);

        final int[] selectedId = {-1};
        JLabel lblUsername = new JLabel("Username: " + currentUser);
        lblUsername.setBounds(230, 75, 300, 25);
        lblUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(lblUsername);

        JLabel lblCreated = new JLabel();
        lblCreated.setBounds(230, 105, 300, 25);
        lblCreated.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(lblCreated);

        JLabel lblPoints = new JLabel();
        lblPoints.setBounds(230, 135, 300, 25);
        lblPoints.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPoints.setForeground(new Color(70, 130, 180));
        panel.add(lblPoints);

        try {
            Connection con = connectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id, created_at, points FROM users WHERE username=?");
            ps.setString(1, currentUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                selectedId[0] = rs.getInt(1);
                lblCreated.setText("Member since: " + rs.getString(2));
                lblPoints.setText("Points: " + rs.getInt(3));
            }
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }

        JSeparator sep = new JSeparator();
        sep.setBounds(100, 175, 540, 10);
        panel.add(sep);

        JLabel l1 = new JLabel("New Username:");
        l1.setBounds(200, 190, 110, 25);
        l1.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(l1);

        JTextField tf1 = new JTextField();
        tf1.setBounds(320, 190, 200, 28);
        panel.add(tf1);

        JLabel l2 = new JLabel("New Password:");
        l2.setBounds(200, 230, 110, 25);
        l2.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(l2);

        JPasswordField pf1 = new JPasswordField();
        pf1.setBounds(320, 230, 200, 28);
        panel.add(pf1);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(200, 280, 140, 35);
        btnUpdate.setBackground(new Color(70, 130, 180));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnUpdate);

        JButton btnDelete = new JButton("Delete Account");
        btnDelete.setBounds(355, 280, 165, 35);
        btnDelete.setBackground(new Color(200, 60, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnDelete);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(200, 330, 320, 35);
        btnLogout.setBackground(new Color(100, 100, 100));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnLogout);

        btnUpdate.addActionListener(ev -> {
            String newUser = tf1.getText().trim();
            String newPass = new String(pf1.getPassword()).trim();
            if (newUser.isEmpty()) { JOptionPane.showMessageDialog(this, "Username cannot be empty."); return; }
            try {
                Connection con = connectionDB.getConnection();
                PreparedStatement ps;
                if (newPass.isEmpty()) {
                    ps = con.prepareStatement("UPDATE users SET username=? WHERE id=?");
                    ps.setString(1, newUser); ps.setInt(2, selectedId[0]);
                } else {
                    ps = con.prepareStatement("UPDATE users SET username=?, password=? WHERE id=?");
                    ps.setString(1, newUser); ps.setString(2, newPass); ps.setInt(3, selectedId[0]);
                }
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Account updated!");
                currentUser = newUser;
                btnProfile.setText(currentUser);
                con.close();
                showProfile();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        btnDelete.addActionListener(ev -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete your account? This cannot be undone.");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection con = connectionDB.getConnection();
                    // delete related records first
                    PreparedStatement ps = con.prepareStatement("DELETE FROM sessions WHERE user_id=?");
                    ps.setInt(1, selectedId[0]); ps.executeUpdate();
                    ps = con.prepareStatement("DELETE FROM membership WHERE user_id=?");
                    ps.setInt(1, selectedId[0]); ps.executeUpdate();
                    ps = con.prepareStatement("DELETE FROM users WHERE id=?");
                    ps.setInt(1, selectedId[0]); ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Account deleted.");
                    con.close();
                    new Login();
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        btnLogout.addActionListener(ev -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Logout?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (sessionTimer != null) sessionTimer.stop();
                new Login();
                dispose();
            }
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHome) showHome();
        if (e.getSource() == btnSessions) showSessions();
        if (e.getSource() == btnMembership) showMembership();
        if (e.getSource() == btnProfile) showProfile();
    }
}