package com.example.ecotrip_tiketwisatatamannasional.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.model.Booking;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<Booking> bookingList;
    private OnActionClickListener listener;

    public interface OnActionClickListener {
        void onEdit(Booking booking);
        void onPay(Booking booking);
        void onDownload(Booking booking);
    }

    public BookingAdapter(List<Booking> bookingList, OnActionClickListener listener) {
        this.bookingList = bookingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvDestinasi.setText(booking.getDestinasi());
        holder.tvNama.setText("Nama: " + booking.getNamaPemesan());
        holder.tvTanggal.setText("Tanggal: " + booking.getTanggal());
        holder.tvTotal.setText("Total: Rp" + String.format("%,.0f", booking.getTotalBayar()).replace(',', '.'));
        
        String status = booking.getStatusPembayaran();
        if (status == null || status.isEmpty() || status.equalsIgnoreCase("Belum Bayar")) {
            holder.tvStatus.setText("Status: Belum Bayar");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.holo_red_dark));
            holder.btnPay.setVisibility(View.VISIBLE);
            holder.btnDownload.setVisibility(View.GONE);
        } else if (status.equalsIgnoreCase("Lunas") || status.equalsIgnoreCase("Pembayaran Berhasil")) {
            holder.tvStatus.setText("Status: Lunas");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.nature_green));
            holder.btnPay.setVisibility(View.GONE);
            holder.btnDownload.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Status: " + status);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.nature_green));
            holder.btnPay.setVisibility(View.GONE);
            holder.btnDownload.setVisibility(View.GONE);
        }

        // Set Image based on destination name
        String dest = booking.getDestinasi().toLowerCase();
        int resId = R.drawable.bromo; // default
        if (dest.contains("semeru")) resId = R.drawable.rakum;
        else if (dest.contains("merapi")) resId = R.drawable.merapi;
        else if (dest.contains("merbabu")) resId = R.drawable.merbabu;
        else if (dest.contains("prau")) resId = R.drawable.prau;
        else if (dest.contains("rinjani")) resId = R.drawable.rinjani;
        else if (dest.contains("kerinci")) resId = R.drawable.kerinci;
        else if (dest.contains("lawu")) resId = R.drawable.lawu;
        else if (dest.contains("raung")) resId = R.drawable.raung;
        else if (dest.contains("arjuno")) resId = R.drawable.ajuno;
        else if (dest.contains("kelud")) resId = R.drawable.kelud;
        else if (dest.contains("slamet")) resId = R.drawable.slamet;
        else if (dest.contains("sindoro")) resId = R.drawable.sindoro;
        else if (dest.contains("ciremai")) resId = R.drawable.ciremai;
        else if (dest.contains("papandayan")) resId = R.drawable.papandayan;
        else if (dest.contains("penanggungan")) resId = R.drawable.penanggungan;
        else if (dest.contains("buthak")) resId = R.drawable.buthak;
        else if (dest.contains("wilis")) resId = R.drawable.wilis;
        else if (dest.contains("kawi")) resId = R.drawable.batutulis;
        else if (dest.contains("pegat")) resId = R.drawable.pegat;
        else if (dest.contains("jaya wijaya")) resId = R.drawable.jayawiajaya;
        
        holder.ivThumb.setImageResource(resId);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(booking));
        holder.btnPay.setOnClickListener(v -> listener.onPay(booking));
        holder.btnDownload.setOnClickListener(v -> listener.onDownload(booking));
        holder.btnDetail.setOnClickListener(v -> showDetailPopup(holder.itemView.getContext(), booking));
    }

    private void showDetailPopup(Context context, Booking booking) {
        String detailMessage = "Nama Rombongan: " + booking.getNamaPemesan() + "\n\n" +
                "NIK: " + booking.getNik() + "\n\n" +
                "Tanggal Mendaki: " + booking.getTanggal() + "\n\n" +
                "Jumlah Personel: " + booking.getJumlahTiket() + " Orang\n\n" +
                "Basecamp: " + booking.getBasecamp() + "\n\n" +
                "Fasilitas: " + (booking.getFasilitas().isEmpty() ? "-" : booking.getFasilitas()) + "\n\n" +
                "Kategori: " + booking.getKategoriTuris() + "\n\n" +
                "Status: " + (booking.getStatusPembayaran() == null ? "Belum Bayar" : booking.getStatusPembayaran()) + "\n\n" +
                "Total Biaya: Rp" + String.format("%,.0f", booking.getTotalBayar()).replace(',', '.');

        new AlertDialog.Builder(context)
                .setTitle("Detail Izin Mendaki")
                .setMessage(detailMessage)
                .setPositiveButton("Tutup", null)
                .setIcon(R.drawable.logo_utama)
                .show();
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void updateData(List<Booking> newList) {
        this.bookingList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestinasi, tvNama, tvTanggal, tvTotal, tvStatus;
        MaterialButton btnEdit, btnPay, btnDetail, btnDownload;
        ImageView ivThumb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestinasi = itemView.findViewById(R.id.tv_destinasi);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnPay = itemView.findViewById(R.id.btn_pay);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnDownload = itemView.findViewById(R.id.btn_download);
            ivThumb = itemView.findViewById(R.id.iv_destinasi_thumb);
        }
    }
}
