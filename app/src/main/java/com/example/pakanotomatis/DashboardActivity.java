package com.example.pakanotomatis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private ProgressBar progressBarPakan;
    private TextView tvPersen;
    private Button btnAturJadwal, btnLihatJadwal, btnNextSchedule; // Tambahkan tombol Atur Jadwal dan Lihat Jadwal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi komponen UI
        progressBarPakan = findViewById(R.id.progress_pakan);
        tvPersen = findViewById(R.id.tv_persen);
        btnAturJadwal = findViewById(R.id.btnAturJadwal); // Tombol Atur Jadwal
        btnLihatJadwal = findViewById(R.id.btnLihatJadwal); // Tombol Lihat Jadwal
        btnNextSchedule = findViewById(R.id.btn_next_schedule); // Tombol Jadwal Berikutnya

        // Ambil data dari sensor (sementara dummy)
        int sisaPakanPersen = getSensorData();

        // Tampilkan data progress
        progressBarPakan.setProgress(sisaPakanPersen);
        tvPersen.setText(sisaPakanPersen + " %");

        // Update tombol jadwal berikutnya (pakai teks multiline)
        btnNextSchedule.setText("Jadwal yang akan datang:\n12:00");

        // Menangani tombol Atur Jadwal
        TextView tvNamaPengguna = findViewById(R.id.tvNamaPengguna);
        tvNamaPengguna.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LihatprofilActivity.class);
            startActivity(intent);
        });


        // Menangani tombol Lihat Jadwal
        btnLihatJadwal.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LihatJadwalActivity.class);
            startActivity(intent);
        });
    }

    // Dummy method untuk ambil data sensor
    private int getSensorData() {
        // Di sini nanti kamu bisa ambil dari Firebase, REST API, dsb
        return 20; // contoh: 20% sisa pakan
    }
}
