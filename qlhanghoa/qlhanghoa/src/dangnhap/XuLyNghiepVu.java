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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class XuLyNghiepVu extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final String URL = "jdbc:mysql://localhost:3306/quanly_hanghoa?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    private ManHinhChinh mainScreen;  // Tham chiếu đến màn hình chính

    // Constructor nhận ManHinhChinh để có thể gọi loadData() cập nhật bảng ngoài
    public XuLyNghiepVu(ManHinhChinh mainScreen) {
        this.mainScreen = mainScreen;

        setTitle("Xử lý nghiệp vụ");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Mã", "Tên", "Danh mục", "Giá", "Tồn"}, 0);
        table = new JTable(model);
        loadData();

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnQLNhanVien = new JButton("Quản lý nhân viên");

        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnQLNhanVien.addActionListener(e -> {
            QuanLyNhanVien qlnv = new QuanLyNhanVien();
            qlnv.setVisible(true);
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnQLNhanVien);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM hanghoa");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ma_hanghoa"),
                        rs.getString("ten_hanghoa"),
                        rs.getInt("ma_danhmuc"),
                        rs.getDouble("gia_ban"),
                        rs.getInt("so_luong_ton")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void themSanPham() {
        String ten = JOptionPane.showInputDialog(this, "Nhập tên sản phẩm:");
        if (ten == null || ten.trim().isEmpty()) return;

        String giaStr = JOptionPane.showInputDialog(this, "Nhập giá bán:");
        String tonStr = JOptionPane.showInputDialog(this, "Nhập số lượng tồn:");

        try {
            double gia = Double.parseDouble(giaStr);
            int ton = Integer.parseInt(tonStr);

            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO hanghoa (ten_hanghoa, ma_danhmuc, ma_ncc, gia_ban, so_luong_ton) VALUES (?, ?, ?, ?, ?)")) {
                ps.setString(1, ten);
                ps.setInt(2, 1); // giả định mã danh mục = 1
                ps.setInt(3, 1); // giả định mã nhà cung cấp = 1
                ps.setDouble(4, gia);
                ps.setInt(5, ton);
                ps.executeUpdate();
                loadData();
                if (mainScreen != null) mainScreen.loadData();  // cập nhật màn hình chính
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá hoặc tồn không hợp lệ!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

private void suaSanPham() {
    int row = table.getSelectedRow();
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Chọn sản phẩm để sửa!");
        return;
    }

    int ma = (int) model.getValueAt(row, 0);
    String tenCu = (String) model.getValueAt(row, 1);
    int danhMucCu = (int) model.getValueAt(row, 2);
    double giaCu = (double) model.getValueAt(row, 3);
    int tonCu = (int) model.getValueAt(row, 4);

    String tenMoi = JOptionPane.showInputDialog(this, "Tên mới:", tenCu);
    if (tenMoi == null || tenMoi.trim().isEmpty()) return;

    String danhMucStr = JOptionPane.showInputDialog(this, "Mã danh mục mới:", danhMucCu);
    String giaStr = JOptionPane.showInputDialog(this, "Giá bán mới:", giaCu);
    String tonStr = JOptionPane.showInputDialog(this, "Số lượng tồn mới:", tonCu);

    try {
        int danhMucMoi = Integer.parseInt(danhMucStr);
        double giaMoi = Double.parseDouble(giaStr);
        int tonMoi = Integer.parseInt(tonStr);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE hanghoa SET ten_hanghoa=?, ma_danhmuc=?, gia_ban=?, so_luong_ton=? WHERE ma_hanghoa = ?")) {

            ps.setString(1, tenMoi);
            ps.setInt(2, danhMucMoi);
            ps.setDouble(3, giaMoi);
            ps.setInt(4, tonMoi);
            ps.setInt(5, ma);

            ps.executeUpdate();
            loadData();
            if (mainScreen != null) mainScreen.loadData();  // cập nhật màn hình chính
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Dữ liệu nhập không hợp lệ!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
    }
}


    private void xoaSanPham() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm để xóa!");
            return;
        }

        int ma = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM hanghoa WHERE ma_hanghoa = ?")) {
            ps.setInt(1, ma);
            ps.executeUpdate();
            loadData();
            if (mainScreen != null) mainScreen.loadData();  // cập nhật màn hình chính
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }
}