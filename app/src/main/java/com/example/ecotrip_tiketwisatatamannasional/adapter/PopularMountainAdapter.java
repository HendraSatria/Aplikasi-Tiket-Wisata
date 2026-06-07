package com.example.ecotrip_tiketwisatatamannasional.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.model.Wisata;

import java.util.List;

public class PopularMountainAdapter extends RecyclerView.Adapter<PopularMountainAdapter.ViewHolder> {

    private List<Wisata> mountainList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Wisata wisata);
    }

    public PopularMountainAdapter(List<Wisata> mountainList, OnItemClickListener listener) {
        this.mountainList = mountainList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mountain_popular, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wisata mountain = mountainList.get(position);
        holder.tvName.setText(mountain.getNama());
        holder.tvLocation.setText(mountain.getLokasi());
        holder.tvPrice.setText("Rp " + String.format("%,d", mountain.getHarga()).replace(',', '.') + " / orang");
        holder.ivMountain.setImageResource(mountain.getGambarResId());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(mountain));
    }

    @Override
    public int getItemCount() {
        return mountainList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMountain;
        TextView tvName, tvLocation, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMountain = itemView.findViewById(R.id.iv_mountain);
            tvName = itemView.findViewById(R.id.tv_mountain_name);
            tvLocation = itemView.findViewById(R.id.tv_mountain_location);
            tvPrice = itemView.findViewById(R.id.tv_mountain_price);
        }
    }
}