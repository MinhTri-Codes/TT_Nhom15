package dao;

import model.ChiTietHoaDon;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class HoaDonDAO {

    // 1. Tìm hóa đơn đang treo của bàn (Chưa thanh toán)
    public int getMaHoaDonCho(int maBan) {
        String sql = "SELECT MaHD FROM hoadon WHERE MaBan = ? AND TrangThai = 'Chưa thanh toán'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("MaHD");
        } catch (Exception e) { e.printStackTrace(); }
        return -1; // Không tìm thấy
    }

    // 2. Tạo hóa đơn mới (Khi khách mới vào)
    public int taoHoaDon(int maBan) {
        String sql = "INSERT INTO hoadon (MaBan, TrangThai, NgayLap) VALUES (?, 'Chưa thanh toán', NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, maBan);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1); // Trả về MaHD vừa tạo
        } catch (Exception e) { e.printStackTrace(); }
        return -1;
    }

    // 3. Thêm món vào hóa đơn (Nếu trùng món thì cộng dồn số lượng)
    public void themMon(int maHD, int maSP, int soLuong) {
        // Kiểm tra xem món đó đã có trong hóa đơn chưa
        String checkSql = "SELECT SoLuong FROM chitiethoadon WHERE MaHD = ? AND MaSP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
            psCheck.setInt(1, maHD);
            psCheck.setInt(2, maSP);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                // Nếu có rồi -> Cập nhật số lượng
                int slCu = rs.getInt("SoLuong");
                String updateSql = "UPDATE chitiethoadon SET SoLuong = ? WHERE MaHD = ? AND MaSP = ?";
                PreparedStatement psUpdate = conn.prepareStatement(updateSql);
                psUpdate.setInt(1, slCu + soLuong);
                psUpdate.setInt(2, maHD);
                psUpdate.setInt(3, maSP);
                psUpdate.executeUpdate();
            } else {
                // Chưa có -> Thêm mới
                // Lấy đơn giá hiện tại của sản phẩm
                String priceSql = "SELECT DonGia FROM sanpham WHERE MaSP = ?";
                PreparedStatement psPrice = conn.prepareStatement(priceSql);
                psPrice.setInt(1, maSP);
                ResultSet rsPrice = psPrice.executeQuery();
                double donGia = 0;
                if(rsPrice.next()) donGia = rsPrice.getDouble("DonGia");

                String insertSql = "INSERT INTO chitiethoadon (MaHD, MaSP, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
                PreparedStatement psInsert = conn.prepareStatement(insertSql);
                psInsert.setInt(1, maHD);
                psInsert.setInt(2, maSP);
                psInsert.setInt(3, soLuong);
                psInsert.setDouble(4, donGia);
                psInsert.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 4. Lấy danh sách món của hóa đơn để hiển thị lên bảng
    public List<ChiTietHoaDon> getChiTietHoaDon(int maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT sp.TenSP, cthd.SoLuong, cthd.DonGia " +
                     "FROM chitiethoadon cthd " +
                     "JOIN sanpham sp ON cthd.MaSP = sp.MaSP " +
                     "WHERE cthd.MaHD = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChiTietHoaDon(rs.getString("TenSP"), rs.getInt("SoLuong"), rs.getDouble("DonGia")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 5. Thanh toán
    public void thanhToan(int maHD, double tongTien) {
        String sql = "UPDATE hoadon SET TrangThai = 'Đã thanh toán', TongTien = ? WHERE MaHD = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, tongTien);
            ps.setInt(2, maHD);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}