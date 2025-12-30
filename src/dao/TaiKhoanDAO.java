package dao;

import model.TaiKhoan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBConnection;

public class TaiKhoanDAO {

    /**
     * Kiểm tra thông tin đăng nhập.
     */
    public TaiKhoan checkLogin(String tenDangNhap, String matKhau) {
        String sql = "SELECT * FROM taikhoan WHERE TenDangNhap = ? AND MatKhau = ?";
        TaiKhoan taiKhoan = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tenDangNhap);
            pstmt.setString(2, matKhau);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    taiKhoan = new TaiKhoan();
                    taiKhoan.setMaTK(rs.getInt("MaTK"));
                    taiKhoan.setRole(rs.getString("Role"));
                    taiKhoan.setTenDangNhap(rs.getString("TenDangNhap"));
                    taiKhoan.setMatKhau(rs.getString("MatKhau"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taiKhoan;
    }

    /**
     * Kiểm tra tên đăng nhập đã tồn tại hay chưa.
     * Dùng để báo lỗi ngay khi người dùng nhập trùng Tài khoản ở Form Thêm nhân viên.
     */
    public boolean isTenDangNhapExists(String tenDangNhap) {
        String sql = "SELECT COUNT(*) FROM taikhoan WHERE TenDangNhap = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tenDangNhap);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật thông tin tài khoản (Dùng khi nhấn nút Sửa nhân viên).
     */
    public boolean updateTaiKhoan(int maTK, String tenDN, String matKhau, String role) {
        String sql = "UPDATE taikhoan SET TenDangNhap = ?, MatKhau = ?, Role = ? WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, tenDN);
            pstmt.setString(2, matKhau);
            pstmt.setString(3, role);
            pstmt.setInt(4, maTK);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}