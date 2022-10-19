package hieuntph22081.fpoly.assignment.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hieuntph22081.fpoly.assignment.model.ThuThu;

@Dao
public interface ThuThuDAO {
    @Query("SELECT * FROM thuthu")
    List<ThuThu> getAllThuThu();

    @Insert
    void insertThuThu(ThuThu thuthu);

    @Update
    void updateThuThu(ThuThu thuthu);

    @Delete
    void deleteThuThu(ThuThu thuthu);

    @Query("SELECT * FROM thuthu WHERE maTT = (:maTT)")
    ThuThu getThuThuByMaTT(String maTT);
}
