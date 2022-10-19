package hieuntph22081.fpoly.assignment.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "phieumuon")
public class PhieuMuon {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int maPM;
    private int maTV;
    private int maSach;
    private String maTT;

    public PhieuMuon(int maPM, int maTV, int maSach, String maTT, String ngay, int traSach, double tienThue) {
        this.maPM = maPM;
        this.maTV = maTV;
        this.maSach = maSach;
        this.maTT = maTT;
        this.ngay = ngay;
        this.traSach = traSach;
        this.tienThue = tienThue;
    }

    private String ngay;
    private int traSach;
    private double tienThue;

    public PhieuMuon() {
    }

    public int getMaPM() {
        return maPM;
    }

    public void setMaPM(int maPM) {
        this.maPM = maPM;
    }

    public int getMaTV() {
        return maTV;
    }

    public void setMaTV(int maTV) {
        this.maTV = maTV;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public String getMaTT() {
        return maTT;
    }

    public void setMaTT(String maTT) {
        this.maTT = maTT;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getTraSach() {
        return traSach;
    }

    public void setTraSach(int traSach) {
        this.traSach = traSach;
    }

    public double getTienThue() {
        return tienThue;
    }

    public void setTienThue(double tienThue) {
        this.tienThue = tienThue;
    }
}
