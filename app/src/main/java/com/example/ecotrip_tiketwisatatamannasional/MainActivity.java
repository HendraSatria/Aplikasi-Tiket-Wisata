package com.example.ecotrip_tiketwisatatamannasional;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.adapter.WisataAdapter;
import com.example.ecotrip_tiketwisatatamannasional.model.Wisata;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvWisata;
    private WisataAdapter adapter;
    private List<Wisata> wisataList;
    private MaterialButton btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvWisata = findViewById(R.id.rv_wisata);
        btnHistory = findViewById(R.id.btn_history);

        // Inisialisasi data wisata
        wisataList = new ArrayList<>();
        wisataList.add(new Wisata("Gunung Bromo - Paket Sunrise", "Probolinggo, Jawa Timur", 35000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Ranu Kumbolo - Camping 2D1N", "Lumajang, Jawa Timur", 35000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Kawah Ijen - Blue Fire Tour", "Banyuwangi, Jawa Timur", 35000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Tumpak Sewu Waterfall", "Lumajang, Jawa Timur", 20000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Pantai Papuma", "Jember, Jawa Timur", 15000, android.R.drawable.ic_menu_gallery));

        // Setup RecyclerView
        adapter = new WisataAdapter(wisataList, this, new WisataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Wisata wisata) {
                Intent intent = new Intent(MainActivity.this, BookingActivity.class);
                intent.putExtra("DATA_WISATA", wisata);
                startActivity(intent);
            }
        });

        rvWisata.setLayoutManager(new LinearLayoutManager(this));
        rvWisata.setAdapter(adapter);

        // Tombol Riwayat
        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}
