package hieuntph22081.fpoly.assignment.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import hieuntph22081.fpoly.assignment.model.PhieuMuon;

@Dao
public interface PhieuMuonDAO {
    @Query("SELECT * FROM PhieuMuon")
    List<PhieuMuon> getAllPhieuMuon();

    @Insert
    void insertPhieuMuon(PhieuMuon phieuMuon);

    @Update
    void updatePhieuMuon(PhieuMuon phieuMuon);

    @Delete
    void deletePhieuMuon(PhieuMuon phieuMuon);

//    @Query("SELECT tenTV FROM phieumuon INNER JOIN thanhvien ON maTV = maTV WHERE maPM = (:maPM)")
//    String getTenTV(String maPM);
//
//    @Query("SELECT tenSach FROM phieumuon INNER JOIN sach ON maSach = maSach WHERE maPM = (:maPM)")
//    String getTenSach(String maPM);

    @Query("SELECT SUM(tienThue) FROM phieumuon WHERE ngay BETWEEN :tuNgay AND :denNgay")
    double getDoanhThu(String tuNgay, String denNgay);

    @Query("SELECT maSach FROM phieumuon GROUP BY maSach ORDER BY COUNT(maSach) DESC LIMIT 10")
    int[] getTop10();
}
