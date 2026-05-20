package com.example.ecotrip_tiketwisatatamannasional.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        void onDelete(Booking booking);
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
        holder.tvStatus.setText("Status: Terkonfirmasi");

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(booking));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(booking));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestinasi, tvNama, tvTanggal, tvTotal, tvStatus;
        MaterialButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestinasi = itemView.findViewById(R.id.tv_destinasi);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
