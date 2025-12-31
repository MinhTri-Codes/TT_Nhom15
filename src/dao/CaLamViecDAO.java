package dao;

import model.CaLamViec;
import utils.DBConnection;
import java.sql.*;

public class CaLamViecDAO {
    // Lấy MaNV từ MaTK của tài khoản đang đăng nhập
    public int getMaNVByMaTK(int maTK) {
        String sql = "SELECT MaNV FROM nhanvien WHERE MaTK = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maTK);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("MaNV");
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    // Lưu thông tin mở ca vào database
    public boolean moCa(CaLamViec ca) {
        // Cấu trúc bảng calamviec yêu cầu TienChuyenKhoan không được null nên mặc định là 0
        String sql = "INSERT INTO calamviec (GioBD, TienMoCa, MaNV, TienChuyenKhoan) VALUES (?, ?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, ca.getGioBD());
            pstmt.setDouble(2, ca.getTienMoCa());
            pstmt.setInt(3, ca.getMaNV());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   // Trong CaLamViecDAO.java
public int getMaCaHienTai(int maNV) {
    // Tìm mã ca cuối cùng của nhân viên mà chưa kết thúc
    String sql = "SELECT MaCa FROM calamviec WHERE MaNV = ? AND GioKT IS NULL ORDER BY MaCa DESC LIMIT 1";
    try (Connection conn = utils.DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, maNV);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) return rs.getInt("MaCa");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // Trả về -1 nếu không tìm thấy ca nào đang mở
}
    // Tính tổng tiền theo phương thức thanh toán của ca hiện tại
    public double getTongTienTheoPhuongThuc(int maCa, String phuongThuc) {
    // Chỉ cần lọc theo MaCa là đủ, không lo bị lẫn ca khác
    String sql = "SELECT SUM(TongTien) FROM hoadon "
               + "WHERE MaCa = ? AND PhuongThuc = ? AND TrangThai = 'Đã thanh toán'";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, maCa);
        pstmt.setString(2, phuongThuc);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) return rs.getDouble(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0;
}

    // Cập nhật thông tin kết ca
    public boolean dongCa(int maCa, double tienMat, double tienCK, double doanhThu) {
        String sql = "UPDATE calamviec SET GioKT = ?, TienMatTrongKet = ?, TienChuyenKhoan = ?, DoanhThu = ? WHERE MaCa = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setDouble(2, tienMat);
            pstmt.setDouble(3, tienCK);
            pstmt.setDouble(4, doanhThu);
            pstmt.setInt(5, maCa);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}