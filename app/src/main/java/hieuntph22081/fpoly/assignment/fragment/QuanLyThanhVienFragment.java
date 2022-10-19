package hieuntph22081.fpoly.assignment.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.adapter.ThanhVienAdapter;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.ThanhVienDAO;
import hieuntph22081.fpoly.assignment.model.ThanhVien;
import hieuntph22081.fpoly.assignment.model.ThanhVien;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuanLyThanhVienFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuanLyThanhVienFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<ThanhVien> thanhViens;
    private ThanhVienDAO thanhVienDAO;
    private ThanhVienAdapter adapter;
    public QuanLyThanhVienFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuanLyThanhVienFragment newInstance() {
        QuanLyThanhVienFragment fragment = new QuanLyThanhVienFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quan_ly_thanh_vien, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewTV);
        fab = view.findViewById(R.id.fabTV);
        thanhVienDAO = MyDatabase.getInstance(getContext()).thanhVienDAO();

        loadData();
        
        fab.setOnClickListener(v -> openInsertTVDialog());
    }

    private void loadData() {
        adapter = new ThanhVienAdapter(getContext());
        thanhViens = thanhVienDAO.getAllThanhVien();
        adapter.setData(thanhViens);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void openInsertTVDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_thanh_vien);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        EditText txtTenTV = dialog.findViewById(R.id.txtThemTenTV);
        EditText txtNgaySinh = dialog.findViewById(R.id.txtThemNgaySinhTV);
        Button btnThem = dialog.findViewById(R.id.btnThemTV);
        Button btnReset = dialog.findViewById(R.id.btnResetTV);

        txtNgaySinh.setOnClickListener(v -> {
            // lay ngay hien tai(Đoán thế)
            final Calendar calendar = Calendar.getInstance();
            int ngay = calendar.get(Calendar.DATE);
            int thang = calendar.get(Calendar.MONTH);
            int nam = calendar.get(Calendar.YEAR);
            // Tao dialog chon ngay
            DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext(), (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                txtNgaySinh.setText(simpleDateFormat.format(calendar.getTime()));
            }, nam, thang, ngay);
            datePickerDialog.show();
        });

        btnThem.setOnClickListener(v -> {
            if (txtTenTV.getText().toString().trim().length() == 0
                    || txtNgaySinh.getText().toString().trim().length() ==0) {
                Toast.makeText(getContext(), "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!checkDate(txtNgaySinh.getText().toString().trim())) {
                Toast.makeText(getContext(), "Ngày phải đúng định dạnh dd/MM/yyyy !!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            ThanhVien thanhVien = new ThanhVien();
            thanhVien.setTenTV(txtTenTV.getText().toString().trim());
            thanhVien.setNgaySinh(txtNgaySinh.getText().toString().trim());
            thanhVienDAO.insertThanhVien(thanhVien);
            thanhViens = thanhVienDAO.getAllThanhVien();
            adapter.setData(thanhViens);
            dialog.dismiss();
        });
        btnReset.setOnClickListener(v -> {
            txtTenTV.setText("");
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public boolean checkDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}