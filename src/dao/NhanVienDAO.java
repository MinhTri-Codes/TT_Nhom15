package dao;

import model.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBConnection;

public class NhanVienDAO {

    // 1. Lấy danh sách kèm theo Role (Dùng JOIN)
    public List<NhanVien> getAllNhanVien() {
    List<NhanVien> list = new ArrayList<>();
    String sql = "SELECT nv.MaNV, nv.TenNV, nv.SDT, nv.NgaySinh, tk.Role, tk.TenDangNhap, tk.MatKhau " +
                 "FROM nhanvien nv " +
                 "LEFT JOIN taikhoan tk ON nv.MaTK = tk.MaTK"; 
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            NhanVien nv = new NhanVien();
            nv.setMaNV(rs.getInt("MaNV"));
            nv.setTenNV(rs.getString("TenNV"));
            nv.setSdt(rs.getString("SDT"));
            nv.setNgaySinh(rs.getDate("NgaySinh"));
            nv.setRole(rs.getString("Role"));
            
            // Bây giờ các hàm này sẽ hoạt động vì đã khai báo ở Bước 1
            nv.setTenDangNhap(rs.getString("TenDangNhap"));
            nv.setMatKhau(rs.getString("MatKhau"));
            
            list.add(nv);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    // 2. Thêm nhân viên đồng thời tạo tài khoản (Dùng Transaction)
   // Cập nhật tham số đầu vào để nhận thêm taiKhoan và matKhau từ giao diện
public boolean addNhanVien(NhanVien nv, String taiKhoan, String matKhau) { 
    Connection conn = null;
    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false); // Bắt đầu Transaction

        // 1. Thêm vào bảng taikhoan trước để lấy MaTK
        String sqlTK = "INSERT INTO taikhoan(TenDangNhap, MatKhau, Role) VALUES(?,?,?)";
        PreparedStatement psTK = conn.prepareStatement(sqlTK, Statement.RETURN_GENERATED_KEYS);
        psTK.setString(1, taiKhoan); // Đã có biến taiKhoan từ tham số
        psTK.setString(2, matKhau);  // Đã có biến matKhau từ tham số
        psTK.setString(3, nv.getRole());
        psTK.executeUpdate();

        ResultSet rs = psTK.getGeneratedKeys();
        int generatedMaTK = 0;
        if (rs.next()) generatedMaTK = rs.getInt(1);

        // 2. Thêm vào bảng nhanvien
        String sqlNV = "INSERT INTO nhanvien(TenNV, SDT, NgaySinh, MaTK) VALUES(?,?,?,?)";
        PreparedStatement psNV = conn.prepareStatement(sqlNV);
        psNV.setString(1, nv.getTenNV());
        psNV.setString(2, nv.getSdt());
        psNV.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
        psNV.setInt(4, generatedMaTK);
        psNV.executeUpdate();

        conn.commit();
        return true;
    } catch (SQLException e) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
        e.printStackTrace();
    }
    return false;
}

// Thêm hàm này để sửa lỗi ở image_20f0fd.png
public boolean resetPassword(int maNV) {
    String sql = "UPDATE taikhoan SET MatKhau = '123' WHERE MaTK = (SELECT MaTK FROM nhanvien WHERE MaNV = ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, maNV);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    // 3. Cập nhật nhân viên và Role (Dùng Transaction)
    public boolean updateNhanVien(NhanVien nv) {
    Connection conn = null;
    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);

        // 1. Cập nhật bảng nhanvien
        String sqlNV = "UPDATE nhanvien SET TenNV=?, SDT=?, NgaySinh=? WHERE MaNV=?";
        PreparedStatement psNV = conn.prepareStatement(sqlNV);
        psNV.setString(1, nv.getTenNV());
        psNV.setString(2, nv.getSdt());
        psNV.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
        psNV.setInt(4, nv.getMaNV());
        psNV.executeUpdate();

        // 2. Cập nhật bảng taikhoan (Sửa cả Role và MatKhau)
        String sqlTK = "UPDATE taikhoan SET Role=?, MatKhau=? WHERE MaTK=(SELECT MaTK FROM nhanvien WHERE MaNV=?)";
        PreparedStatement psTK = conn.prepareStatement(sqlTK);
        psTK.setString(1, nv.getRole());
        psTK.setString(2, nv.getMatKhau()); // Lấy mật khẩu từ đối tượng NhanVien
        psTK.setInt(3, nv.getMaNV());
        psTK.executeUpdate();

        conn.commit();
        return true;
    } catch (SQLException e) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
        e.printStackTrace();
    }
    return false;
}

    // 4. Tìm kiếm ID kèm Role
    public NhanVien getNhanVienById(int maNV) {
        String sql = "SELECT nv.*, tk.Role FROM nhanvien nv "
                   + "LEFT JOIN taikhoan tk ON nv.MaTK = tk.MaTK WHERE nv.MaNV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NhanVien nv = new NhanVien();
                    nv.setMaNV(rs.getInt("MaNV"));
                    nv.setTenNV(rs.getString("TenNV"));
                    nv.setSdt(rs.getString("SDT"));
                    nv.setNgaySinh(rs.getDate("NgaySinh"));
                    nv.setRole(rs.getString("Role"));
                    return nv;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 5. Xóa nhân viên (Nếu có CASCADE trong DB thì xóa NV sẽ xóa luôn TK)
    public boolean deleteNhanVien(int maNV) {
        String sql = "DELETE FROM taikhoan WHERE MaTK = (SELECT MaTK FROM nhanvien WHERE MaNV = ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}