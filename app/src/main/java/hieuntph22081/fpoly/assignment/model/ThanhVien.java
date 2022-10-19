package hieuntph22081.fpoly.assignment.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "thanhvien")
public class ThanhVien {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int maTV;
    private String tenTV;
    private String ngaySinh;

    public ThanhVien() {
    }

    public ThanhVien(int maTV, String tenTV, String ngaySinh) {
        this.maTV = maTV;
        this.tenTV = tenTV;
        this.ngaySinh = ngaySinh;
    }

    public int getMaTV() {
        return maTV;
    }

    public void setMaTV(int maTV) {
        this.maTV = maTV;
    }

    public String getTenTV() {
        return tenTV;
    }

    public void setTenTV(String tenTV) {
        this.tenTV = tenTV;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
