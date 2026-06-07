package com.example.ecotrip_tiketwisatatamannasional;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLoginGoogle = findViewById(R.id.btn_login_google);
        btnLoginGoogle.setOnClickListener(v -> {
            // Simulasi Login Google
            SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("userName", "Hendra");
            editor.apply();

            Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke halaman sebelumnya
        });
    }
}
