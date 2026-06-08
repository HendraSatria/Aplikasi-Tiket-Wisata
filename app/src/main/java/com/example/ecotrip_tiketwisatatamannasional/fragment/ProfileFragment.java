package com.example.ecotrip_tiketwisatatamannasional.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecotrip_tiketwisatatamannasional.LoginActivity;
import com.example.ecotrip_tiketwisatatamannasional.R;
import com.example.ecotrip_tiketwisatatamannasional.SplashActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private TextView tvCurrentLanguage;

    private boolean isUserLoggedIn() {
        SharedPreferences pref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        return pref.getBoolean("isLoggedIn", false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        MaterialButton btnLogout = view.findViewById(R.id.btn_logout);
        MaterialButton btnLogin = view.findViewById(R.id.btn_login_profile);
        TextView tvName = view.findViewById(R.id.tv_profile_name);
        TextView tvEmail = view.findViewById(R.id.tv_profile_email);
        View layoutProfileInfo = view.findViewById(R.id.layout_profile_info);
        ImageView ivVerified = view.findViewById(R.id.iv_verified);
        ImageView btnRefresh = view.findViewById(R.id.btn_refresh);
        
        View menuLanguage = view.findViewById(R.id.menu_language);
        tvCurrentLanguage = view.findViewById(R.id.tv_current_language);

        // Update current language text
        updateLanguageDisplay();

        if (isUserLoggedIn()) {
            layoutProfileInfo.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            
            SharedPreferences pref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            tvName.setText(pref.getString("userName", "Pendaki EcoTrip"));
            tvEmail.setText(pref.getString("userEmail", "hendra@ecotrip.com"));
            
            // Simulasi status verifikasi
            ivVerified.setVisibility(View.VISIBLE);
        } else {
            layoutProfileInfo.setVisibility(View.INVISIBLE); // Keep space but hide info
            tvName.setText("Guest User");
            tvEmail.setText("Silakan login untuk akses penuh");
            btnLogout.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            ivVerified.setVisibility(View.GONE);
        }

        btnRefresh.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Memperbarui data...", Toast.LENGTH_SHORT).show();
        });

        btnLogin.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));

        btnLogout.setOnClickListener(v -> {
            SharedPreferences pref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            pref.edit().clear().apply();
            
            Toast.makeText(getContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        menuLanguage.setOnClickListener(v -> showLanguageSelectionDialog());

        // Click listeners for menus
        view.findViewById(R.id.menu_edit_profile).setOnClickListener(v -> {
            if (isUserLoggedIn()) {
                showEditProfileDialog();
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        // Add Terms & Conditions Listener
        // Assuming the last relative layout in section 2 is Syarat dan Ketentuan
        // Looking at fragment_profile.xml, it's the 3rd RelativeLayout in section 2
        ViewGroup section2 = (ViewGroup) view.findViewById(R.id.btn_logout).getParent();
        // Index search based on XML structure:
        // Section 2 Header is at some index, then a CardView.
        // We need to find the specific layout. Let's find by looking for the one with specific icons or text if possible.
        // Or we can find all RelativeLayouts and pick the one with "Syarat dan Ketentuan" text.
        setupProfileMenuActions(view);

        return view;
    }

    private void setupProfileMenuActions(View view) {
        // Find Syarat dan Ketentuan menu by searching for text
        findMenuAndSetAction(view, "Syarat dan Ketentuan", v -> showTermsDialog());
        findMenuAndSetAction(view, "Tentang Tiket", v -> showAboutDialog());
        findMenuAndSetAction(view, "Kebijakan Privasi", v -> showPrivacyDialog());
        findMenuAndSetAction(view, "Riwayat Pendakian", v -> {
            if (getActivity() != null) {
                com.google.android.material.bottomnavigation.BottomNavigationView bnv = getActivity().findViewById(R.id.bottom_navigation);
                if (bnv != null) bnv.setSelectedItemId(R.id.nav_transaksi);
            }
        });
    }

    private void findMenuAndSetAction(View root, String textPart, View.OnClickListener listener) {
        ArrayList<View> outViews = new ArrayList<>();
        root.findViewsWithText(outViews, textPart, View.FIND_VIEWS_WITH_TEXT);
        for (View v : outViews) {
            if (v instanceof TextView) {
                View parent = (View) v.getParent();
                if (parent instanceof android.widget.RelativeLayout) {
                    parent.setOnClickListener(listener);
                }
            }
        }
    }

    private void showEditProfileDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Ubah Profil")
                .setMessage("Fitur untuk mengubah foto profil, nomor HP, dan alamat akan tersedia pada update versi 2.0.")
                .setPositiveButton("Mengerti", null)
                .show();
    }

    private void showTermsDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Syarat & Ketentuan EcoTrip")
                .setMessage("1. Calon pendaki wajib membawa identitas asli (KTP/Paspor).\n\n" +
                        "2. Dilarang keras membawa senjata tajam (kecuali untuk keperluan masak), miras, dan narkoba.\n\n" +
                        "3. Wajib membawa kembali sampah turun. Pelanggaran dikenakan denda Eco-Point atau administratif.\n\n" +
                        "4. Pembatalan (Refund) hanya dilayani H-3 dengan potongan biaya administrasi 25%.\n\n" +
                        "5. Kondisi fisik harus sehat. Surat keterangan sehat dari dokter wajib dibawa saat check-in di basecamp.")
                .setPositiveButton("Saya Setuju", null)
                .show();
    }

    private void showAboutDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Tentang Tiket Pendakian")
                .setMessage("EcoTrip adalah sistem perizinan pendakian (SIMAKSI) digital resmi yang bekerja sama dengan berbagai Balai Taman Nasional di Indonesia. Tujuan kami adalah mempermudah pendaftaran serta menjaga kelestarian alam melalui sistem Eco-Point.")
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showPrivacyDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Kebijakan Privasi")
                .setMessage("Data pribadi Anda (Nama, NIK, No HP) digunakan secara eksklusif untuk kepentingan verifikasi di pos pendakian dan tim SAR jika terjadi keadaan darurat. Kami tidak membagikan data Anda kepada pihak ketiga untuk tujuan iklan.")
                .setPositiveButton("Mengerti", null)
                .show();
    }

    private void updateLanguageDisplay() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String lang = prefs.getString("My_Lang", "in"); // Default Indonesian
        if (lang.equals("en")) {
            tvCurrentLanguage.setText("English");
        } else {
            tvCurrentLanguage.setText("Indonesia");
        }
    }

    private void showLanguageSelectionDialog() {
        String[] languages = {"Indonesia", "English"};
        int checkedItem = 0; // Default Indonesia
        
        SharedPreferences prefs = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String currentLang = prefs.getString("My_Lang", "in");
        if (currentLang.equals("en")) checkedItem = 1;

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pilih Bahasa / Select Language")
                .setSingleChoiceItems(languages, checkedItem, (dialog, which) -> {
                    if (which == 0) {
                        setLocale("in");
                    } else {
                        setLocale("en");
                    }
                    dialog.dismiss();
                    // Restart activity to apply changes
                    Intent intent = requireActivity().getIntent();
                    requireActivity().finish();
                    startActivity(intent);
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireActivity().getResources().updateConfiguration(config, requireActivity().getResources().getDisplayMetrics());
        
        // Save to preferences
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}
