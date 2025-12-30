package dao;

import model.Ban;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class BanDAO {

    // 1. GIỮ NGUYÊN: Lấy danh sách bàn
    public List<Ban> getAllBan() {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM ban";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Ban(rs.getInt("MaBan"), rs.getString("TenBan"), rs.getString("TrangThai")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. GIỮ NGUYÊN: Cập nhật trạng thái bàn (Dùng cho Thu Ngân)
    public void updateTrangThai(int maBan, String trangThai) {
        String sql = "UPDATE ban SET TrangThai = ? WHERE MaBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maBan);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. THÊM MỚI: Chức năng thêm bàn mới (Mặc định trạng thái là 'Trống')
    public boolean addBan(String tenBan) {
        String sql = "INSERT INTO ban (TenBan, TrangThai) VALUES (?, 'Trống')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenBan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. THÊM MỚI: Chức năng sửa tên bàn
    public boolean updateBan(int maBan, String tenBanMoi) {
        String sql = "UPDATE ban SET TenBan = ? WHERE MaBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenBanMoi);
            ps.setInt(2, maBan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. THÊM MỚI: Chức năng xóa bàn
    public boolean deleteBan(int maBan) {
        String sql = "DELETE FROM ban WHERE MaBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}