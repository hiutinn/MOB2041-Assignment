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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.adapter.SachAdapter;
import hieuntph22081.fpoly.assignment.database.LoaiSachDAO;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.SachDAO;
import hieuntph22081.fpoly.assignment.model.LoaiSach;
import hieuntph22081.fpoly.assignment.model.Sach;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuanLySachFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuanLySachFragment extends Fragment {
    private SachAdapter adapter;
    private List<Sach> sachList;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SachDAO sachDAO;
    private LoaiSachDAO loaiSachDAO;
    public QuanLySachFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuanLySachFragment newInstance() {
        QuanLySachFragment fragment = new QuanLySachFragment();
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
        return inflater.inflate(R.layout.fragment_quan_ly_sach, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewSach);
        fab = view.findViewById(R.id.fabSach);
        sachDAO = MyDatabase.getInstance(getContext()).sachDAO();
        loaiSachDAO = MyDatabase.getInstance(getContext()).loaiSachDAO();
        loadData();
        fab.setOnClickListener(v -> openInsertSachDialog());
    }

    private void openInsertSachDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_sach);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        EditText txtTenSach = dialog.findViewById(R.id.txtThemTenSach);
        EditText txtTienThue = dialog.findViewById(R.id.txtThemTienSach);
        Spinner spnLoaiSach = dialog.findViewById(R.id.spnThemLoaiSach);
        Button btnThem = dialog.findViewById(R.id.btnThemSach);
        Button btnReset = dialog.findViewById(R.id.btnResetSach);

        List<LoaiSach> loaiSachList = loaiSachDAO.getAllLoaiSach();
        List<String> tenLoaiList = new ArrayList<>();
        for (LoaiSach loaiSach : loaiSachList) {
            tenLoaiList.add(loaiSach.getTenLoai());
        }
        spnLoaiSach.setAdapter(new ArrayAdapter<String>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tenLoaiList));


        btnThem.setOnClickListener(v -> {
            if (txtTienThue.getText().toString().trim().length() == 0
            || txtTenSach.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }

            Sach sach = new Sach();
            sach.setTenSach(txtTenSach.getText().toString().trim());
            try {
                if (Double.parseDouble(txtTienThue.getText().toString().trim()) < 0) {
                    Toast.makeText(getContext(), "Tiền thuê phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                sach.setGiaThue(Double.parseDouble(txtTienThue.getText().toString().trim()));
            }catch (Exception e) {
                Toast.makeText(getContext(), "Tiền thuê phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }
            sach.setMaLoaiSach(loaiSachList.get(spnLoaiSach.getSelectedItemPosition()).getMaLoai());
            sachDAO.insertSach(sach);
            loadData();
            dialog.dismiss();
        });
        btnReset.setOnClickListener(v -> {
            txtTenSach.setText("");
            txtTienThue.setText("");

        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public void loadData() {
        sachList = sachDAO.getAllSach();
        adapter = new SachAdapter(getContext());
        adapter.setData(sachList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


}