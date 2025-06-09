/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package qlhanghoa;

/**
 *
 * @author PC ACER
 */
public class Qlhanghoa {

    public static void main(String[] args) {
        // Đảm bảo chạy trên luồng sự kiện của Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            new dangnhap.ManHinhDangNhap().setVisible(true);
        });
    }
    
}
