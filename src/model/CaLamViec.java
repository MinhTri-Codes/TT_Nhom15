package model;

import java.sql.Timestamp;

public class CaLamViec {
    private int maNV;
    private double tienMoCa;
    private Timestamp gioBD;

    public CaLamViec(int maNV, double tienMoCa) {
        this.maNV = maNV;
        this.tienMoCa = tienMoCa;
        this.gioBD = new Timestamp(System.currentTimeMillis());
    }

    public int getMaNV() { return maNV; }
    public double getTienMoCa() { return tienMoCa; }
    public Timestamp getGioBD() { return gioBD; }
}