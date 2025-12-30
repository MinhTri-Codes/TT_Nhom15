package model;

public class ChiTietHoaDon {
    private String tenSP;
    private int soLuong;
    private double donGia;

    public ChiTietHoaDon(String tenSP, int soLuong, double donGia) {
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return soLuong * donGia;
    }

    // Getter
    public String getTenSP() { return tenSP; }
    public int getSoLuong() { return soLuong; }
    public double getDonGia() { return donGia; }
}