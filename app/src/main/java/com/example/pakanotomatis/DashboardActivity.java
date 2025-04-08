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
    private Button btnAturJadwal, btnLihatJadwal, btnNextSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Inisialisasi komponen UI
        progressBarPakan = findViewById(R.id.progress_pakan);
        tvPersen = findViewById(R.id.tv_persen);
        btnAturJadwal = findViewById(R.id.btnAturJadwal);
        btnLihatJadwal = findViewById(R.id.btnLihatJadwal);
        btnNextSchedule = findViewById(R.id.btn_next_schedule);

        // Ambil data dari sensor (sementara dummy)
        int sisaPakanPersen = getSensorData();

        // Tampilkan data progress
        progressBarPakan.setProgress(sisaPakanPersen);
        tvPersen.setText(sisaPakanPersen + " %");

        // Update tombol jadwal berikutnya
        btnNextSchedule.setText("Jadwal yang akan datang:\n12:00");

        // Event tombol Atur Jadwal
        btnAturJadwal.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AturJadwalActivity.class);
            startActivity(intent);
        });

        // Event tombol Lihat Jadwal
        btnLihatJadwal.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LihatJadwalActivity.class);
            startActivity(intent);
        });
    }

    // Dummy method untuk ambil data sensor
    private int getSensorData() {
        return 20;
    }
}
