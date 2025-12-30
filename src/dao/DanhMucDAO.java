package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DanhMuc;
import utils.DBConnection;

public class DanhMucDAO {
    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        String sql = "SELECT * FROM danhmuc";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new DanhMuc(rs.getInt("MaDM"), rs.getString("TenDanhMuc")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}