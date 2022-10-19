package hieuntph22081.fpoly.assignment.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.PhieuMuonDAO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoanhThuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoanhThuFragment extends Fragment {
    EditText txtTuNgay, txtDenNgay;
    TextView tvDoanhThu;
    Button btnDoanhThu;
    PhieuMuonDAO phieuMuonDAO;
    public DoanhThuFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DoanhThuFragment newInstance() {
        DoanhThuFragment fragment = new DoanhThuFragment();
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
        return inflater.inflate(R.layout.fragment_doanh_thu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtTuNgay = view.findViewById(R.id.txtTuNgay);
        txtDenNgay = view.findViewById(R.id.txtdenNgay);
        tvDoanhThu = view.findViewById(R.id.tvDoanhThu);
        btnDoanhThu = view.findViewById(R.id.btnDoanhThu);
        phieuMuonDAO = MyDatabase.getInstance(getContext()).phieuMuonDAO();

        txtTuNgay.setOnClickListener(v -> {
            // lay ngay hien tai(Đoán thế)
            final Calendar calendar = Calendar.getInstance();
            int ngay = calendar.get(Calendar.DATE);
            int thang = calendar.get(Calendar.MONTH);
            int nam = calendar.get(Calendar.YEAR);
            // Tao dialog chon ngay
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                txtTuNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }, nam, thang, ngay);
            datePickerDialog.show();
        });

        txtDenNgay.setOnClickListener(v -> {
            // lay ngay hien tai(Đoán thế)
            final Calendar calendar = Calendar.getInstance();
            int ngay = calendar.get(Calendar.DATE);
            int thang = calendar.get(Calendar.MONTH);
            int nam = calendar.get(Calendar.YEAR);
            // Tao dialog chon ngay
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view1, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                txtDenNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }, nam, thang, ngay);
            datePickerDialog.show();
        });

        btnDoanhThu.setOnClickListener(v -> {
            if (txtTuNgay.getText().toString().length() == 0
            || txtDenNgay.getText().toString().length() == 0) {
                Toast.makeText(getContext(), "Không để trống các trường dữ liệu !!!", Toast.LENGTH_SHORT).show();
                return;
            }
            String tuNgay = txtTuNgay.getText().toString();
            String denNgay = txtDenNgay.getText().toString();
            double doanhThu = phieuMuonDAO.getDoanhThu(tuNgay,denNgay);
            tvDoanhThu.setText("Doanh thu: " + doanhThu + " VNĐ");
        });
    }

//    public String myDateFormater(String str) {
//        String strArr[] = str.split("/");
//        str = strArr[2]+ "/" + strArr[1] + "/" + strArr[0];
//        return str;
//    }
}