package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private ProgressBar progressBarPakan;
    private TextView tvPersen, tvNamaPengguna;
    private Button btnAturJadwal, btnLihatJadwal, btnNextSchedule;

    private ImageView iconNotifikasi, btnTutupNotifikasi;
    private RelativeLayout layoutNotifikasi;
    private TextView badgeNotifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi komponen UI
        progressBarPakan = findViewById(R.id.progress_pakan);
        tvPersen = findViewById(R.id.tv_persen);
        tvNamaPengguna = findViewById(R.id.tvNamaPengguna);
        btnAturJadwal = findViewById(R.id.btnAturJadwal);
        btnLihatJadwal = findViewById(R.id.btnLihatJadwal);
        btnNextSchedule = findViewById(R.id.btn_next_schedule);

        // Notifikasi
        iconNotifikasi = findViewById(R.id.iconNotifikasi);
        layoutNotifikasi = findViewById(R.id.layoutNotifikasi);
        btnTutupNotifikasi = findViewById(R.id.btnTutupNotifikasi);
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

        // Notifikasi hanya muncul saat ikon diklik
        iconNotifikasi.setOnClickListener(v -> {
            layoutNotifikasi.setVisibility(View.VISIBLE);
            badgeNotifikasi.setVisibility(View.GONE); // Sembunyikan badge saat dibuka
        });

        // Tutup notifikasi saat tombol X ditekan
        btnTutupNotifikasi.setOnClickListener(v -> layoutNotifikasi.setVisibility(View.GONE));

        // Tombol ke halaman Atur Jadwal
        btnAturJadwal.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AturJadwalActivity.class);
            startActivity(intent);
        });

        // Tombol ke halaman Lihat Jadwal
        btnLihatJadwal.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LihatJadwalActivity.class);
            startActivity(intent);
        });

        // Klik nama pengguna ke halaman profil
        tvNamaPengguna.setOnClickListener(view -> {
            Intent intent = new Intent(DashboardActivity.this, LihatprofilActivity.class);
            startActivity(intent);
        });
    }

    // Dummy method ambil data sensor
    private int getSensorData() {
        return 20; // Misalnya pakan tersisa 20%
    }
}
