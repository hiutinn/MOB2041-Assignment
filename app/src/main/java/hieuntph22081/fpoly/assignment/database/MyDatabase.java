package hieuntph22081.fpoly.assignment.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import hieuntph22081.fpoly.assignment.model.LoaiSach;
import hieuntph22081.fpoly.assignment.model.PhieuMuon;
import hieuntph22081.fpoly.assignment.model.Sach;
import hieuntph22081.fpoly.assignment.model.ThanhVien;
import hieuntph22081.fpoly.assignment.model.ThuThu;

@Database(entities = {Sach.class, LoaiSach.class, ThuThu.class, ThanhVien.class, PhieuMuon.class}, version = 1)
//room.schemaLocation
public abstract class MyDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "ASM_MOD2041.db";
    public static MyDatabase instance;
    public static synchronized MyDatabase getInstance(Context context) {
        if (instance == null) {
            RoomDatabase.Callback rdc = new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    db.execSQL("INSERT INTO thuthu VALUES ('admin','Nguyen Tien Hieu','admin')");
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

            instance = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, DATABASE_NAME)
                    .addCallback(rdc)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract SachDAO sachDAO();
    public abstract LoaiSachDAO loaiSachDAO();
    public abstract ThuThuDAO thuThuDAO();
    public abstract ThanhVienDAO thanhVienDAO();
    public abstract PhieuMuonDAO phieuMuonDAO();
}
