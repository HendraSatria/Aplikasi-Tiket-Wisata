package com.example.ecotrip_tiketwisatatamannasional.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.model.MountainStatus;

import java.util.List;

public class MountainStatusAdapter extends RecyclerView.Adapter<MountainStatusAdapter.ViewHolder> {

    private List<MountainStatus> statusList;

    public MountainStatusAdapter(List<MountainStatus> statusList) {
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mountain_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MountainStatus status = statusList.get(position);
        holder.tvName.setText(status.getName());
        holder.tvLevel.setText(status.getLevel());
        holder.tvWeather.setText(status.getWeather());
        holder.tvTemperature.setText(status.getTemperature());
        holder.viewIndicator.setBackgroundTintList(android.content.res.ColorStateList.valueOf(status.getStatusColor()));
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLevel, tvWeather, tvTemperature;
        View viewIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_status_mountain_name);
            tvLevel = itemView.findViewById(R.id.tv_status_level);
            tvWeather = itemView.findViewById(R.id.tv_weather_desc);
            tvTemperature = itemView.findViewById(R.id.tv_temperature);
            viewIndicator = itemView.findViewById(R.id.view_status_indicator);
        }
    }
}