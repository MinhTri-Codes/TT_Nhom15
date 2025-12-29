/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class TaiKhoan {
    private int maTK;
    private String role;
    private String tenDangNhap;
    private String matKhau;

    // 1. Constructor rỗng (Default Constructor)
    public TaiKhoan() {
    }

    // 2. Constructor đầy đủ tham số
    public TaiKhoan(int maTK, String role, String tenDangNhap, String matKhau) {
        this.maTK = maTK;
        this.role = role;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    // 3. Getters và Setters
    public int getMaTK() {
        return maTK;
    }

    public void setMaTK(int maTK) {
        this.maTK = maTK;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
