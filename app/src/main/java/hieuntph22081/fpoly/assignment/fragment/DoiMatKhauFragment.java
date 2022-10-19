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
 * Use the {@link DoiMatKhauFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoiMatKhauFragment extends Fragment {
    private TextInputEditText txtMatKhauCu, txtMatKhauMoi, txtNhapLaiMK;
    private Button btnDoiMatKhau;
    private ThuThuDAO thuThuDAO;
    private ThuThu currentUser;
    public DoiMatKhauFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DoiMatKhauFragment newInstance() {
        DoiMatKhauFragment fragment = new DoiMatKhauFragment();
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
        return inflater.inflate(R.layout.fragment_doi_mat_khau, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtMatKhauCu = view.findViewById(R.id.doiMatKhauTxtMatKhauCu);
        txtMatKhauMoi = view.findViewById(R.id.doiMatKhauTxtMatKhauMoi);
        txtNhapLaiMK = view.findViewById(R.id.doiMatKhauTxtNhapLaiMK);
        btnDoiMatKhau = view.findViewById(R.id.btnDoiMatKhau);
        thuThuDAO = MyDatabase.getInstance(getContext()).thuThuDAO();
        currentUser = thuThuDAO.getThuThuByMaTT(getArguments().getString("maTT"));

        btnDoiMatKhau.setOnClickListener(v -> {
            if (!txtMatKhauCu.getText().toString().equals(currentUser.getMatKhau())) {
                Toast.makeText(getContext(), "Mật khẩu cũ không đúng !!!", Toast.LENGTH_SHORT).show();
                txtMatKhauCu.requestFocus();
                return;
            }

            if (txtMatKhauCu.getText().toString().length() == 0
            || txtMatKhauMoi.getText().toString().length() == 0
            || txtNhapLaiMK.getText().toString().length() == 0) {
                Toast.makeText(getContext(), "Không để trống dữ liệu!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!txtNhapLaiMK.getText().toString().trim().equals(txtMatKhauMoi.getText().toString().trim())) {
                Toast.makeText(getContext(), "Mật khẩu không trùng khớp !!!", Toast.LENGTH_SHORT).show();
                txtNhapLaiMK.requestFocus();
                return;
            }

            currentUser.setMatKhau(txtMatKhauMoi.getText().toString());
            Toast.makeText(getContext(), "Đổi mật khẩu thành công!!!", Toast.LENGTH_SHORT).show();
        });
    }
}