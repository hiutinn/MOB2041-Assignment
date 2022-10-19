package hieuntph22081.fpoly.assignment.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hieuntph22081.fpoly.assignment.model.LoaiSach;

@Dao
public interface LoaiSachDAO {
    @Query("SELECT * FROM loaisach")
    List<LoaiSach> getAllLoaiSach();

    @Insert
    void insertLoaiSach(LoaiSach loaiSach);

    @Update
    void updateLoaiSach(LoaiSach loaiSach);

    @Delete
    void deleteLoaiSach(LoaiSach loaiSach);

    @Query("SELECT maLoai FROM loaisach")
    int[] getMaLoai();

    @Query("Select tenLoai from loaisach where maLoai = :maLoai")
    String getTenLoaiByMaLoai(int maLoai);
}
