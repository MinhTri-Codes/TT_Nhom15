package model;

public class SanPham {
    private int maSP;
    private String tenSP;
    private double donGia;
    private int maDM;
    private String tenDM; // Dùng để hiển thị tên danh mục lên bảng
    private String imgUrl;

    public SanPham() {}

    public SanPham(int maSP, String tenSP, double donGia, int maDM, String imgUrl) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.donGia = donGia;
        this.maDM = maDM;
        this.imgUrl = imgUrl;
    }

    // Getters và Setters...
    public int getMaSP() { return maSP; }
    public void setMaSP(int maSP) { this.maSP = maSP; }
    public String getTenSP() { return tenSP; }
    public void setTenSP(String tenSP) { this.tenSP = tenSP; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public int getMaDM() { return maDM; }
    public void setMaDM(int maDM) { this.maDM = maDM; }
    public String getTenDM() { return tenDM; }
    public void setTenDM(String tenDM) { this.tenDM = tenDM; }
    public String getImgUrl() { return imgUrl; }
    public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }
}