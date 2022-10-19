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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.database.LoaiSachDAO;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.model.LoaiSach;
import hieuntph22081.fpoly.assignment.model.Sach;

public class LoaiSachAdapter extends RecyclerView.Adapter<LoaiSachAdapter.LoaiSachViewHolder>{
    private Context context;
    private List<LoaiSach> loaiSachList;
    private LoaiSachDAO loaiSachDAO;

    public LoaiSachAdapter(Context context, List<LoaiSach> loaiSachList) {
        this.context = context;
        this.loaiSachList = loaiSachList;
        loaiSachDAO = MyDatabase.getInstance(context).loaiSachDAO();
    }

    public void setData(List<LoaiSach> loaiList) {
        this.loaiSachList = loaiList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoaiSachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_loai_sach,parent,false);
        return new LoaiSachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiSachViewHolder holder, int position) {
        LoaiSach loaiSach = loaiSachList.get(position);
        holder.tvTenLoai.setText(loaiSach.getTenLoai());

        holder.imgXoaLoaiSach.setOnClickListener(v -> openDeleteDialog(loaiSach));

        holder.itemView.setOnLongClickListener(v -> openEditDialog(loaiSach));
    }

    private boolean openEditDialog(LoaiSach loaiSach) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_loai_sach);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        EditText txtMaLoai = dialog.findViewById(R.id.txtSuaMaLoaiSach);
        EditText txtTenLoai = dialog.findViewById(R.id.txtSuaTenLoaiSach);
        Button btnSua = dialog.findViewById(R.id.btnSuaLoaiSach);
        Button btnHuy = dialog.findViewById(R.id.btnHuyLoaiSach);

        txtMaLoai.setText(String.valueOf(loaiSach.getMaLoai()));
        txtTenLoai.setText(loaiSach.getTenLoai());

        btnSua.setOnClickListener(v -> {
            if (txtMaLoai.getText().toString().trim().length() == 0
                    || txtTenLoai.getText().toString().trim().length() == 0) {
                Toast.makeText(context, "Không được để trống dữ liệu!!", Toast.LENGTH_SHORT).show();
                return;
            }
            loaiSach.setTenLoai(txtTenLoai.getText().toString().trim());
            loaiSachDAO.updateLoaiSach(loaiSach);
            loaiSachList = loaiSachDAO.getAllLoaiSach();
            setData(loaiSachList);
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

    private void openDeleteDialog(LoaiSach loaiSach) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xóa");
        builder.setMessage("Bạn có muốn xóa không ?");
        builder.setNegativeButton("OK", (dialog, which) -> {
            loaiSachDAO.deleteLoaiSach(loaiSach);
            setData(loaiSachDAO.getAllLoaiSach());
        });
        builder.setPositiveButton("Hủy", (dialog, which) -> {

        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return loaiSachList.size();
    }

    public class LoaiSachViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenLoai;
        ImageView imgXoaLoaiSach;
        public LoaiSachViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenLoai = itemView.findViewById(R.id.tvTenLoaiSach);
            imgXoaLoaiSach = itemView.findViewById(R.id.imgXoaLoaiSach);
        }
    }
}
