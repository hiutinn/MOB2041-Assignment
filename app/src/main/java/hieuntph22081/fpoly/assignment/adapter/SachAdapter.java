package hieuntph22081.fpoly.assignment.adapter;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.database.LoaiSachDAO;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.SachDAO;
import hieuntph22081.fpoly.assignment.model.LoaiSach;
import hieuntph22081.fpoly.assignment.model.Sach;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.SachViewHolder>{
    private Context context;
    private List<Sach> sachList;
    private SachDAO sachDAO;
    private LoaiSachDAO loaiSachDAO;

    public SachAdapter(Context context) {
        this.context = context;
        this.sachDAO = MyDatabase.getInstance(context).sachDAO();
        this.loaiSachDAO = MyDatabase.getInstance(context).loaiSachDAO();
    }

    public void setData(List<Sach> sachList) {
        this.sachList = sachList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_sach,parent,false);
        return new SachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SachViewHolder holder, int position) {
        Sach sach = sachList.get(position);
        holder.tvTenSach.setText("Tên sách: " + sach.getTenSach());
        holder.tvTienThue.setText("Tiền thuê: " + sach.getGiaThue());
        holder.tvLoaiSach.setText("Loại sách: " + loaiSachDAO.getTenLoaiByMaLoai(sach.getMaLoaiSach()));
        holder.imgXoaSach.setOnClickListener(v -> openDeleteDialog(sach));
        holder.itemView.setOnLongClickListener(v -> openEditDialog(sach));
    }

    private boolean openEditDialog(Sach sach) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_sach);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        EditText txtMaSach = dialog.findViewById(R.id.txtSuaMaSach);
        txtMaSach.setText(String.valueOf(sach.getMaSach()));
        EditText txtTenSach = dialog.findViewById(R.id.txtSuaTenSach);
        txtTenSach.setText(sach.getTenSach());
        EditText txtTienThue = dialog.findViewById(R.id.txtSuaTienSach);
        txtTienThue.setText(String.valueOf(sach.getGiaThue()));


        Spinner spnLoaiSach = dialog.findViewById(R.id.spnSuaLoaiSach);
        List<LoaiSach> loaiSachList = loaiSachDAO.getAllLoaiSach();
        List<String> tenLoaiList = new ArrayList<>();
        for (LoaiSach loaiSach : loaiSachList) {
            tenLoaiList.add(loaiSach.getTenLoai());
        }
        spnLoaiSach.setAdapter(new ArrayAdapter<String>(context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, tenLoaiList));

        int indexSpn = 0;
        for (int i = 0; i < loaiSachDAO.getMaLoai().length; i++) {
            if (sachDAO.getMaSach()[i] == sach.getMaLoaiSach()) {
                indexSpn = i;
                break;
            }
        }
        spnLoaiSach.setSelection(indexSpn);

        Button btnSua = dialog.findViewById(R.id.btnSuaSach);
        Button btnHuy = dialog.findViewById(R.id.btnHuySach);

        btnSua.setOnClickListener(v -> {
            if (txtMaSach.getText().toString().trim().length() == 0
                    || txtTienThue.getText().toString().trim().length() == 0
                    || txtTenSach.getText().toString().trim().length() == 0) {
                Toast.makeText(context, "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }

            sach.setTenSach(txtTenSach.getText().toString().trim());
            try {
                if (Double.parseDouble(txtTienThue.getText().toString().trim()) < 0) {
                    Toast.makeText(context, "Tiền thuê phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                sach.setGiaThue(Double.parseDouble(txtTienThue.getText().toString().trim()));
            }catch (Exception e) {
                Toast.makeText(context, "Tiền thuê phải là số!", Toast.LENGTH_SHORT).show();
                return;
            }
            sach.setMaLoaiSach(loaiSachList.get(spnLoaiSach.getSelectedItemPosition()).getMaLoai());
            sachDAO.updateSach(sach);
            setData(sachDAO.getAllSach());
            dialog.dismiss();
        });
        btnHuy.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.setCancelable(true);
        dialog.show();
        return false;
    }

    private void openDeleteDialog(Sach sach) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sachDAO.deleteSach(sach);
                setData(sachDAO.getAllSach());
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
        return sachList.size();
    }

    public class SachViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSach, tvTienThue, tvLoaiSach;
        ImageView imgXoaSach;
        public SachViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoaiSach = itemView.findViewById(R.id.tvLoaiSach);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvTienThue = itemView.findViewById(R.id.tvTienThue);
            imgXoaSach = itemView.findViewById(R.id.imgXoaSach);
        }
    }
}
