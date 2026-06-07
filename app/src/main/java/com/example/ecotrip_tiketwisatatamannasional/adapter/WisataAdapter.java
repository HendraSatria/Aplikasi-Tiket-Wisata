package com.example.ecotrip_tiketwisatatamannasional.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.model.Wisata;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.ViewHolder> {

    private List<Wisata> wisataList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Wisata wisata);
    }

    public WisataAdapter(List<Wisata> wisataList, Context context, OnItemClickListener listener) {
        this.wisataList = wisataList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wisata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wisata wisata = wisataList.get(position);
        holder.tvNama.setText(wisata.getNama());
        holder.tvLokasi.setText(wisata.getLokasi());
        holder.tvHarga.setText("Mulai Rp" + String.format("%,d", wisata.getHarga()).replace(',', '.'));
        holder.imgWisata.setImageResource(wisata.getGambarResId());

        holder.btnPesan.setOnClickListener(v -> listener.onItemClick(wisata));
    }

    @Override
    public int getItemCount() {
        return wisataList.size();
    }

    public void filterList(List<Wisata> filteredList) {
        this.wisataList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgWisata;
        TextView tvNama, tvLokasi, tvHarga;
        MaterialButton btnPesan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWisata = itemView.findViewById(R.id.img_wisata);
            tvNama = itemView.findViewById(R.id.tv_nama_wisata);
            tvLokasi = itemView.findViewById(R.id.tv_lokasi);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            btnPesan = itemView.findViewById(R.id.btn_pesan);
        }
    }
}
