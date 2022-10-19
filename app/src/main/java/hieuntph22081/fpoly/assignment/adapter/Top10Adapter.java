package hieuntph22081.fpoly.assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.model.Sach;

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.Top10ViewHolder>{
    Context context;
    List<Sach> saches;

    public Top10Adapter(Context context, List<Sach> saches) {
        this.context = context;
        this.saches = saches;
    }

    @NonNull
    @Override
    public Top10ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_top_10,parent,false);
        return new Top10ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Top10ViewHolder holder, int position) {
        Sach sach = saches.get(position);
        holder.tvTenSach.setText(sach.getTenSach());
        holder.tvGiaTien.setText(String.valueOf(sach.getGiaThue()));
    }

    @Override
    public int getItemCount() {
        return saches.size();
    }

    public class Top10ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenSach, tvGiaTien;
        public Top10ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSach = itemView.findViewById(R.id.top10TvTenSach);
            tvGiaTien = itemView.findViewById(R.id.top10TvGiaThue);
        }
    }
}
