package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class ThongKeDAO {

    /**
     * 1. THỐNG KÊ CA LÀM VIỆC (Dùng cho ThongKeCaLamJPanel)
     * Trả về: MaCa, GioBD, GioKT, TienMoCa, TienMatTrongKet, TienChuyenKhoan, DoanhThu, MaNV, Role
     */
    public List<Object[]> getDSThongKeCa(String tuNgay, String denNgay) {
        List<Object[]> list = new ArrayList<>();
        // Truy vấn JOIN 3 bảng để lấy đầy đủ thông tin nhân viên và vai trò
        String sql = "SELECT cl.MaCa, cl.GioBD, cl.GioKT, cl.TienMoCa, cl.TienMatTrongKet, " +
                     "cl.TienChuyenKhoan, cl.DoanhThu, cl.MaNV, tk.Role " +
                     "FROM calamviec cl " +
                     "LEFT JOIN nhanvien nv ON cl.MaNV = nv.MaNV " +
                     "LEFT JOIN taikhoan tk ON nv.MaTK = tk.MaTK " +
                     "WHERE DATE(cl.GioBD) BETWEEN ? AND ? " +
                     "ORDER BY cl.GioBD DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tuNgay);
            ps.setString(2, denNgay);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("MaCa"),                  // 0
                    rs.getTimestamp("GioBD"),           // 1
                    rs.getTimestamp("GioKT"),           // 2
                    rs.getBigDecimal("TienMoCa"),       // 3
                    rs.getBigDecimal("TienMatTrongKet"),// 4
                    rs.getBigDecimal("TienChuyenKhoan"),// 5
                    rs.getBigDecimal("DoanhThu"),        // 6
                    rs.getInt("MaNV"),                  // 7
                    rs.getString("Role")                 // 8
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 2. THỐNG KÊ HÓA ĐƠN CHI TIẾT (Dùng cho ThongKeJPanel)
     * Trả về: MaHD, NgayLap, TongTien, MaCa, MaNV, TenNV
     */
    public List<Object[]> getDSHoaDonChiTiet(String tuNgay, String denNgay) {
        List<Object[]> list = new ArrayList<>();
        // JOIN bảng hoadon với nhanvien để lấy tên người lập hóa đơn
        String sql = "SELECT h.MaHD, h.NgayLap, h.TongTien, h.MaCa, h.MaNV, nv.TenNV " +
                     "FROM hoadon h " +
                     "LEFT JOIN nhanvien nv ON h.MaNV = nv.MaNV " +
                     "WHERE h.TrangThai = N'Đã thanh toán' " +
                     "AND DATE(h.NgayLap) BETWEEN ? AND ? " +
                     "ORDER BY h.NgayLap DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tuNgay);
            ps.setString(2, denNgay);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("MaHD"),              // 0
                    rs.getTimestamp("NgayLap"),     // 1
                    rs.getBigDecimal("TongTien"),    // 2
                    rs.getObject("MaCa"),           // 3 (Dùng getObject để nhận được null)
                    rs.getInt("MaNV"),              // 4
                    rs.getString("TenNV")           // 5
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 3. TÍNH TỔNG DOANH THU (Dùng cho nhãn hiển thị tổng tiền nhanh)
     */
    public double getTongDoanhThu(String tuNgay, String denNgay) {
        String sql = "SELECT SUM(TongTien) FROM hoadon " +
                     "WHERE TrangThai = N'Đã thanh toán' " +
                     "AND DATE(NgayLap) BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tuNgay);
            ps.setString(2, denNgay);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}