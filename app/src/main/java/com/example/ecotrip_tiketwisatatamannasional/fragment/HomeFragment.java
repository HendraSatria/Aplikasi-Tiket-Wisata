package com.example.ecotrip_tiketwisatatamannasional.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecotrip_tiketwisatatamannasional.MainActivity;
import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.TiketMasukActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Dynamic Greeting
        setDynamicGreeting(view.findViewById(R.id.tv_greeting));

        // 2. Menu Tiket Masuk
        view.findViewById(R.id.menu_tiket_masuk).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TiketMasukActivity.class);
            startActivity(intent);
        });

        // 3. Header Icons (Search & History)
        view.findViewById(R.id.iv_search_home).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TiketMasukActivity.class));
        });

        view.findViewById(R.id.iv_history_home).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                BottomNavigationView nav = getActivity().findViewById(R.id.bottom_navigation);
                nav.setSelectedItemId(R.id.nav_transaksi);
            }
        });

        // 4. Promo Card
        view.findViewById(R.id.card_promo).setOnClickListener(v -> {
            showInfoDialog("Promo Akhir Pekan", "Dapatkan cashback 10% untuk pemesanan tiket Gunung Bromo setiap hari Sabtu & Minggu menggunakan kode: ECOWEEKEND");
        });

        // 5. Setup Menu Grid Navigation (Info Dialogs)
        setupMenuNavigation(view);

        return view;
    }

    private void setDynamicGreeting(TextView tvGreeting) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (timeOfDay < 12) {
            greeting = "Selamat Pagi, Hendra! Siap mendaki hari ini?";
        } else if (timeOfDay < 15) {
            greeting = "Selamat Siang, Hendra! Sudah cek perlengkapan?";
        } else if (timeOfDay < 18) {
            greeting = "Selamat Sore, Hendra! Nikmati senja di puncak.";
        } else {
            greeting = "Selamat Malam, Hendra! Siapkan fisik untuk esok.";
        }
        tvGreeting.setText(greeting);
    }

    private void setupMenuNavigation(View view) {
        // Karena di XML banyak yang belum ada ID-nya, kita gunakan index jika perlu 
        // atau kita tambahkan ID di XML. Mari kita beri aksi pada menu yang umum:

        // Mengambil parent (GridLayout) untuk memberi listener ke tiap menu
        ViewGroup menuGrid = (ViewGroup) view.findViewById(R.id.menu_tiket_masuk).getParent();
        
        for (int indexMenu = 0; indexMenu < menuGrid.getChildCount(); indexMenu++) {
            View menu = menuGrid.getChildAt(indexMenu);
            final int currentIndex = indexMenu;
            
            if (menu.getId() == R.id.menu_tiket_masuk) continue;

            menu.setOnClickListener(v -> {
                switch (currentIndex) {
                    case 1: // Travel & Ojek
                        showInfoDialog("Travel & Ojek", "Layanan antar jemput basecamp dan ojek pendakian tersedia di lokasi. Hubungi pos perizinan setempat untuk pemesanan.");
                        break;
                    case 2: // Porter & Guide
                        showInfoDialog("Porter & Guide", "Butuh bantuan membawa barang? Jasa porter berlisensi tersedia mulai dari Rp 250.000/hari. Pastikan memesan H-1.");
                        break;
                    case 3: // Sewa Alat
                        showInfoDialog("Sewa Alat", "Tersedia penyewaan Tenda, Carrier, Sleeping Bag, dan Nesting di setiap basecamp pendakian.");
                        break;
                    case 5: // Camping
                        showInfoDialog("Info Camping", "Gunakan area yang telah ditentukan (Camping Ground). Jangan mendirikan tenda di jalur pendakian.");
                        break;
                    default:
                        Toast.makeText(getContext(), "Fitur informasi akan segera diperbarui", Toast.LENGTH_SHORT).show();
                        break;
                }
            });
        }
    }

    private void showInfoDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Mengerti", null)
                .show();
    }
}
