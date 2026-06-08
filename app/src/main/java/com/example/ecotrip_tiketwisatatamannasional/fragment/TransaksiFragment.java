package com.example.ecotrip_tiketwisatatamannasional.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
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

        // Handle return from payment callback
        handleIntent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handleIntent();
    }

    private void handleIntent() {
        if (getActivity() != null && getActivity().getIntent() != null) {
            Uri data = getActivity().getIntent().getData();
            if (data != null && "ecotrip".equals(data.getScheme())) {
                String id = data.getQueryParameter("id");
                String status = data.getQueryParameter("status");
                if ("success".equals(status) && id != null) {
                    // Reset intent data to prevent multiple triggers
                    getActivity().getIntent().setData(null);
                    
                    // Perintah user: jika pembayaran berhasil maka HAPUS data (abaikan status)
                    deleteBooking(id);
                    showSuccessDialog();
                }
            }
        }
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Pembayaran Berhasil")
                .setMessage("Terima kasih! Pembayaran Anda telah kami terima.\nTiket elektronik Anda kini telah aktif.")
                .setPositiveButton("Tutup", (dialog, which) -> {
                    ambilData();
                })
                .setCancelable(false)
                .show();
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
                                    obj.optString("basecamp", "-"),
                                    obj.optString("status_pembayaran", "Belum Bayar")
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
                public void onPay(Booking booking) {
                    startPayment(booking);
                }

                @Override
                public void onDownload(Booking booking) {
                    Toast.makeText(getContext(), "Mengunduh E-Ticket...", Toast.LENGTH_SHORT).show();
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
        
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR Code Pembayaran (DANA/QRIS)");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CustomScannerActivity.class);
        barcodeLauncher.launch(options);
    }

    private void handlePaymentSuccess(String scanData) {
        if (currentPayingBooking != null) {
            ProgressDialog loading = new ProgressDialog(getContext());
            loading.setMessage("Memverifikasi pembayaran...");
            loading.setCancelable(false);
            loading.show();

            // Simulasi verifikasi otomatis
            new android.os.Handler().postDelayed(() -> {
                if (loading.isShowing()) loading.dismiss();
                
                // Perintah user: jika pembayaran berhasil maka HAPUS data (abaikan status)
                deleteBooking(currentPayingBooking.getIdBooking());
                
                new AlertDialog.Builder(getContext())
                        .setTitle("Pembayaran Berhasil")
                        .setMessage("Transaksi Anda telah selesai dan berhasil diverifikasi.\nData transaksi telah dihapus dari daftar.")
                        .setPositiveButton("Tutup", (dialog, which) -> {
                            // Tutup dialog ini dan refresh data
                            ambilData();
                        })
                        .setCancelable(false)
                        .show();
            }, 2000);
        }
    }

    private void updatePaymentStatus(String id, String status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiConfig.URL_UPDATE,
                response -> {
                    Toast.makeText(getContext(), "Status pembayaran diperbarui", Toast.LENGTH_SHORT).show();
                    ambilData();
                },
                error -> Toast.makeText(getContext(), "Gagal update status: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_booking", id);
                params.put("status_pembayaran", status);
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
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
