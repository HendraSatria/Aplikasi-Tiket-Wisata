package com.example.ecotrip_tiketwisatatamannasional;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ecotrip_tiketwisatatamannasional.fragment.ChatFragment;
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
            
            // Cek jika tab yang diklik sudah aktif, jangan reload
            if (id == bottomNav.getSelectedItemId() && getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                return false;
            }

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_transaksi) {
                selectedFragment = new TransaksiFragment();
            } else if (id == R.id.nav_chat) {
                selectedFragment = new ChatFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
            return false;
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
                bottomNav.setSelectedItemId(R.id.nav_home);
            }
        }
    }

    @Override
    protected void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String target = intent.getStringExtra("TARGET_FRAGMENT");
        if ("TRANSAKSI".equals(target)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new TransaksiFragment())
                    .commit();
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_transaksi);
        }
    }
}
