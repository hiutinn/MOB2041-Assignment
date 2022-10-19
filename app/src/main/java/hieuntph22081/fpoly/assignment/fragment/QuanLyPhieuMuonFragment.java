package hieuntph22081.fpoly.assignment.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.adapter.PhieuMuonAdapter;
import hieuntph22081.fpoly.assignment.adapter.PhieuMuonAdapter;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.PhieuMuonDAO;
import hieuntph22081.fpoly.assignment.database.SachDAO;
import hieuntph22081.fpoly.assignment.database.ThanhVienDAO;
import hieuntph22081.fpoly.assignment.model.LoaiSach;
import hieuntph22081.fpoly.assignment.model.PhieuMuon;
import hieuntph22081.fpoly.assignment.model.PhieuMuon;
import hieuntph22081.fpoly.assignment.model.Sach;
import hieuntph22081.fpoly.assignment.model.ThanhVien;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuanLyPhieuMuonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuanLyPhieuMuonFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private List<PhieuMuon> phieuMuons;
    private PhieuMuonDAO phieuMuonDAO;
    private PhieuMuonAdapter adapter;
    private ThanhVienDAO thanhVienDAO;
    private SachDAO sachDAO;
    public QuanLyPhieuMuonFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuanLyPhieuMuonFragment newInstance() {
        QuanLyPhieuMuonFragment fragment = new QuanLyPhieuMuonFragment();
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
        return inflater.inflate(R.layout.fragment_quan_ly_phieu_muon, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewPm);
        fab = view.findViewById(R.id.fabPm);
        phieuMuonDAO = MyDatabase.getInstance(getContext()).phieuMuonDAO();
        thanhVienDAO = MyDatabase.getInstance(getContext()).thanhVienDAO();
        sachDAO = MyDatabase.getInstance(getContext()).sachDAO();
        loadData();

        fab.setOnClickListener(v -> openInsertTVDialog());
    }

    private void openInsertTVDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_phieu_muon);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // Do du lieu ra spinner
        Spinner spnThemPmMaTV = dialog.findViewById(R.id.spnThemPmMaTV);
        List<ThanhVien> tvList = thanhVienDAO.getAllThanhVien();
        List<String> tenTVList = new ArrayList<>();
        tenTVList.add("(Nhấp chọn)");
        for (ThanhVien tv : tvList) {
            tenTVList.add(tv.getTenTV());
        }
        spnThemPmMaTV.setAdapter(new ArrayAdapter<>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tenTVList));
        // Do du lieu ra spinner
        Spinner spnThemPmMaSach = dialog.findViewById(R.id.spnThemPmMaSach);
        List<Sach> sachList = sachDAO.getAllSach();
        List<String> tensachList = new ArrayList<>();
        tensachList.add("(Nhấp chọn)");
        for (Sach sach : sachList) {
            tensachList.add(sach.getTenSach());
        }
        spnThemPmMaSach.setAdapter(new ArrayAdapter<>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tensachList));


        EditText txtThemPmTienThue = dialog.findViewById(R.id.txtThemPmTienThue);
        spnThemPmMaSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    txtThemPmTienThue.setText(String.valueOf(sachList.get(position-1).getGiaThue()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        EditText txtThemPmNgayThue = dialog.findViewById(R.id.txtThemPmNgayThue);

        CheckBox chkTraSach = dialog.findViewById(R.id.chkTraSach);
        Button btnThem = dialog.findViewById(R.id.btnThemPM);
        Button btnReset = dialog.findViewById(R.id.btnResetPM);


        txtThemPmNgayThue.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

        btnThem.setOnClickListener(v -> {
            if (txtThemPmTienThue.getText().toString().trim().length() == 0
                    || txtThemPmNgayThue.getText().toString().trim().length() ==0) {
                Toast.makeText(getContext(), "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkDate(txtThemPmNgayThue.getText().toString().trim())) {
                Toast.makeText(getContext(), "Ngày phải đúng định dạnh dd/MM/yyyy !!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            PhieuMuon phieuMuon = new PhieuMuon();

            if (spnThemPmMaTV.getSelectedItemPosition() == 0){
                Toast.makeText(dialog.getContext(), "Hãy chọn thành viên", Toast.LENGTH_SHORT).show();
                return;
            } else {
                phieuMuon.setMaTV(tvList.get(spnThemPmMaTV.getSelectedItemPosition()-1).getMaTV());
            }

            if (spnThemPmMaSach.getSelectedItemPosition() == 0){
                Toast.makeText(dialog.getContext(), "Hãy chọn sách", Toast.LENGTH_SHORT).show();
                return;
            } else {
                phieuMuon.setMaSach(sachList.get(spnThemPmMaSach.getSelectedItemPosition()-1).getMaSach());
            }

            phieuMuon.setNgay(txtThemPmNgayThue.getText().toString().trim());
            try {
                double tien = Double.parseDouble(txtThemPmTienThue.getText().toString().trim());
                if (tien < 0) {
                    Toast.makeText(dialog.getContext(), "Tiền phải >= 0 !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                phieuMuon.setTienThue(tien);
            } catch (Exception e) {
                Toast.makeText(dialog.getContext(), "Tiền phải là số !!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (chkTraSach.isChecked())
                phieuMuon.setTraSach(1);
            else
                phieuMuon.setTraSach(0);

            phieuMuonDAO.insertPhieuMuon(phieuMuon);
            Toast.makeText(getContext(), "Thêm phiếu mượn thành công !!!", Toast.LENGTH_SHORT).show();
            phieuMuons = phieuMuonDAO.getAllPhieuMuon();
            adapter.setData(phieuMuons);
            dialog.dismiss();
        });
        btnReset.setOnClickListener(v -> {
            txtThemPmNgayThue.setText("");
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    public boolean checkDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void loadData() {
        adapter = new PhieuMuonAdapter(getContext());
        phieuMuons = phieuMuonDAO.getAllPhieuMuon();
        adapter.setData(phieuMuons);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}