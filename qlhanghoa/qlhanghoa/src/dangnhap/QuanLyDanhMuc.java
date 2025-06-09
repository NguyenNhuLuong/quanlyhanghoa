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

public class QuanLyDanhMuc extends JFrame {
    private JComboBox<String> cmbDanhMuc;
    private JTextArea txtKetQua;
    private final String URL = "jdbc:mysql://localhost:3306/quanly_hanghoa?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    public QuanLyDanhMuc() {
        setTitle("Quản lý danh mục");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cmbDanhMuc = new JComboBox<>();
        txtKetQua = new JTextArea();
        txtKetQua.setEditable(false);

        loadDanhMuc();

        cmbDanhMuc.addActionListener(e -> loadSanPhamTheoDanhMuc());

        add(cmbDanhMuc, BorderLayout.NORTH);
        add(new JScrollPane(txtKetQua), BorderLayout.CENTER);
    }

    private void loadDanhMuc() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT ma_danhmuc, ten_danhmuc FROM danhmuc");
            while (rs.next()) {
                cmbDanhMuc.addItem(rs.getInt("ma_danhmuc") + " - " + rs.getString("ten_danhmuc"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh mục: " + e.getMessage());
        }
    }

    private void loadSanPhamTheoDanhMuc() {
        String selected = (String) cmbDanhMuc.getSelectedItem();
        if (selected == null) return;
        int maDM = Integer.parseInt(selected.split(" - ")[0]);

        txtKetQua.setText("");
        String sql = "SELECT ten_hanghoa, gia_ban, so_luong_ton FROM hanghoa WHERE ma_danhmuc = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDM);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                txtKetQua.append("Tên: " + rs.getString("ten_hanghoa") + ", Giá: " + rs.getDouble("gia_ban") + ", Tồn: " + rs.getInt("so_luong_ton") + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải sản phẩm: " + e.getMessage());
        }
    }
}
