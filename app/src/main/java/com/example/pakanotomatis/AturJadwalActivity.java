package com.example.pakanotomatis;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AturJadwalActivity extends AppCompatActivity {

    EditText edtJudul;
    TimePicker timePicker;
    DatabaseHelper db;
    ImageView btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_jadwal);

        // Inisialisasi komponen
        edtJudul = findViewById(R.id.edtJudul);
        timePicker = findViewById(R.id.timePicker);
        Button btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali);

        db = new DatabaseHelper(this);

        // Simpan data jadwal saat tombol ditekan
        btnSimpan.setOnClickListener(v -> {
            String judul = edtJudul.getText().toString().trim();
            int jam = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                jam = timePicker.getHour();
            }
            int menit = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
    }
}
