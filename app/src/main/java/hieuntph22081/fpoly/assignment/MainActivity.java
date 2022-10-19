package hieuntph22081.fpoly.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.ThuThuDAO;
import hieuntph22081.fpoly.assignment.fragment.DoanhThuFragment;
import hieuntph22081.fpoly.assignment.fragment.DoiMatKhauFragment;
import hieuntph22081.fpoly.assignment.fragment.QuanLyLoaiSachFragment;
import hieuntph22081.fpoly.assignment.fragment.QuanLyPhieuMuonFragment;
import hieuntph22081.fpoly.assignment.fragment.QuanLySachFragment;
import hieuntph22081.fpoly.assignment.fragment.QuanLyThanhVienFragment;
import hieuntph22081.fpoly.assignment.fragment.ThemNguoiDungFragment;
import hieuntph22081.fpoly.assignment.fragment.Top10SachFragment;
import hieuntph22081.fpoly.assignment.model.ThuThu;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private ThuThu currentUser;
    private ThuThuDAO thuThuDAO;
    private TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolBar);
        frameLayout = findViewById(R.id.frameLayout);
        thuThuDAO = MyDatabase.getInstance(this).thuThuDAO();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new QuanLyPhieuMuonFragment());
        this.setTitle(R.string.nav_qlPhieuMuon);
        navigationView.getMenu().getItem(0).setChecked(true);

        Intent intent = getIntent();
        String maTT = intent.getExtras().getBundle("bundle").getString("maTT");
        currentUser = thuThuDAO.getThuThuByMaTT(maTT);
        MenuItem themNguoiDungItem = navigationView.getMenu().findItem(R.id.themNguoiDung);

        if (!currentUser.getMaTT().equalsIgnoreCase("admin"))
            themNguoiDungItem.setEnabled(false);
        else
            themNguoiDungItem.setEnabled(true);

        tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        tvUserName.setText(currentUser.getHoTen());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.quanLyPhieuMuon) {
            replaceFragment(new QuanLyPhieuMuonFragment());
            this.setTitle(R.string.nav_qlPhieuMuon);
        } else if (id == R.id.quanLySach) {
            replaceFragment(new QuanLySachFragment());
            this.setTitle(R.string.nav_qlSach);
        } else if (id == R.id.quanLyLoaiSach) {
            replaceFragment(new QuanLyLoaiSachFragment());
            this.setTitle(R.string.nav_qlLoaiSach);
        } else if (id == R.id.quanLyThanhVien) {
            replaceFragment(new QuanLyThanhVienFragment());
            this.setTitle(R.string.nav_qlThanhVien);
        } else if (id == R.id.top10Sach) {
            replaceFragment(new Top10SachFragment());
            this.setTitle(R.string.nav_top10);
        } else if (id == R.id.doanhThu) {
            replaceFragment(new DoanhThuFragment());
            this.setTitle(R.string.nav_doanhThu);
        } else if (id == R.id.doiMatKhau) {
            Bundle bundle = new Bundle();
            bundle.putString("maTT", currentUser.getMaTT());
            DoiMatKhauFragment doiMatKhauFragment = new DoiMatKhauFragment();
            doiMatKhauFragment.setArguments(bundle);
            replaceFragment(doiMatKhauFragment);
            this.setTitle(R.string.nav_doiMatKhau);
        } else if (id == R.id.themNguoiDung) {
            replaceFragment(new ThemNguoiDungFragment());
            this.setTitle(R.string.nav_themNguoiDung);
        } else if (id == R.id.dangXuat) {
            savePreference(currentUser.getMaTT(),currentUser.getMatKhau(),true,true);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(navigationView);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
        } else {
            super.onBackPressed(); // Thoát
        }
    }

    void savePreference(String ma, String pw, boolean isLogout, boolean status) {
        SharedPreferences s = getSharedPreferences("MY_FILE",MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        if (!status) { // Khong luu
            editor.clear();
        } else { // luu
            editor.putString("U",ma);
            editor.putString("P",pw);
            editor.putBoolean("isLogout", isLogout);
            editor.putBoolean("CHK",status);
        }
        editor.commit();
    }
}