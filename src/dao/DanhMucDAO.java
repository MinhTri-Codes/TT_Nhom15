package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DanhMuc;
import utils.DBConnection;

public class DanhMucDAO {
    // Lấy danh sách kèm số lượng sản phẩm để đổ vào tbDanhMuc
    public List<Object[]> getDanhMucWithCount() {
        List<Object[]> list = new ArrayList<>();
        // Sử dụng LEFT JOIN để đếm số sản phẩm thuộc từng danh mục
        String sql = "SELECT dm.MaDM, dm.TenDanhMuc, COUNT(sp.MaSP) as SoMon " +
                     "FROM danhmuc dm LEFT JOIN sanpham sp ON dm.MaDM = sp.MaDM " +
                     "GROUP BY dm.MaDM, dm.TenDanhMuc";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("MaDM"), rs.getString("TenDanhMuc"), rs.getInt("SoMon")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean addDanhMuc(String ten) {
        String sql = "INSERT INTO danhmuc (TenDanhMuc) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ten);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean updateDanhMuc(int ma, String ten) {
        String sql = "UPDATE danhmuc SET TenDanhMuc = ? WHERE MaDM = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ten);
            ps.setInt(2, ma);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean deleteDanhMuc(int ma) {
        String sql = "DELETE FROM danhmuc WHERE MaDM = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ma);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    // Hàm bổ trợ lấy toàn bộ danh sách cho ComboBox
    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM danhmuc";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new model.DanhMuc(rs.getInt("MaDM"), rs.getString("TenDanhMuc")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}