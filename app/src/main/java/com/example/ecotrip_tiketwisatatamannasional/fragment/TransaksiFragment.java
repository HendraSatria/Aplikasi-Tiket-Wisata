package com.example.ecotrip_tiketwisatatamannasional.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ecotrip_tiketwisatatamannasional.ApiConfig;
import com.example.ecotrip_tiketwisatatamannasional.CustomScannerActivity;
import com.example.ecotrip_tiketwisatatamannasional.R;
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

public class TransaksiFragment extends Fragment {

    private RecyclerView rvHistory;
    private BookingAdapter adapter;
    private List<Booking> bookingList;
    private List<Booking> filteredList;
    private View emptyState;

    private Booking currentPayingBooking;

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(getContext(), "Scan dibatalkan", Toast.LENGTH_LONG).show();
                } else {
                    handlePaymentSuccess(result.getContents());
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaksi, container, false);

        rvHistory = view.findViewById(R.id.rv_history);
        emptyState = view.findViewById(R.id.layout_empty_state); // Pastikan ada di XML
        com.google.android.material.textfield.TextInputEditText etSearch = view.findViewById(R.id.et_search_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingList = new ArrayList<>();
        filteredList = new ArrayList<>();
        
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

        ambilData();

        return view;
    }

    private void ambilData() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Memuat data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiConfig.URL_TAMPIL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONArray array = new JSONArray(response);
                        bookingList.clear();
                        for (int k = 0; k < array.length(); k++) {
                            JSONObject obj = array.getJSONObject(k);
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
                        
                        updateUI();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        updateUI();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Gagal memuat: Periksa koneksi ke server", Toast.LENGTH_SHORT).show();
                    updateUI();
                });

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void filter(String text) {
        filteredList.clear();
        for (Booking booking : bookingList) {
            if (booking.getDestinasi().toLowerCase().contains(text.toLowerCase()) ||
                booking.getNamaPemesan().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(booking);
            }
        }
        if (adapter != null) {
            adapter.updateData(filteredList);
        }
        
        if (filteredList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
        } else {
            emptyState.setVisibility(View.GONE);
        }
    }

    private void updateUI() {
        if (bookingList.isEmpty()) {
            if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            if (emptyState != null) emptyState.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            
            filteredList.clear();
            filteredList.addAll(bookingList);
            
            adapter = new BookingAdapter(filteredList, new BookingAdapter.OnActionClickListener() {
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
        }
    }

    private void showEditDateDialog(Booking booking) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String tglBaru = sdf.format(calendar.getTime());
            updateJadwal(booking.getIdBooking(), tglBaru);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // Minimal tanggal hari ini
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateJadwal(String id, String tgl) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.URL_UPDATE,
                response -> {
                    Toast.makeText(getContext(), "Jadwal berhasil diubah", Toast.LENGTH_SHORT).show();
                    ambilData();
                },
                error -> Toast.makeText(getContext(), "Gagal update: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_booking", id);
                params.put("tanggal", tgl);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    private void startPayment(Booking booking) {
        currentPayingBooking = booking;
        String total = String.format(Locale.getDefault(), "%,.0f", booking.getTotalBayar()).replace(',', '.');

        android.widget.LinearLayout layout = new android.widget.LinearLayout(getContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 40, 40, 40);

        android.widget.TextView tvInstruction = new android.widget.TextView(getContext());
        tvInstruction.setText("Silakan transfer ke nomor DANA berikut:");
        tvInstruction.setTextColor(android.graphics.Color.BLACK);
        layout.addView(tvInstruction);

        android.widget.TextView tvNomorDana = new android.widget.TextView(getContext());
        tvNomorDana.setText("0812-3456-7890");
        tvNomorDana.setTextSize(24);
        tvNomorDana.setPadding(0, 20, 0, 20);
        tvNomorDana.setTextColor(android.graphics.Color.parseColor("#118EEA"));
        tvNomorDana.setTypeface(null, android.graphics.Typeface.BOLD);
        tvNomorDana.setGravity(android.view.Gravity.CENTER);
        layout.addView(tvNomorDana);

        new AlertDialog.Builder(requireContext())
                .setTitle("Bayar via DANA")
                .setMessage("Lanjutkan pembayaran untuk pendakian " + booking.getDestinasi() + "\nTotal: Rp" + total)
                .setView(layout)
                .setPositiveButton("Konfirmasi Transfer", (dialog, which) -> {
                    Toast.makeText(getContext(), "Bukti transfer akan segera diverifikasi.", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Batal", null)
                .setCancelable(true)
                .show();
    }

    private void handlePaymentSuccess(String scanData) {
        if (currentPayingBooking != null) {
            String total = String.format("%,.0f", currentPayingBooking.getTotalBayar()).replace(',', '.');
            new AlertDialog.Builder(getContext())
                    .setTitle("Konfirmasi Pembayaran")
                    .setMessage("Scan Berhasil!\nData: " + scanData + "\n\nNominal Rp" + total + " akan dibayarkan.")
                    .setPositiveButton("Bayar Sekarang", (dialog, which) -> {
                        Toast.makeText(getContext(), "Pembayaran Berhasil untuk " + currentPayingBooking.getDestinasi(), Toast.LENGTH_LONG).show();
                        // Di sini bisa ditambahkan update status bayar ke database
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        }
    }

    private void showDeleteConfirmDialog(Booking booking) {
        new AlertDialog.Builder(getContext())
                .setTitle("Batalkan Izin")
                .setMessage("Apakah Anda yakin ingin membatalkan izin mendaki ke " + booking.getDestinasi() + "?")
                .setPositiveButton("Ya, Batalkan", (dialog, which) -> deleteBooking(booking.getIdBooking()))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteBooking(String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.URL_DELETE,
                response -> {
                    Toast.makeText(getContext(), "Izin berhasil dibatalkan", Toast.LENGTH_SHORT).show();
                    ambilData();
                },
                error -> Toast.makeText(getContext(), "Gagal menghapus", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_booking", id);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
