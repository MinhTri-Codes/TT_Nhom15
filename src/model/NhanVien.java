
package model;

import java.util.Date;

public class NhanVien {
    private int maNV;
    private String tenNV;
    private String sdt;
    private Date ngaySinh;
    private int maTK;    
    private String role; 
    
    // THÊM 2 TRƯỜNG NÀY VÀO ĐỂ HẾT LỖI "CANNOT FIND SYMBOL"
    private String tenDangNhap;
    private String matKhau;

    public NhanVien() {
    }

    // Cập nhật Constructor đầy đủ để nhận thêm 2 trường mới
    public NhanVien(int maNV, String tenNV, String sdt, Date ngaySinh, String role, String tenDangNhap, String matKhau) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
        this.role = role;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    // --- THÊM GETTER/SETTER CHO 2 TRƯỜNG MỚI ---
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    // (Các Getter/Setter cũ giữ nguyên...)
    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }
    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
