/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dangnhap;

/**
 *
 * @author PC ACER
 */


import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManHinhDangNhap extends JFrame {
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap, btnThoat;

    private final String URL = "jdbc:mysql://localhost:3306/quanly_hanghoa?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    public ManHinhDangNhap() {
        setTitle("Đăng nhập hệ thống");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin đăng nhập"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTenDangNhap = new JLabel("Tên đăng nhập:");
        lblTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTenDangNhap, gbc);

        txtTenDangNhap = new JTextField(20);
        txtTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(txtTenDangNhap, gbc);

        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblMatKhau, gbc);

        txtMatKhau = new JPasswordField(20);
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(txtMatKhau, gbc);

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 14));
        btnThoat = new JButton("Thoát");
        btnThoat.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.add(btnDangNhap);
        buttonPanel.add(btnThoat);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        setContentPane(panel);

        btnDangNhap.addActionListener(e -> {
            String username = txtTenDangNhap.getText().trim();
            String password = new String(txtMatKhau.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            if (kiemTraDangNhap(username, password)) {
                new ManHinhChinh(username).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        });

        btnThoat.addActionListener(e -> System.exit(0));
    }

    private boolean kiemTraDangNhap(String username, String password) {
        boolean ketQua = false;
        String sql = "SELECT * FROM taikhoan WHERE ten_dang_nhap = ? AND mat_khau = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            ketQua = rs.next();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + ex.getMessage());
        }

        return ketQua;
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy driver MySQL: " + e.getMessage());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ManHinhDangNhap frame = new ManHinhDangNhap();
            frame.setVisible(true);
        });
    }
}
