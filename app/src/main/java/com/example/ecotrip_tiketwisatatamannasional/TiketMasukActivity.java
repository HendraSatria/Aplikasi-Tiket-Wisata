package com.example.ecotrip_tiketwisatatamannasional;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.ecotrip_tiketwisatatamannasional.adapter.WisataAdapter;
import com.example.ecotrip_tiketwisatatamannasional.model.Wisata;

import java.util.ArrayList;
import java.util.List;

public class TiketMasukActivity extends AppCompatActivity {

    private RecyclerView rvTiketMasuk;
    private WisataAdapter adapter;
    private List<Wisata> wisataList;
    private EditText etSearch;

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
        etSearch = findViewById(R.id.et_search);

        wisataList = new ArrayList<>();
        // Jawa Timur
        wisataList.add(new Wisata("Gunung Kelud ", " Jawa Timur", 15000, R.drawable.kelud));
        wisataList.add(new Wisata("Gunung Wilis ", "Tulungagung, Jawa Timur", 15000, R.drawable.wilis));
        wisataList.add(new Wisata("Gunung Kawi Batutulis", " Jawa Timur", 15000, R.drawable.batutulis));
        wisataList.add(new Wisata("Gunung Kawinajang", "Jawa Timur", 15000, R.drawable.najang));
        wisataList.add(new Wisata("Gunung Pegat  ", " Jawa Timur", 10000, R.drawable.pegat));

        wisataList.add(new Wisata("Gunung Semeru (Mahameru)", "Lumajang, Jawa Timur", 25000, R.drawable.rakum));
        wisataList.add(new Wisata("Gunung Bromo (Penanjakan)", "Probolinggo, Jawa Timur", 35000, R.drawable.bromo));
        wisataList.add(new Wisata("Gunung Arjuno - Welirang", "Pasuruan/Malang", 30000, R.drawable.ajuno));
        wisataList.add(new Wisata("Gunung Buthak (Via Panderman)", "Batu/Malang", 15000, R.drawable.buthak));
        wisataList.add(new Wisata("Gunung Penanggungan", "Mojokerto, Jawa Timur", 20000, R.drawable.penanggungan));
        wisataList.add(new Wisata("Gunung Raung", "Banyuwangi/Bondowoso", 40000, R.drawable.raung));

        // Jawa Tengah & DIY
        wisataList.add(new Wisata("Gunung Merapi (Via Selo)", "Boyolali, Jawa Tengah", 25000,R.drawable.merapi));
        wisataList.add(new Wisata("Gunung Merbabu", "Magelang/Boyolali", 25000, R.drawable.merbabu));
        wisataList.add(new Wisata("Gunung Prau", "Wonosobo, Jawa Tengah", 20000, R.drawable.prau));
        wisataList.add(new Wisata("Gunung Lawu", "Karanganyar/Magetan", 20000, R.drawable.lawu));
        wisataList.add(new Wisata("Gunung Slamet", "Purbalingga, Jawa Tengah", 30000, R.drawable.slamet));
        wisataList.add(new Wisata("Gunung Sindoro - Sumbing", "Temanggung, Jawa Tengah", 25000, R.drawable.sindoro));

        // Jawa Barat & Banten

        wisataList.add(new Wisata("Gunung Ciremai", "Kuningan, Jawa Barat", 50000, R.drawable.ciremai));
        wisataList.add(new Wisata("Gunung Papandayan", "Garut, Jawa Barat", 30000, R.drawable.papandayan));

        // Luar Jawa
        wisataList.add(new Wisata("Gunung Rinjani", "Lombok, NTB", 50000, R.drawable.rinjani));
        wisataList.add(new Wisata("Gunung Kerinci", "Jambi, Sumatera", 40000, R.drawable.kerinci));
        wisataList.add(new Wisata("Puncak Jaya Wijaya (Cartenz)", "Papua", 1000000, R.drawable.jayawiajaya));

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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String text) {
        List<Wisata> filteredList = new ArrayList<>();
        for (Wisata item : wisataList) {
            if (item.getNama().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}
