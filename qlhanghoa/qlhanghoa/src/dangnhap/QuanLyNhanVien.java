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

public class QuanLyNhanVien extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private final String URL = "jdbc:mysql://localhost:3306/quanly_hanghoa?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    public QuanLyNhanVien() {
        setTitle("Quản lý nhân viên");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Mã NV", "Tên", "Giới tính", "SĐT"}, 0);
        table = new JTable(model);
        loadData();

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");

        btnThem.addActionListener(e -> themNhanVien());
        btnSua.addActionListener(e -> suaNhanVien());
        btnXoa.addActionListener(e -> xoaNhanVien());

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM nhanvien");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ma_nv"),
                        rs.getString("ten_nv"),
                        rs.getString("gioitinh"),
                        rs.getString("sdt")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void themNhanVien() {
        String ten = JOptionPane.showInputDialog(this, "Nhập tên nhân viên:");
        String gioiTinh = JOptionPane.showInputDialog(this, "Giới tính:");
        String sdt = JOptionPane.showInputDialog(this, "Số điện thoại:");

        if (ten == null || gioiTinh == null || sdt == null || ten.trim().isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("INSERT INTO nhanvien (ten_nv, gioitinh, sdt) VALUES (?, ?, ?)")) {
            ps.setString(1, ten);
            ps.setString(2, gioiTinh);
            ps.setString(3, sdt);
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

    private void suaNhanVien() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn nhân viên để sửa!");
            return;
        }

        int ma = (int) model.getValueAt(row, 0);
        String tenCu = (String) model.getValueAt(row, 1);
        String gioiTinhCu = (String) model.getValueAt(row, 2);
        String sdtCu = (String) model.getValueAt(row, 3);

        String tenMoi = JOptionPane.showInputDialog(this, "Tên mới:", tenCu);
        String gioiTinhMoi = JOptionPane.showInputDialog(this, "Giới tính mới:", gioiTinhCu);
        String sdtMoi = JOptionPane.showInputDialog(this, "SĐT mới:", sdtCu);

        if (tenMoi == null || gioiTinhMoi == null || sdtMoi == null || tenMoi.trim().isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("UPDATE nhanvien SET ten_nv=?, gioitinh=?, sdt=? WHERE ma_nv=?")) {
            ps.setString(1, tenMoi);
            ps.setString(2, gioiTinhMoi);
            ps.setString(3, sdtMoi);
            ps.setInt(4, ma);
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }

    private void xoaNhanVien() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn nhân viên để xóa!");
            return;
        }

        int ma = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM nhanvien WHERE ma_nv=?")) {
            ps.setInt(1, ma);
            ps.executeUpdate();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }
}

