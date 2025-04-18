package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AturJadwalActivity extends AppCompatActivity {

    EditText edtJudul;
    TimePicker timePicker;
    DatabaseHelper db;
    ImageView btnKembali;

    // Navigasi bawah
    LinearLayout navBeranda, navAturJadwal, navLihatJadwal, navProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_jadwal);

        // Inisialisasi komponen input
        edtJudul = findViewById(R.id.edtJudul);
        timePicker = findViewById(R.id.timePicker);
        Button btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);
        db = new DatabaseHelper(this);

        // Inisialisasi navigasi bawah
        navBeranda = findViewById(R.id.navBeranda);
        navAturJadwal = findViewById(R.id.navAturJadwal);
        navLihatJadwal = findViewById(R.id.navLihatJadwal);
        navProfil = findViewById(R.id.navProfil);

        // Simpan data
        btnSimpan.setOnClickListener(v -> {
            String judul = edtJudul.getText().toString().trim();
            int jam = 0;
            int menit = 0;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                jam = timePicker.getHour();
                menit = timePicker.getMinute();
            }

            String jamDipilih = String.format("%02d:%02d", jam, menit);

            if (!judul.isEmpty()) {
                db.insertJadwal(judul, jamDipilih);
                Toast.makeText(this, "Jadwal disimpan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());

        // Navigasi bawah
        navBeranda.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });

        navAturJadwal.setOnClickListener(v -> {
            // Saat ini sudah di halaman ini
        });

        navLihatJadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, LihatJadwalActivity.class));
            finish();
        });

        navProfil.setOnClickListener(v -> {
            startActivity(new Intent(this, LihatprofilActivity.class));
            finish();
        });
    }
}
