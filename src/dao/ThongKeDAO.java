package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class ThongKeDAO {
    // Lấy danh sách hóa đơn đã thanh toán theo khoảng ngày
    public List<Object[]> getDSHoaDon(String tuNgay, String denNgay) {
        List<Object[]> list = new ArrayList<>();
        // Câu lệnh SQL lấy Mã HĐ, Ngày lập và Tổng tiền
        String sql = "SELECT MaHD, NgayLap, TongTien FROM hoadon " +
                     "WHERE TrangThai = N'Đã thanh toán' " +
                     "AND NgayLap BETWEEN ? AND ? ORDER BY NgayLap DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // Định dạng ngày cho SQL là yyyy-MM-dd 00:00:00 và yyyy-MM-dd 23:59:59
            ps.setString(1, tuNgay + " 00:00:00");
            ps.setString(2, denNgay + " 23:59:59");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("MaHD"), 
                    rs.getTimestamp("NgayLap"), 
                    rs.getDouble("TongTien")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Tính tổng doanh thu trong khoảng ngày đó
    public double getTongDoanhThu(String tuNgay, String denNgay) {
        String sql = "SELECT SUM(TongTien) FROM hoadon WHERE TrangThai = N'Đã thanh toán' " +
                     "AND NgayLap BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tuNgay + " 00:00:00");
            ps.setString(2, denNgay + " 23:59:59");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}