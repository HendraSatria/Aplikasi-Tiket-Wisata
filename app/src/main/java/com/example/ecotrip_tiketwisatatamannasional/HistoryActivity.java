package com.example.ecotrip_tiketwisatatamannasional;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecotrip_tiketwisatatamannasional.adapter.BookingAdapter;
import com.example.ecotrip_tiketwisatatamannasional.model.Booking;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private BookingAdapter adapter;
    private List<Booking> bookingList;

    private String URL_TAMPIL = "http://192.168.1.9/ecotrip/tampil_booking.php";
    private String URL_UPDATE = "http://192.168.1.9/ecotrip/update_booking.php";
    private String URL_DELETE = "http://192.168.1.9/ecotrip/delete_booking.php";

    private Booking currentPayingBooking;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(this, "Scan dibatalkan", Toast.LENGTH_LONG).show();
                } else {
                    handlePaymentSuccess(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        rvHistory = findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        bookingList = new ArrayList<>();
        ambilData();
    }

    private void ambilData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_TAMPIL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONArray array = new JSONArray(response);
                        bookingList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            bookingList.add(new Booking(
                                    obj.getString("id_booking"),
                                    obj.getString("nama_pemesan"),
                                    obj.getString("nik"),
                                    obj.getString("destinasi"),
                                    obj.getString("tanggal"),
                                    obj.getString("kategori_turis"),
                                    obj.getInt("jumlah_tiket"),
                                    obj.getString("fasilitas"),
                                    obj.getDouble("total_bayar"),
                                    obj.optString("basecamp", "-")
                            ));
                        }
                        adapter = new BookingAdapter(bookingList, new BookingAdapter.OnActionClickListener() {
                            @Override
                            public void onEdit(Booking booking) {
                                showEditDateDialog(booking);
                            }

                            @Override
                            public void onDelete(Booking booking) {
                                showDeleteConfirmDialog(booking);
                            }

                            @Override
                            public void onPay(Booking booking) {
                                startPayment(booking);
                            }
                        });
                        rvHistory.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void startPayment(Booking booking) {
        currentPayingBooking = booking;
        String total = String.format("%,.0f", booking.getTotalBayar()).replace(',', '.');
        
        new AlertDialog.Builder(this)
                .setTitle("Bayar Izin")
                .setMessage("Lanjutkan pembayaran izin untuk " + booking.getDestinasi() + "\nNominal: Rp" + total)
                .setPositiveButton("Scan Barcode/QR", (dialog, which) -> {
                    ScanOptions options = new ScanOptions();
                    options.setPrompt("Scan Barcode Pembayaran");
                    options.setBeepEnabled(true);
                    options.setOrientationLocked(true);
                    options.setCaptureActivity(CustomScannerActivity.class);
                    barcodeLauncher.launch(options);
                })
                .setNegativeButton("Batal", null)
                .show();
    }

    private void handlePaymentSuccess(String scanData) {
        if (currentPayingBooking != null) {
            String total = String.format("%,.0f", currentPayingBooking.getTotalBayar()).replace(',', '.');
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Pembayaran")
                    .setMessage("Scan Berhasil!\nData: " + scanData + "\n\nNominal Rp" + total + " akan dibayarkan.")
                    .setPositiveButton("Bayar Sekarang", (dialog, which) -> {
                        Toast.makeText(this, "Pembayaran Berhasil untuk " + currentPayingBooking.getDestinasi(), Toast.LENGTH_LONG).show();
                        // Di sini bisa ditambahkan update status bayar ke database
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        }
    }

    private void showEditDateDialog(Booking booking) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String tglBaru = sdf.format(calendar.getTime());
            updateJadwal(booking.getIdBooking(), tglBaru);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void updateJadwal(String id, String tgl) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                response -> {
                    Toast.makeText(this, "Jadwal izin diperbarui", Toast.LENGTH_SHORT).show();
                    ambilData();
                },
                error -> Toast.makeText(this, "Gagal update: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_booking", id);
                params.put("tanggal", tgl);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void showDeleteConfirmDialog(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Batalkan Izin")
                .setMessage("Apakah Anda yakin ingin membatalkan izin ini?")
                .setPositiveButton("Ya, Batalkan", (dialog, which) -> deleteBooking(booking.getIdBooking()))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteBooking(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                response -> {
                    Toast.makeText(this, "Izin dibatalkan", Toast.LENGTH_SHORT).show();
                    ambilData();
                },
                error -> Toast.makeText(this, "Gagal hapus: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_booking", id);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
