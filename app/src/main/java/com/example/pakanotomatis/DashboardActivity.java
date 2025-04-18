package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private ProgressBar progressBarPakan;
    private TextView tvPersen, tvNamaPengguna, badgeNotifikasi;
    private Button btnNextSchedule;
    private ImageView iconNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi komponen UI
        progressBarPakan = findViewById(R.id.progress_pakan);
        tvPersen = findViewById(R.id.tv_persen);
        tvNamaPengguna = findViewById(R.id.tvNamaPengguna);
        btnNextSchedule = findViewById(R.id.btn_next_schedule);
        iconNotifikasi = findViewById(R.id.iconNotifikasi);
        badgeNotifikasi = findViewById(R.id.badgeNotifikasi);

        // Ambil data dari sensor (sementara dummy)
        int sisaPakanPersen = getSensorData();

        // Tampilkan data progress
        progressBarPakan.setProgress(sisaPakanPersen);
        tvPersen.setText(sisaPakanPersen + " %");

        // Update tombol jadwal berikutnya
        btnNextSchedule.setText("Jadwal yang akan datang:\n12:00");

        // Tampilkan badge jika sisa pakan di bawah 30%
        if (sisaPakanPersen < 30) {
            badgeNotifikasi.setVisibility(View.VISIBLE);
            badgeNotifikasi.setText("1");
        } else {
            badgeNotifikasi.setVisibility(View.GONE);
        }

        // Klik ikon notifikasi
        iconNotifikasi.setOnClickListener(v -> badgeNotifikasi.setVisibility(View.GONE));

        // Klik nama pengguna ke halaman profil
        tvNamaPengguna.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, LihatprofilActivity.class);
            startActivity(intent);
        });

        // Bottom navigation click handling
        setupBottomNav();
    }

    private void setupBottomNav() {
        LinearLayout bottomNav = findViewById(R.id.bottomNavigationView);

        LinearLayout navAturJadwal = (LinearLayout) bottomNav.getChildAt(1);
        LinearLayout navLihatJadwal = (LinearLayout) bottomNav.getChildAt(2);
        LinearLayout navProfil = (LinearLayout) bottomNav.getChildAt(3);

        navAturJadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, AturJadwalActivity.class));
        });

        navLihatJadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, LihatJadwalActivity.class));
        });

        navProfil.setOnClickListener(v -> {
            startActivity(new Intent(this, LihatprofilActivity.class));
        });
    }


    // Dummy method ambil data sensor
    private int getSensorData() {
        return 20; // Misalnya pakan tersisa 20%
    }
}
