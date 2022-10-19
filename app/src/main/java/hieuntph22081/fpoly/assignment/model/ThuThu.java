package hieuntph22081.fpoly.assignment.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "thuthu")
public class ThuThu implements Serializable {
    @PrimaryKey
    @NonNull
    private String maTT;
    private String hoTen;
    private String matKhau;

    public ThuThu() {
    }

    public ThuThu(String maTT, String hoTen, String matKhau) {
        this.maTT = maTT;
        this.hoTen = hoTen;
        this.matKhau = matKhau;
    }

    public String getMaTT() {
        return maTT;
    }

    public void setMaTT(String maTT) {
        this.maTT = maTT;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
