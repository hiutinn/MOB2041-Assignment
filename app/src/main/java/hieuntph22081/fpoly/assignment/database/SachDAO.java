package hieuntph22081.fpoly.assignment.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hieuntph22081.fpoly.assignment.model.Sach;

@Dao
public interface SachDAO {
    @Query("SELECT * FROM sach")
    List<Sach> getAllSach();

    @Insert
    void insertSach(Sach sach);

    @Update
    void updateSach(Sach sach);

    @Delete
    void deleteSach(Sach sach);

    @Query("SELECT * FROM sach WHERE maSach = :maSach")
    Sach getSachByMaSach(int maSach);

    @Query("SELECT tenSach FROM sach WHERE maSach = :maSach")
    String getTenSachByMaSach(int maSach);

    @Query("SELECT maSach FROM sach")
    int[] getMaSach();
}
