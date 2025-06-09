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

public class ManHinhChinh extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    private final String URL = "jdbc:mysql://localhost:3306/quanly_hanghoa?useSSL=false&serverTimezone=UTC";
    private final String USER = "root";
    private final String PASS = "";

    public ManHinhChinh(String tenNguoiDung) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        setTitle("Quản lý hàng hóa - Xin chào " + tenNguoiDung);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Thanh menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuTaiKhoan = new JMenu("Tài khoản");
        JMenuItem itemDangXuat = new JMenuItem("Đăng xuất");
        menuTaiKhoan.add(itemDangXuat);
        menuBar.add(Box.createHorizontalGlue());  // đẩy về bên phải
        menuBar.add(menuTaiKhoan);
        setJMenuBar(menuBar);

        // Label chào mừng
        JLabel lblChaoMung = new JLabel("Chào mừng " + tenNguoiDung + " đến với hệ thống quản lý hàng hóa.", JLabel.CENTER);
        lblChaoMung.setFont(new Font("Arial", Font.BOLD, 22));
        lblChaoMung.setForeground(new Color(0, 102, 153));
        lblChaoMung.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Bảng dữ liệu
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã hàng", "Tên hàng", "Số lượng", "Giá"});
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel chức năng
        JPanel panelChucNang = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelChucNang.setBackground(new Color(245, 245, 250));

        JButton btnDanhMuc = new JButton("Quản lý danh mục");
        JButton btnTimKiem = new JButton("Tìm kiếm sản phẩm");
        JButton btnXuLy = new JButton("Xử lý nghiệp vụ");
        JButton btnTinhTong = new JButton("Tính tổng tiền & số lượng");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnDanhMuc.setFont(buttonFont);
        btnTimKiem.setFont(buttonFont);
        btnXuLy.setFont(buttonFont);
        btnTinhTong.setFont(buttonFont);

        panelChucNang.add(btnDanhMuc);
        panelChucNang.add(btnTimKiem);
        panelChucNang.add(btnXuLy);
        panelChucNang.add(btnTinhTong);

        // Gộp các thành phần vào panel chính
        JPanel panelChinh = new JPanel(new BorderLayout(10, 10));
        panelChinh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelChinh.add(lblChaoMung, BorderLayout.NORTH);
        panelChinh.add(scrollPane, BorderLayout.CENTER);
        panelChinh.add(panelChucNang, BorderLayout.SOUTH);

        add(panelChinh);
        loadData();

        // Xử lý sự kiện các nút
        btnDanhMuc.addActionListener(e -> new QuanLyDanhMuc().setVisible(true));
        btnTimKiem.addActionListener(e -> new TimKiemSanPham().setVisible(true));
        btnXuLy.addActionListener(e -> new XuLyNghiepVu(this).setVisible(true));

        btnTinhTong.addActionListener(e -> {
            double tongTien = 0;
            int tongSoLuong = 0;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int soLuong = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                double gia = Double.parseDouble(tableModel.getValueAt(i, 3).toString());

                tongSoLuong += soLuong;
                tongTien += soLuong * gia;
            }

            JOptionPane.showMessageDialog(this,
                    "Tổng số lượng sản phẩm: " + tongSoLuong + "\n" +
                    "Tổng tiền: " + String.format("%,.2f", tongTien) + " VNĐ",
                    "Kết quả tính tổng",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        itemDangXuat.addActionListener(e -> {
            int luaChon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (luaChon == JOptionPane.YES_OPTION) {
                this.dispose();
                new ManHinhDangNhap().setVisible(true);
            }
        });
    }

    public void loadData() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT ma_hanghoa, ten_hanghoa, so_luong_ton, gia_ban FROM hanghoa";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0); // Xóa dữ liệu cũ

            while (rs.next()) {
                Object[] row = {
                        rs.getString("ma_hanghoa"),
                        rs.getString("ten_hanghoa"),
                        rs.getInt("so_luong_ton"),
                        rs.getDouble("gia_ban")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
        }
    }
}
