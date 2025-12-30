package model;

public class DanhMuc {
    private int maDM;
    private String tenDanhMuc;

    public DanhMuc(int maDM, String tenDanhMuc) {
        this.maDM = maDM;
        this.tenDanhMuc = tenDanhMuc;
    }

    public int getMaDM() { return maDM; }
    public String getTenDanhMuc() { return tenDanhMuc; }

    // Rất quan trọng: Ghi đè toString để ComboBox hiển thị tên
    @Override
    public String toString() {
        return tenDanhMuc;
    }
}