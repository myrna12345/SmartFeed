package com.example.pakanotomatis;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AturJadwalActivity extends AppCompatActivity {

    EditText edtJudul;
    TextView txtJam;
    String jamDipilih = "00:00";
    DatabaseHelper db;
    Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atur_jadwal);

        // Inisialisasi komponen UI
        edtJudul = findViewById(R.id.edtJudul);
        txtJam = findViewById(R.id.txtJam);
        Button btnPilihJam = findViewById(R.id.btnPilihJam);
        Button btnSimpan = findViewById(R.id.btnSimpan);
        btnKembali = findViewById(R.id.btnKembali); // Tombol Kembali

        db = new DatabaseHelper(this);

        // Fungsi untuk memilih jam
        btnPilihJam.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                jamDipilih = String.format("%02d:%02d", hourOfDay, minute);
                txtJam.setText(jamDipilih);
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        });

        // Fungsi untuk menyimpan jadwal
        btnSimpan.setOnClickListener(v -> {
            String judul = edtJudul.getText().toString();
            if (!judul.isEmpty()) {
                db.insertJadwal(judul, jamDipilih);
                Toast.makeText(this, "Jadwal disimpan", Toast.LENGTH_SHORT).show();
                finish(); // Menutup activity dan kembali ke sebelumnya
            } else {
                Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show();
            }
        });

        // Fungsi untuk tombol kembali
        btnKembali.setOnClickListener(v -> onBackPressed()); // Kembali ke activity sebelumnya
    }
}
