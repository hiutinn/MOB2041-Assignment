package hieuntph22081.fpoly.assignment.fragment;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.adapter.LoaiSachAdapter;
import hieuntph22081.fpoly.assignment.adapter.SachAdapter;
import hieuntph22081.fpoly.assignment.database.LoaiSachDAO;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.model.LoaiSach;
import hieuntph22081.fpoly.assignment.model.Sach;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuanLyLoaiSachFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuanLyLoaiSachFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<LoaiSach> loaiSachList;
    private LoaiSachAdapter adapter;
    private LoaiSachDAO loaiSachDAO;
    public QuanLyLoaiSachFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuanLyLoaiSachFragment newInstance() {
        QuanLyLoaiSachFragment fragment = new QuanLyLoaiSachFragment();
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
        return inflater.inflate(R.layout.fragment_quan_ly_loai_sach, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewLoaiSach);
        fab = view.findViewById(R.id.fabLoaiSach);
        loaiSachDAO = MyDatabase.getInstance(getContext()).loaiSachDAO();
        loadData();
        fab.setOnClickListener(v -> openInsertLoaiSachDialog());
    }

    private void loadData() {
        loaiSachList = loaiSachDAO.getAllLoaiSach();
        adapter = new LoaiSachAdapter(getContext(),loaiSachList);
        adapter.setData(loaiSachList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void openInsertLoaiSachDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_loai_sach);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        EditText txtTenLoai = dialog.findViewById(R.id.txtThemTenLoaiSach);
        Button btnThem = dialog.findViewById(R.id.btnThemLoaiSach);
        Button btnReset = dialog.findViewById(R.id.btnResetLoaiSach);

        btnThem.setOnClickListener(v -> {
            if (txtTenLoai.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }
            LoaiSach loaiSach = new LoaiSach();
            loaiSach.setTenLoai(txtTenLoai.getText().toString().trim());
            loaiSachDAO.insertLoaiSach(loaiSach);
            loaiSachList = loaiSachDAO.getAllLoaiSach();
            adapter.setData(loaiSachList);
            dialog.dismiss();
        });
        btnReset.setOnClickListener(v -> {
            txtTenLoai.setText("");
        });
        dialog.setCancelable(true);
        dialog.show();
    }
}