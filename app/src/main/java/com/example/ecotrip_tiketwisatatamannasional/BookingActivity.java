package com.example.ecotrip_tiketwisatatamannasional;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecotrip_tiketwisatatamannasional.model.Wisata;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private TextInputEditText etNama, etNik, etTanggal, etJumlah;
    private Spinner spinnerPintu;
    private RadioGroup rgWisatawan;
    private RadioButton rbDomestik, rbMancanegara;
    private CheckBox cbJeep, cbCamping, cbPorter;
    private TextView tvTotal, tvNamaWisata;
    private ImageView ivGunung;
    private MaterialButton btnHitung, btnBooking;

    private Wisata wisata;
    private double totalBayar = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Binding view
        etNama = findViewById(R.id.et_nama);
        etNik = findViewById(R.id.et_nik);
        etTanggal = findViewById(R.id.et_tanggal);
        etJumlah = findViewById(R.id.et_jumlah);
        spinnerPintu = findViewById(R.id.spinner_pintu);
        rgWisatawan = findViewById(R.id.rg_wisatawan);
        rbDomestik = findViewById(R.id.rb_domestik);
        rbMancanegara = findViewById(R.id.rb_mancanegara);
        cbJeep = findViewById(R.id.cb_jeep);
        cbCamping = findViewById(R.id.cb_camping);
        cbPorter = findViewById(R.id.cb_porter);
        tvTotal = findViewById(R.id.tv_total_bayar);
        tvNamaWisata = findViewById(R.id.tv_nama_wisata);
        ivGunung = findViewById(R.id.iv_gunung_booking);
        btnHitung = findViewById(R.id.btn_hitung);
        btnBooking = findViewById(R.id.btn_booking);

        // Get data wisata from Intent
        wisata = (Wisata) getIntent().getSerializableExtra("DATA_WISATA");
        if (wisata != null) {
            tvNamaWisata.setText(wisata.getNama());
            ivGunung.setImageResource(wisata.getGambarResId());
            // Update UI hints based on selected wisata price
            rbDomestik.setText("Domestik (Rp" + String.format("%,d", wisata.getHarga()).replace(',', '.') + ")");
            rbMancanegara.setText("Mancanegara (Rp" + String.format("%,d", wisata.getHarga() * 5).replace(',', '.') + ")");
        }

        // Setup Spinner based on Selected Mountain
        String[] pintuMasuk = getJalurPendakian(wisata != null ? wisata.getNama() : "");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pintuMasuk);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPintu.setAdapter(adapter);

        // DatePicker
        etTanggal.setOnClickListener(v -> showDatePicker());

        btnHitung.setOnClickListener(v -> hitungTotal());

        btnBooking.setOnClickListener(v -> {
            if (validasiInput()) {
                showKonfirmasiDialog();
            }
        });
    }

    private boolean validasiInput() {
        if (etNama.getText().toString().isEmpty()) {
            etNama.setError("Nama harus diisi");
            return false;
        }
        if (etNik.getText().toString().length() < 16) {
            etNik.setError("NIK minimal 16 digit");
            return false;
        }
        if (etTanggal.getText().toString().isEmpty()) {
            Toast.makeText(this, "Pilih tanggal booking", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etJumlah.getText().toString().isEmpty()) {
            etJumlah.setError("Jumlah tiket harus diisi");
            return false;
        }
        return true;
    }

    private void showKonfirmasiDialog() {
        hitungTotal();
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Konfirmasi Booking")
                .setMessage("Apakah data yang Anda masukkan sudah benar?\nTotal: " + tvTotal.getText().toString())
                .setPositiveButton("Ya, Simpan", (dialog, which) -> simpanBooking())
                .setNegativeButton("Cek Lagi", null)
                .show();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            etTanggal.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Batasi agar tidak bisa pilih tanggal kemarin
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void hitungTotal() {
        if (etJumlah.getText().toString().isEmpty()) {
            return;
        }

        int jumlah = Integer.parseInt(etJumlah.getText().toString());
        long hargaBase = (wisata != null) ? wisata.getHarga() : 0;
        long hargaWisatawan = rbDomestik.isChecked() ? hargaBase : (hargaBase * 5);
        
        long tambahan = 0;
        if (cbJeep.isChecked()) tambahan += 600000;
        if (cbCamping.isChecked()) tambahan += 50000;
        if (cbPorter.isChecked()) tambahan += 250000;

        totalBayar = (hargaWisatawan * jumlah) + tambahan;
        tvTotal.setText("Total Bayar: Rp" + String.format("%,.0f", totalBayar).replace(',', '.'));
    }

    private void simpanBooking() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan booking...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.URL_INSERT,
                response -> {
                    progressDialog.dismiss();
                    Toast.makeText(BookingActivity.this, "Booking Berhasil!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BookingActivity.this, MainActivity.class);
                    intent.putExtra("TARGET_FRAGMENT", "TRANSAKSI");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(BookingActivity.this, "Gagal: Pastikan Server Aktif (" + error.getMessage() + ")", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_pemesan", etNama.getText().toString());
                params.put("nik", etNik.getText().toString());
                params.put("destinasi", wisata.getNama());
                params.put("tanggal", etTanggal.getText().toString());
                params.put("kategori_turis", rbDomestik.isChecked() ? "Domestik" : "Mancanegara");
                params.put("jumlah_tiket", etJumlah.getText().toString());
                
                String fasilitas = "";
                if (cbJeep.isChecked()) fasilitas += "Sewa Jeep, ";
                if (cbCamping.isChecked()) fasilitas += "Camping, ";
                if (cbPorter.isChecked()) fasilitas += "Jasa Porter, ";
                params.put("fasilitas", fasilitas);
                params.put("basecamp", spinnerPintu.getSelectedItem().toString()); // Added
                params.put("total_bayar", String.valueOf(totalBayar));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String[] getJalurPendakian(String namaGunung) {
        if (namaGunung == null) return new String[]{"Pintu Utama"};
        
        String name = namaGunung.toLowerCase();
        
        if (name.contains("kelud")) {
            return new String[]{"Via Ngantang", "Via Karangrejo", "Via Tulungrejo", "Kediri (Wisata)"};
        } else if (name.contains("batutulis")) {
            return new String[]{"Via Precet Wagir (Jalur Tercepat)", "Via Keraton ", "Via Kucur Dau", "Via Panderman", "Via Ngantang ", "Via Mrinci Batu"};
        } else if (name.contains("kawinajang")) {
            return new String[]{"Via Ngantang", "Via Bendosari", "Via Tulungrejo", "Via Sebaluh", "Via Sumberpitu"};
        } else if (name.contains("bromo")) {
            return new String[]{"Via Cemorolawang", "Via Wonokitri", "Via Ngadas", "Via Tosari"};
        } else if (name.contains("semeru")) {
            return new String[]{"Via Ranupane"};
        } else if (name.contains("arjuno") || name.contains("welirang")) {
            return new String[]{"Via Tretes", "Via Lawang", "Via Purwosari", "Via Sumber Brantas (Batu)"};
        } else if (name.contains("merbabu")) {
            return new String[]{"Via Selo", "Via Suwanting", "Via Wekas", "Via Cunthel", "Via Thekelan"};
        } else if (name.contains("merapi")) {
            return new String[]{"Via Selo", "Via Sapuangin"};
        } else if (name.contains("prau")) {
            return new String[]{"Via Patakbanteng", "Via Kalilembu", "Via Dieng", "Via Dwarawati", "Via Igirmranak"};
        } else if (name.contains("lawu")) {
            return new String[]{"Via Cemoro Sewu", "Via Cemoro Kandang", "Via Candi Cetho", "Via Singolangu"};
        } else if (name.contains("slamet")) {
            return new String[]{"Via Bambangan", "Via Guci", "Via Dipajaya", "Via Baturraden"};
        } else if (name.contains("sindoro")) {
            return new String[]{"Via Kledung", "Via Alang-alang Sewu", "Via Bansari", "Via Ndoro Arum"};
        } else if (name.contains("sumbing")) {
            return new String[]{"Via Garung", "Via Bowongso", "Via Sipetung", "Via Mangli"};
        } else if (name.contains("rinjani")) {
            return new String[]{"Via Senaru", "Via Sembalun", "Via Aik Berik", "Via Timbanuh"};
        } else if (name.contains("kerinci")) {
            return new String[]{"Via Kersik Tuo"};
        } else if (name.contains("ciremai")) {
            return new String[]{"Via Linggarjati", "Via Palutungan", "Via Apuy", "Via Cisantana"};
        } else if (name.contains("papandayan")) {
            return new String[]{"Via Cisurupan"};
        } else if (name.contains("penanggungan")) {
            return new String[]{"Via Tamiajeng", "Via Jolotundo", "Via Trawas", "Via Kunjorowesi"};
        } else if (name.contains("buthak")) {
            return new String[]{"Via Panderman (Batu)", "Via Sirah Kencong (Blitar)"};
        } else if (name.contains("wilis")) {
            return new String[]{"Via Bajulan (Nganjuk)", "Via Mojo (Kediri)", "Via Kare (Madiun)"};
        } else if (name.contains("raung")) {
            return new String[]{"Via Kalibaru", "Via Sumberwringin"};
        } else if (name.contains("jaya wijaya")) {
            return new String[]{"Via Ugimba", "Via Sugapa"};
        }
        
        return new String[]{"Pintu Utama", "Jalur Umum"};
    }
}
