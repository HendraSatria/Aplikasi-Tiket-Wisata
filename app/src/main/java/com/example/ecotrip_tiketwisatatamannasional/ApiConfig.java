package com.example.ecotrip_tiketwisatatamannasional;

public class ApiConfig {
    // Ganti IP ini sesuai dengan IP Laptop Anda saat menjalankan server lokal
    public static final String BASE_URL = "http://192.168.0.104/ecotrip/";
    
    public static final String URL_INSERT = BASE_URL + "insert_booking.php";
    public static final String URL_TAMPIL = BASE_URL + "tampil_booking.php";
    public static final String URL_UPDATE = BASE_URL + "update_booking.php";
    public static final String URL_DELETE = BASE_URL + "delete_booking.php";
}
