/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class loginform extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegis;
    private JButton btnClear;

    private Connection conn;

    public loginform() {
        setTitle("Login Form");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Panel form input (username & password)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        panel.add(formPanel, BorderLayout.CENTER);

        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnLogin = new JButton("Login");
        btnRegis = new JButton("Register");
        btnClear = new JButton("Clear");

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegis);
        buttonPanel.add(btnClear);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        connectDB();

        // Action Listener
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        btnRegis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                regisAction();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearAction();
            }
        });
    }

    private void connectDB() {
        try {
            String url = "jdbc:postgresql://localhost:5432/db_login";
            String user = "postgres"; 
            String password = "baherjaya"; 

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi berhasil.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi gagal: " + e.getMessage());
        }
    }

    private void loginAction() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi username dan password!");
            return;
        }

        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login berhasil! Selamat datang " + username);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void regisAction() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!");
            return;
        }

        try {
            // Cek apakah username sudah ada
            String checkSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Username sudah digunakan!");
            } else {
                // Insert user baru
                String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Registrasi berhasil! Silakan login.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearAction() {
        txtUsername.setText("");
        txtPassword.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new loginform().setVisible(true);
        });
    }
}
