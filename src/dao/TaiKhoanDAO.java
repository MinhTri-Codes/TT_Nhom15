/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBConnection;

public class TaiKhoanDAO {
    
    /**
     * Kiểm tra thông tin đăng nhập và trả về đối tượng TaiKhoan nếu thành công.
     * @param tenDangNhap Tên đăng nhập từ form.
     * @param matKhau Mật khẩu từ form.
     * @return Đối tượng TaiKhoan nếu tìm thấy, ngược lại trả về null.
     */
    public TaiKhoan checkLogin(String tenDangNhap, String matKhau) {
        // Câu truy vấn SQL để tìm tài khoản
        String sql = "SELECT * FROM taikhoan WHERE TenDangNhap = ? AND MatKhau = ?";
        TaiKhoan taiKhoan = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Thiết lập tham số cho PreparedStatement
            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);

            // Thực thi truy vấn
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Nếu tìm thấy kết quả (người dùng hợp lệ), tạo đối tượng TaiKhoan
                    taiKhoan = new TaiKhoan();
                    taiKhoan.setMaTK(rs.getInt("MaTK"));
                    taiKhoan.setRole(rs.getString("Role"));
                    taiKhoan.setTenDangNhap(rs.getString("TenDangNhap"));
                    taiKhoan.setMatKhau(rs.getString("MatKhau"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return taiKhoan;
    }
}
