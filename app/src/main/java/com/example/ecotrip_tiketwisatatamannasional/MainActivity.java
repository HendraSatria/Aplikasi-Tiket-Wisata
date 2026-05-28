package com.example.ecotrip_tiketwisatatamannasional;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ecotrip_tiketwisatatamannasional.fragment.HomeFragment;
import com.example.ecotrip_tiketwisatatamannasional.fragment.ProfileFragment;
import com.example.ecotrip_tiketwisatatamannasional.fragment.TransaksiFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_transaksi) {
                selectedFragment = new TransaksiFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            String target = getIntent().getStringExtra("TARGET_FRAGMENT");
            if ("TRANSAKSI".equals(target)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new TransaksiFragment())
                        .commit();
                bottomNav.setSelectedItemId(R.id.nav_transaksi);
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
            }
        }
    }
}
