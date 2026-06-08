package com.example.ecotrip_tiketwisatatamannasional;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sop);

        Toolbar toolbar = findViewById(R.id.toolbar_sop);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("SOP Pendakian");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}