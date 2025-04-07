package com.example.pakanotomatis;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LihatprofilActivity extends AppCompatActivity {

    private TextView textNama, textEmail, textPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_profil);

        // Inisialisasi TextView dari layout
        textNama = findViewById(R.id.text_nama);
        textEmail = findViewById(R.id.text_email);
        textPhone = findViewById(R.id.text_phone);

        // Ambil data dari Intent
        String nama = getIntent().getStringExtra("nama");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");

        // Tampilkan data jika tersedia
        textNama.setText(nama != null ? nama : "Nama tidak tersedia");
        textEmail.setText(email != null ? email : "Email tidak tersedia");
        textPhone.setText(phone != null ? phone : "Telepon tidak tersedia");
    }
}
