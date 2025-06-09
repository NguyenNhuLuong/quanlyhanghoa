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

public class TimKiemSanPham extends JFrame {
    private JTextField txtTuKhoa;
    private JTextArea txtKetQua;
    private final String URL = "jdbc:mysql://localhost:3306/quanly_hanghoa?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    public TimKiemSanPham() {
        setTitle("Tìm kiếm sản phẩm");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtTuKhoa = new JTextField();
        JButton btnTim = new JButton("Tìm kiếm");
        txtKetQua = new JTextArea();
        txtKetQua.setEditable(false);

        btnTim.addActionListener(e -> timSanPham());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(txtTuKhoa, BorderLayout.CENTER);
        topPanel.add(btnTim, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(txtKetQua), BorderLayout.CENTER);
    }

    private void timSanPham() {
        String tuKhoa = txtTuKhoa.getText().trim();
        if (tuKhoa.isEmpty()) return;

        txtKetQua.setText("");
        String sql = "SELECT * FROM hanghoa WHERE ten_hanghoa LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + tuKhoa + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                txtKetQua.append("Mã: " + rs.getInt("ma_hanghoa") + ", Tên: " + rs.getString("ten_hanghoa") + ", Giá: " + rs.getDouble("gia_ban") + ", Tồn: " + rs.getInt("so_luong_ton") + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage());
        }
    }
}
