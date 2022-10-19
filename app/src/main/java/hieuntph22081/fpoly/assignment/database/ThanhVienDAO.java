package hieuntph22081.fpoly.assignment.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hieuntph22081.fpoly.assignment.model.Sach;
import hieuntph22081.fpoly.assignment.model.ThanhVien;

@Dao
public interface ThanhVienDAO {
    @Query("SELECT * FROM ThanhVien")
    List<ThanhVien> getAllThanhVien();

    @Insert
    void insertThanhVien(ThanhVien ThanhVien);

    @Update
    void updateThanhVien(ThanhVien ThanhVien);

    @Delete
    void deleteThanhVien(ThanhVien ThanhVien);

    @Query("SELECT tenTV FROM ThanhVien WHERE maTV = (:maTV)")
    String getTenTVByMaTV(int maTV);

    @Query("SELECT maTV FROM thanhvien")
    int[] getMaTV();
}
