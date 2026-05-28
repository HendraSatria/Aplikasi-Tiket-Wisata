package com.example.ecotrip_tiketwisatatamannasional;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecotrip_tiketwisatatamannasional.adapter.WisataAdapter;
import com.example.ecotrip_tiketwisatatamannasional.model.Wisata;

import java.util.ArrayList;
import java.util.List;

public class TiketMasukActivity extends AppCompatActivity {

    private RecyclerView rvTiketMasuk;
    private WisataAdapter adapter;
    private List<Wisata> wisataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiket_masuk);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvTiketMasuk = findViewById(R.id.rv_tiket_masuk);

        wisataList = new ArrayList<>();
        wisataList.add(new Wisata("Gunung Kawi - Puncak Kawi Najang", "Malang, Jawa Timur", 25000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Bukit Jabal", "Jember, Jawa Timur", 10000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Bukit Lincing", "Pasuruan, Jawa Timur", 15000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Arjuno - Welirang", "Malang/Pasuruan, Jawa Timur", 30000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Bokong", "Pasuruan, Jawa Timur", 10000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Bekel", "Mojokerto, Jawa Timur", 15000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Buthak", "Malang/Blitar, Jawa Timur", 25000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Kelud", "Kediri, Jawa Timur", 20000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Penanggungan", "Mojokerto, Jawa Timur", 20000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Puthuk Siwur", "Mojokerto, Jawa Timur", 15000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Gunung Bromo - Tiket Masuk", "Probolinggo, Jawa Timur", 35000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Ranu Kumbolo - Izin Camp 2D1N", "Lumajang, Jawa Timur", 55000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Kawah Ijen - Tiket Masuk", "Banyuwangi, Jawa Timur", 25000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Tumpak Sewu Waterfall", "Lumajang, Jawa Timur", 25000, android.R.drawable.ic_menu_gallery));
        wisataList.add(new Wisata("Pantai Papuma", "Jember, Jawa Timur", 20000, android.R.drawable.ic_menu_gallery));

        adapter = new WisataAdapter(wisataList, this, new WisataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Wisata wisata) {
                Intent intent = new Intent(TiketMasukActivity.this, BookingActivity.class);
                intent.putExtra("DATA_WISATA", wisata);
                startActivity(intent);
            }
        });

        rvTiketMasuk.setLayoutManager(new LinearLayoutManager(this));
        rvTiketMasuk.setAdapter(adapter);
    }
}
