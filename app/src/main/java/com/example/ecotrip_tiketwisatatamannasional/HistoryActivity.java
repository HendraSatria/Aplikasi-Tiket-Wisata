package com.example.ecotrip_tiketwisatatamannasional;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecotrip_tiketwisatatamannasional.adapter.BookingAdapter;
import com.example.ecotrip_tiketwisatatamannasional.model.Booking;

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

    private String URL_TAMPIL = "http://192.168.1.11/ecotrip/tampil_booking.php";
    private String URL_UPDATE = "http://192.168.1.11/ecotrip/update_booking.php";
    private String URL_DELETE = "http://192.168.1.11/ecotrip/delete_booking.php";

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
                                    obj.getDouble("total_bayar")
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
                    Toast.makeText(this, "Jadwal diperbarui", Toast.LENGTH_SHORT).show();
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
                .setTitle("Konfirmasi")
                .setMessage(getString(R.string.konfirmasi_hapus))
                .setPositiveButton("Ya, Batalkan", (dialog, which) -> deleteBooking(booking.getIdBooking()))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteBooking(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE,
                response -> {
                    Toast.makeText(this, "Booking dibatalkan", Toast.LENGTH_SHORT).show();
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
