  package com.example.pakanotomatis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class LupaKataSandiActivity extends AppCompatActivity {

    EditText etEmail;
    Button btnResetKataSandi;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_kata_sandi);

        // Inisialisasi view
        etEmail = findViewById(R.id.et_email_reset);
        btnResetKataSandi = findViewById(R.id.btn_reset_kata_sandi);

        // Setup toolbar dengan tombol kembali
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reset Kata Sandi");
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        // Aksi tombol Reset Kata Sandi
        btnResetKataSandi.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(LupaKataSandiActivity.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(LupaKataSandiActivity.this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            } else {
                // Simulasi kirim email (bisa pakai Firebase atau API nanti)
                Toast.makeText(LupaKataSandiActivity.this, "Link reset telah dikirim ke email kamu", Toast.LENGTH_LONG).show();

                // Arahkan kembali ke halaman login
                Intent intent = new Intent(LupaKataSandiActivity.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
