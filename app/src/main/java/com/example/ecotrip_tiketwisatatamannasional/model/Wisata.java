package com.example.ecotrip_tiketwisatatamannasional.model;

import java.io.Serializable;

public class Wisata implements Serializable {
    private String nama;
    private String lokasi;
    private int harga;
    private int gambarResId; // Resource ID untuk gambar lokal

    public Wisata(String nama, String lokasi, int harga, int gambarResId) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.harga = harga;
        this.gambarResId = gambarResId;
    }

    public String getNama() { return nama; }
    public String getLokasi() { return lokasi; }
    public int getHarga() { return harga; }
    public int getGambarResId() { return gambarResId; }
}
