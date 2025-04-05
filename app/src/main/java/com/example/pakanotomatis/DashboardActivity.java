package com.example.pakanotomatis;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private ProgressBar progressBarPakan;
    private TextView tvPersen;
    private Button btnNextSchedule; // ganti dari TextView ke Button

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        progressBarPakan = findViewById(R.id.progress_pakan);
        tvPersen = findViewById(R.id.tv_persen);
        btnNextSchedule = findViewById(R.id.btn_next_schedule); // gunakan id button yang baru

        // Ambil data dari sensor (sementara dummy)
        int sisaPakanPersen = getSensorData();

        // Tampilkan data progress
        progressBarPakan.setProgress(sisaPakanPersen);
        tvPersen.setText(sisaPakanPersen + " %");

        // Update tombol jadwal berikutnya (pakai teks multiline)
        btnNextSchedule.setText("Jadwal yang akan datang:\n12:00");
    }

    // Dummy method untuk ambil data sensor
    private int getSensorData() {
        // Di sini nanti kamu bisa ambil dari Firebase, REST API, dsb
        return 20; // contoh: 20% sisa pakan
    }
}
