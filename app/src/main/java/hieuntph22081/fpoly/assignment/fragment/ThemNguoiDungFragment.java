package hieuntph22081.fpoly.assignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.ThuThuDAO;
import hieuntph22081.fpoly.assignment.model.ThuThu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThemNguoiDungFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThemNguoiDungFragment extends Fragment {
    private TextInputEditText txtMaTT, txtHoTen, txtMatKhau, txtNhapLaiMK;
    private Button btnThemNguoiDung;
    private ThuThuDAO thuThuDAO;
    public ThemNguoiDungFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ThemNguoiDungFragment newInstance() {
        ThemNguoiDungFragment fragment = new ThemNguoiDungFragment();
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
        return inflater.inflate(R.layout.fragment_them_nguoi_dung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtMaTT = view.findViewById(R.id.themNguoiDungTxtMaTT);
        txtHoTen = view.findViewById(R.id.themNguoiDungTxtHoTen);
        txtMatKhau = view.findViewById(R.id.themNguoiDungTxtMatKhau);
        txtNhapLaiMK = view.findViewById(R.id.themNguoiDungTxtNhapLaiMK);
        btnThemNguoiDung = view.findViewById(R.id.btnThemNguoiDung);
        thuThuDAO = MyDatabase.getInstance(getContext()).thuThuDAO();



        btnThemNguoiDung.setOnClickListener(v -> {
            if (txtMaTT.getText().toString().trim().length() == 0
                    || txtHoTen.getText().toString().trim().length() == 0
                    || txtMatKhau.getText().toString().trim().length() == 0
                    || txtNhapLaiMK.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), "Không để trống dữ liệu !!!", Toast.LENGTH_SHORT).show();
                return;
            }

            for (ThuThu thuThu : thuThuDAO.getAllThuThu()) {
                if (txtMaTT.getText().toString().trim().equalsIgnoreCase(thuThu.getMaTT())) {
                    Toast.makeText(getContext(), "Mã đã tồn tại!!!", Toast.LENGTH_SHORT).show();
                    txtMaTT.requestFocus();
                    return;
                }
            }

            if (!txtNhapLaiMK.getText().toString().trim().equals(txtMatKhau.getText().toString().trim())) {
                Toast.makeText(getContext(), "Mật khẩu không trùng khớp !!!", Toast.LENGTH_SHORT).show();
                txtNhapLaiMK.requestFocus();
                return;
            }

            ThuThu thuThu = new ThuThu();
            thuThu.setMaTT(txtMaTT.getText().toString().trim());
            thuThu.setHoTen(txtHoTen.getText().toString().trim());
            thuThu.setMatKhau(txtMatKhau.getText().toString().trim());
            thuThuDAO.insertThuThu(thuThu);
            Toast.makeText(getContext(), "Thêm người dùng thành công!!!", Toast.LENGTH_SHORT).show();
        });

    }
}