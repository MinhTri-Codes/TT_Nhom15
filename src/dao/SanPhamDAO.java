package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.SanPham;
import utils.DBConnection;

public class SanPhamDAO {
    // Lấy toàn bộ sản phẩm kèm tên danh mục (JOIN bảng)
    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT sp.*, dm.TenDanhMuc FROM sanpham sp "
                   + "JOIN danhmuc dm ON sp.MaDM = dm.MaDM";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getInt("MaSP"));
                sp.setTenSP(rs.getString("TenSP"));
                sp.setDonGia(rs.getDouble("DonGia"));
                sp.setMaDM(rs.getInt("MaDM"));
                sp.setTenDM(rs.getString("TenDanhMuc"));
                sp.setImgUrl(rs.getString("img_url"));
                list.add(sp);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Lấy sản phẩm theo ID để hiển thị ảnh
    public SanPham getSanPhamById(int id) {
        String sql = "SELECT * FROM sanpham WHERE MaSP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SanPham(rs.getInt("MaSP"), rs.getString("TenSP"), 
                                     rs.getDouble("DonGia"), rs.getInt("MaDM"), 
                                     rs.getString("img_url"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean addSanPham(SanPham sp) {
        String sql = "INSERT INTO sanpham (TenSP, DonGia, MaDM, img_url) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setDouble(2, sp.getDonGia());
            ps.setInt(3, sp.getMaDM());
            ps.setString(4, sp.getImgUrl());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean updateSanPham(SanPham sp) {
        String sql = "UPDATE sanpham SET TenSP=?, DonGia=?, MaDM=?, img_url=? WHERE MaSP=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setDouble(2, sp.getDonGia());
            ps.setInt(3, sp.getMaDM());
            ps.setString(4, sp.getImgUrl());
            ps.setInt(5, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean deleteSanPham(int id) {
        String sql = "DELETE FROM sanpham WHERE MaSP = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}