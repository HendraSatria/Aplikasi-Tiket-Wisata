package com.example.ecotrip_tiketwisatatamannasional.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.SopActivity;
import com.example.ecotrip_tiketwisatatamannasional.GearActivity;
import com.example.ecotrip_tiketwisatatamannasional.TiketMasukActivity;
import com.example.ecotrip_tiketwisatatamannasional.adapter.MountainStatusAdapter;
import com.example.ecotrip_tiketwisatatamannasional.adapter.PopularMountainAdapter;
import com.example.ecotrip_tiketwisatatamannasional.model.MountainStatus;
import com.example.ecotrip_tiketwisatatamannasional.model.Wisata;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerBanner;
    private LinearLayout layoutIndicators;
    private final Handler sliderHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. Dynamic Greeting
        setDynamicGreeting(view.findViewById(R.id.tv_greeting));

        // 1b. Image Slider
        setupBannerSlider(view);
        setupIndicators(view);

        // 2. Menu Tiket Masuk
        view.findViewById(R.id.menu_tiket_masuk).setOnClickListener(v -> checkLoginAndNavigate(new Intent(getActivity(), TiketMasukActivity.class)));

        // 3. Header Icons (Cart & Notifications)
        view.findViewById(R.id.iv_cart_home).setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                showCartDialog();
            } else {
                startActivity(new Intent(getActivity(), com.example.ecotrip_tiketwisatatamannasional.LoginActivity.class));
            }
        });

        view.findViewById(R.id.iv_notification_home).setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                showNotificationDialog();
            } else {
                startActivity(new Intent(getActivity(), com.example.ecotrip_tiketwisatatamannasional.LoginActivity.class));
            }
        });

        // 4. Promo Card
        view.findViewById(R.id.card_promo).setOnClickListener(v -> {
            showInfoDialog("Promo Akhir Pekan", "Dapatkan cashback 10% untuk pemesanan tiket Gunung Bromo setiap hari Sabtu & Minggu menggunakan kode: ECOWEEKEND");
        });

        // 5. Setup Menu Grid Navigation (Info Dialogs)
        setupMenuNavigation(view);

        // 6. Popular Destinations
        setupPopularDestinations(view);

        // 7. Mountain Status
        setupMountainStatus(view);

        return view;
    }

    private void setupMountainStatus(View view) {
        RecyclerView rvStatus = view.findViewById(R.id.rv_mountain_status);
        if (rvStatus == null) return;

        List<MountainStatus> statusList = new ArrayList<>();
        statusList.add(new MountainStatus("Bromo", "Level II (Waspada)", "Cerah", "12°C", android.graphics.Color.parseColor("#FFC107")));
        statusList.add(new MountainStatus("Semeru", "Level III (Siaga)", "Berawan", "8°C", android.graphics.Color.RED));
        statusList.add(new MountainStatus("Slamet", "Level II (Waspada)", "Hujan Ringan", "10°C", android.graphics.Color.parseColor("#FFC107")));
        statusList.add(new MountainStatus("Prau", "Level I (Normal)", "Cerah", "14°C", android.graphics.Color.parseColor("#4CAF50")));
        statusList.add(new MountainStatus("Merbabu", "Level I (Normal)", "Cerah Berawan", "13°C", android.graphics.Color.parseColor("#4CAF50")));
        statusList.add(new MountainStatus("Lawu", "Level I (Normal)", "Cerah Berawan", "11°C", android.graphics.Color.parseColor("#4CAF50")));
        statusList.add(new MountainStatus("Ijen", "Level I (Normal)", "Cerah", "16°C", android.graphics.Color.parseColor("#4CAF50")));
        statusList.add(new MountainStatus("Raung", "Level II (Waspada)", "Mendung", "12°C", android.graphics.Color.parseColor("#FFC107")));
        statusList.add(new MountainStatus("Argopuro", "Level I (Normal)", "Hujan Sedang", "15°C", android.graphics.Color.parseColor("#4CAF50")));
        statusList.add(new MountainStatus("Arjuno", "Level I (Normal)", "Cerah", "14°C", android.graphics.Color.parseColor("#4CAF50")));

        MountainStatusAdapter adapter = new MountainStatusAdapter(statusList);
        rvStatus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvStatus.setAdapter(adapter);

        View tvSeeAllStatus = view.findViewById(R.id.tv_see_all_status);
        if (tvSeeAllStatus != null) {
            tvSeeAllStatus.setOnClickListener(v -> {
                showInfoDialog("Status Gunung Lengkap", "Menampilkan seluruh status aktivitas vulkanik, prakiraan cuaca, dan estimasi suhu di Taman Nasional.");
            });
        }
    }

    private void setupPopularDestinations(View view) {
        RecyclerView rvPopular = view.findViewById(R.id.rv_popular_destinations);
        TextView tvSeeAll = view.findViewById(R.id.tv_see_all);

        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v -> {
                checkLoginAndNavigate(new Intent(getActivity(), TiketMasukActivity.class));
            });
        }

        List<Wisata> popularList = new ArrayList<>();
        popularList.add(new Wisata("Gunung Bromo", "Probolinggo, Jawa Timur", 35000, R.drawable.bromo));
        popularList.add(new Wisata("Gunung Semeru", "Lumajang, Jawa Timur", 25000, R.drawable.rakum));
        popularList.add(new Wisata("Gunung Rinjani", "Lombok, NTB", 50000, R.drawable.rinjani));
        popularList.add(new Wisata("Gunung Prau", "Wonosobo, Jawa Tengah", 20000, R.drawable.prau));

        PopularMountainAdapter adapter = new PopularMountainAdapter(popularList, wisata -> {
            Intent intent = new Intent(getActivity(), com.example.ecotrip_tiketwisatatamannasional.BookingActivity.class);
            intent.putExtra("DATA_WISATA", wisata);
            checkLoginAndNavigate(intent);
        });

        if (rvPopular != null) {
            rvPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPopular.setAdapter(adapter);
        }
    }

    private void setupBannerSlider(View view) {
        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);

        List<BannerItem> bannerItems = new ArrayList<>();
        bannerItems.add(new BannerItem(R.drawable.img1_utama, "BOOKING TIKET MASUK GUNUNG DI INDONESIA JADI LEBIH MUDAH"));
        bannerItems.add(new BannerItem(R.drawable.promo, "PROMO AKHIR PEKAN: CASHBACK 10% UNTUK GUNUNG BROMO"));
        bannerItems.add(new BannerItem(R.drawable.rakum, "JELAJAHI KEINDAHAN DANAU RANUKUMBOLO DI GUNUNG SEMERU"));

        viewPagerBanner.setAdapter(new BannerAdapter(bannerItems));

        // Auto Slider Logic
        viewPagerBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000); // 3 seconds delay
            }
        });
    }

    private void setupIndicators(View view) {
        layoutIndicators = view.findViewById(R.id.layout_indicators);
        ImageView[] indicators = new ImageView[3]; // Ada 3 item di banner
        
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(requireContext());
            indicators[i].setImageDrawable(androidx.core.content.ContextCompat.getDrawable(
                    requireContext(), R.drawable.inactive_indicator_shape));
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            indicators[i].setLayoutParams(params);
            
            layoutIndicators.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int index) {
        if (layoutIndicators == null) return;
        int childCount = layoutIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicators.getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            if (i == index) {
                imageView.setImageDrawable(androidx.core.content.ContextCompat.getDrawable(
                        requireContext(), R.drawable.active_indicator_shape));
                // Set size for active indicator (32dp x 8dp)
                params.width = (int) (32 * getResources().getDisplayMetrics().density);
                params.height = (int) (8 * getResources().getDisplayMetrics().density);
            } else {
                imageView.setImageDrawable(androidx.core.content.ContextCompat.getDrawable(
                        requireContext(), R.drawable.inactive_indicator_shape));
                // Set size for inactive indicator (8dp x 8dp)
                params.width = (int) (8 * getResources().getDisplayMetrics().density);
                params.height = (int) (8 * getResources().getDisplayMetrics().density);
            }
            imageView.setLayoutParams(params);
        }
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPagerBanner != null && viewPagerBanner.getAdapter() != null) {
                int currentItem = viewPagerBanner.getCurrentItem();
                int totalItems = viewPagerBanner.getAdapter().getItemCount();
                if (totalItems > 0) {
                    int nextItem = (currentItem + 1) % totalItems;
                    viewPagerBanner.setCurrentItem(nextItem);
                }
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    // Banner Data Class
    private static class BannerItem {
        int imageRes;
        String description;

        BannerItem(int imageRes, String description) {
            this.imageRes = imageRes;
            this.description = description;
        }
    }

    // Banner Adapter Class
    private static class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
        private final List<BannerItem> bannerItems;

        BannerAdapter(List<BannerItem> bannerItems) {
            this.bannerItems = bannerItems;
        }

        @NonNull
        @Override
        public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BannerViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
            BannerItem item = bannerItems.get(position);
            holder.imageView.setImageResource(item.imageRes);
            holder.tvDesc.setText(item.description);
        }

        @Override
        public int getItemCount() {
            return bannerItems.size();
        }

        static class BannerViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvDesc;

            BannerViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.iv_banner);
                tvDesc = itemView.findViewById(R.id.tv_banner_desc);
            }
        }
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

    private boolean isUserLoggedIn() {
        SharedPreferences pref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return pref.getBoolean("isLoggedIn", false);
    }

    private void checkLoginAndNavigate(Intent intent) {
        if (isUserLoggedIn()) {
            startActivity(intent);
        } else {
            Intent loginIntent = new Intent(getActivity(), com.example.ecotrip_tiketwisatatamannasional.LoginActivity.class);
            startActivity(loginIntent);
        }
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
            
            if (menu.getId() == R.id.menu_sop) {
                menu.setOnClickListener(v -> {
                    startActivity(new Intent(getActivity(), SopActivity.class));
                });
                continue;
            }

            if (menu.getId() == R.id.menu_gear) {
                menu.setOnClickListener(v -> {
                    startActivity(new Intent(getActivity(), GearActivity.class));
                });
                continue;
            }

            menu.setOnClickListener(v -> {
                if (!isUserLoggedIn()) {
                    startActivity(new Intent(getActivity(), com.example.ecotrip_tiketwisatatamannasional.LoginActivity.class));
                    return;
                }
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
                    case 4: // Event
                        showEventDialog();
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

    private void showCartDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Keranjang Izin Pendakian")
                .setMessage("1. Izin Gunung Lawu (Via Cetho)\n   Tanggal: 25 Juni 2024\n   Status: Menunggu Pembayaran\n\nTotal Tagihan: Rp 20.000\n\nSilakan selesaikan pembayaran di menu Transaksi.")
                .setPositiveButton("Ke Transaksi", (dialog, which) -> {
                    if (getActivity() != null) {
                        BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
                        if (bnv != null) {
                            bnv.setSelectedItemId(R.id.nav_transaksi);
                        }
                    }
                })
                .setNegativeButton("Tutup", null)
                .show();
    }

    private void showNotificationDialog() {
        String[] notifications = {
                "⚠️ Jalur Merbabu via Selo ditutup sementara karena cuaca buruk.",
                "✅ Pembayaran Izin Bromo Anda telah dikonfirmasi.",
                "🔥 Promo 10% untuk pendakian Rinjani bulan depan!",
                "🌱 Eco-Point Anda bertambah +50 dari aksi bersih gunung."
        };

        new AlertDialog.Builder(getContext())
                .setTitle("Notifikasi Terbaru")
                .setItems(notifications, null)
                .setPositiveButton("Tandai Dibaca", null)
                .show();
    }

    private void showEventDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Event & Promo EcoTrip")
                .setMessage("📅 Upcoming Events:\n\n" +
                        "1. Merbabu Clean Up (20-21 Juni)\n   Dapatkan 200 Eco-Points!\n\n" +
                        "2. Festival Semeru (17 Agustus)\n   Upacara bersama di Kalimati.\n\n" +
                        "3. Promo Hari Bumi\n   Diskon 20% untuk semua jalur di Jawa Tengah.")
                .setPositiveButton("Ikuti Event", (dialog, which) -> {
                    Toast.makeText(getContext(), "Pendaftaran event akan dibuka segera!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Tutup", null)
                .show();
    }

    private void showInfoDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Mengerti", null)
                .show();
    }
}
