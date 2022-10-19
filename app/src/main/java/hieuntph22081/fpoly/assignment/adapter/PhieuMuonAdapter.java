package hieuntph22081.fpoly.assignment.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.PhieuMuonDAO;
import hieuntph22081.fpoly.assignment.database.SachDAO;
import hieuntph22081.fpoly.assignment.database.ThanhVienDAO;
import hieuntph22081.fpoly.assignment.model.PhieuMuon;
import hieuntph22081.fpoly.assignment.model.Sach;
import hieuntph22081.fpoly.assignment.model.ThanhVien;

public class PhieuMuonAdapter extends RecyclerView.Adapter<PhieuMuonAdapter.PhieuMuonViewHolder>{
    private Context context;
    private List<PhieuMuon> phieuMuons;
    private PhieuMuonDAO phieuMuonDAO;
    private SachDAO sachDAO;
    private ThanhVienDAO thanhVienDAO;

    public PhieuMuonAdapter(Context context) {
        this.context = context;
        phieuMuonDAO = MyDatabase.getInstance(context).phieuMuonDAO();
        sachDAO = MyDatabase.getInstance(context).sachDAO();
        thanhVienDAO = MyDatabase.getInstance(context).thanhVienDAO();
    }

    public void setData(List<PhieuMuon> phieuMuons) {
        this.phieuMuons = phieuMuons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhieuMuonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_phieu_muon, parent, false);
        return new PhieuMuonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhieuMuonViewHolder holder, int position) {
        PhieuMuon phieuMuon = phieuMuons.get(position);
        holder.tvPmTenTV.setText("Thành viên: " + thanhVienDAO.getTenTVByMaTV(phieuMuon.getMaTV()));
        holder.tvPmTenSach.setText("Sách: " + sachDAO.getTenSachByMaSach(phieuMuon.getMaSach()));
        if (phieuMuon.getTraSach() == 1) {
            holder.tvTraSach.setText("Đã trả sách");
            holder.tvTraSach.setTextColor(Color.BLUE);
        } else {
            holder.tvTraSach.setText("Chưa trả sách");
            holder.tvTraSach.setTextColor(Color.RED);
        }
        holder.tvPmTienThue.setText("Tiền thuê: " + phieuMuon.getTienThue() + "VNĐ");
        holder.tvPmNgayThue.setText("Ngày thuê: " + phieuMuon.getNgay());
        holder.imgDelete.setOnClickListener(v -> openDeleteDialog(phieuMuon));
        holder.itemView.setOnLongClickListener(v -> openEditDialog(phieuMuon));
    }

    private boolean openEditDialog(PhieuMuon phieuMuon) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_phieu_muon);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        EditText txtSuaMaPM = dialog.findViewById(R.id.txtSuaMaPM);
        txtSuaMaPM.setText(String.valueOf(phieuMuon.getMaPM()));

        // Do du lieu ra spinner
        Spinner spnSuaPmMaTV = dialog.findViewById(R.id.spnSuaPmMaTV);
        List<ThanhVien> tvList = thanhVienDAO.getAllThanhVien();
        List<String> tenTVList = new ArrayList<>();
        tenTVList.add("(Nhấp chọn)");
        for (ThanhVien tv : tvList) {
            tenTVList.add(tv.getTenTV());
        }
        spnSuaPmMaTV.setAdapter(new ArrayAdapter<String>(context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tenTVList));
        int indexSpnTv = 0;
        for (int i = 0; i< thanhVienDAO.getMaTV().length; i++) {
            if (thanhVienDAO.getMaTV()[i] == phieuMuon.getMaTV()) {
                indexSpnTv = i;
                break;
            }
        }
        spnSuaPmMaTV.setSelection(indexSpnTv + 1);

        // Do du lieu ra spinner
        Spinner spnSuaPmMaSach = dialog.findViewById(R.id.spnSuaPmMaSach);
        List<Sach> sachList = sachDAO.getAllSach();
        List<String> tensachList = new ArrayList<>();
        tensachList.add("(Nhấp chọn)");
        for (Sach sach : sachList) {
            tensachList.add(sach.getTenSach());
        }
        spnSuaPmMaSach.setAdapter(new ArrayAdapter<String>(context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tensachList));

        int indexSpnMs = 0;
        for (int i = 0; i<sachDAO.getMaSach().length; i++) {
            if (sachDAO.getMaSach()[i] == phieuMuon.getMaSach()) {
                indexSpnMs = i;
                break;
            }
        }
        spnSuaPmMaSach.setSelection(indexSpnMs + 1);

        EditText txtSuaPmNgayThue = dialog.findViewById(R.id.txtSuaPmNgayThue);
        txtSuaPmNgayThue.setText(phieuMuon.getNgay());
        EditText txtSuaPmTienThue = dialog.findViewById(R.id.txtSuaPmTienThue);
        txtSuaPmTienThue.setText(String.valueOf(phieuMuon.getTienThue()));

        spnSuaPmMaSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    txtSuaPmTienThue.setText(String.valueOf(sachList.get(position-1).getGiaThue()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CheckBox chkTraSach = dialog.findViewById(R.id.chkSuaTraSach);
        chkTraSach.setChecked(phieuMuon.getTraSach() == 1);
        Button btnSua = dialog.findViewById(R.id.btnSuaPM);
        Button btnHuy = dialog.findViewById(R.id.btnHuyPM);

        txtSuaPmNgayThue.setOnClickListener(v -> {
            // lay ngay hien tai(Đoán thế)
            final Calendar calendar = Calendar.getInstance();
            int ngay = calendar.get(Calendar.DATE);
            int thang = calendar.get(Calendar.MONTH);
            int nam = calendar.get(Calendar.YEAR);
            // Tao dialog chon ngay
            DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext(), (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                txtSuaPmNgayThue.setText(simpleDateFormat.format(calendar.getTime()));
            }, nam, thang, ngay);
            datePickerDialog.show();
        });

        btnSua.setOnClickListener(v -> {
            if (txtSuaMaPM.getText().toString().trim().length() == 0
                    || txtSuaPmTienThue.getText().toString().trim().length() == 0
                    || txtSuaPmNgayThue.getText().toString().trim().length() ==0) {
                Toast.makeText(context, "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }
            
            if (!checkDate(txtSuaPmNgayThue.getText().toString().trim())) {
                Toast.makeText(context, "Ngày phải đúng định dạnh dd/MM/yyyy !!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (spnSuaPmMaTV.getSelectedItemPosition() == 0){
                Toast.makeText(dialog.getContext(), "Hãy chọn thành viên", Toast.LENGTH_SHORT).show();
                return;
            } else {
                phieuMuon.setMaTV(tvList.get(spnSuaPmMaTV.getSelectedItemPosition()-1).getMaTV());
            }

            if (spnSuaPmMaSach.getSelectedItemPosition() == 0){
                Toast.makeText(dialog.getContext(), "Hãy chọn sách", Toast.LENGTH_SHORT).show();
                return;
            } else {
                phieuMuon.setMaSach(sachList.get(spnSuaPmMaSach.getSelectedItemPosition()-1).getMaSach());
            }

            phieuMuon.setNgay(txtSuaPmNgayThue.getText().toString().trim());
            try {
                double tien = Double.parseDouble(txtSuaPmTienThue.getText().toString().trim());
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

            phieuMuonDAO.updatePhieuMuon(phieuMuon);
            phieuMuons = phieuMuonDAO.getAllPhieuMuon();
            setData(phieuMuons);
            dialog.dismiss();
        });
        btnHuy.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.setCancelable(true);
        dialog.show();
        return false;
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

    private void openDeleteDialog(PhieuMuon phieuMuon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setNegativeButton("OK", (dialog, which) -> {
            phieuMuonDAO.deletePhieuMuon(phieuMuon);
            setData(phieuMuonDAO.getAllPhieuMuon());
        });
        builder.setPositiveButton("Hủy", (dialog, which) -> {

        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return phieuMuons.size();
    }

    public class PhieuMuonViewHolder extends RecyclerView.ViewHolder {
        TextView tvPmTenTV, tvPmTenSach, tvTraSach, tvPmTienThue, tvPmNgayThue;
        ImageView imgDelete;
        public PhieuMuonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPmTenTV = itemView.findViewById(R.id.tvPmTenTV);
            tvPmTenSach = itemView.findViewById(R.id.tvPmTenSach);
            tvTraSach = itemView.findViewById(R.id.tvTraSach);
            tvPmTienThue = itemView.findViewById(R.id.tvPmTienThue);
            tvPmNgayThue = itemView.findViewById(R.id.tvPmNgayThue);
            imgDelete = itemView.findViewById(R.id.imgXoaPm);
        }
    }
}
