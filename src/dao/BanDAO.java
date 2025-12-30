package dao;

import model.Ban;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class BanDAO {
    // Lấy danh sách bàn
    public List<Ban> getAllBan() {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM ban";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Ban(rs.getInt("MaBan"), rs.getString("TenBan"), rs.getString("TrangThai")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Cập nhật trạng thái bàn (Trống <-> Có Khách)
    public void updateTrangThai(int maBan, String trangThai) {
        String sql = "UPDATE ban SET TrangThai = ? WHERE MaBan = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maBan);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}