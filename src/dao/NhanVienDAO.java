/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import model.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

/**
 *
 * @author lengu
 */
public class NhanVienDAO {
  public List<NhanVien> getAllNhanVien(){
      List<NhanVien> list = new ArrayList<>();
      String sql = "SELECT * FROM nhanvien";
      try (Connection conn = DBConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql);
              ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
              NhanVien nv = new NhanVien();
              nv.setMaNV(rs.getInt("MaNV"));
              nv.setTenNV(rs.getString("TenNV"));
              nv.setSdt(rs.getString("SDT"));
              nv.setNgaySinh(rs.getDate("NgaySinh"));
              list.add(nv);
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return list;
  }
  public boolean addNhanVien(NhanVien nv){
    String sql = "INSERT INTO nhanvien(TenNV,SDT,NgaySinh) VALUES(?,?,?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps= conn.prepareStatement(sql)){
    ps.setString(1,nv.getTenNV());
    ps.setString(2, nv.getSdt());
    ps.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
    return ps.executeUpdate()>0;
    } catch(Exception e)
    {
        e.printStackTrace();
    }
    return false;
  }
  public boolean updateNhanVien(NhanVien nv){
      String sql = "UPDATE nhanvien SET TenNV=?, SDT=?, NgaySinh=? WHERE MaNV=?";
      try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps= conn.prepareStatement(sql)){
          ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSdt());
            ps.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            ps.setInt(4, nv.getMaNV());
            return ps.executeUpdate() > 0;
      } catch (Exception e) {
          e.printStackTrace();
      }
      return false;
      }
   public boolean deleteNhanVien(int MaNV){
    String sql = "DELETE FROM nhanvien WHERE MaNV=?";
       try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps= conn.prepareStatement(sql)){
           ps.setInt(1,MaNV);
           return ps.executeUpdate()>0;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return false;
  }
}
  

