package com.example.ecotrip_tiketwisatatamannasional.model;

public class Booking {
    private String idBooking;
    private String namaPemesan;
    private String nik;
    private String destinasi;
    private String tanggal;
    private String kategoriTuris;
    private int jumlahTiket;
    private String fasilitas;
    private double totalBayar;
    private String basecamp; // Added field

    public Booking(String idBooking, String namaPemesan, String nik, String destinasi, String tanggal, String kategoriTuris, int jumlahTiket, String fasilitas, double totalBayar, String basecamp) {
        this.idBooking = idBooking;
        this.namaPemesan = namaPemesan;
        this.nik = nik;
        this.destinasi = destinasi;
        this.tanggal = tanggal;
        this.kategoriTuris = kategoriTuris;
        this.jumlahTiket = jumlahTiket;
        this.fasilitas = fasilitas;
        this.totalBayar = totalBayar;
        this.basecamp = basecamp;
    }

    public String getIdBooking() { return idBooking; }
    public String getNamaPemesan() { return namaPemesan; }
    public String getNik() { return nik; }
    public String getDestinasi() { return destinasi; }
    public String getTanggal() { return tanggal; }
    public String getKategoriTuris() { return kategoriTuris; }
    public int getJumlahTiket() { return jumlahTiket; }
    public String getFasilitas() { return fasilitas; }
    public double totalBayar() { return totalBayar; } // keeping original name or getter
    public double getTotalBayar() { return totalBayar; }
    public String getBasecamp() { return basecamp; }
}
