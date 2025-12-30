package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import utils.DBConnection;

public class PhaCheDAO {
    
    // Class con để lưu dữ liệu đổ vào bảng
    public static class MonCanLam {
        public int maHD, maSP;
        public String tenBan, tenSP, trangThai;
        public int soLuong;

        public MonCanLam(int maHD, int maSP, String tenBan, String tenSP, int soLuong, String trangThai) {
            this.maHD = maHD;
            this.maSP = maSP;
            this.tenBan = tenBan;
            this.tenSP = tenSP;
            this.soLuong = soLuong;
            this.trangThai = trangThai;
        }
    }

    // 1. Lấy danh sách món đang "Chờ làm" hoặc "Đang làm"
    public List<MonCanLam> getDanhSachMonCanLam() {
        List<MonCanLam> list = new ArrayList<>();
        // Join 3 bảng để lấy Tên Bàn và Tên Món
        String sql = "SELECT hd.MaHD, ct.MaSP, b.TenBan, sp.TenSP, ct.SoLuong, ct.TrangThai " +
                     "FROM chitiethoadon ct " +
                     "JOIN hoadon hd ON ct.MaHD = hd.MaHD " +
                     "JOIN ban b ON hd.MaBan = b.MaBan " +
                     "JOIN sanpham sp ON ct.MaSP = sp.MaSP " +
                     "WHERE ct.TrangThai IN ('Chờ làm', 'Đang làm') " +
                     "ORDER BY hd.MaHD ASC"; // Món nào order trước hiện trước

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new MonCanLam(
                    rs.getInt("MaHD"),
                    rs.getInt("MaSP"),
                    rs.getString("TenBan"),
                    rs.getString("TenSP"),
                    rs.getInt("SoLuong"),
                    rs.getString("TrangThai")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Update trạng thái (Chờ làm -> Đang làm -> Xong)
    public void updateTrangThaiMon(int maHD, int maSP, String trangThaiMoi) {
        String sql = "UPDATE chitiethoadon SET TrangThai = ? WHERE MaHD = ? AND MaSP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThaiMoi);
            ps.setInt(2, maHD);
            ps.setInt(3, maSP);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}