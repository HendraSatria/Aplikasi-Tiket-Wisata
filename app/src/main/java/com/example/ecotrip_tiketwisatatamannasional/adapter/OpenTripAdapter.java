package com.example.ecotrip_tiketwisatatamannasional.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.model.OpenTrip;

import java.util.List;

public class OpenTripAdapter extends RecyclerView.Adapter<OpenTripAdapter.ViewHolder> {

    private List<OpenTrip> openTripList;
    private OnJoinChatClickListener listener;

    public interface OnJoinChatClickListener {
        void onJoinChatClick(OpenTrip trip);
    }

    public OpenTripAdapter(List<OpenTrip> openTripList) {
        this.openTripList = openTripList;
    }

    public void setOnJoinChatClickListener(OnJoinChatClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_open_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OpenTrip trip = openTripList.get(position);
        holder.tvMountain.setText(trip.getMountain());
        holder.tvDate.setText(trip.getDate());
        holder.tvDesc.setText(trip.getDescription());
        holder.tvHostName.setText(trip.getHostName());
        holder.ivAvatar.setImageResource(trip.getHostAvatarRes());

        holder.btnJoinChat.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJoinChatClick(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return openTripList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMountain, tvDate, tvDesc, tvHostName;
        ImageView ivAvatar;
        Button btnJoinChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMountain = itemView.findViewById(R.id.tv_trip_mountain);
            tvDate = itemView.findViewById(R.id.tv_trip_date);
            tvDesc = itemView.findViewById(R.id.tv_trip_desc);
            tvHostName = itemView.findViewById(R.id.tv_host_name);
            ivAvatar = itemView.findViewById(R.id.iv_host_avatar);
            btnJoinChat = itemView.findViewById(R.id.btn_join_chat);
        }
    }
}