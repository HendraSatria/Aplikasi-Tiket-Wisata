package com.example.ecotrip_tiketwisatatamannasional.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.adapter.OpenTripAdapter;
import com.example.ecotrip_tiketwisatatamannasional.model.OpenTrip;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        RecyclerView rvOpenTrip = view.findViewById(R.id.rv_open_trip);
        rvOpenTrip.setLayoutManager(new LinearLayoutManager(getContext()));

        List<OpenTrip> tripList = new ArrayList<>();
        tripList.add(new OpenTrip("Gunung Rinjani", "15 - 18 Juli 2024", "Cari teman share cost sewa jeep dan guide. Sisa 3 slot!", "Hendra Wijaya", R.drawable.logo_utama));
        tripList.add(new OpenTrip("Gunung Merbabu", "20 - 21 Juni 2024", "Mendaki santai via Selo. Sudah ada 4 orang, cari 2 lagi.", "Siska Amelia", R.drawable.logo_utama));
        tripList.add(new OpenTrip("Gunung Prau", "5 - 6 Juli 2024", "Tek tok atau camp 1 malam. Bebas mau via mana aja.", "Budi Santoso", R.drawable.logo_utama));
        tripList.add(new OpenTrip("Gunung Semeru", "10 - 13 Agustus 2024", "Persiapan fisik bareng yuk sebelum ke Mahameru.", "Andi Pratama", R.drawable.logo_utama));

        OpenTripAdapter adapter = new OpenTripAdapter(tripList);
        adapter.setOnJoinChatClickListener(trip -> {
            // Nomor WA Admin/Host (contoh)
            String phoneNumber = "6285692131183"; // Ganti dengan nomor asli
            String message = "Halo " + trip.getHostName() + ", saya tertarik bergabung di Open Trip " + trip.getMountain() + " tanggal " + trip.getDate() + ". Apakah masih ada slot?";
            
            try {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + Uri.encode(message);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (Exception e) {
                Toast.makeText(getContext(), "WhatsApp tidak terpasang atau terjadi kesalahan.", Toast.LENGTH_SHORT).show();
            }
        });

        rvOpenTrip.setAdapter(adapter);

        view.findViewById(R.id.fab_add_trip).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Fitur Buat Open Trip segera hadir!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
