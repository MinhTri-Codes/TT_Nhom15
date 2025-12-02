/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * 
 */
public class NhanVien {
    private int maNV;
    private String tenNV;
    private String sdt;
    private Date ngaySinh;

    
    public NhanVien() {
    }


    public NhanVien(int maNV, String tenNV, String sdt, Date ngaySinh) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
    }

 
    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}   
