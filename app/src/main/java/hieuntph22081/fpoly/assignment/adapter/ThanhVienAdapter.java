package hieuntph22081.fpoly.assignment.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.PhieuMuonDAO;
import hieuntph22081.fpoly.assignment.database.ThanhVienDAO;
import hieuntph22081.fpoly.assignment.model.PhieuMuon;
import hieuntph22081.fpoly.assignment.model.ThanhVien;

public class ThanhVienAdapter extends RecyclerView.Adapter<ThanhVienAdapter.ThanhVienViewHolder>{
    private Context context;
    private List<ThanhVien> thanhViens;
    private ThanhVienDAO thanhVienDAO;
    private PhieuMuonDAO phieuMuonDAO;

    public ThanhVienAdapter(Context context) {
        this.context = context;
        thanhVienDAO = MyDatabase.getInstance(context).thanhVienDAO();
        phieuMuonDAO = MyDatabase.instance.phieuMuonDAO();
    }

    public void setData(List<ThanhVien> thanhViens) {
        this.thanhViens = thanhViens;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThanhVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_thanh_vien,parent,false);
        return new ThanhVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThanhVienViewHolder holder, int position) {
        ThanhVien thanhVien = thanhViens.get(position);
        holder.tvTenTv.setText(thanhVien.getTenTV());
        holder.tvNgaySinh.setText(thanhVien.getNgaySinh());
        holder.imgXoaTV.setOnClickListener(v -> openDeleteDialog(thanhVien));
        holder.itemView.setOnLongClickListener(v -> openEditDialog(thanhVien));
    }

    private boolean openEditDialog(ThanhVien thanhVien) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_thanh_vien);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        EditText txtMaTV = dialog.findViewById(R.id.txtSuaMaTV);
        EditText txtTenTV = dialog.findViewById(R.id.txtSuaTenTV);
        EditText txtNgaySinh = dialog.findViewById(R.id.txtSuaNgaySinhTV);
        Button btnSua = dialog.findViewById(R.id.btnSuaTV);
        Button btnHuy = dialog.findViewById(R.id.btnHuyTV);

        txtMaTV.setText(String.valueOf(thanhVien.getMaTV()));
        txtTenTV.setText(thanhVien.getTenTV());
        txtNgaySinh.setText(thanhVien.getNgaySinh());

        txtNgaySinh.setOnClickListener(v -> {
            // lay ngay hien tai(Đoán thế)
            final Calendar calendar = Calendar.getInstance();
            int ngay = calendar.get(Calendar.DATE);
            int thang = calendar.get(Calendar.MONTH);
            int nam = calendar.get(Calendar.YEAR);
            // Tao dialog chon ngay
            DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext(), (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                txtNgaySinh.setText(simpleDateFormat.format(calendar.getTime()));
            }, nam, thang, ngay);
            datePickerDialog.show();
        });

        btnSua.setOnClickListener(v -> {
            if (txtMaTV.getText().toString().trim().length() == 0
                    || txtTenTV.getText().toString().trim().length() == 0
                    || txtNgaySinh.getText().toString().trim().length() ==0) {
                Toast.makeText(context, "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!checkDate(txtNgaySinh.getText().toString().trim())) {
                Toast.makeText(context, "Ngày phải đúng định dạnh dd/MM/yyyy !!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            thanhVien.setTenTV(txtTenTV.getText().toString().trim());
            thanhVien.setNgaySinh(txtNgaySinh.getText().toString().trim());
            thanhVienDAO.updateThanhVien(thanhVien);
            thanhViens = thanhVienDAO.getAllThanhVien();
            setData(thanhViens);
            Toast.makeText(context, "Sửa thành công !!!", Toast.LENGTH_SHORT).show();
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
    
    private void openDeleteDialog(ThanhVien thanhVien) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (PhieuMuon pm : phieuMuonDAO.getAllPhieuMuon()) {
                    if (pm.getMaTV() == (thanhVien.getMaTV())) {
                        Toast.makeText(context, "Không thể xóa thành viên này!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                thanhVienDAO.deleteThanhVien(thanhVien);
                Toast.makeText(context, "Đã xóa thành viên", Toast.LENGTH_SHORT).show();
                setData(thanhVienDAO.getAllThanhVien());
            }
        });
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return thanhViens.size();
    }

    public class ThanhVienViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenTv, tvNgaySinh;
        ImageView imgXoaTV;
        public ThanhVienViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTv = itemView.findViewById(R.id.tvTenTV);
            tvNgaySinh = itemView.findViewById(R.id.tvNgaySinh);
            imgXoaTV = itemView.findViewById(R.id.imgXoaTV);
        }
    }
}
